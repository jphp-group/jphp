package org.develnext.jphp.framework.classes.exception;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(FrameworkExtension.NS + "exception\\ClassLoaderException")
public class PClassLoaderException extends BaseException {
    public PClassLoaderException(Environment env) {
        super(env);
    }

    public PClassLoaderException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
