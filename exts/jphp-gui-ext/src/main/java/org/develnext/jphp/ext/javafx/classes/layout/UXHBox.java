package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXHBox")
public class UXHBox<T extends HBox> extends UXPane<T> {
    interface WrappedInterface {
        @Property double spacing();
        @Property Pos alignment();
        @Property boolean fillHeight();

        void requestLayout();
    }

    public UXHBox(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXHBox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new HBox();
    }

    @Signature
    public void __construct(List<Node> children) {
        __construct(children, 0);
    }

    @Signature
    public void __construct(List<Node> children, double spacing) {
        __wrappedObject = new HBox(children.toArray(new Node[children.size()]));
        getWrappedObject().setSpacing(spacing);
    }

    @Signature
    public static void setHgrow(Node node, @Nullable Priority value) {
        HBox.setHgrow(node, value);
    }

    @Signature
    public static Priority getHgrow(Node node) {
        return HBox.getHgrow(node);
    }

    @Signature
    public static void setMargin(Node node, Insets insets) {
        HBox.setMargin(node, insets);
    }
}
