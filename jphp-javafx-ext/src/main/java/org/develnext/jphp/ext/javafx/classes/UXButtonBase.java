package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ButtonBase;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Reflection.Name(JavaFXExtension.NAMESPACE + "UXButtonBase")
public class UXButtonBase extends UXLabeled {
    interface WrappedInterface {
        @Property boolean armed();
    }

    public UXButtonBase(Environment env, ButtonBase wrappedObject) {
        super(env, wrappedObject);
    }

    public UXButtonBase(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ButtonBase getWrappedObject() {
        return (ButtonBase) super.getWrappedObject();
    }
}
