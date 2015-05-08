package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXImageView")
public class UXImageView extends UXNode {
    interface WrappedInterface {
        @Property boolean smooth();
        @Property boolean preserveRatio();
        @Property Image image();
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
}
