package org.develnext.jphp.ext.image.classes;

import org.develnext.jphp.ext.image.GraphicExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.reflection.ClassEntity;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Reflection.Name("Image")
@Reflection.Namespace(GraphicExtension.NS)
public class PImage extends BaseObject implements ICloneableObject<PImage> {
    private BufferedImage image;

    public PImage(Environment env, BufferedImage image) {
        super(env);
        this.image = image;
    }

    public PImage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public BufferedImage getImage() {
        return image;
    }

    @Signature
    public void __construct(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Getter
    public int getWidth() {
        return image.getWidth();
    }

    @Getter
    public int getHeight() {
        return image.getHeight();
    }

    @Getter
    public int[] getSize() {
        return new int[] { getWidth(), getHeight() };
    }

    @Signature
    public Color getPixel(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    @Signature
    public void setPixel(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    @Signature
    public PImage rotate(double angle)
    {
        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
                cos = Math.abs(Math.cos(Math.toRadians(angle)));

        int w = image.getWidth(null), h = image.getHeight(null);

        int neww = (int) Math.floor(w*cos + h*sin),
                newh = (int) Math.floor(h*cos + w*sin);

        BufferedImage bimg = new BufferedImage(neww, newh, image.getType());
        Graphics2D g = bimg.createGraphics();

        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(Math.toRadians(angle), w/2, h/2);
        g.drawRenderedImage(image, null);
        g.dispose();

        this.image = bimg;
        return this;
    }

    @Signature
    public PImage resize(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, this.image.getType());
        Graphics2D gc = image.createGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gc.drawImage(this.image, 0, 0, width, height, null);
        gc.dispose();

        this.image = image;
        return this;
    }

    @Signature
    public static PImage open(Environment env, InputStream source) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(source);
        return new PImage(env, bufferedImage);
    }

    @Signature
    public boolean save(OutputStream outputStream, String format) throws IOException {
        //final ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
        return ImageIO.write(image, format, outputStream);
    }

    @Override
    public PImage __clone(Environment env, TraceInfo trace) {
        BufferedImage copyOfImage =
                new BufferedImage(getWidth(), getHeight(), image.getType());
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(image, 0, 0, null);

        return new PImage(env, copyOfImage);
    }
}
