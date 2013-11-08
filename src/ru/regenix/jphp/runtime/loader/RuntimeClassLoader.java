package ru.regenix.jphp.runtime.loader;

import ru.regenix.jphp.runtime.reflection.ClassEntity;

public class RuntimeClassLoader extends ClassLoader {

    public RuntimeClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> loadClass(ClassEntity clazz){
        byte[] data = clazz.getData();
        Class<?> result = defineClass(clazz.getName().replace('\\', '/'), data, 0, data.length);
        clazz.setNativeClazz(result);
        return result;
    }
}
