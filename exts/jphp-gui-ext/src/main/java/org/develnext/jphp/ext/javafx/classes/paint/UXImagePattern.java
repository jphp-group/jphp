package org.develnext.jphp.ext.javafx.classes.paint;

import javafx.scene.image.Image;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Stop;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "paint\\UXImagePattern")
public class UXImagePattern extends UXPaint<ImagePattern>{
    public UXImagePattern(Environment env, ImagePattern wrappedObject) {
        super(env, wrappedObject);
    }
    public UXImagePattern(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    interface WrappedInterface{
        @Reflection.Getter double getWidth();
        @Reflection.Getter double getHeight();

        @Reflection.Getter double getX();
        @Reflection.Getter double getY();

        @Reflection.Getter Image getImage();

        @Reflection.Getter("proportional") boolean isProportional();
    }

    @Reflection.Signature
    public void __construct(Image image, double x, double y, double width, double height, boolean proportional){
        __wrappedObject = new ImagePattern(image, x, y, width, height, proportional);
    }
    @Reflection.Signature
    public void __construct(Image image){
        __wrappedObject = new ImagePattern(image);
    }
}