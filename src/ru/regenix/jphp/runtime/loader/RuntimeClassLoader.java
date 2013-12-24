package ru.regenix.jphp.runtime.loader;

import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.runtime.reflection.helper.ClosureEntity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RuntimeClassLoader extends ClassLoader {

    protected Map<String, ClassEntity> internalClasses = new HashMap<String, ClassEntity>();
    protected Map<String, FunctionEntity> internalFunctions = new HashMap<String, FunctionEntity>();
    protected Map<String, ModuleEntity> internalModules = new HashMap<String, ModuleEntity>();

    public RuntimeClassLoader(ClassLoader parent) {
        super(parent);
    }

    public ClassEntity getClass(String internalName){
        return internalClasses.get(internalName);
    }

    public FunctionEntity getFunction(String internalName){
        return internalFunctions.get(internalName);
    }

    public ModuleEntity getModule(String internalName){
        return internalModules.get(internalName);
    }

    protected Class<?> loadClass(ClassEntity clazz) throws NoSuchMethodException, NoSuchFieldException {
        byte[] data = clazz.getData();
        Class<?> result = defineClass(clazz.getInternalName(), data, 0, data.length);
        clazz.setNativeClazz(result);
        for(MethodEntity method : clazz.getMethods().values()){
            if (method.getNativeMethod() == null){
                method.setNativeMethod(
                        result.getDeclaredMethod(method.getInternalName(), Environment.class, Memory[].class)
                );
                method.getNativeMethod().setAccessible(true);
            }
        }
        internalClasses.put(clazz.getInternalName(), clazz);
        return result;
    }

    protected Class<?> loadClosure(ClosureEntity closure) throws NoSuchMethodException, NoSuchFieldException {
        return loadClass(closure);
    }

    protected Class<?> loadFunction(FunctionEntity function) throws NoSuchMethodException {
        byte[] data = function.getData();
        String className = function.getInternalName();

        Class<?> result = defineClass(className, data, 0, data.length);
        function.setNativeClazz(result);
        Method method = result.getDeclaredMethod(
                "__invoke", Environment.class, Memory[].class
        );
        function.setNativeMethod(method);
        internalFunctions.put(className, function);
        return result;
    }

    public boolean loadModule(ModuleEntity module){
        String internal = module.getFulledClassName(Constants.NAME_DELIMITER);

        boolean ret = false;
        if (!module.isLoaded()){
            internalModules.put(internal, module);
            try {
                for(ClosureEntity closure : module.getClosures())
                    loadClosure(closure);

                for(ClassEntity clazz : module.getClasses()){
                    if (clazz.getType() != ClassEntity.Type.INTERFACE)
                        loadClass(clazz);
                }

                for(FunctionEntity function : module.getFunctions())
                    loadFunction(function);

            } catch (NoSuchMethodException e){
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

            module.setLoaded(true);
            ret = true;
        }

        Class<?> result = defineClass(
                internal, module.getData(), 0, module.getData().length
        );
        module.setNativeClazz(result);

        try {
            Method method = result.getDeclaredMethod(
                    "__include", Environment.class, Memory[].class, ArrayMemory.class
            );
            module.setNativeMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }
}
