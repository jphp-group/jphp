package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXColorPicker;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXColorPicker;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialColorPicker extends UXColorPicker {
    interface WrappedInterface {
    }

    public UXMaterialColorPicker(Environment env, JFXColorPicker wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialColorPicker(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXColorPicker();
    }

    @Override
    @Reflection.Signature
    public void __construct(@Reflection.Nullable Color text) {
        __wrappedObject = new JFXColorPicker(text);
    }

    @Override
    public JFXColorPicker getWrappedObject() {
        return (JFXColorPicker) super.getWrappedObject();
    }
}
