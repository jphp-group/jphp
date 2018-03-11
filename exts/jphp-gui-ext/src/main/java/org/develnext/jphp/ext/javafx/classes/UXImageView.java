package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXImageView")
public class UXImageView extends UXNode<ImageView> {
    interface WrappedInterface {
        @Property boolean smooth();
        @Property boolean preserveRatio();
        @Property double fitWidth();
        @Property double fitHeight();
        @Property @Nullable Image image();
    }
    public UXImageView(Environment env, ImageView wrappedObject) {
        super(env, wrappedObject);
    }

    public UXImageView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ImageView();
    }

    @Signature
    public void __construct(@Nullable Image image) {
        __wrappedObject = new ImageView(image);
    }

    @Override
    @Signature
    public void setWidth(double value) {
        super.setWidth(value);
        getWrappedObject().setFitWidth(value);
    }

    @Override
    @Signature
    public void setHeight(double value) {
        super.setHeight(value);
        getWrappedObject().setFitHeight(value);
    }

    @Override
    @Signature
    public void setSize(double[] values) {
        super.setSize(values);

        if (values.length >= 2) {
            getWrappedObject().setFitWidth(values[0]);
            getWrappedObject().setFitHeight(values[1]);
        }
    }
}
