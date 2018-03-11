package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXDatePicker;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXColorPicker;
import org.develnext.jphp.ext.javafx.classes.UXDatePicker;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialDatePicker extends UXDatePicker {
    interface WrappedInterface {
        @Reflection.Property("overlay") boolean overLay();
    }

    public UXMaterialDatePicker(Environment env, JFXDatePicker wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialDatePicker(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXDatePicker();
    }

    @Override
    public JFXDatePicker getWrappedObject() {
        return (JFXDatePicker) super.getWrappedObject();
    }

    @Reflection.Getter
    public Color getDefaultColor() {
        return (Color) getWrappedObject().getDefaultColor();
    }

    @Reflection.Setter
    public void setDefaultColor(@Reflection.Nullable Color color) {
        getWrappedObject().setDefaultColor(color);
    }
}
