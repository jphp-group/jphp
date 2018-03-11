package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXTextArea;
import org.develnext.jphp.ext.javafx.classes.UXTextField;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import org.develnext.jphp.ext.javafx.jfoenix.support.JFXTextAreaFixed;
import org.develnext.jphp.ext.javafx.jfoenix.support.JFXTextAreaSkin;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialTextArea extends UXTextArea {
    interface WrappedInterface {
    }

    public UXMaterialTextArea(Environment env, JFXTextAreaFixed wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialTextArea(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public void __construct() {
        __wrappedObject = new JFXTextAreaFixed();
    }

    @Override
    @Signature
    public void __construct(String text) {
        __wrappedObject = new JFXTextAreaFixed(text);
    }

    @Override
    public JFXTextAreaFixed getWrappedObject() {
        return (JFXTextAreaFixed) super.getWrappedObject();
    }

    @Getter
    public Color getFocusColor() {
        return (Color) getWrappedObject().getFocusColor();
    }

    @Getter
    public Color getUnfocusColor() {
        return (Color) getWrappedObject().getUnFocusColor();
    }

    @Setter
    public void setFocusColor(Color value) {
        getWrappedObject().setFocusColor(value);
    }

    @Setter
    public void setUnfocusColor(Color value) {
        getWrappedObject().setUnFocusColor(value);
    }
}
