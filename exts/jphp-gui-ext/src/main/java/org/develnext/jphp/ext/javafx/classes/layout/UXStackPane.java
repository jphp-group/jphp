package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXStackPane")
public class UXStackPane<T extends StackPane> extends UXPane<T> {
    interface WrappedInterface {

    }

    public UXStackPane(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXStackPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new StackPane();
    }

    @Signature
    public void __construct(List<Node> children) {
        __wrappedObject = new StackPane(children.toArray(new Node[children.size()]));
    }
}
