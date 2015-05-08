package org.develnext.jphp.ext.javafx.classes.event;

import javafx.stage.WindowEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NAMESPACE + "event\\UXWindowEvent")
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
}
