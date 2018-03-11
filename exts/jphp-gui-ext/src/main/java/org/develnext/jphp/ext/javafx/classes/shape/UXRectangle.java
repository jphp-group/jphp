package org.develnext.jphp.ext.javafx.classes.shape;

import javafx.scene.shape.Rectangle;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "shape\\UXRectangle")
public class UXRectangle extends UXShape<Rectangle> {
    interface WrappedInterface {
        @Property double arcWidth();
        @Property double arcHeight();
    }

    public UXRectangle(Environment env, Rectangle wrappedObject) {
        super(env, wrappedObject);
    }

    public UXRectangle(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Rectangle();
    }

    @Override
    @Signature
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setWidth(size[0]);
            getWrappedObject().setHeight(size[1]);
        }
    }

    @Override
    @Signature
    protected void setWidth(double v) {
        getWrappedObject().setWidth(v);
    }

    @Override
    @Signature
    protected void setHeight(double v) {
        getWrappedObject().setHeight(v);
    }
}
