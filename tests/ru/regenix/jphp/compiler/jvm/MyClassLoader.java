package ru.regenix.jphp.compiler.jvm;


final public class MyClassLoader extends ClassLoader {

    public Class<?> loadClass(String name, byte[] data) throws ClassNotFoundException {
        return defineClass(name, data, 0, data.length);
    }
}
