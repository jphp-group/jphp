package org.develnext.jphp.ext.javafx.classes;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.control.CanvasEx;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.reflection.ClassEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

@Name(JavaFXExtension.NS + "UXCanvas")
public class UXCanvas<T extends Canvas> extends UXNode<Canvas> {
    interface WrappedInterface {

    }

    public UXCanvas(Environment env, CanvasEx wrappedObject) {
        super(env, wrappedObject);
    }

    public UXCanvas(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXCanvas(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new CanvasEx();
    }

    @Override
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Signature
    public UXGraphicsContext getGraphicsContext(Environment env) {
        return new UXGraphicsContext(env, getWrappedObject().getGraphicsContext2D());
    }

    @Signature
    public UXGraphicsContext gc(Environment env) {
        return getGraphicsContext(env);
    }

    @Signature
    public void save(Environment env, Memory stream) throws IOException, InvocationTargetException, InterruptedException {
        save(env, stream, "png");
    }

    @Signature
    public void save(Environment env, Memory stream, String format) throws IOException, InvocationTargetException, InterruptedException {
        SnapshotParameters param = new SnapshotParameters();
        param.setDepthBuffer(true);
        param.setFill(javafx.scene.paint.Color.TRANSPARENT);

        try {
            final WritableImage image = getWrappedObject().snapshot(param, null);

            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

            try {
                OutputStream out = Stream.getOutputStream(env, stream);

                if (out == null) {
                    throw new IOException();
                }

                try {

                    ImageIO.write(bImage, format, out);

                } finally {
                    Stream.closeStream(env, out);
                }
            } catch (IOException e) {
                env.wrapThrow(e);
            }
        } catch (IllegalArgumentException e) {
            ;
        }
    }

    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;
    }

    @Signature
    public void writeImageAsync(final String format, final Memory outputStream, @Nullable final javafx.scene.paint.Color transparentColor, @Nullable final Invoker callback, final Environment env) throws IOException {
        Platform.runLater(() -> {
            SnapshotParameters param = new SnapshotParameters();
            param.setDepthBuffer(true);
            param.setFill(javafx.scene.paint.Color.TRANSPARENT);

            try {
                final WritableImage image = getWrappedObject().snapshot(param, null);

                new Thread(() -> {
                    BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

                    if (transparentColor != null) {
                        final int markerRGB = new Color(
                                (int) (transparentColor.getRed() * 255),
                                (int) (transparentColor.getGreen() * 255),
                                (int) (transparentColor.getBlue() * 255)
                        ).getRGB();

                        ImageFilter filter = new RGBImageFilter() {
                            public final int filterRGB(int x, int y, int rgb) {
                                if (markerRGB == rgb) {
                                    return 0x00FFFFFF & rgb;
                                } else {
                                    return rgb;
                                }
                            }
                        };

                        ImageProducer ip = new FilteredImageSource(bImage.getSource(), filter);

                        Image image1 = Toolkit.getDefaultToolkit().createImage(ip);
                        bImage = imageToBufferedImage(image1);
                    }

                    try {
                        OutputStream out = Stream.getOutputStream(env, outputStream);

                        if (out == null) {
                            throw new IOException();
                        }

                        ImageIO.write(bImage, format, out);
                        if (callback != null) {
                            Platform.runLater(() -> callback.callAny(true));
                        }

                        Stream.closeStream(env, out);
                    } catch (IOException e) {
                        if (callback != null) {
                            Platform.runLater(() -> callback.callAny(false));
                        }
                        env.wrapThrow(e);
                    }
                }).start();
            } catch (IllegalArgumentException e) {
                ;
            }
        });
    }

    @Override
    @Signature
    protected void setSize(double[] size) {
        if (size.length >= 2) {
            getWrappedObject().setWidth(size[0]);
            getWrappedObject().setHeight(size[1]);
        }
    }

    @Override
    protected double[] getSize() {
        return new double[]{getWrappedObject().getWidth(), getWrappedObject().getHeight()};
    }
}
