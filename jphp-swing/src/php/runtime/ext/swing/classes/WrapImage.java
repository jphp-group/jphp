package php.runtime.ext.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.stream.Stream;
import php.runtime.ext.java.JavaObject;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "Image")
public class WrapImage extends RootObject {
    public final static int TYPE_INT_RGB        = BufferedImage.TYPE_INT_RGB;
    public final static int TYPE_INT_ARGB       = BufferedImage.TYPE_INT_ARGB;
    public final static int TYPE_INT_ARGB_PRE   = BufferedImage.TYPE_INT_ARGB_PRE;
    public final static int TYPE_INT_BGR        = BufferedImage.TYPE_INT_BGR;
    public final static int TYPE_3BYTE_BGR      = BufferedImage.TYPE_3BYTE_BGR;
    public final static int TYPE_4BYTE_ABGR     = BufferedImage.TYPE_4BYTE_ABGR;
    public final static int TYPE_4BYTE_ABGR_PRE = BufferedImage.TYPE_4BYTE_ABGR_PRE;
    public final static int TYPE_USHORT_565_RGB = BufferedImage.TYPE_USHORT_565_RGB;
    public final static int TYPE_USHORT_555_RGB = BufferedImage.TYPE_USHORT_555_RGB;
    public final static int TYPE_BYTE_GRAY      = BufferedImage.TYPE_BYTE_GRAY;
    public final static int TYPE_USHORT_GRAY    = BufferedImage.TYPE_USHORT_GRAY;
    public final static int TYPE_BYTE_BINARY    = BufferedImage.TYPE_BYTE_BINARY;
    public final static int TYPE_BYTE_INDEXED   = BufferedImage.TYPE_BYTE_INDEXED;

    protected BufferedImage image;

    public WrapImage(Environment env) {
        super(env);
    }

    public WrapImage(Environment env, BufferedImage image) {
        super(env);
        setImage(image);
    }

    public WrapImage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Signature({
            @Arg("width"),
            @Arg("height"),
            @Arg(value = "type", optional = @Optional(value = "1", type = HintType.INT))
    })
    public Memory __construct(Environment env, Memory... args){
        image = new BufferedImage(args[0].toInteger(), args[1].toInteger(), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory __getType(Environment env, Memory... args){
        return LongMemory.valueOf(image.getType());
    }

    @Signature
    public Memory __getWidth(Environment env, Memory... args){
        return LongMemory.valueOf(image.getWidth());
    }

    @Signature
    public Memory __getHeight(Environment env, Memory... args){
        return LongMemory.valueOf(image.getHeight());
    }

    @Signature(@Arg("property"))
    public Memory getProperty(Environment env, Memory... args){
        Object value = image.getProperty(args[0].toString());
        if (value == Image.UndefinedProperty)
            return Memory.NULL;
        return new ObjectMemory(JavaObject.of(env, value));
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("w"), @Arg("h")})
    public Memory getSubimage(Environment env, Memory... args){
        return new ObjectMemory(new WrapImage(env, image.getSubimage(
                args[0].toInteger(), args[1].toInteger(),
                args[2].toInteger(), args[3].toInteger()
        )));
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("rgb")})
    public Memory setRGB(Environment env, Memory... args){
        image.setRGB(args[0].toInteger(), args[1].toInteger(), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y")})
    public Memory getRGB(Environment env, Memory... args){
        return LongMemory.valueOf(image.getRGB(args[0].toInteger(), args[1].toInteger()));
    }

    @Signature
    public Memory getGraphics(Environment env, Memory... args){
        Graphics graphics = image.getGraphics();
        if (graphics == null)
            return Memory.NULL;

        return new ObjectMemory(new WrapGraphics(env, graphics));
    }

    @Signature
    public Memory flush(Environment env, Memory... args){
        image.flush();
        return Memory.NULL;
    }

    @Signature(@Arg("stream"))
    public static Memory read(Environment env, Memory... args) {
        InputStream input = Stream.getInputStream(env, args[0]);
        try {
            BufferedImage image = ImageIO.read(input);
            if (image == null)
                return Memory.NULL;

            return new ObjectMemory(new WrapImage(env, image));
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        } finally {
            Stream.closeStream(env, input);
        }
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "image", typeClass = SwingExtension.NAMESPACE + "Image"),
            @Arg("format"),
            @Arg("stream")
    })
    public static Memory write(Environment env, Memory... args) {
        OutputStream output = Stream.getOutputStream(env, args[2]);
        try {
            ImageIO.write(args[0].toObject(WrapImage.class).getImage(), args[1].toString(), output);
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        } finally {
            Stream.closeStream(env, output);
        }
        return Memory.NULL;
    }
}
