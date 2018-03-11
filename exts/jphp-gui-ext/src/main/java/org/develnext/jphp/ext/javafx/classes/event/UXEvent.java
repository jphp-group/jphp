package org.develnext.jphp.ext.javafx.classes.event;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Abstract
@Reflection.Name(JavaFXExtension.NS + "event\\UXEvent")
public class UXEvent extends BaseWrapper<Event> {
    interface WrappedInterface {
        boolean isConsumed();
        void consume();
    }

    public UXEvent(Environment env, Event wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEvent(Environment env, ActionEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEvent(Environment env, InputEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEvent(Environment env, InputMethodEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Object copyFor(Object newSource) {
        return getWrappedObject().copyFor(newSource, null);
    }

    @Getter
    protected Memory getTarget(Environment env) throws Throwable {
        Object target = getWrappedObject().getTarget();
        return Memory.wrap(env, target);
    }

    @Getter
    protected Memory getSender(Environment env) throws Throwable {
        Object target = getWrappedObject().getSource();
        return Memory.wrap(env, target);
    }

    @Signature
    public static Event makeMock(Object sender) {
        return new Event(sender, null, EventType.ROOT);
    }
}
