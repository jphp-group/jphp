package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXCheckbox;
import org.develnext.jphp.ext.javafx.classes.UXComboBox;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(JFoenixExtension.NS)
public class UXMaterialComboBox extends UXComboBox {
    interface WrappedInterface {
        @Property
        boolean labelFloat();
    }

    public UXMaterialComboBox(Environment env, JFXComboBox wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialComboBox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXComboBox<>();
    }

    @Override
    public JFXComboBox getWrappedObject() {
        return (JFXComboBox) super.getWrappedObject();
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
