package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "UXMediaPlayer")
public class UXMediaPlayer extends BaseWrapper<MediaPlayer> {
    interface WrappedInterface {
        @Property double balance();
        @Property double rate();
        @Property double volume();
        @Property boolean mute();
        @Property MediaPlayer.Status status();

        @Property double currentRate();
        @Property int currentCount();
        @Property int cycleCount();

        void seek(Duration time);
        void play();
        void pause();
        void stop();
    }

    public UXMediaPlayer(Environment env, MediaPlayer wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMediaPlayer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Media media) {
        __wrappedObject = new MediaPlayer(media);
    }

    @Getter
    public Duration getCurrentTime() {
        return getWrappedObject().getCurrentTime();
    }

    @Setter
    public void setCurrentTime(Duration duration) {
        getWrappedObject().seek(duration);
    }

    @Getter
    public Media getMedia() {
        return getWrappedObject().getMedia();
    }

    @Getter
    public long getCurrentTimeAsPercent() {
        Duration duration = getWrappedObject().getMedia().getDuration();
        Duration currentTime = getWrappedObject().getCurrentTime();

        return Math.round(currentTime.toMillis() * 100.0 / duration.toMillis());
    }

    @Setter
    public void setCurrentTimeAsPercent(long percent) {
        if (percent > 100) {
            percent = 100;
        }

        Duration duration = getWrappedObject().getMedia().getDuration();

        getWrappedObject().seek(Duration.millis(Math.round(duration.toMillis() * percent / 100.0)));
    }
}
