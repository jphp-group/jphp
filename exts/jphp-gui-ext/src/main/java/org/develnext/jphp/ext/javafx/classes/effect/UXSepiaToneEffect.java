package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.SepiaTone;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXSepiaToneEffect")
public class UXSepiaToneEffect extends UXEffect<SepiaTone> {
    interface WrappedInterface {
        @Property double level();
    }

    public UXSepiaToneEffect(Environment env, SepiaTone wrappedObject) {
        super(env, wrappedObject);
    }

    public UXSepiaToneEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new SepiaTone();
    }
}
