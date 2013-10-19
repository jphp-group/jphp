package ru.regenix.jphp;

/**
 * User: Dim-S (dz@dim-s.net)
 * Date: 18.10.13
 */
public class MyClassLoader extends ClassLoader {

    public Class<?> loadClass(String name, byte[] data) throws ClassNotFoundException {
        return defineClass(name, data, 0, data.length);
    }
}
