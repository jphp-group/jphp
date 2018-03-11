package org.develnext.jphp.ext.javafx.classes.animation;

import javafx.animation.Animation;
import javafx.event.Event;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NS + "animation\\UXAnimation")
public class UXAnimation<T extends Animation> extends BaseWrapper<Animation> {
    interface WrappedInterface {
        @Property
        Animation.Status status();

        @Property
        double rate();

        @Property
        double currentRate();

        @Property
        int cycleCount();

        @Property
        double targetFramerate();

        @Property
        boolean autoReverse();

        @Property
        Duration currentTime();

        @Property
        Duration cycleDuration();

        @Property
        Duration delay();

        @Property
        Duration totalDuration();

        void play();
        void playFrom(Duration duration);
        void jumpTo(Duration duration);
        void playFromStart();
        void pause();
        void stop();
    }

    public UXAnimation(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public UXAnimation(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public T getWrappedObject() {
        return (T) super.getWrappedObject();
    }

    @Reflection.Signature
    @SuppressWarnings("unchecked")
    public void on(String event, Invoker invoker, String group) {
        EventProvider eventProvider = EventProvider.get(getWrappedObject(), event);

        if (eventProvider != null) {
            eventProvider.on(getWrappedObject(), event, group, invoker);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Reflection.Signature
    public void on(String event, Invoker invoker) {
        on(event, invoker, "general");
    }

    @Reflection.Signature
    @SuppressWarnings("unchecked")
    public void off(String event, @Reflection.Nullable String group) {
        EventProvider eventProvider = EventProvider.get(getWrappedObject(), event);

        if (eventProvider != null) {
            eventProvider.off(getWrappedObject(), event, group);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Reflection.Signature
    public void off(String event) {
        off(event, null);
    }

    @Reflection.Signature
    public void trigger(String event, @Reflection.Nullable Event e) {
        EventProvider eventProvider = EventProvider.get(getWrappedObject(), event);

        if (eventProvider != null) {
            eventProvider.trigger(getWrappedObject(), event, e);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }
}
