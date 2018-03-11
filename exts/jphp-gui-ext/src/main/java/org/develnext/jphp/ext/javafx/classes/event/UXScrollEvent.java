package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.input.ScrollEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "event\\UXScrollEvent")
public class UXScrollEvent extends UXEvent {
    interface WrappedInterface {
        @Property double deltaX();
        @Property double deltaY();
        @Property double textDeltaX();
        @Property double textDeltaY();
        @Property double totalDeltaX();
        @Property double totalDeltaY();
        @Property double multiplierX();
        @Property double multiplierY();
        @Property int touchCount();
    }


    public UXScrollEvent(Environment env, ScrollEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXScrollEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ScrollEvent getWrappedObject() {
        return (ScrollEvent) super.getWrappedObject();
    }

    @Getter
    protected String getEventType() {
        return getWrappedObject().getEventType().getName();
    }

    @Signature
    public boolean isDirect() {
        return getWrappedObject().isDirect();
    }

    @Signature
    public boolean isInertia() {
        return getWrappedObject().isInertia();
    }

    @Getter
    public Memory getPickResult(Environment env) {
        return ArrayMemory.ofNullableBean(env, getWrappedObject().getPickResult());
    }

    @Getter
    public boolean getAltDown() {
        return getWrappedObject().isAltDown();
    }

    @Getter
    public boolean getControlDown() {
        return getWrappedObject().isControlDown();
    }

    @Getter
    public boolean getMetaDown() {
        return getWrappedObject().isMetaDown();
    }

    @Getter
    public boolean getShiftDown() {
        return getWrappedObject().isShiftDown();
    }

    @Getter
    public boolean getShortcutDown() {
        return getWrappedObject().isShortcutDown();
    }

}
