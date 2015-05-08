package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "UXWebView")
public class UXWebView extends UXParent {
    interface WrappedInterface {
        @Property boolean contextMenuEnabled();
        @Property double fontScale();
        @Property FontSmoothingType fontSmoothingType();

        @Property double maxHeight();
        @Property double maxWidth();
        @Property double minHeight();
        @Property double minWidth();

        @Property WebEngine engine();
    }

    public UXWebView(Environment env, WebView wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWebView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public WebView getWrappedObject() {
        return (WebView) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new WebView();
    }

    @Signature
    @Override
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setPrefSize(size[0], size[1]);
        }
    }

    @Signature
    @Override
    protected double[] getSize() {
        return new double[] { getWrappedObject().getPrefWidth(), getWrappedObject().getPrefHeight() };
    }

    @Signature
    @Override
    protected double getWidth() {
        return getWrappedObject().getPrefWidth();
    }

    @Signature
    @Override
    protected void setWidth(double v) {
        getWrappedObject().setPrefWidth(v);
    }

    @Signature
    @Override
    protected double getHeight() {
        return getWrappedObject().getPrefHeight();
    }

    @Signature
    @Override
    protected void setHeight(double v) {
        getWrappedObject().setPrefHeight(v);
    }
}
