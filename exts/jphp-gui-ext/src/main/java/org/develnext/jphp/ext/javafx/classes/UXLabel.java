package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXLabel")
public class UXLabel<T extends Label> extends UXLabeled<Label> {
    interface WrappedInterface {
        @Property Node labelFor();
    }

    public UXLabel(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXLabel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Label();
        getWrappedObject().setMnemonicParsing(false);
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new Label(text);
        getWrappedObject().setMnemonicParsing(false);
    }

    @Signature
    public void __construct(String text, @Nullable Node graphic) {
        __wrappedObject = new Label(text, graphic);
        getWrappedObject().setMnemonicParsing(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }
}
