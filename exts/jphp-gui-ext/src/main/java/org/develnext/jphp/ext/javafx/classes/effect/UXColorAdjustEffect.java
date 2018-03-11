package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXColorAdjustEffect")
public class UXColorAdjustEffect extends UXEffect<ColorAdjust> {
    interface WrappedInterface {
        @Property double brightness();
        @Property double contrast();
        @Property double hue();
        @Property double saturation();

        @Property Effect input();
    }

    public UXColorAdjustEffect(Environment env, ColorAdjust wrappedObject) {
        super(env, wrappedObject);
    }

    public UXColorAdjustEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ColorAdjust();
    }
}
