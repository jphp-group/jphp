package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXBloomEffect")
public class UXBloomEffect extends UXEffect<Bloom> {
    interface WrappedInterface {
        @Property double threshold();
        @Property Effect input();
    }

    public UXBloomEffect(Environment env, Bloom wrappedObject) {
        super(env, wrappedObject);
    }

    public UXBloomEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Bloom();
    }

    @Signature
    public void __construct(double threshold) {
        __wrappedObject = new Bloom(threshold);
    }
}
