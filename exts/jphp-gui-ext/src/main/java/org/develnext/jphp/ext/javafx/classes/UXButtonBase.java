package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ButtonBase;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Reflection.Name(JavaFXExtension.NS + "UXButtonBase")
public class UXButtonBase<T extends ButtonBase> extends UXLabeled<ButtonBase> {
    interface WrappedInterface {
        @Property boolean armed();
    }

    public UXButtonBase(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXButtonBase(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }
}
