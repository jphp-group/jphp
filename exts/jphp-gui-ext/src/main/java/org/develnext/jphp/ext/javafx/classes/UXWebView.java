package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXWebView")
public class UXWebView extends UXParent {
    interface WrappedInterface {
        @Property boolean contextMenuEnabled();
        @Property double fontScale();
        @Property FontSmoothingType fontSmoothingType();

        @Property double maxHeight();
        @Property double maxWidth();
        @Property double minHeight();
        @Property double minWidth();

        @Property double zoom();

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

    @Getter
    public String getUrl() {
        return getWrappedObject().getEngine().getLocation();
    }

    @Setter
    public void setUrl(String url) {
        getWrappedObject().getEngine().load(url);
    }

    @Signature
    @Override
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            setWidth(size[0]);
            setHeight(size[1]);
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

    @Getter
    public double[] getMinSize() {
        return new double[] { getWrappedObject().getMinWidth(), getWrappedObject().getMinHeight() };
    }

    @Setter
    public void setMinSize(double[] args) {
        if (args.length >= 2) {
            getWrappedObject().setMinSize(args[0], args[1]);
        }
    }

    @Getter
    public double[] getMaxSize() {
        return new double[] { getWrappedObject().getMaxWidth(), getWrappedObject().getMaxHeight() };
    }

    @Setter
    public void setMaxSize(double[] args) {
        if (args.length >= 2) {
            getWrappedObject().setMaxSize(args[0], args[1]);
        }
    }
}
