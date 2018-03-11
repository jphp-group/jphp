package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXTextField;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialTextField extends UXTextField<JFXTextField> {
    interface WrappedInterface {
    }

    public UXMaterialTextField(Environment env, JFXTextField wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialTextField(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public void __construct() {
        __wrappedObject = new JFXTextField();
    }

    @Override
    @Signature
    public void __construct(String text) {
        __wrappedObject = new JFXTextField(text);
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
