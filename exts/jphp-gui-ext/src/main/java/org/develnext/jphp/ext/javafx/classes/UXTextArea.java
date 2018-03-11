package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.TextArea;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXTextArea")
public class UXTextArea extends UXTextInputControl {
    interface WrappedInterface {
        @Property boolean wrapText();
        @Property double scrollLeft();
        @Property double scrollTop();
        @Property int prefColumnCount();
        @Property int prefRowCount();
    }

    public UXTextArea(Environment env, TextArea wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTextArea(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public TextArea getWrappedObject() {
        return (TextArea) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TextArea();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new TextArea(text);
    }
}
