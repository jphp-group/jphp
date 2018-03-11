package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.layout.UXRegion;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NS + "UXControl")
public class UXControl<T extends Control> extends UXRegion<Control> {
    interface WrappedInterface {
        @Property boolean resizable();

        @Property @Nullable ContextMenu contextMenu();
        @Property @Nullable Tooltip tooltip();
    }

    public UXControl(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXControl(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Setter
    public void setTooltipText(Memory value) {
        if (value.isNull()) {
            if (getWrappedObject().tooltipProperty().isBound()) {
                getWrappedObject().tooltipProperty().unbind();
            }

            getWrappedObject().setTooltip(null);
            return;
        }

        Tooltip tooltip = getWrappedObject().getTooltip();

        if (tooltip == null) {
            tooltip = new Tooltip();

            if (getWrappedObject().tooltipProperty().isBound()) {
                getWrappedObject().tooltipProperty().unbind();
            }

            getWrappedObject().setTooltip(tooltip);
        }

        tooltip.setText(value.toString());
    }

    @Getter(hiddenInDebugInfo = true)
    public String getTooltipText() {
        Tooltip tooltip = getWrappedObject().getTooltip();

        if (tooltip != null) {
            return tooltip.getText();
        }

        return null;
    }

    @Signature
    @Override
    public void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setPrefSize(size[0], size[1]);
            getWrappedObject().resize(size[0], size[1]);
        }
    }

    @Signature
    @Override
    protected double[] getSize() {
        return new double[] { getWidth(), getHeight() };
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
