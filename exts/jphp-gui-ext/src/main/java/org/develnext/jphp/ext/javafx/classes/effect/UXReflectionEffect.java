package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXReflectionEffect")
public class UXReflectionEffect extends UXEffect<Reflection> {
    interface WrappedInterface {
        @Property double topOffset();
        @Property double topOpacity();
        @Property double bottomOpacity();
    }

    public UXReflectionEffect(Environment env, Reflection wrappedObject) {
        super(env, wrappedObject);
    }

    public UXReflectionEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Reflection();
    }
}
