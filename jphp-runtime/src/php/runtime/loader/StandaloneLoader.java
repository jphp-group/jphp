package php.runtime.loader;

import php.runtime.Memory;
import php.runtime.Startup;
import php.runtime.common.Callback;
import php.runtime.common.LangMode;
import php.runtime.env.*;
import php.runtime.env.handler.EntityFetchHandler;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.core.classes.WrapClassLoader;
import php.runtime.ext.support.Extension;
import php.runtime.launcher.LaunchException;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.loader.dump.StandaloneLibrary;
import php.runtime.loader.dump.StandaloneLibraryDumper;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class StandaloneLoader {
    protected final CompileScope scope;
    protected final Environment env;
    private ClassLoader classLoader;
    protected final Properties config;

    protected boolean isDebug;

    private StandaloneLibrary library;

    public StandaloneLoader() {
        config = new Properties();
        scope = new CompileScope();
        env = new ConcurrentEnvironment(scope, System.out);
        env.getDefaultBuffer().setImplicitFlush(true);

        scope.addClassEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String originName, String name) {
                ModuleEntity module = fetchClass(name);

                if (module != null) {
                        loadModule(module);
                        scope.loadModule(module, false);
                        scope.registerModule(module);
                }
            }
        });

        scope.addFunctionEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String originName, String name) {
                ModuleEntity module = fetchFunction(name);

                if (module != null) {
                        loadModule(module);
                        scope.loadModule(module, false);
                        scope.registerModule(module);
                }
            }
        });

        scope.addConstantEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String originName, String name) {
                ModuleEntity module = fetchConstant(name);

                if (module != null) {
                        loadModule(module);
                        scope.loadModule(module, false);
                        scope.registerModule(module);
                }
            }
        });
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

                for (String name : config.stringPropertyNames()){
                    scope.configuration.put(name, new StringMemory(config.getProperty(name)));
                }

                isDebug = Startup.isDebug();
                scope.setDebugMode(isDebug);

                scope.setLangMode(
                        LangMode.valueOf(getConfigValue("env.langMode", LangMode.MODERN.name()).toString().toUpperCase())
                );
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

        run("JPHP-INF/.bootstrap.php");
    }

    public void run(String bootstrapScriptName) {
        loadExtensions();

        try {
            loadLibrary();

            ModuleEntity bootstrap = fetchModule(bootstrapScriptName);

            if (bootstrap != null) {
                bootstrap.includeNoThrow(env);
            } else {
                System.out.println("(!) Cannot find bootstrap script.");
            }
        } catch (IOException e) {
            throw new CriticalException(e);
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

        return _fetch(name.toLowerCase(), library.getClassModules());
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
}
