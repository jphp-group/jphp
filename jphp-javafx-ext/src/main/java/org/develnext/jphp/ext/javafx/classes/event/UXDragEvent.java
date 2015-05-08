package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NAMESPACE + "event\\UXDragEvent")
public class UXDragEvent extends UXEvent {
    interface WrappedInterface {
        @Property TransferMode acceptedTransferMode();
        @Property TransferMode transferMode();

        @Property double sceneX();
        @Property double sceneY();
        @Property double screenX();
        @Property double screenY();
        @Property double x();
        @Property double y();

        @Property boolean accepted();
        @Property boolean dropCompleted();
    }


    public UXDragEvent(Environment env, DragEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXDragEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public DragEvent getWrappedObject() {
        return (DragEvent) super.getWrappedObject();
    }

    @Getter
    protected Memory getGestureSource(Environment env) {
        return Memory.wrap(env, getWrappedObject().getGestureSource());
    }

    @Getter
    protected Memory getGestureTarget(Environment env) {
        return Memory.wrap(env, getWrappedObject().getGestureTarget());
    }
}
