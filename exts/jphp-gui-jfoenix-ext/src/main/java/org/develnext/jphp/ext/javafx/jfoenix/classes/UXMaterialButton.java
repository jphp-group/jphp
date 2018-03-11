package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXButton;
import org.develnext.jphp.ext.javafx.classes.UXNode;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialButton extends UXButton {
    interface WrappedInterface {
        @Property
        JFXButton.ButtonType buttonType();
    }

    public UXMaterialButton(Environment env, JFXButton wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXButton();
    }

    @Override
    @Reflection.Signature
    public void __construct(String text) {
        __wrappedObject = new JFXButton(text);
    }

    @Override
    @Reflection.Signature
    public void __construct(String text, @Reflection.Nullable Node graphic) {
        __wrappedObject = new JFXButton(text, graphic);
    }

    @Override
    public JFXButton getWrappedObject() {
        return (JFXButton) super.getWrappedObject();
    }

    @Getter
    public Color getRipplerFill() {
        return (Color) getWrappedObject().getRipplerFill();
    }

    @Setter
    public void setRipplerFill(Color color) {
        getWrappedObject().setRipplerFill(color);
    }
}
