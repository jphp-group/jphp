package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NS + "effect\\UXEffect")
public class UXEffect<T extends Effect> extends BaseWrapper<Effect> {
    interface WrappedInterface {
    }

    public UXEffect(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }
}
