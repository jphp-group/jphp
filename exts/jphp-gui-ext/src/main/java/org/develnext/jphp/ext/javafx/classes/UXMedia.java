package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.media.Media;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Reflection.Name(JavaFXExtension.NS + "UXMedia")
public class UXMedia extends BaseWrapper<Media> {
    interface WrappedInterface {
        @Property Duration duration();
        @Property int width();
        @Property int height();

        @Property String source();
    }

    public UXMedia(Environment env, Media wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMedia(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String source) {
        __wrappedObject = new Media(new File(source).toURI().toString());
    }

    @Signature
    public static Media createFromUrl(String path) throws URISyntaxException {
        return new Media(path);
    }

    @Signature
    public static Media createFromResource(String path) throws URISyntaxException, IOException {
        URL resource = JavaFXExtension.class.getResource(path);

        if (resource == null) {
            throw  new IOException("Unable to load resource - " + path);
        }

        return new Media(resource.toURI().toString());
    }
}
