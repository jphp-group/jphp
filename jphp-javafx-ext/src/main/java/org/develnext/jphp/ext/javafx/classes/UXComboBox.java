package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXComboBox")
public class UXComboBox extends UXComboBoxBase {
    interface WrappedInterface {
        @Property ObservableList items();
        @Property int visibleRowCount();
    }

    public UXComboBox(Environment env, ComboBoxBase wrappedObject) {
        super(env, wrappedObject);
    }

    public UXComboBox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ComboBox getWrappedObject() {
        return (ComboBox) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ComboBox();
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void __construct(Environment env, ForeachIterator iterator) {
        while (iterator.next()) {
            getWrappedObject().getItems().add(Memory.unwrap(env, iterator.getValue()));
        }
    }
}
