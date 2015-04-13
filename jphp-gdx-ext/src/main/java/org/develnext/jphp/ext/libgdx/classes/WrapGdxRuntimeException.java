package org.develnext.jphp.ext.libgdx.classes;

import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name(LibGDXExtension.NAMESPACE + "GdxRuntimeException")
public class WrapGdxRuntimeException extends JavaException {
    public WrapGdxRuntimeException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public WrapGdxRuntimeException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
