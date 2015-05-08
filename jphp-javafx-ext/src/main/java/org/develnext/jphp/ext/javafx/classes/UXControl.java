package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXControl")
public class UXControl extends UXParent {
    interface WrappedInterface {
        @Property boolean resizable();

        @Getter ContextMenu getContextMenu();
        @Setter void setContextMenu(@Nullable ContextMenu menu);

        @Getter Tooltip getTooltip();
        @Setter void setTooltip(@Nullable Tooltip tooltip);
    }

    public UXControl(Environment env, Control wrappedObject) {
        super(env, wrappedObject);
    }

    public UXControl(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Control getWrappedObject() {
        return (Control) super.getWrappedObject();
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
