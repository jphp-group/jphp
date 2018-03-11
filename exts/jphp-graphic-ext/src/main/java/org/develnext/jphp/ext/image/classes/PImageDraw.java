package org.develnext.jphp.ext.image.classes;

import org.develnext.jphp.ext.image.GraphicExtension;
import org.develnext.jphp.ext.image.support.DrawOptions;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Reflection.Name("ImageDraw")
@Reflection.Namespace(GraphicExtension.NS)
public class PImageDraw extends BaseObject {
    private Graphics2D gc;

    public PImageDraw(Environment env, Graphics2D gc) {
        super(env);
        this.gc = gc;
    }

    public PImageDraw(Environment env, BufferedImage image) {
        super(env);
        this.gc = image.createGraphics();
    }

    public PImageDraw(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    protected Graphics2D applyOptions(DrawOptions options) {
        if (options != null) {
            if (options.getFill() != null) {
                gc.setPaint(options.getFill());
            }
        }

        return gc;
    }

    @Signature
    public void __construct(PImage image) {
        gc = image.getImage().createGraphics();
        setAntialiasing(true);
        setTextAntialiasing(Memory.TRUE);
    }

    @Setter
    public void setFill(@Nullable Color color) {
        gc.setPaint(color);
    }

    @Getter
    public Color getFill() {
        return gc.getPaint() instanceof Color ? (Color) gc.getPaint() : null;
    }

    @Setter
    protected void setFont(@Nullable Font font) {
        gc.setFont(font);
    }

    @Getter
    protected Font getFont() {
        return gc.getFont();
    }

    @Setter
    protected void setAntialiasing(boolean value) {
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, value ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Setter
    protected boolean getAntialiasing() {
        return gc.getRenderingHint(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON;
    }

    @Setter
    protected void setTextAntialiasing(Memory value) {
        Object _value = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;

        switch (value.toString().toUpperCase()) {
            case "GASP": _value = RenderingHints.VALUE_TEXT_ANTIALIAS_GASP; break;
            case "LCD_HBGR": _value = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR; break;
            case "LCD_HRGB": _value = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB; break;
            case "LCD_VBGR": _value = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR; break;
            case "LCD_VRGB": _value = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB; break;
            default:
                if (value.toBoolean()) {
                    _value = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
                }
        }

        gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, _value);
    }

    @Getter
    protected Memory getTextAntialiasing() {
        Object hint = gc.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
            return Memory.FALSE;
        } else if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
            return Memory.TRUE;
        } else if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_GASP) {
            return StringMemory.valueOf("GASP");
        } else if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR) {
            return StringMemory.valueOf("LCD_HBGR");
        } else if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB) {
            return StringMemory.valueOf("LCD_HRGB");
        } else if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR) {
            return StringMemory.valueOf("LCD_VBGR");
        } else if (hint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB) {
            return StringMemory.valueOf("LCD_VRGB");
        }

        return Memory.FALSE;
    }

    @Signature
    public void clipRect(double x, double y, double width, double height) {
        gc.clip(new Rectangle2D.Double(x, y, width, height));
    }

    @Signature
    public void clipEllipse(double x, double y, double width, double height) {
        gc.clip(new Ellipse2D.Double(x, y, width, height));
    }

    @Signature
    public void image(int x, int y, PImage image) {
        gc.drawImage(image.getImage(), x, y, null);
    }

    @Signature
    public double[] textSize(String string) {
        return textSize(string, new DrawOptions());
    }

    @Signature
    public double[] textSize(String string, DrawOptions options) {
        Rectangle2D bounds = gc.getFontMetrics(options.getFont() == null ? getFont() : options.getFont())
                .getStringBounds(string, gc);

        return new double[] { bounds.getWidth(), bounds.getHeight() };
    }

    @Signature
    public void text(float x, float y, String text) {
        text(x, y, text, new DrawOptions());
    }

    @Signature
    public void text(float x, float y, String text, DrawOptions options) {
        if (options.getFill() != null) {
            gc.setColor(options.getFill());
            gc.setPaint(options.getFill());
        }

        if (options.getFont() != null) {
            gc.setFont(options.getFont());
        }

        gc.drawString(text, x, y);
    }

    @Signature
    public void point(int x, int y) {
        point(x, y, new DrawOptions());
    }

    @Signature
    public void point(int x, int y, DrawOptions options) {
        applyOptions(options).fillRect(x, y, 1, 1);
    }

    @Signature
    public void line(int x1, int y1, int x2, int y2) {
        line(x1, y1, x2, y2, new DrawOptions());
    }

    @Signature
    public void line(int x1, int y1, int x2, int y2, DrawOptions options) {
        applyOptions(options).drawLine(x1, y1, x2, y2);
    }

    @Signature
    public void rect(int x, int y, int width, int height) {
        rect(x, y, width, height, new DrawOptions());
    }

    @Signature
    public void rect(int x, int y, int width, int height, DrawOptions options) {
        applyOptions(options);

        if (options.isOutline()) {
            gc.drawRect(x, y, width, height);
        } else {
            gc.fillRect(x, y, width, height);
        }
    }

    @Signature
    public void ellipse(int x, int y, int width, int height) {
        ellipse(x, y, width, height, new DrawOptions());
    }

    @Signature
    public void ellipse(int x, int y, int width, int height, DrawOptions options) {
        applyOptions(options);

        if (options.isOutline()) {
            gc.drawOval(x, y, width, height);
        } else {
            gc.fillOval(x, y, width, height);
        }
    }

    @Signature
    public void roundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        roundRect(x, y, width, height, arcWidth, arcHeight, new DrawOptions());
    }

    @Signature
    public void roundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, DrawOptions options) {
        applyOptions(options);

        if (options.isOutline()) {
            gc.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        } else {
            gc.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
    }

    @Signature
    public void circle(int x, int y, int radius) {
        circle(x, y, radius, new DrawOptions());
    }

    @Signature
    public void circle(int x, int y, int radius, DrawOptions options) {
        applyOptions(options);

        if (options.isOutline()) {
            gc.drawOval(x, y, radius * 2, radius * 2);
        } else {
            gc.fillOval(x, y, radius * 2, radius * 2);
        }
    }

    @Signature
    public void polygon(Environment env, ForeachIterator array) {
        polygon(env, array, new DrawOptions());
    }

    @Signature
    public void polygon(Environment env, ForeachIterator array, DrawOptions options) {
        List<Integer> xPoints = new ArrayList<>();
        List<Integer> yPoints = new ArrayList<>();

        while (array.next()) {
            Memory value = array.getValue();

            Integer x = null, y = null;

            ForeachIterator sub = value.getNewIterator(env);
            while (sub.next()) {
                if (x == null) {
                    y = x = sub.getValue().toInteger();
                } else {
                    y = sub.getValue().toInteger();
                }
            }

            if (x != null && y != null) {
                xPoints.add(x);
                yPoints.add(y);
            }
        }

        int[] xs = xPoints.stream().mapToInt(i -> i).toArray();
        int[] ys = yPoints.stream().mapToInt(i -> i).toArray();

        applyOptions(options);
        if (options.isOutline()) {
            gc.drawPolygon(xs, ys, Math.min(xs.length, ys.length));
        } else {
            gc.fillPolygon(xs, ys, Math.min(xs.length, ys.length));
        }
    }

    @Signature
    public void arc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        arc(x, y, width, height, startAngle, arcAngle, new DrawOptions());
    }

    @Signature
    public void arc(int x, int y, int width, int height, int startAngle, int arcAngle, DrawOptions options) {
        applyOptions(options);

        if (options.isOutline()) {
            gc.drawArc(x, y, width, height, startAngle, arcAngle);
        } else {
            gc.fillArc(x, y, width, height, startAngle, arcAngle);
        }
    }

    @Signature
    public void dispose() {
        gc.dispose();
    }

    @Signature
    public void __destruct() {
        gc.dispose();
    }
}
