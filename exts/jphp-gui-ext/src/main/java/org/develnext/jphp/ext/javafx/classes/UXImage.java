package org.develnext.jphp.ext.javafx.classes;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.develnext.jphp.ext.image.classes.PImage;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ClassEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Reflection.Name(JavaFXExtension.NS + "UXImage")
public class UXImage extends BaseWrapper<Image> {
    interface WrappedInterface {
        @Property double width();
        @Property double height();
        @Property double progress();

        void cancel();
    }

    public UXImage(Environment env, Image wrappedObject) {
        super(env, wrappedObject);
    }
    public UXImage(Environment env, WritableImage wrappedObject) {
        super(env, wrappedObject);
    }

    public UXImage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env, Memory arg) throws IOException {
        if (arg.instanceOf(PImage.class)) {
            __wrappedObject = SwingFXUtils.toFXImage(arg.toObject(PImage.class).getImage(), null);
        } else {
            InputStream is = Stream.getInputStream(env, arg);
            try {
                __wrappedObject = new Image(is);
            } finally {
                Stream.closeStream(env, is);
            }
        }
    }

    @Signature
    public void __construct(Environment env, Memory is, double requiredWidth, double requiredHeight) throws IOException {
        __construct(env, is, requiredWidth, requiredHeight, true);
    }

    @Signature
    public void __construct(Environment env, Memory arg, double requiredWidth, double requiredHeight, boolean proportional) throws IOException {
        if (arg.instanceOf(PImage.class)) {
            __wrappedObject = SwingFXUtils.toFXImage(arg.toObject(PImage.class).resize((int) requiredWidth, (int) requiredHeight).getImage(), null);
        } else {
            InputStream is = Stream.getInputStream(env, arg);
            try {
                __wrappedObject = new Image(is, requiredWidth, requiredHeight, proportional, false);
            } finally {
                Stream.closeStream(env, is);
            }
        }
    }

    @Signature
    public Color getPixelColor(int x, int y) {
        PixelReader pixelReader = getWrappedObject().getPixelReader();
        try {
            return pixelReader == null ? null : pixelReader.getColor(x, y);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    @Signature
    public int getPixelARGB(int x, int y) {
        PixelReader pixelReader = getWrappedObject().getPixelReader();
        try {
            return pixelReader == null ? -1 : pixelReader.getArgb(x, y);
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    @Signature
    public boolean isError() {
        return getWrappedObject().isError();
    }

    @Signature
    public boolean isBackgroundLoading() {
        return getWrappedObject().isBackgroundLoading();
    }

    @Signature
    public static Image ofUrl(String url) {
        return new Image(url);
    }

    @Signature
    public static Image ofUrl(String url, boolean background) {
        return new Image(url, background);
    }

    @Signature
    public void save(OutputStream stream) throws IOException {
        save(stream, "png");
    }

    @Signature
    public Memory toNative(Environment env) throws Throwable {
        MemoryOperation operation = MemoryOperation.get(BufferedImage.class, null);

        if (operation != null) {
            BufferedImage image = SwingFXUtils.fromFXImage(this.getWrappedObject(), null);
            image = convertToCompatible(image);

            return operation.unconvert(env, env.trace(), image);
        } else {
            return Memory.NULL;
        }
    }

    @Signature
    public static UXImage ofNative(Environment env, Memory memory) throws Throwable {
        MemoryOperation operation = MemoryOperation.get(BufferedImage.class, null);

        if (operation != null) {
            BufferedImage convert = (BufferedImage) operation.convert(env, env.trace(), memory);
            WritableImage image = SwingFXUtils.toFXImage(convert, null);
            return new UXImage(env, image);
        }

        return null;
    }

    public static BufferedImage convertToCompatible(BufferedImage image) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        BufferedImage compatible =
                gc.createCompatibleImage(image.getWidth(),
                        image.getHeight());

        if (compatible.getType() == image.getType())
            return image;

        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                compatible.getColorModel().getColorSpace(),null);

        return op.filter(image,compatible);
    }

    @Signature
    public void save(OutputStream stream, String format) throws IOException {
        BufferedImage image = SwingFXUtils.fromFXImage(this.getWrappedObject(), null);
        image = convertToCompatible(image);

        ImageIO.write(image, format, stream);
    }
}
