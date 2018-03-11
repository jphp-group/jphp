package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXButton")
public class UXButton extends UXButtonBase {
    interface WrappedInterface {
    }

    public UXButton(Environment env, Button wrappedObject) {
        super(env, wrappedObject);
    }

    public UXButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Button getWrappedObject() {
        return (Button) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Button();
        getWrappedObject().setMnemonicParsing(false);
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new Button(text);
    }
    @Signature
    public void __construct(String text, @Nullable Node graphic) {
        __wrappedObject = new Button(text, graphic);
    }
}
