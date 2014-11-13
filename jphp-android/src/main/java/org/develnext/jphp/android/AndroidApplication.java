package org.develnext.jphp.android;

import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AndroidApplication {
    protected final CompileScope scope;
    protected final Environment env;
    protected final ClassLoader classLoader;

    protected final Class<?> dalvikClassLoader;

    public AndroidApplication(CompileScope scope) {
        this.scope       = scope;
        this.env         = new Environment(scope);
        this.classLoader = scope.getClassLoader();

        try {
            dalvikClassLoader = Class.forName("dalvik.system.DexClassLoader");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Environment getEnvironment() {
        return env;
    }

    public List<ModuleEntity> loadLibrary(File dexFile) throws IOException {
        List<ModuleEntity> result = new ArrayList<ModuleEntity>();

        String cachePath = System.getProperty("jphp.class.cache.path");
        if (cachePath == null) {
            throw new RuntimeException("cachePath is null");
        }

        try {
            ClassLoader dexClassLoader = (ClassLoader) dalvikClassLoader.getConstructor(
                    String.class, String.class, String.class, ClassLoader.class
            ).newInstance(dexFile.getPath(), cachePath, null, classLoader);

            DataInputStream classesDump = new DataInputStream(dexClassLoader.getResourceAsStream("classes.dump"));

            int classSize = classesDump.readInt();

            for (int i = 0; i < classSize; i++) {
                String moduleName = classesDump.readUTF();
                int moduleLength = classesDump.readInt();
                byte[] data = new byte[moduleLength];

                classesDump.read(data);

                ModuleDumper moduleDumper = new ModuleDumper(new Context(new File(moduleName)), env, true);

                ModuleEntity moduleEntity = moduleDumper.load(new ByteArrayInputStream(data));
                loadModule(moduleEntity, dexClassLoader);

                scope.loadModule(moduleEntity, false);
                env.registerModule(moduleEntity);

                result.add(moduleEntity);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected void loadModule(ModuleEntity moduleEntity, ClassLoader dexClassLoader) {
        try {
            moduleEntity.setNativeClazz(dexClassLoader.loadClass(moduleEntity.getInternalName()));

            for (FunctionEntity functionEntity : moduleEntity.getFunctions()) {
                functionEntity.setNativeClazz(
                        dexClassLoader.loadClass(functionEntity.getInternalName())
                );
            }

            for (ClassEntity classEntity : moduleEntity.getClasses()) {
                classEntity.setNativeClazz(
                        dexClassLoader.loadClass(classEntity.getInternalName())
                );
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
