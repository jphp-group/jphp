package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import javafx.scene.control.ProgressIndicator;
import org.develnext.jphp.ext.javafx.classes.UXProgressIndicator;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Namespace(JFoenixExtension.NS)
public class UXMaterialProgressIndicator extends UXProgressIndicator {
    interface WrappedInterface {
        @Property double radius();
    }

    public UXMaterialProgressIndicator(Environment env, JFXSpinner wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialProgressIndicator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXSpinner();
    }

    @Override
    @Reflection.Signature
    public void __construct(double progress) {
        __wrappedObject = new JFXSpinner(progress);
    }

    @Override
    public JFXSpinner getWrappedObject() {
        return (JFXSpinner) super.getWrappedObject();
    }



}
