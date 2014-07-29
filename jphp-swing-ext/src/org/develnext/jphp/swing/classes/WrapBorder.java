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
import java.awt.geom.Rectangle2D;
import java.beans.ConstructorProperties;

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
        return new ObjectMemory(new WrapBorder(env, new LineBorder(
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
            return new ObjectMemory(new WrapBorder(env, new SoftBevelBorder(
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

    private static Border sharedDashedBorder;

    public static Border createDashedBorder(Paint paint, float thickness, float length, float spacing, boolean rounded) {
        boolean shared = !rounded && (paint == null) && (thickness == 1.0f) && (length == 1.0f) && (spacing == 1.0f);
        if (shared && (sharedDashedBorder != null)) {
            return sharedDashedBorder;
        }
        if (thickness < 1.0f) {
            throw new IllegalArgumentException("thickness is less than 1");
        }
        if (length < 1.0f) {
            throw new IllegalArgumentException("length is less than 1");
        }
        if (spacing < 0.0f) {
            throw new IllegalArgumentException("spacing is less than 0");
        }
        int cap = rounded ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE;
        int join = rounded ? BasicStroke.JOIN_ROUND : BasicStroke.JOIN_MITER;
        float[] array = { thickness * (length - 1.0f), thickness * (spacing + 1.0f) };
        Border border = new StrokeBorder(
                new BasicStroke(thickness, cap, join, thickness * 2.0f, array, 0.0f), paint
        );

        if (shared) {
            sharedDashedBorder = border;
        }
        return border;
    }

    @Signature({
            @Arg(value = "color"),
            @Arg(value = "thickness", optional = @Optional("1")),
            @Arg(value = "length", optional = @Optional("2")),
            @Arg(value = "spacing", optional = @Optional("1")),
            @Arg(value = "rounded", optional = @Optional("false"))
    })
    public static Memory createDashed(Environment env, Memory... args) {
        return new ObjectMemory(new WrapBorder(env, createDashedBorder(
                WrapColor.of(args[0]),
                args[1].toFloat(),
                args[2].toFloat(),
                args[3].toFloat(),
                args[4].toBoolean()
        )));
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

    private static class StrokeBorder extends AbstractBorder {
        private final BasicStroke stroke;
        private final Paint paint;


        public StrokeBorder(BasicStroke stroke) {
            this(stroke, null);
        }

        @ConstructorProperties({ "stroke", "paint" })
        public StrokeBorder(BasicStroke stroke, Paint paint) {
            if (stroke == null) {
                throw new NullPointerException("border's stroke");
            }
            this.stroke = stroke;
            this.paint = paint;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            float size = this.stroke.getLineWidth();
            if (size > 0.0f) {
                g = g.create();
                if (g instanceof Graphics2D) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setStroke(this.stroke);
                    g2d.setPaint(this.paint != null ? this.paint : c == null ? null : c.getForeground());
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.draw(new Rectangle2D.Float(x + size / 2, y + size / 2, width - size, height - size));
                }
                g.dispose();
            }
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            int size = (int) Math.ceil(this.stroke.getLineWidth());
            insets.set(size, size, size, size);
            return insets;
        }

        public BasicStroke getStroke() {
            return this.stroke;
        }

        public Paint getPaint() {
            return this.paint;
        }
    }
}
