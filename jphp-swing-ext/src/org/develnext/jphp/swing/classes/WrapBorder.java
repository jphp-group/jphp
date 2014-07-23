package org.develnext.jphp.swing.classes;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.RootObject;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "Border")
public class WrapBorder extends RootObject {
    protected Border border;

    public WrapBorder(Environment env, Border border) {
        super(env);
        this.border = border;
    }

    public WrapBorder(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Border getBorder() {
        return border;
    }

    @Signature
    public Memory isOpaque(Environment env, Memory... args) {
        return border.isBorderOpaque() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("top"), @Arg("left"), @Arg("bottom"), @Arg("right")})
    public static Memory createEmpty(Environment env, Memory... args) {
        return new ObjectMemory(new WrapBorder(env, new EmptyBorder(
            args[0].toInteger(), args[1].toInteger(),
            args[2].toInteger(), args[3].toInteger()
        )));
    }

    @Signature({
            @Arg("type"),
            @Arg("highlightColor"),
            @Arg("shadowColor")
    })
    public static Memory createBevel(Environment env, Memory... args) {
        try {
            return new ObjectMemory(new WrapBorder(env, BorderFactory.createBevelBorder(
                    BevelBorder.class.getField(args[0].toString().toUpperCase()).getInt(null),
                    WrapColor.of(args[1]),
                    WrapColor.of(args[2])
            )));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Signature({
            @Arg("title"),
            @Arg(value = "border", nativeType = WrapBorder.class, optional = @Optional("null")),
            @Arg(value = "titleFont", nativeType = WrapFont.class, optional = @Optional("null")),
            @Arg(value = "titleColor", optional = @Optional("null"))
    })
    public static Memory createTitled(Environment env, Memory... args) {
        Border parent = null;
        if (!args[1].isNull())
            parent = args[1].toObject(WrapBorder.class).getBorder();

        Font font = null;
        if (!args[2].isNull())
            font = args[2].toObject(WrapFont.class).getFont();

        Color color = null;
        if (!args[3].isNull())
            color = WrapColor.of(args[3]);

        return new ObjectMemory(new WrapBorder(env, BorderFactory.createTitledBorder(
                parent,
                args[0].toString(),
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                font,
                color
        )));
    }

    @Signature({
            @Arg("color"),
            @Arg(value = "size", optional = @Optional("1")),
            @Arg(value = "rounded", optional = @Optional("false"))
    })
    public static Memory createLine(Environment env, Memory... args) {
        return new ObjectMemory(new WrapBorder(env, BorderFactory.createLineBorder(
                WrapColor.of(args[0]), args[1].toInteger(), args[2].toBoolean()
        )));
    }

    @Signature({
            @Arg("type"),
            @Arg("highlightColor"),
            @Arg("shadowColor")
    })
    public static Memory createSoftBevel(Environment env, Memory... args) {
        try {
            return new ObjectMemory(new WrapBorder(env, BorderFactory.createSoftBevelBorder(
                    BevelBorder.class.getField(args[0].toString().toUpperCase()).getInt(null),
                    WrapColor.of(args[1]),
                    WrapColor.of(args[2])
            )));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Signature({
            @Arg("type"),
            @Arg("highlightColor"),
            @Arg("shadowColor")
    })
    public static Memory createEtchedBevel(Environment env, Memory... args) {
        try {
            return new ObjectMemory(new WrapBorder(env, BorderFactory.createEtchedBorder(
                    EtchedBorder.class.getField(args[0].toString().toUpperCase()).getInt(null),
                    WrapColor.of(args[1]),
                    WrapColor.of(args[2])
            )));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Signature({
            @Arg(value = "outside", nativeType = WrapBorder.class),
            @Arg(value = "inside", nativeType = WrapBorder.class)
    })
    public static Memory createCompound(Environment env, Memory... args) {
        return new ObjectMemory(new WrapBorder(env, BorderFactory.createCompoundBorder(
                args[0].toObject(WrapBorder.class).getBorder(),
                args[1].toObject(WrapBorder.class).getBorder()
        )));
    }
}
