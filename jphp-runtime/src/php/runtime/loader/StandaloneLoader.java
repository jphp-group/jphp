package php.runtime.loader;

import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.handler.EntityFetchHandler;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.support.Extension;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class StandaloneLoader {
    protected final CompileScope scope;
    protected final Environment env;
    private ClassLoader classLoader;

    protected final Map<String, Module> classes;
    protected final Map<String, Module> functions;
    protected final Map<String, Module> constants;
    protected final Map<String, Module> modules;

    public StandaloneLoader() {
        scope = new CompileScope();
        env = new Environment(scope, System.out);

        classes = new HashMap<String, Module>();
        functions = new HashMap<String, Module>();
        constants = new HashMap<String, Module>();

        modules = new HashMap<String, Module>();

        scope.setClassEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String name) {
                ModuleEntity module = fetchClass(name);

                if (module != null) {
                    loadModule(module);
                    scope.loadModule(module, false);
                    scope.registerModule(module);
                }
            }
        });

        scope.setFunctionEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String name) {
                ModuleEntity module = fetchFunction(name);

                if (module != null) {
                    loadModule(module);
                    scope.loadModule(module, false);
                    scope.registerModule(module);
                }
            }
        });

        scope.setConstantEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String name) {
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
    }

    public void loadExtensions() {
        if (classLoader == null) {
            throw new NullPointerException("classLoader is null");
        }

        Scanner scanner = new Scanner(classLoader.getResourceAsStream("JPHP-INF/standalone.extensions.list"));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (!line.isEmpty()) {
                try {
                    scope.registerExtension((Extension) Class.forName(line).newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void loadLibrary() throws IOException {
        if (classLoader == null) {
            throw new NullPointerException("classLoader is null");
        }

        this.loadClassesDump(classLoader.getResourceAsStream("JPHP-INF/classes.dump"));
    }

    public void run() {
        loadExtensions();

        try {
            loadLibrary();

            ModuleEntity bootstrap = fetchModule("bootstrap");

            if (bootstrap != null) {
                bootstrap.includeNoThrow(env);
            } else {
                System.out.println("(!) Cannot find bootstrap script.");
            }
        } catch (IOException e) {
            throw new CriticalException(e);
        }
    }

    protected ModuleEntity _fetch(String name, Map<String, Module> source) {
        Module module = source.get(name);

        if (module == null) {
            return null;
        }

        InputStream input = classLoader.getResourceAsStream(module.internalName + ".dump");

        if (input == null) {
            return null;
        }

        ModuleDumper moduleDumper = new ModuleDumper(new Context(new File(module.name)), env, true);

        try {
            return moduleDumper.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ModuleEntity fetchModule(String name) {
        ModuleEntity entity = _fetch(name.toLowerCase(), modules);

        if (entity != null) {
            loadModule(entity);
            scope.loadModule(entity, false);
        }

        return entity;
    }

    public ModuleEntity fetchClass(String name) {
        return _fetch(name.toLowerCase(), classes);
    }

    public ModuleEntity fetchFunction(String name) {
        return _fetch(name.toLowerCase(), functions);
    }

    public ModuleEntity fetchConstant(String name) {
        return _fetch(name, constants);
    }

    protected void loadClassesDump(InputStream input) throws IOException {
        DataInputStream classesDump = new DataInputStream(input);

        int size = classesDump.readInt();

        for (int i = 0; i < size; i++) {
            String name = classesDump.readUTF();
            String internalName = classesDump.readUTF();

            // classes
            int classesSize = classesDump.readInt();
            Set<String> classes = new HashSet<String>();
            for (int j = 0; j < classesSize; j++) {
                classes.add(classesDump.readUTF());
            }

            // functions
            int functionsSize = classesDump.readInt();
            Set<String> functions = new HashSet<String>();
            for (int j = 0; j < functionsSize; j++) {
                functions.add(classesDump.readUTF());
            }

            // constants
            int constantSize = classesDump.readInt();
            Set<String> constants = new HashSet<String>();
            for (int j = 0; j < constantSize; j++) {
                constants.add(classesDump.readUTF());
            }

            Module module = new Module(name, internalName, classes, functions, constants);
            registerModule(module);
        }
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
                        classLoader.loadClass(classEntity.getInternalName())
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

    protected void registerModule(Module module) {
        for (String name : module.classes) {
            classes.put(name.toLowerCase(), module);
        }

        for (String name : module.functions) {
            functions.put(name.toLowerCase(), module);
        }

        for (String name : module.constants) {
            constants.put(name, module);
        }

        String name = module.name;

        if (name.endsWith(".php")) {
            name = name.substring(0, name.lastIndexOf(".php"));
        }

        modules.put(name.toLowerCase(), module);
    }

    protected static class Module {
        public final String name;
        public final String internalName;

        public final Set<String> classes;
        public final Set<String> functions;
        public final Set<String> constants;

        public Module(String name, String internalName,
                      Set<String> classes, Set<String> functions, Set<String> constants) {
            this.name = name;
            this.internalName = internalName;
            this.classes = classes;
            this.functions = functions;
            this.constants = constants;
        }
    }
}
