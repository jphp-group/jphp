package ru.regenix.jphp.runtime.loader;

import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;

import java.lang.reflect.Method;

public class RuntimeClassLoader extends ClassLoader {

    public RuntimeClassLoader(ClassLoader parent) {
        super(parent);
    }

    protected Class<?> loadClass(ClassEntity clazz) throws NoSuchMethodException {
        byte[] data = clazz.getData();
        Class<?> result = defineClass(clazz.getName().replace('\\', Constants.NAME_DELIMITER), data, 0, data.length);
        clazz.setNativeClazz(result);
        for(MethodEntity method : clazz.getMethods().values()){
                method.setNativeMethod(
                        result.getDeclaredMethod(method.getName(), Environment.class, String.class, Memory[].class)
                );
            method.getNativeMethod().setAccessible(true);
        }

        return result;
    }

    protected Class<?> loadFunction(FunctionEntity function) throws NoSuchMethodException {
        byte[] data = function.getData();
        String className = function.getModule().getFulledFunctionClassName(function.getName(), Constants.NAME_DELIMITER);

        Class<?> result = defineClass(className, data, 0, data.length);
        function.setNativeClazz(result);
        Method method = result.getDeclaredMethod(
                "__invoke", Environment.class, String.class, Memory[].class
        );
        function.setNativeMethod(method);
        return result;
    }

    public boolean loadModule(ModuleEntity module){
        Class<?> result = defineClass(
                module.getFulledClassName(Constants.NAME_DELIMITER), module.getData(), 0, module.getData().length
        );
        module.setNativeClazz(result);

        try {
            Method method = result.getDeclaredMethod(
                    "__include", Environment.class, String.class, Memory[].class, ArrayMemory.class
            );
            module.setNativeMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (!module.isLoaded()){
            try {
                for(ClassEntity clazz : module.getClasses())
                    loadClass(clazz);

                for(FunctionEntity function : module.getFunctions())
                    loadFunction(function);

            } catch (NoSuchMethodException e){
                throw new RuntimeException(e);
            }

            module.setLoaded(true);
            return true;
        }
        return false;
    }
}
