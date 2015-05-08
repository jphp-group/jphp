package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXLabel")
public class UXLabel extends UXLabeled {
    interface WrappedInterface {
        @Property Node labelFor();
    }

    public UXLabel(Environment env, Label wrappedObject) {
        super(env, wrappedObject);
    }

    public UXLabel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Label();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new Label(text);
    }
    @Signature
    public void __construct(String text, Node graphic) {
        __wrappedObject = new Label(text, graphic);
    }

    @Override
    public Label getWrappedObject() {
        return (Label) super.getWrappedObject();
    }
}
