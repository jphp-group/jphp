package org.develnext.jphp.ext.game.classes.event;

import javafx.event.EventTarget;
import org.develnext.jphp.ext.game.GameExtension;
import org.develnext.jphp.ext.game.support.CollisionEvent;
import org.develnext.jphp.ext.game.support.Vec2d;
import org.develnext.jphp.ext.javafx.classes.event.UXEvent;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(GameExtension.NS + "\\event")
public class UXCollisionEvent extends UXEvent {
    interface WrappedInterface {
        @Property Vec2d normal();
        @Property Vec2d tangent();
    }

    public UXCollisionEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public UXCollisionEvent(Environment env, CollisionEvent wrappedObject) {
        super(env, wrappedObject);
    }

    @Override
    public CollisionEvent getWrappedObject() {
        return (CollisionEvent) super.getWrappedObject();
    }

    @Signature
    public void __construct(CollisionEvent parent, Object sender, Object target) {
        __wrappedObject = new CollisionEvent(parent, sender, (EventTarget) target);
    }
}
