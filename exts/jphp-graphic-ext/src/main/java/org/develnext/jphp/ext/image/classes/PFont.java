package org.develnext.jphp.ext.image.classes;

import org.develnext.jphp.ext.image.GraphicExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.util.List;

@Reflection.Name("Font")
@Reflection.Namespace(GraphicExtension.NS)
public class PFont extends BaseObject {
    private Font font;

    public PFont(Environment env, Font font) {
        super(env);
        this.font = font;
    }

    public PFont(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Font getFont() {
        return font;
    }

    @Signature
    public void __construct(String name, int size) {
        __construct(name, size, false);
    }

    @Signature
    public void __construct(String name, int size, boolean bold) {
        __construct(name, size, bold, false);
    }

    @Signature
    public void __construct(String name, int size, boolean bold, boolean italic) {
        int flags = Font.PLAIN;

        if (bold) flags |= Font.BOLD;
        if (italic) flags |= Font.ITALIC;

        font = new Font(name, flags, size);
    }

    @Signature
    public boolean canDisplay(char ch) {
        return getFont().canDisplay(ch);
    }

    @Getter
    public String getName() {
        return getFont().getName();
    }

    @Getter
    public String getFontName() {
        return getFont().getFontName();
    }

    @Getter
    public String getFamily() {
        return getFont().getFamily();
    }

    @Getter
    public int getSize() {
        return getFont().getSize();
    }

    @Getter
    public boolean isBold() {
        return getFont().isBold();
    }

    @Getter
    public boolean isItalic() {
        return getFont().isItalic();
    }

    @Getter
    public boolean isPlain() {
        return getFont().isPlain();
    }

    @Signature
    public PFont asBold(Environment env) {
        return new PFont(env, getFont().deriveFont(getFont().getStyle() | Font.BOLD));
    }

    @Signature
    public PFont asItalic(Environment env) {
        return new PFont(env, getFont().deriveFont(getFont().getStyle() | Font.ITALIC));
    }

    @Signature
    public PFont asWithSize(Environment env, float size) {
        return new PFont(env, getFont().deriveFont(size));
    }

    @Signature
    public static ArrayMemory allFonts(Environment env) {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();

        Font[] allFonts = e.getAllFonts();
        ArrayMemory result = ArrayMemory.createListed(allFonts.length);

        for (Font font : allFonts) {
            result.add(new PFont(env, font));
        }

        return result.toConstant();
    }

    @Signature
    public static boolean register(PFont font) {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return e.registerFont(font.getFont());
    }
}
