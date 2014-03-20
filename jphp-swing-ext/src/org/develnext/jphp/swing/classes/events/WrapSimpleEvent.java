package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.RootObject;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.util.EventObject;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "event\\SimpleEvent")
public class WrapSimpleEvent extends RootObject {
    protected ObjectMemory cachedTarget;
    protected EventObject event;

    public WrapSimpleEvent(Environment env, EventObject event) {
        super(env);
        this.event = event;
    }

    public WrapSimpleEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    protected Memory __getTarget(Environment env, Memory... args){
        if (!(event.getSource() instanceof Component))
            return Memory.NULL;

        if (cachedTarget != null)
            return cachedTarget;
        return cachedTarget = new ObjectMemory(UIElement.of(env, (Component) event.getSource()));
    }
}
