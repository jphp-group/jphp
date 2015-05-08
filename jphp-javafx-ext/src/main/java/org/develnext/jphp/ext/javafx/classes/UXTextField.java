package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXTextField")
public class UXTextField extends UXTextInputControl {
    interface WrappedInterface {
        @Property Pos alignment();
        @Property int prefColumnCount();
    }

    public UXTextField(Environment env, TextField wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTextField(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public TextField getWrappedObject() {
        return (TextField) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TextField();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new TextField(text);
    }
}
