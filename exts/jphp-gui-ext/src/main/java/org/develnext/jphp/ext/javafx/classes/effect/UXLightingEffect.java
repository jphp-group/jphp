package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXLightingEffect")
public class UXLightingEffect extends UXEffect<Lighting> {
    interface WrappedInterface {
        @Property double diffuseConstant();
        @Property double specularConstant();
        @Property double specularExponent();
        @Property double surfaceScale();
        @Property Effect contentInput();
        @Property Effect bumpInput();
    }

    public UXLightingEffect(Environment env, Lighting wrappedObject) {
        super(env, wrappedObject);
    }

    public UXLightingEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Lighting();
    }
}
