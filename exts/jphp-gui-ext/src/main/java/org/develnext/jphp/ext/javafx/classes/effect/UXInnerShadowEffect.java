package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "effect\\UXInnerShadowEffect")
public class UXInnerShadowEffect extends UXEffect<InnerShadow> {
    interface WrappedInterface {
        @Property double radius();
        @Property Color color();
        @Property double offsetX();
        @Property double offsetY();

        @Property double height();
        @Property double width();

        @Property BlurType blurType();

        @Property Effect input();
    }

    public UXInnerShadowEffect(Environment env, InnerShadow wrappedObject) {
        super(env, wrappedObject);
    }

    public UXInnerShadowEffect(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new InnerShadow();
    }

    @Signature
    public void __construct(double radius) {
        __wrappedObject = new InnerShadow();
        getWrappedObject().setRadius(radius);
    }

    @Signature
    public void __construct(double radius, Color color) {
        __wrappedObject = new InnerShadow(radius, color);
    }

    @Signature
    public void __construct(double radius, Color color, double offsetX, double offsetY) {
        __wrappedObject = new InnerShadow(radius, offsetX, offsetY, color);
    }

    @Setter
    public void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setWidth(size[0]);
            getWrappedObject().setHeight(size[1]);
        }
    }

    @Getter
    public double[] getSize() {
        return new double[] { getWrappedObject().getWidth(), getWrappedObject().getHeight() };
    }
}
