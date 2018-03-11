package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXButton;
import org.develnext.jphp.ext.javafx.classes.UXToggleButton;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialToggleButton extends UXToggleButton {
    interface WrappedInterface {
    }

    public UXMaterialToggleButton(Environment env, JFXToggleButton wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialToggleButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXToggleButton();
    }

    @Override
    @Reflection.Signature
    public void __construct(String text) {
        __wrappedObject = new JFXToggleButton();
        getWrappedObject().setText(text);
    }

    @Override
    public JFXToggleButton getWrappedObject() {
        return (JFXToggleButton) super.getWrappedObject();
    }

    @Getter
    public Color getToggleColor() {
        return (Color) getWrappedObject().getToggleColor();
    }

    @Setter
    public void setToggleColor(Color color) {
        getWrappedObject().setToggleColor(color);
    }

    @Getter
    public Color getToggleLineColor() {
        return (Color) getWrappedObject().getToggleLineColor();
    }

    @Setter
    public void setToggleLineColor(Color color) {
        getWrappedObject().setToggleLineColor(color);
    }

    @Getter
    public Color getUnToggleColor() {
        return (Color) getWrappedObject().getUnToggleColor();
    }

    @Setter
    public void setUnToggleColor(Color color) {
        getWrappedObject().setUnToggleColor(color);
    }

    @Getter
    public Color getUnToggleLineColor() {
        return (Color) getWrappedObject().getUnToggleLineColor();
    }

    @Setter
    public void setUnToggleLineColor(Color color) {
        getWrappedObject().setUnToggleLineColor(color);
    }
}
