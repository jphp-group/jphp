package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXTabPane;
import org.develnext.jphp.ext.javafx.classes.UXTabPane;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import org.develnext.jphp.ext.javafx.jfoenix.support.JFXTabPaneFixed;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialTabPane extends UXTabPane {
    interface WrappedInterface {
    }

    public UXMaterialTabPane(Environment env, JFXTabPaneFixed wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialTabPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXTabPaneFixed();
    }

    @Override
    public JFXTabPaneFixed getWrappedObject() {
        return (JFXTabPaneFixed) super.getWrappedObject();
    }

    @Getter
    public boolean getDisableAnimation() {
        return getWrappedObject().isDisableAnimation();
    }

    @Reflection.Setter
    public void setDisableAnimation(boolean value) {
        getWrappedObject().setDisableAnimation(value);
    }
}
