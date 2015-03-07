package org.develnext.jphp.framework.classes.loader;

import org.develnext.jphp.framework.FrameworkExtension;
import org.develnext.jphp.framework.classes.exception.PClassLoaderException;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.SplClassLoader;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name(FrameworkExtension.NS + "loader\\ClassLoader")
abstract public class PClassLoader extends BaseObject {
    protected SplClassLoader splClassLoader = null;

    public PClassLoader(Environment env) {
        super(env);
    }

    public PClassLoader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    abstract public boolean loadClass(Environment env, String className) throws Throwable;

    @Signature
    public void register(Environment env, boolean prepend) {
        if (splClassLoader == null) {
            ArrayMemory callback = new ArrayMemory();
            callback.add(this);
            callback.add("loadClass");

            Invoker invoker = Invoker.valueOf(env, null, callback);

            env.registerAutoloader(splClassLoader = new SplClassLoader(invoker, callback), prepend);
        } else {
            env.exception(PClassLoaderException.class, "ClassLoader is already registered");
        }
    }

    @Signature
    public void register(Environment env) {
        register(env, false);
    }

    @Signature
    public void unregister(Environment env) {
        if (splClassLoader == null) {
            env.exception(PClassLoaderException.class, "ClassLoader is not registered");
        }

        env.unRegisterAutoloader(splClassLoader);
        splClassLoader = null;
    }
}
