package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXContextMenu")
public class UXContextMenu<T extends ContextMenu> extends UXPopupWindow<ContextMenu> {
    interface WrappedInterface {
        @Property ObservableList<MenuItem> items();
    }

    public UXContextMenu(Environment env, ContextMenu wrappedObject) {
        super(env, wrappedObject);
    }

    public UXContextMenu(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ContextMenu();
    }
}
