package org.develnext.jphp.ext.javafx.classes.shape;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.UXNode;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "shape\\UXShape")
public abstract class UXShape<T extends Shape> extends UXNode<Shape> {
    interface WrappedInterface {
        @Property boolean smooth();
        @Property double strokeWidth();
        @Property StrokeType strokeType();

        @Property Paint fill();
        @Property Paint stroke();
    }

    public UXShape(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXShape(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }
}
