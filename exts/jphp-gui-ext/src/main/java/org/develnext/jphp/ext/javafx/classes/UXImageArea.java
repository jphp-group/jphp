package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;
import org.develnext.jphp.ext.javafx.support.ImageViewEx;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXImageArea")
public class UXImageArea extends UXCanvas<ImageViewEx> {
    interface WrappedInterface {
        @Property boolean centered();
        @Property boolean stretch();
        @Property boolean smartStretch();
        @Property boolean autoSize();
        @Property boolean proportional();
        @Property boolean mosaic();
        @Property double mosaicGap();
        @Property boolean preserveRatio();
        @Property String text();

        @Property boolean flipX();
        @Property boolean flipY();

        @Property @Nullable Image image();
        @Property @Nullable Image hoverImage();
        @Property @Nullable Image clickImage();
    }
    public UXImageArea(Environment env, ImageViewEx wrappedObject) {
        super(env, wrappedObject);
    }

    public UXImageArea(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ImageViewEx();
    }

    @Signature
    public void __construct(@Nullable Image image) {
        __wrappedObject = new ImageViewEx();
        getWrappedObject().setImage(image);
    }

    @Getter
    public UXFont getFont(Environment env) {
        return new UXFont(env, getWrappedObject().getFont(), font -> getWrappedObject().setFont(font));
    }

    @Setter
    public void setFont(Font font) {
        getWrappedObject().setFont(font);
    }

    @Setter
    public void setTextColor(Color color) {
        getWrappedObject().setTextFill(color);
    }

    @Getter
    public Color getTextColor() {
        Paint textFill = getWrappedObject().getTextFill();

        if (textFill instanceof Color) {
            return (Color) textFill;
        }

        return null;
    }

    @Setter
    public void setBackgroundColor(@Nullable Color color) {
        getWrappedObject().setBackground(color);
    }

    @Getter
    public Color getBackgroundColor() {
        Paint textFill = getWrappedObject().getBackground();

        if (textFill instanceof Color) {
            return (Color) textFill;
        }

        return null;
    }
}
