package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.UXControl;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "layout\\UXScrollPane")
public class UXScrollPane<T extends ScrollPane> extends UXControl<ScrollPane> {
    interface WrappedInterface {
        @Property Node content();
        @Property Bounds viewportBounds();

        @Property boolean fitToWidth();
        @Property boolean fitToHeight();

        @Property ScrollPane.ScrollBarPolicy vbarPolicy();
        @Property ScrollPane.ScrollBarPolicy hbarPolicy();

        @Property("scrollMaxX") double hmax();
        @Property("scrollMaxY") double vmax();
        @Property("scrollMinX") double hmin();
        @Property("scrollMinY") double vmin();
    }

    public UXScrollPane(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXScrollPane(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ScrollPane();
    }

    @Signature
    public void __construct(@Reflection.Nullable Node content) {
        __wrappedObject = new ScrollPane(content);
    }

    @Override
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Getter
    public double getScrollX() {
        return getWrappedObject().getHvalue();
    }

    @Setter
    public void setScrollX(double value) {
        getWrappedObject().setHvalue(value);
    }

    @Getter
    public double getScrollY() {
        return getWrappedObject().getVvalue();
    }

    @Setter
    public void setScrollY(double value) {
        getWrappedObject().setVvalue(value);
    }

    @Signature
    public void scrollToNode(Node node) {
        T scrollPane = getWrappedObject();

        // total content height
        double h = scrollPane.getContent().getBoundsInLocal().getHeight();
        double w = scrollPane.getContent().getBoundsInLocal().getWidth();

        // center y of content
        double y = (node.getBoundsInParent().getMaxY() +  node.getBoundsInParent().getMinY()) / 2.0;
        double x = (node.getBoundsInParent().getMaxX() +  node.getBoundsInParent().getMinX()) / 2.0;

        // height of viewPort
        double viewHeight = scrollPane.getViewportBounds().getHeight();
        double viewWidth = scrollPane.getViewportBounds().getWidth();

        scrollPane.setVvalue(scrollPane.getVmax() * ((y - 0.5 * viewHeight) / (h - viewHeight)));
        scrollPane.setHvalue(scrollPane.getVmax() * ((x - 0.5 * viewWidth) / (w - viewWidth)));
    }

    /*@Signature
    public void scrollToNode(Node node) {
        double width = getWrappedObject().getContent().getBoundsInLocal().getWidth();
        double height = getWrappedObject().getContent().getBoundsInLocal().getHeight();

        double x = node.getBoundsInParent().getMaxX();
        double y = node.getBoundsInParent().getMaxY();

        // scrolling values range from 0 to 1
        getWrappedObject().setVvalue(y / height);
        getWrappedObject().setHvalue(x/width);
    } */
}
