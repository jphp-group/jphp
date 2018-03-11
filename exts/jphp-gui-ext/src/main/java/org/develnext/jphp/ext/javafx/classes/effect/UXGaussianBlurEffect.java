package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Lighting;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXGaussianBlurEffect")
public class UXGaussianBlurEffect extends UXEffect<GaussianBlur> {
    interface WrappedInterface {
        @Property double radius();
        @Property Effect input();
    }

    public UXGaussianBlurEffect(Environment env, GaussianBlur wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGaussianBlurEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new GaussianBlur();
    }

    @Signature
    public void __construct(double radius) {
        __wrappedObject = new GaussianBlur(radius);
    }
}
