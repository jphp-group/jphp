package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.input.ContextMenuEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "event\\UXContextMenuEvent")
public class UXContextMenuEvent extends UXEvent {
    interface WrappedInterface {
        @Property double sceneX();
        @Property double sceneY();

        @Property double screenX();
        @Property double screenY();

        @Property double x();
        @Property double y();

        @Property boolean keyboardTrigger();
    }

    public UXContextMenuEvent(Environment env, ContextMenuEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXContextMenuEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ContextMenuEvent getWrappedObject() {
        return (ContextMenuEvent) super.getWrappedObject();
    }
}
