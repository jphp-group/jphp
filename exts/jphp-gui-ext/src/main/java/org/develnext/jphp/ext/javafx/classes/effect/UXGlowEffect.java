package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXGlowEffect")
public class UXGlowEffect extends UXEffect<Glow> {
    interface WrappedInterface {
        @Property double level();
        @Property Effect input();
    }

    public UXGlowEffect(Environment env, Glow wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGlowEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Glow();
    }

    @Signature
    public void __construct(double level) {
        __wrappedObject = new Glow(level);
    }
}
