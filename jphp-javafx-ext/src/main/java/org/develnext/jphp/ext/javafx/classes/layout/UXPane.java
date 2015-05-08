package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.UXParent;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "layout\\UXPane")
public class UXPane extends UXParent {
    interface WrappedInterface {

    }

    public UXPane(Environment env, Pane wrappedObject) {
        super(env, wrappedObject);
    }

    public UXPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Pane getWrappedObject() {
        return (Pane) super.getWrappedObject();
    }

    @Getter
    public ObservableList<Node> getChildren() {
        return getWrappedObject().getChildren();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Pane();
    }
}
