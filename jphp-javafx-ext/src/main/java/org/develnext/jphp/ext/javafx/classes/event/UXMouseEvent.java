package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Abstract
@Name(JavaFXExtension.NAMESPACE + "event\\UXMouseEvent")
public class UXMouseEvent extends UXEvent {
    interface WrappedInterface {
        @Property double x();
        @Property double y();
        @Property double screenX();
        @Property double screenY();

        @Property int clickCount();

        @Property MouseButton button();

        @Property boolean dragDetect();

        @Property boolean stillSincePress();
        @Property boolean shiftDown();
        @Property boolean controlDown();
        @Property boolean altDown();
        @Property boolean metaDown();
        @Property boolean shortcutDown();

        @Property boolean primaryButtonDown();
        @Property boolean secondaryButtonDown();
        @Property boolean middleButtonDown();
    }

    public UXMouseEvent(Environment env, MouseEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMouseEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public MouseEvent getWrappedObject() {
        return (MouseEvent) super.getWrappedObject();
    }
}
