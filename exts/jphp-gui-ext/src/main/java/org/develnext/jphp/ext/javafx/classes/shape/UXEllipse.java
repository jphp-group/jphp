package org.develnext.jphp.ext.javafx.classes.shape;

import javafx.scene.shape.Ellipse;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "shape\\UXEllipse")
public class UXEllipse extends UXShape<Ellipse> {
    interface WrappedInterface {
        @Property double radiusX();
        @Property double radiusY();
    }

    public UXEllipse(Environment env, Ellipse wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEllipse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Ellipse();
    }

    @Signature
    public void __construct(double radiusX) {
        __wrappedObject = new Ellipse(radiusX, radiusX);
    }

    @Signature
    public void __construct(double radiusX, double radiusY) {
        __wrappedObject = new Ellipse(radiusX, radiusY);
    }

    @Override
    @Signature
    protected void setWidth(double v) {
        getWrappedObject().setRadiusX(v / 2);
    }

    @Override
    protected void setHeight(double v) {
        getWrappedObject().setRadiusY(v / 2);
    }


    @Override
    public void setX(double v) {
        super.setX(v + getWrappedObject().getRadiusX());
    }

    @Override
    public void setY(double v) {
        super.setY(v + getWrappedObject().getRadiusY());
    }

    @Override
    public double getX() {
        return super.getX() - getWrappedObject().getRadiusX();
    }

    @Override
    public double getY() {
        return super.getY() - getWrappedObject().getRadiusY();
    }

    @Override
    protected void setSize(double[] size) {
        if (size.length > 1) {
            getWrappedObject().setRadiusX(size[0] / 2);
            getWrappedObject().setRadiusY(size[1] / 2);
        }
    }
}
