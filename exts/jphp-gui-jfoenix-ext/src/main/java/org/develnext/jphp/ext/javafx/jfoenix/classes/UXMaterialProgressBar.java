package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.javafx.classes.UXProgressBar;
import org.develnext.jphp.ext.javafx.classes.UXTextField;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(JFoenixExtension.NS)
public class UXMaterialProgressBar extends UXProgressBar {
    interface WrappedInterface {
    }

    public UXMaterialProgressBar(Environment env, JFXProgressBar wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialProgressBar(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public void __construct() {
        __wrappedObject = new JFXProgressBar();
    }

    @Override
    public void __construct(double progress) {
        __wrappedObject = new JFXProgressBar(progress);
    }

    @Override
    public JFXProgressBar getWrappedObject() {
        return (JFXProgressBar) super.getWrappedObject();
    }
}
