package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXVBox")
public class UXVBox<T extends VBox> extends UXPane<VBox> {
    interface WrappedInterface {
        @Property double spacing();
        @Property Pos alignment();
        @Property boolean fillWidth();

        void requestLayout();
    }

    public UXVBox(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXVBox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new VBox();
    }

    @Signature
    public void __construct(List<Node> children) {
        __construct(children, 0);
    }

    @Signature
    public void __construct(List<Node> children, double spacing) {
        __wrappedObject = new VBox(children.toArray(new Node[children.size()]));
        getWrappedObject().setSpacing(spacing);
    }

    @Signature
    public static void setVgrow(Node node, @Reflection.Nullable Priority value) {
        VBox.setVgrow(node, value);
    }

    @Signature
    public static Priority getVgrow(Node node) {
        return VBox.getVgrow(node);
    }

    @Signature
    public static void setMargin(Node node, Insets insets) {
        VBox.setMargin(node, insets);
    }
}
