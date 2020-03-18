package php.runtime.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import php.runtime.Memory;
import php.runtime.Startup;
import php.runtime.common.Callback;
import php.runtime.common.LangMode;
import php.runtime.common.StringUtils;
import php.runtime.env.*;
import php.runtime.env.handler.EntityFetchHandler;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.core.classes.WrapClassLoader;
import php.runtime.ext.support.Extension;
import php.runtime.launcher.LaunchException;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.loader.dump.StandaloneLibrary;
import php.runtime.loader.dump.StandaloneLibraryDumper;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.reflection.support.ReflectionUtils;

public class StandaloneLoader {
    protected final CompileScope scope;
    protected final Environment env;
    private ClassLoader classLoader;
    protected final Properties config;

    protected boolean isDebug;

    private StandaloneLibrary library;

    private final Set<String> classesLoaded = ConcurrentHashMap.newKeySet(1000);

    public StandaloneLoader() {
        config = new Properties();
        scope = new CompileScope();
        env = new ConcurrentEnvironment(scope, System.out);
        env.getDefaultBuffer().setImplicitFlush(true);

        scope.addClassEntityFetchHandler(moduleRegisteringFetchHandler(this::fetchClass));
        scope.addFunctionEntityFetchHandler(moduleRegisteringFetchHandler(this::fetchFunction));
        scope.addConstantEntityFetchHandler(moduleRegisteringFetchHandler(this::fetchConstant));
    }

    private EntityFetchHandler moduleRegisteringFetchHandler(Function<String, ModuleEntity> moduleFetcher) {
        return (scope, name, lowerName) -> {
            ModuleEntity module = moduleFetcher.apply(lowerName);

            if (module != null) {
                loadModule(module);
                scope.loadModule(module, false);
                scope.registerModule(module);
            }
        };
    }

