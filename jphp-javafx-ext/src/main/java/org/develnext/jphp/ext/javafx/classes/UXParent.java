package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.Parent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXParent")
public class UXParent extends UXNode {
    interface WrappedInterface {
        void layout();
        void requestLayout();
    }

    public UXParent(Environment env, Parent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXParent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Parent getWrappedObject() {
        return (Parent) super.getWrappedObject();
    }
}
