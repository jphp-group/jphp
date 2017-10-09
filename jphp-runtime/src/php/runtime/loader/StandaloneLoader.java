package php.runtime.loader;

import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.handler.EntityFetchHandler;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.support.Extension;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.loader.dump.StandaloneLibrary;
import php.runtime.loader.dump.StandaloneLibraryDumper;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class StandaloneLoader {
    protected final CompileScope scope;
    protected final Environment env;
    private ClassLoader classLoader;

    private StandaloneLibrary library;

    public StandaloneLoader() {
        scope = new CompileScope();
        env = new Environment(scope, System.out);
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

    public void loadLibrary() throws IOException {
        if (classLoader == null) {
            throw new NullPointerException("classLoader is null");
        }

        this.loadClassesDump(classLoader.getResourceAsStream("JPHP-INF/library.dump"));
    }

    public void run() {
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
        }

        InputStream input = classLoader.getResourceAsStream(module.getInternalName() + ".dump");

        if (input == null) {
            return null;
        }

        ModuleDumper moduleDumper = new ModuleDumper(
                new Context(new File(module.getInternalName())), env, true
        );

        try {
            return moduleDumper.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ModuleEntity fetchModule(String name) {
        ModuleEntity entity = _fetch(name, library.getModules());

        if (entity != null) {
            loadModule(entity);
            scope.loadModule(entity, false);
            scope.addUserModule(entity);
        }

        return entity;
    }

    public ModuleEntity fetchClass(String name) {
        return _fetch(name.toLowerCase(), library.getClassModules());
    }

    public ModuleEntity fetchFunction(String name) {
        return _fetch(name.toLowerCase(), library.getFunctionModules());
    }

    public ModuleEntity fetchConstant(String name) {
        return _fetch(name, library.getConstantModules());
    }

    public CompileScope getScope() {
        return scope;
    }

    protected void loadClassesDump(InputStream input) throws IOException {
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