    public StandaloneLoader(ClassLoader classLoader) {
        this();
        this.setClassLoader(classLoader);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.scope.setNativeClassLoader(classLoader);
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
            //throw new LaunchException(e.getMessage());
        }
    }

    public void loadExtensions() {
        if (classLoader == null) {
            throw new NullPointerException("classLoader is null");
        }

        ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class, classLoader);

        for (Extension extension : loader) {
            scope.registerExtension(extension);
        }
    }

    public void initExtensions() {
        scope.getClassLoader().onAddLibrary(new Callback<Void, URL>() {
            @Override
            public Void call(URL param) {
                loadExtensions();
                return null;
            }
        });
    }

    public void loadLibrary() throws IOException {
        if (classLoader == null) {
            throw new NullPointerException("classLoader is null");
        }

        this.loadLibraryDump(classLoader.getResourceAsStream("JPHP-INF/library.dump"));
    }

    public void readConfig() {
        InputStream resource = classLoader.getResourceAsStream("JPHP-INF/launcher.conf");

        if (resource != null) {
            try {
                config.load(resource);

                for (String name : config.stringPropertyNames()) {
                    scope.configuration.put(name, new StringMemory(config.getProperty(name)));
                }

                isDebug = Startup.isDebug();

                CompileScopeOptions options = new CompileScopeOptions.Builder()
                        .debugMode(isDebug)
                        .langMode(LangMode.valueOf(getConfigValue("env.langMode", LangMode.MODERN.name()).toString().toUpperCase()))
                        .build();

                scope.setOptions(options);
            } catch (IOException e) {
                throw new LaunchException(e.getMessage());
            }
        }
    }

    public void run() {
        readConfig();

        String classLoader = config.getProperty(
                "env.classLoader",
                ReflectionUtils.getClassName(WrapClassLoader.WrapLauncherClassLoader.class)
        );

        if (classLoader != null && !(classLoader.isEmpty())) {
            ClassEntity classLoaderEntity = env.fetchClass(classLoader);

            if (classLoaderEntity == null) {
                throw new LaunchException("Class loader class is not found: " + classLoader);
            }

            try {
                WrapClassLoader loader = classLoaderEntity.newObject(env, TraceInfo.UNKNOWN, true);
                env.invokeMethod(loader, "register", Memory.TRUE);
            } catch (Throwable e) {
                throw new CriticalException(e);
            }
        }

        _run();
    }

    protected void _run() {
        loadExtensions();

        String file = config.getProperty("bootstrap.file", "res://JPHP-INF/.bootstrap.php");

        ModuleEntity bootstrap = fetchModule(file);

        if (bootstrap != null) {
            bootstrap.includeNoThrow(env);

            String[] includes = StringUtils.split(config.getProperty("bootstrap.files", System.getProperty("bootstrap.files", "")),'|');

            if (includes != null) {
                for (String include : includes) {
                    if (include.trim().isEmpty()) continue;

                    ModuleEntity module = fetchModule(include);

                    if (module == null) {
                        throw new LaunchException("Cannot include file " + include + ", it's not found.");
                    }

                    module.includeNoThrow(env);
                }
            }
        } else {
            throw new LaunchException("Cannot include file " + file + ", it's not found.");
        }
    }

    protected ModuleEntity _fetch(String name, Map<String, StandaloneLibrary.Module> source) {
        StandaloneLibrary.Module module = source.get(name);

        if (module == null) {
            return null;
            //throw new CriticalException("Cannot find module of entity: " + name);
        }

        if (scope.findUserModule(module.getName()) != null) {
            return null;
        }

        InputStream input = classLoader.getResourceAsStream(module.getInternalName() + ".dump");

        if (input == null) {
            throw new CriticalException("Cannot find resource: " + module.getInternalName() + ".dump");
        }

        ModuleDumper moduleDumper = new ModuleDumper(
                new Context(new File(module.getInternalName())), env, true
        );

        try {
            return moduleDumper.load(input);
        } catch (IOException e) {
            throw new CriticalException(e);
        }
    }

    public ModuleEntity fetchModule(String name) {
        if (library == null) {
            throw new CriticalException("Standalone library is not loaded");
        }

        ModuleEntity entity = _fetch(name, library.getModules());

        if (entity == null && name.startsWith("res://")) {
            entity = _fetch(name.substring(6), library.getModules());
        }

        if (entity != null) {
            loadModule(entity);
            scope.loadModule(entity, false);
            scope.addUserModule(entity);
        }

        return entity;
    }

    public ModuleEntity fetchClass(String name) {
        if (library == null) {
            throw new CriticalException("Standalone library is not loaded");
        }

        String lowerCase = name.toLowerCase();

        if (classesLoaded.add(lowerCase)) {
            ModuleEntity entity = _fetch(lowerCase, library.getClassModules());

            if (entity == null) {
                classesLoaded.remove(lowerCase);
            }

            return entity;
        } else {
            return null;
        }
    }

    public ModuleEntity fetchFunction(String name) {
        if (library == null) {
            throw new CriticalException("Standalone library is not loaded");
        }

        return _fetch(name.toLowerCase(), library.getFunctionModules());
    }

    public ModuleEntity fetchConstant(String name) {
        if (library == null) {
            throw new CriticalException("Standalone library is not loaded");
        }

        return _fetch(name, library.getConstantModules());
    }

    public CompileScope getScope() {
        return scope;
    }

    protected void loadLibraryDump(InputStream input) throws IOException {
        StandaloneLibraryDumper dumper = new StandaloneLibraryDumper();
        library = dumper.load(input);
    }

    protected void loadModule(ModuleEntity moduleEntity) {
        try {
            moduleEntity.setNativeClazz(classLoader.loadClass(moduleEntity.getInternalName()));

            for (FunctionEntity functionEntity : moduleEntity.getFunctions()) {
                functionEntity.setNativeClazz(
                        classLoader.loadClass(functionEntity.getInternalName())
                );
            }

            for (ClassEntity classEntity : moduleEntity.getClasses()) {
                classEntity.setNativeClazz(
                        classLoader.loadClass(classEntity.getCompiledInternalName())
                );
            }

            for (ClosureEntity closureEntity : moduleEntity.getClosures()) {
                closureEntity.setNativeClazz(
                        classLoader.loadClass(closureEntity.getInternalName())
                );
            }

            for (GeneratorEntity generatorEntity : moduleEntity.getGenerators()) {
                generatorEntity.setNativeClazz(
                        classLoader.loadClass(generatorEntity.getInternalName())
                );
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Environment getScopeEnvironment() {
        return env;
    }

    public void setArgv(String[] args) {
        if (args == null) {
            env.getGlobals().put("argv", new ArrayMemory());
            env.getGlobals().put("argc", Memory.CONST_INT_0);
        } else {
            env.getGlobals().put("argv", ArrayMemory.ofStrings(args));
            env.getGlobals().put("argc", LongMemory.valueOf(args.length));
        }
    }
}
