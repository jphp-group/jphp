package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.web.WebEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Abstract
@Name(JavaFXExtension.NS + "event\\UXWebEvent")
public class UXWebEvent extends UXEvent {
    interface WrappedInterface {
        @Property Object data();
    }

    public UXWebEvent(Environment env, WebEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWebEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public WebEvent getWrappedObject() {
        return (WebEvent) super.getWrappedObject();
    }
}
