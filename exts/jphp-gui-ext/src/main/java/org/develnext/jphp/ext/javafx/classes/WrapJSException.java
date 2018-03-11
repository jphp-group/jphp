package org.develnext.jphp.ext.javafx.classes;

import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "JSException")
public class WrapJSException extends JavaException {
    public WrapJSException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public WrapJSException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
