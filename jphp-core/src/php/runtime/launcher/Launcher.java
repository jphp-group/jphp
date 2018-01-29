package php.runtime.launcher;

import org.develnext.jphp.core.opcode.ModuleOpcodePrinter;
import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.Startup;
import php.runtime.common.Callback;
import php.runtime.common.LangMode;
import php.runtime.common.StringUtils;
import php.runtime.env.*;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.classes.WrapClassLoader;
import php.runtime.ext.core.classes.lib.FsUtils;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.support.Extension;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class Launcher {
    protected final String[] args;
    protected CompileScope compileScope;
    protected Environment environment;
    protected Properties config;
    protected String pathToConf;

    protected OutputStream out;
    protected boolean isDebug;

    private static Launcher current;

    private ClassLoader classLoader = Launcher.class.getClassLoader();

    private long startTime = System.currentTimeMillis();

    public Launcher(String pathToConf, String[] args) {
        current = this;

        this.args = args;
        this.out = System.out != null ? System.out : new ByteArrayOutputStream();
        this.compileScope = new CompileScope();
        this.pathToConf = pathToConf == null ? "JPHP-INF/launcher.conf" : pathToConf;
    }

    public Launcher(String[] args) {
        this(null, args);
    }

    public Launcher(ClassLoader classLoader) {
        this();
        this.classLoader = classLoader;
    }

    public Launcher() {
        this(new String[0]);
    }

    public Memory getConfigValue(String key) {
        return getConfigValue(key, Memory.NULL);
    }

    public Memory getConfigValue(String key, Memory def) {
        String result = config.getProperty(key);
        if (result == null)
            return def;

        return new StringMemory(result);
    }

    public Memory getConfigValue(String key, String def) {
        return getConfigValue(key, new StringMemory(def));
    }

    public Collection<InputStream> getResources(String name) {
        List<InputStream> result = new ArrayList<InputStream>();
        try {
            Enumeration<URL> urls = classLoader.getResources(name);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                result.add(url.openStream());
            }
            return result;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public InputStream getResource(String name) {
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) {
            try {
                return new FileInputStream(name);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        return stream;
    }

    protected Context getContext(String file) {
        InputStream bootstrap = getResource(file);
        if (bootstrap != null) {
            return new Context(bootstrap, file, environment.getDefaultCharset());
        } else
            return null;
    }

    public ModuleEntity loadFrom(String file) throws Throwable {
        return environment.getModuleManager().fetchModule(file);
    }

    protected void readConfig() {
        this.config = new Properties();
        this.compileScope.configuration = new HashMap<>();

        String externalConfig = System.getProperty("jphp.config");

        if (externalConfig != null) {
            try {
                FileInputStream inStream = new FileInputStream(externalConfig);
                config.load(inStream);
                inStream.close();

                for (String name : config.stringPropertyNames()) {
                    compileScope.configuration.put(name, new StringMemory(config.getProperty(name)));
                }
            } catch (IOException e) {
                throw new LaunchException("Unable to load the config -Djphp.config=" + externalConfig);
            }
        }

        InputStream resource;

        resource = getResource(pathToConf);

        if (resource != null) {
            try {
                config.load(resource);

                for (String name : config.stringPropertyNames()) {
                    compileScope.configuration.put(name, new StringMemory(config.getProperty(name)));
                }

                isDebug = Startup.isDebug();

                compileScope.setDebugMode(isDebug);

                compileScope.setLangMode(
                        LangMode.valueOf(getConfigValue("env.langMode", LangMode.MODERN.name()).toString().toUpperCase())
                );
            } catch (IOException e) {
                throw new LaunchException(e.getMessage());
            }
        }
    }

    protected void loadExtensions() {
        ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class, compileScope.getClassLoader());

        for (Extension extension : loader) {
            compileScope.registerExtension(extension);
        }
    }

    protected void initExtensions() {
        String tmp = getConfigValue("env.extensions", "spl").toString();
        String[] _extensions = StringUtils.split(tmp, ",");

        Set<String> extensions = new HashSet<String>();
        for (String ext : _extensions) {
            extensions.add(ext.trim());
        }

        loadExtensions();

        compileScope.getClassLoader().onAddLibrary(new Callback<Void, URL>() {
            @Override
            public Void call(URL param) {
                loadExtensions();
                return null;
            }
        });

        if (getConfigValue("env.autoregister_extensions", Memory.TRUE).toBoolean()) {
            for (InputStream list : getResources("JPHP-INF/extensions.list")) {
                Scanner scanner = new Scanner(list);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        System.out.println("WARNING!!! Auto-registration via JPHP-INF/extensions.list is deprected, " + line + ", use META-INF/services/php.runtime.ext.support.Extension file");

                        extensions.add(line);
                    }
                }
            }
        }

        for (String ext : extensions) {
            String className = Information.EXTENSIONS.get(ext.trim().toLowerCase());
            if (className == null) {
                className = ext.trim();
            }

            compileScope.registerExtension(className);
        }

        this.environment = getConfigValue("env.concurrent", "1").toBoolean()
                ? new ConcurrentEnvironment(compileScope, out)
                : new Environment(compileScope, out);

        environment.setErrorFlags(ErrorType.E_ALL.value ^ ErrorType.E_NOTICE.value);
        environment.getDefaultBuffer().setImplicitFlush(true);
    }

    public void printTrace(String name) {
        long t = System.currentTimeMillis() - startTime;
        System.out.printf("[%s] = " + t + " millis\n", name);
        startTime = System.currentTimeMillis();
    }

    public void run() throws Throwable {
        run(true);
    }

    public void run(boolean mustBootstrap) throws Throwable {
        run(mustBootstrap, false);
    }

    public void beforeIncludeBootstrap() {
    }

    public void afterIncludeBootstrap() {
    }

    public void run(boolean mustBootstrap, boolean disableExtensions) throws Throwable {
        readConfig();
        if (!disableExtensions) {
            initExtensions();
        }

        if (isDebug()) {
            if (compileScope.getTickHandler() == null) {
                throw new LaunchException("Cannot find a debugger, please add the jphp-debugger dependency");
            }
        }

        if (Startup.isShowInitDelay()) {
            long t = System.currentTimeMillis() - startTime;
            Startup.trace("Startup time = " + t + "ms");
        }

        String file = config.getProperty("bootstrap.file", "res://JPHP-INF/.bootstrap.php");

        if ("php".equals(FsUtils.ext(file))) {
            if (Stream.exists(environment, StringMemory.valueOf(file.substring(0, file.length() - 4) + ".phb")).toBoolean()) {
                file = file.substring(0, file.length() - 4) + ".phb";
            }
        }

        String classLoader = config.getProperty("env.classLoader", ReflectionUtils.getClassName(WrapClassLoader.WrapLauncherClassLoader.class));

        if (classLoader != null && !(classLoader.isEmpty())) {
            ClassEntity classLoaderEntity = environment.fetchClass(classLoader);

            if (classLoaderEntity == null) {
                throw new LaunchException("Class loader class is not found: " + classLoader);
            }

            WrapClassLoader loader = classLoaderEntity.newObject(environment, TraceInfo.UNKNOWN, true);
            environment.invokeMethod(loader, "register", Memory.TRUE);
        }

        if (file != null && !file.isEmpty()) {
            try {
                ModuleEntity bootstrap = loadFrom(file);

                if (bootstrap == null) {
                    throw new IOException();
                }

                beforeIncludeBootstrap();

                if (new StringMemory(config.getProperty("bootstrap.showBytecode", "")).toBoolean()) {
                    ModuleOpcodePrinter moduleOpcodePrinter = new ModuleOpcodePrinter(bootstrap);
                    System.out.println(moduleOpcodePrinter.toString());
                }

                ArrayMemory argv = ArrayMemory.ofStrings(this.args);

                String path = URLDecoder.decode(
                        Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
                        "UTF-8"
                );

                argv.unshift(StringMemory.valueOf(path));

                environment.getGlobals().put("argv", argv);
                environment.getGlobals().put("argc", LongMemory.valueOf(argv.size()));

                CallStackItem stackItem = new CallStackItem(bootstrap.getTrace());
                environment.pushCall(stackItem);
                try {
                    bootstrap.includeNoThrow(environment);
                } finally {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        compileScope.triggerProgramShutdown(environment);

                        if (StringMemory.valueOf(config.getProperty("env.doFinal", "1")).toBoolean()) {
                            try {
                                environment.doFinal();
                            } catch (Throwable throwable) {
                                throw new LaunchException(throwable);
                            }
                        }
                    }));

                    afterIncludeBootstrap();

                    environment.popCall();
                }
            } catch (IOException e) {
                throw new LaunchException("Cannot find '" + file + "' resource for `bootstrap.file` option");
            }
        } else if (mustBootstrap)
            throw new LaunchException("Please set value of the `bootstrap.file` option in the launcher.conf file");
    }

    public boolean isDebug() {
        return isDebug;
    }

    public OutputStream getOut() {
        return out;
    }

    public CompileScope getCompileScope() {
        return compileScope;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public static Launcher current() {
        return current;
    }

    public static void main(String[] args) throws Throwable {
        Launcher launcher = new Launcher(args);
        Launcher.current = launcher;
        launcher.run();
    }
}
