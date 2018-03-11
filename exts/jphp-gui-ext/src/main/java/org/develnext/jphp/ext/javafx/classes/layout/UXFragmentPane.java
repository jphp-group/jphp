package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.FragmentPane;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXFragmentPane")
public class UXFragmentPane<T extends FragmentPane> extends UXVBox<FragmentPane> {
    interface WrappedInterface {
        @Property
        Parent layout();

        void applyFragment(@Reflection.Nullable Stage form);
    }

    public UXFragmentPane(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXFragmentPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new FragmentPane();
    }

    @Signature
    public void __construct(List<Node> children) {
        __construct(children, 0);
    }

    @Signature
    public void __construct(List<Node> children, double spacing) {
        __wrappedObject = new FragmentPane(children.toArray(new Node[children.size()]));
        getWrappedObject().setSpacing(spacing);
    }
}
