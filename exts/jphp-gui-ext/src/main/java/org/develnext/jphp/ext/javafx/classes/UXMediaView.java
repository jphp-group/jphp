package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.media.Media;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.io.File;

@Reflection.Name(JavaFXExtension.NS + "UXMediaView")
public class UXMediaView extends UXNode<MediaView> {
    interface WrappedInterface {
        @Property("player") @Nullable MediaPlayer mediaPlayer();
        @Property("proportional") boolean preserveRatio();
        @Property boolean smooth();
    }

    public UXMediaView(Environment env, MediaView wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMediaView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new MediaView();
    }

    @Override
    @Signature
    public void setWidth(double value) {
        super.setWidth(value);
        getWrappedObject().setFitWidth(value);
    }

    @Override
    @Signature
    public void setHeight(double value) {
        super.setHeight(value);
        getWrappedObject().setFitHeight(value);
    }

    @Override
    @Signature
    public void setSize(double[] values) {
        super.setSize(values);

        if (values.length >= 2) {
            getWrappedObject().setFitWidth(values[0]);
            getWrappedObject().setFitHeight(values[1]);
        }
    }

    @Signature
    public void open(String fileName) {
        open(fileName, true);
    }

    @Signature
    public void open(String fileName, boolean autoPlay) {
        getWrappedObject().setMediaPlayer(new MediaPlayer(new Media(new File(fileName).toURI().toString())));

        if (autoPlay) {
            play();
        }
    }

    @Signature
    public boolean play() {
        if (getWrappedObject().getMediaPlayer() == null) {
            return false;
        }

        getWrappedObject().getMediaPlayer().play();
        return true;
    }

    @Signature
    public boolean stop() {
        if (getWrappedObject().getMediaPlayer() == null) {
            return false;
        }

        getWrappedObject().getMediaPlayer().stop();
        return true;
    }

    @Signature
    public boolean pause() {
        if (getWrappedObject().getMediaPlayer() == null) {
            return false;
        }

        getWrappedObject().getMediaPlayer().pause();
        return true;
    }
}
