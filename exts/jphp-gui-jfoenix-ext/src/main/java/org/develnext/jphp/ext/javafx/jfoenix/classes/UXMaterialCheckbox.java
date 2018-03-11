package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXCheckBox;

import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXCheckbox;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(JFoenixExtension.NS)
public class UXMaterialCheckbox extends UXCheckbox {
    public UXMaterialCheckbox(Environment env, JFXCheckBox wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialCheckbox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXCheckBox();
    }

    @Override
    @Reflection.Signature
    public void __construct(String text) {
        __wrappedObject = new JFXCheckBox(text);
    }

    @Override
    public JFXCheckBox getWrappedObject() {
        return (JFXCheckBox) super.getWrappedObject();
    }

    @Getter
    public Color getCheckedColor() {
        return (Color) getWrappedObject().getCheckedColor();
    }

    @Getter
    public Color getUncheckedColor() {
        return (Color) getWrappedObject().getUnCheckedColor();
    }

    @Setter
    public void setCheckedColor(Color value) {
        getWrappedObject().setCheckedColor(value);
    }

    @Setter
    public void setUncheckedColor(Color value) {
        getWrappedObject().setUnCheckedColor(value);
    }
}
