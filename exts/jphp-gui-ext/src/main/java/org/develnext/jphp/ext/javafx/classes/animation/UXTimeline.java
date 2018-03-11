package org.develnext.jphp.ext.javafx.classes.animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.Collection;

@Reflection.Name(JavaFXExtension.NS + "animation\\UXTimeline")
public class UXTimeline extends UXAnimation<Timeline> {
    interface WrappedInterface {
        @Property ObservableList<KeyFrame> keyFrames();
    }

    public UXTimeline(Environment env, Timeline wrappedObject) {
        super(env, wrappedObject);
    }

    public UXTimeline(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Timeline();
    }

    @Signature
    public void __construct(Collection<KeyFrame> frames) {
        __wrappedObject = new Timeline();
        getWrappedObject().getKeyFrames().addAll(frames);
    }
}
