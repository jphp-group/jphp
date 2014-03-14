package org.develnext.jphp.core.compiler.jvm;


final public class MyClassLoader extends ClassLoader {

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> loadClass(String name, byte[] data) throws ClassNotFoundException {
        return defineClass(name, data, 0, data.length);
    }
}
