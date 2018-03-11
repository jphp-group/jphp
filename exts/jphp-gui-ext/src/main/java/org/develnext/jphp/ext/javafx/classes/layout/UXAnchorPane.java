package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "layout\\UXAnchorPane")
public class UXAnchorPane<T extends AnchorPane> extends UXPane<AnchorPane> {
    interface WrappedInterface {

    }

    public UXAnchorPane(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXAnchorPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new AnchorPane();
    }

    @Signature
    public static void setLeftAnchor(Node node, double value) {
        AnchorPane.setLeftAnchor(node, value);
    }

    @Signature
    public static void setRightAnchor(Node node, double value) {
        AnchorPane.setRightAnchor(node, value);
    }

    @Signature
    public static void setTopAnchor(Node node, double value) {
        AnchorPane.setTopAnchor(node, value);
    }

    @Signature
    public static void setBottomAnchor(Node node, double value) {
        AnchorPane.setBottomAnchor(node, value);
    }

    @Signature
    public static double getLeftAnchor(Node node) {
        return AnchorPane.getLeftAnchor(node);
    }

    @Signature
    public static double getRightAnchor(Node node) {
        return AnchorPane.getRightAnchor(node);
    }

    @Signature
    public static double getTopAnchor(Node node) {
        return AnchorPane.getTopAnchor(node);
    }

    @Signature
    public static double getBottomAnchor(Node node) {
        return AnchorPane.getBottomAnchor(node);
    }

    @Signature
    public static void setAnchor(Node node, double value) {
        AnchorPane.setBottomAnchor(node, value);
        AnchorPane.setTopAnchor(node, value);
        AnchorPane.setRightAnchor(node, value);
        AnchorPane.setLeftAnchor(node, value);
    }
}
