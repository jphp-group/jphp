package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.scene.layout.StackPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NAMESPACE + "layout\\UXStackPane")
public class UXStackPane extends UXPane {
    interface WrappedInterface {

    }

    public UXStackPane(Environment env, StackPane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXStackPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new StackPane();
    }
}
