package org.develnext.jphp.ext.javafx.classes.event;

import javafx.scene.web.WebErrorEvent;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.lang.IObject;
import php.runtime.reflection.ClassEntity;

@Reflection.Abstract
@Name(JavaFXExtension.NS + "event\\UXWebErrorEvent")
public class UXWebErrorEvent extends UXEvent {
    interface WrappedInterface {
        @Property String message();
    }

    public UXWebErrorEvent(Environment env, WebErrorEvent wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWebErrorEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public WebErrorEvent getWrappedObject() {
        return (WebErrorEvent) super.getWrappedObject();
    }

    @Getter
    public IObject getException(Environment env) {
        return new JavaException(env, getWrappedObject().getException());
    }
}
