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

@Reflection.Name(JavaFXExtension.NS + "UXSplitMenuButton")
public class UXSplitMenuButton extends UXMenuButton<SplitMenuButton> {

    public UXSplitMenuButton(Environment env, SplitMenuButton wrappedObject) {
        super(env, wrappedObject);
    }

    public UXSplitMenuButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    @Override
    public void __construct() {
        __wrappedObject = new SplitMenuButton();
    }

    @Signature
    @Override
    public void __construct(String text) {
        __construct();
        getWrappedObject().setText(text);
    }

    @Signature
    @Override
    public void __construct(String text, @Nullable Node graphic) {
        __construct(text);
        setGraphic(graphic);

        getWrappedObject().fire();
    }
}
