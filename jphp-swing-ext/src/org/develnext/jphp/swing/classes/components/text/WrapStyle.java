package org.develnext.jphp.swing.classes.components.text;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapColor;
import org.develnext.jphp.swing.classes.components.support.RootObject;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "text\\Style")
public class WrapStyle extends RootObject {
    protected Style style;

    public WrapStyle(Environment env, Style style) {
        super(env);
        this.style = style;
    }

    public WrapStyle(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Style getStyle() {
        return style;
    }

    @Signature(@Arg(value = "value", optional = @Optional("null")))
    protected Memory __setBold(Environment env, Memory... args) {
        if (args[0].isNull())
            style.removeAttribute(StyleConstants.Bold);
        else
            StyleConstants.setBold(style, args[0].toBoolean());

        return Memory.NULL;
    }

    @Signature
    protected Memory __getBold(Environment env, Memory... args) {
        return TrueMemory.valueOf(StyleConstants.isBold(style));
    }

    @Signature(@Arg(value = "value", optional = @Optional("null")))
    protected Memory __setBackground(Environment env, Memory... args) {
        if (args[0].isNull())
            style.removeAttribute(StyleConstants.Background);
        else
            StyleConstants.setBackground(style, WrapColor.of(args[0]));

        return Memory.NULL;
    }

    @Signature
    protected Memory __getBackground(Environment env, Memory... args) {
        Color r = StyleConstants.getBackground(style);
        if (r == null)
            return Memory.NULL;

        return new ObjectMemory(new WrapColor(env, r));
    }


    @Signature(@Arg(value = "value", optional = @Optional("null")))
    protected Memory __setForeground(Environment env, Memory... args) {
        if (args[0].isNull())
            style.removeAttribute(StyleConstants.Foreground);
        else
            StyleConstants.setForeground(style, WrapColor.of(args[0]));

        return Memory.NULL;
    }

    @Signature
    protected Memory __getForeground(Environment env, Memory... args) {
        Color r = StyleConstants.getForeground(style);
        if (r == null)
            return Memory.NULL;

        return new ObjectMemory(new WrapColor(env, r));
    }
}
