package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXIndexedCell")
public class UXCell<T extends IndexedCell> extends UXLabeled<Cell> {
    interface WrappedInterface {
        @Property Object item();

        @Property boolean editable();
        @Property boolean editing();
        @Property boolean selected();
        @Property boolean empty();
    }

    public UXCell(Environment env, IndexedCell wrappedObject) {
        super(env, wrappedObject);
    }

    public UXCell(Environment env, Cell wrappedObject) {
        super(env, wrappedObject);
    }

    public UXCell(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Cell<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public void startEdit() {
        getWrappedObject().startEdit();
    }

    @Signature
    public void cancelEdit() {
        getWrappedObject().cancelEdit();
    }

    @Signature
    public void commitEdit(Object newValue) {
        getWrappedObject().commitEdit(newValue);
    }

    @Signature
    public void updateSelected(boolean value) {
        getWrappedObject().updateSelected(value);
    }
}
