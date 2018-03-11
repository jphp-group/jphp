package org.develnext.jphp.ext.javafx.classes.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "event\\UXWindowEvent")
public class UXWindowEvent extends UXEvent {
    public UXWindowEvent(Environment env, WindowEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWindowEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public WindowEvent getWrappedObject() {
        return (WindowEvent) super.getWrappedObject();
    }


    @Signature
    public static WindowEvent makeMock(Window sender) {
        return makeMock(sender, "any");
    }

    @Signature
    public static WindowEvent makeMock(Window sender, String name) {
        EventType<WindowEvent> eventType = WindowEvent.ANY;

        switch (name.toUpperCase()) {
            case "SHOWING":
                eventType = WindowEvent.WINDOW_SHOWING;
                break;
            case "SHOW":
                eventType = WindowEvent.WINDOW_SHOWN;
                break;
            case "HIDE":
                eventType = WindowEvent.WINDOW_HIDDEN;
                break;
            case "HIDING":
                eventType = WindowEvent.WINDOW_HIDING;
                break;
            case "CLOSEQUERY":
                eventType = WindowEvent.WINDOW_CLOSE_REQUEST;
                break;
        }

        return new WindowEvent(sender, eventType);
    }
}
