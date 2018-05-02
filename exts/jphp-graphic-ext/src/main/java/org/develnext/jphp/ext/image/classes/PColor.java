package org.develnext.jphp.ext.image.classes;

import org.develnext.jphp.ext.image.GraphicExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

@Name("Color")
@Namespace(GraphicExtension.NS)
public class PColor extends BaseObject {
    private Color color;

    public PColor(Environment env, String color) {
        super(env);
        this.color = Color.decode(color);
    }

    public PColor(Environment env, Color color) {
        super(env);
        this.color = color;
    }

    public PColor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Color getColor() {
        return color;
    }

    @Signature
    public void __construct(Memory value) {
        color = Color.decode(value.toString());
    }

    @Signature
    public static Color ofRGB(int red, int blue, int green) {
        return new Color(red, blue, green);
    }

    @Signature
    public static Color ofRGB(int red, int blue, int green, float opacity) {
        return new Color(red, blue, green, opacity);
    }

    @Signature
    public String toHexString() {
        return String.format("%02x%02x%02x", getRed(), getGreen(), getBlue());
    }

    @Signature
    public Color brighter() {
        return color.brighter();
    }

    @Signature
    public Color darker() {
        return color.darker();
    }

    @Getter
    public int getRed() {
        return color.getRed();
    }

    @Getter
    public int getGreen() {
        return color.getGreen();
    }

    @Getter
    public int getBlue() {
        return color.getBlue();
    }

    @Getter
    public int getAlpha() {
        return color.getAlpha();
    }

    @Getter
    public int getRgb() {
        return color.getRGB();
    }
}
