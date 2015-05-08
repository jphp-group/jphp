package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ProgressIndicator;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXProgressIndicator")
public class UXProgressIndicator extends UXControl {
    interface WrappedInterface {
        @Property double progress();
        @Property boolean indeterminate();
    }

    public UXProgressIndicator(Environment env, ProgressIndicator wrappedObject) {
        super(env, wrappedObject);
    }

    public UXProgressIndicator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ProgressIndicator getWrappedObject() {
        return (ProgressIndicator) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ProgressIndicator();
    }

    @Signature
    public void __construct(double progress) {
        __wrappedObject = new ProgressIndicator(progress);
    }
}
