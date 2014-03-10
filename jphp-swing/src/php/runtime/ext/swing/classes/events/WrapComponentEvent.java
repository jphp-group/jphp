package php.runtime.ext.swing.classes.events;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.ext.swing.classes.components.support.UIElement;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.event.ComponentEvent;

@Reflection.Name(SwingExtension.NAMESPACE + "event\\ComponentEvent")
abstract public class WrapComponentEvent extends RootObject {
    public ObjectMemory cachedTarget;

    public WrapComponentEvent(Environment env) {
        super(env);
    }

    public WrapComponentEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    abstract protected ComponentEvent getEvent();

    @Reflection.Signature
    protected Memory __getTarget(Environment env, Memory... args){
        if (cachedTarget != null)
            return cachedTarget;
        return cachedTarget = new ObjectMemory(UIElement.of(env, getEvent().getComponent()));
    }
}
