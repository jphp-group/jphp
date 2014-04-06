package php.runtime.launcher;

import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.CompileScope;
import php.runtime.env.ConcurrentEnvironment;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.Extension;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.memory.StringMemory;
import php.runtime.opcode.ModuleOpcodePrinter;
import php.runtime.reflection.ModuleEntity;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Launcher {
    protected final String[] args;
    protected CompileScope compileScope;
    protected Environment environment;
    protected Properties config;
    protected String pathToConf;

    protected OutputStream out;
    protected boolean isDebug;

    private static Launcher current;

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

    public InputStream getResource(String name){
        InputStream stream = Launcher.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            try {
                return new FileInputStream(name);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        return stream;
    }

    protected Context getContext(String file){
        InputStream bootstrap = getResource(file);
        if (bootstrap != null) {
            return new Context(bootstrap, file, environment.getDefaultCharset());
        } else
            return null;
    }

    public void initModule(ModuleEntity moduleEntity){
        compileScope.loadModule(moduleEntity);
        compileScope.addUserModule(moduleEntity);
        environment.registerModule(moduleEntity);
    }

    public ModuleEntity loadFromCompiled(String file) throws IOException {
        InputStream inputStream = getResource(file);
        if (inputStream == null)
            return null;
        Context context = new Context(inputStream, file, environment.getDefaultCharset());

        ModuleDumper moduleDumper = new ModuleDumper(context, environment, true);
        return moduleDumper.load(inputStream);
    }

    public ModuleEntity loadFromFile(String file) throws IOException {
        InputStream inputStream = getResource(file);
        if (inputStream == null)
            return null;
        Context context = new Context(inputStream, file, environment.getDefaultCharset());

        JvmCompiler compiler = new JvmCompiler(environment, context);
        return compiler.compile(false);
    }

    public ModuleEntity loadFrom(String file) throws IOException {
        if (file.endsWith(".phb"))
            return loadFromCompiled(file);
        else
            return loadFromFile(file);
    }

    protected void readConfig(){
        this.config = new Properties();
        this.compileScope.configuration = new HashMap<String, Memory>();

        InputStream resource;

        resource = getResource(pathToConf);
        if (resource != null) {
            try {
                this.config.load(resource);

                for (String name : config.stringPropertyNames()){
                    compileScope.configuration.put(name, new StringMemory(config.getProperty(name)));
                }
                this.isDebug = getConfigValue("env.debug").toBoolean();
            } catch (IOException e) {
                throw new LaunchException(e.getMessage());
            }
        } else {
            throw new LaunchException("Cannot find configuration: " + pathToConf);
        }
    }

    protected void initExtensions(){
        String tmp = getConfigValue("env.extensions", "spl").toString();
        String[] extensions = StringUtils.split(tmp, ",");

        for(String ext : extensions){
            String className = Information.EXTENSIONS.get(ext.trim().toLowerCase());
            if (className == null)
                className = ext.trim();

            try {
                Extension extension = (Extension) Class.forName(className).newInstance();
                compileScope.registerExtension(extension);
            } catch (Exception e) {
                throw new LaunchException("Extension load error: " + e.getClass() + " - " + e.getMessage());
            }
        }

        this.environment = getConfigValue("env.concurrent").toBoolean()
                ? new ConcurrentEnvironment(compileScope, out)
                : new Environment(compileScope, out);

        environment.setErrorFlags(ErrorType.E_ALL.value ^ ErrorType.E_NOTICE.value);
        environment.getDefaultBuffer().setImplicitFlush(true);
    }

    public void run() throws Throwable {
        run(true);
    }

    public void printTrace(String name) {
        long t = System.currentTimeMillis() - startTime;
        System.out.printf("[%s] = " + t + " millis\n", name);
        startTime = System.currentTimeMillis();
    }

    public void run(boolean mustBootstrap) throws Throwable {
        readConfig();
        initExtensions();

        if (isDebug()){
            long t = System.currentTimeMillis() - startTime;
            System.out.println("Starting delay = " + t + " millis");
        }

        String file = config.getProperty("bootstrap.file", null);
        if (file != null){
            try {
                ModuleEntity bootstrap = loadFrom(file);
                initModule(bootstrap);
                if (new StringMemory(config.getProperty("bootstrap.showBytecode", "")).toBoolean()) {
                    ModuleOpcodePrinter moduleOpcodePrinter = new ModuleOpcodePrinter(bootstrap);
                    System.out.println(moduleOpcodePrinter.toString());
                }
                try {
                    bootstrap.include(environment);
                } catch (Exception e){
                    environment.catchUncaught(e);
                }

            } catch (IOException e) {
                throw new LaunchException("Cannot find '" + file + "' resource");
            } catch (Exception e) {
                environment.catchUncaught(e);
            }
        } else if (mustBootstrap)
            throw new LaunchException("Please set value of the `bootstrap.file` option in the launcher.conf file");
    }

    public boolean isDebug() {
        return isDebug;
    }

    public OutputStream getOut(){
        return out;
    }

    public CompileScope getCompileScope() {
        return compileScope;
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
