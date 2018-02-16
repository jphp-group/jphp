package org.develnext.jphp.ext.image.classes;

import org.develnext.jphp.ext.image.GraphicExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

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
}
