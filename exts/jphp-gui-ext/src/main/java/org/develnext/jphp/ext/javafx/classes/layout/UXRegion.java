package org.develnext.jphp.ext.javafx.classes.layout;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.UXParent;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "layout\\UXRegion")
public class UXRegion<T extends Region> extends UXParent<Region> {
    interface WrappedInterface {
        @Property double maxWidth();
        @Property double maxHeight();
        @Property double minWidth();
        @Property double minHeight();

        @Property(hiddenInDebugInfo = true) Insets padding();
    }

    public UXRegion(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXRegion(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Region();
    }

    @Getter(hiddenInDebugInfo = true)
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
    public double[] getPrefSize() {
        double w = getWrappedObject().getPrefWidth();
        double h = getWrappedObject().getPrefHeight();
        return new double[] {w, h};
    }

    @Setter
    public void setPrefSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setPrefSize(size[0], size[1]);
        }
    }

    @Getter
    public double getPrefWidth() {
        return getWrappedObject().getPrefWidth();
    }

    @Setter
    public void setPrefWidth(double value) {
        getWrappedObject().setPrefWidth(value);
    }

    @Getter
    public double getPrefHeight() {
        return getWrappedObject().getPrefHeight();
    }

    @Setter
    public void setPrefHeight(double value) {
        getWrappedObject().setPrefHeight(value);
    }

    @Getter
    public double getPaddingLeft() {
        return getWrappedObject().getPadding().getLeft();
    }

    @Setter
    public void setPaddingLeft(double value) {
        getWrappedObject().setPadding(new Insets(
                getPaddingTop(), getPaddingRight(), getPaddingBottom(), value
        ));
    }

    @Getter
    public double getPaddingRight() {
        return getWrappedObject().getPadding().getRight();
    }

    @Setter
    public void setPaddingRight(double value) {
        getWrappedObject().setPadding(new Insets(
                getPaddingTop(), value, getPaddingBottom(), getPaddingLeft()
        ));
    }

    @Getter
    public double getPaddingTop() {
        return getWrappedObject().getPadding().getTop();
    }

    @Setter
    public void setPaddingTop(double value) {
        getWrappedObject().setPadding(new Insets(
                value, getPaddingRight(), getPaddingBottom(), getPaddingLeft()
        ));
    }

    @Getter
    public double getPaddingBottom() {
        return getWrappedObject().getPadding().getBottom();
    }

    @Setter
    public void setPaddingBottom(double value) {
        getWrappedObject().setPadding(new Insets(
                getPaddingTop(), getPaddingRight(), value, getPaddingLeft()
        ));
    }

    @Override
    @Signature
    public void setSize(double[] args) {
        if (args.length >= 2) {
            getWrappedObject().setPrefWidth(args[0]);
            getWrappedObject().setPrefHeight(args[1]);
        }
    }

    @Getter(hiddenInDebugInfo = true)
    public double[] getMaxSize() {
        return new double[] { getWrappedObject().getMaxWidth(), getWrappedObject().getMaxHeight() };
    }

    @Setter
    public void setMaxSize(double[] args) {
        if (args.length >= 2) {
            getWrappedObject().setMaxSize(args[0], args[1]);
        }
    }


    @Setter
    public void setBackgroundColor(@Nullable Color color) {
        if (color == null) {
            getWrappedObject().setBackground(null);
        } else {
            getWrappedObject().setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    @Getter
    public Color getBackgroundColor() {
        Background background = getWrappedObject().getBackground();

        if (background != null && background.getFills().size() > 0) {
            Paint fill = background.getFills().get(0).getFill();
            if (fill instanceof Color) {
                return (Color) fill;
            }
        }

        return null;
    }
}
