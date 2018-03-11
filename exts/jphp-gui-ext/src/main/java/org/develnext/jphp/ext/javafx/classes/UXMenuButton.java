package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXMenuButton")
public class UXMenuButton<T extends MenuButton> extends UXButtonBase<MenuButton> {
    interface WrappedInterface {
        @Property ObservableList<MenuItem> items();
        @Property Side popupSide();
    }

    public UXMenuButton(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMenuButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new MenuButton();
    }

    @Signature
    public void __construct(String text) {
        __wrappedObject = new MenuButton(text);
    }

    @Signature
    public void __construct(String text, @Nullable Node graphic) {
        __wrappedObject = new MenuButton(text, graphic);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public void showMenu() {
        getWrappedObject().show();
    }

    @Signature
    public void hideMenu() {
        getWrappedObject().hide();
    }
}
