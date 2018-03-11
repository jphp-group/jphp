package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXButton;
import org.develnext.jphp.ext.javafx.classes.UXSlider;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialSlider extends UXSlider {
    interface WrappedInterface {
        @Property
        JFXSlider.IndicatorPosition indicatorPosition();
    }

    public UXMaterialSlider(Environment env, JFXSlider wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialSlider(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXSlider();
    }

    @Override
    public JFXSlider getWrappedObject() {
        return (JFXSlider) super.getWrappedObject();
    }
}
