package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXPasswordField;
import org.develnext.jphp.ext.javafx.classes.UXTextField;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialPasswordField extends UXPasswordField {
    interface WrappedInterface {
    }

    public UXMaterialPasswordField(Environment env, JFXPasswordField wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialPasswordField(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public void __construct() {
        __wrappedObject = new JFXPasswordField();
    }

    @Override
    @Signature
    public void __construct(String text) {
        __wrappedObject = new JFXPasswordField();
        getWrappedObject().setText(text);
    }

    @Override
    public JFXPasswordField getWrappedObject() {
        return (JFXPasswordField) super.getWrappedObject();
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
