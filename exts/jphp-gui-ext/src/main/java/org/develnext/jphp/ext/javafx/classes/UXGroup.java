package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.UXParent;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "UXGroup")
public class UXGroup<T extends Group> extends UXParent<Group> {
    interface WrappedInterface {
    }

    public UXGroup(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXGroup(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Group();
    }

    @Signature
    public void __construct(List<Node> children) {
        __wrappedObject = new Group(children.toArray(new Node[children.size()]));
    }
}
