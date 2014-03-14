package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.RootObject;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.event.CaretEvent;

import java.awt.*;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(SwingExtension.NAMESPACE + "event\\CaretEvent")
public class WrapCaretEvent extends RootObject {
    public ObjectMemory cachedTarget;
    protected CaretEvent event;

    public WrapCaretEvent(Environment env) {
        super(env);
    }

    public WrapCaretEvent(Environment env, CaretEvent event) {
        super(env);
        this.event = event;
    }

    public WrapCaretEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory __getDot(Environment env, Memory... args){
        return LongMemory.valueOf(event.getDot());
    }

    @Signature
    public Memory __getMark(Environment env, Memory... args){
        return LongMemory.valueOf(event.getMark());
    }

    @Signature
    protected Memory __getTarget(Environment env, Memory... args){
        if (!(event.getSource() instanceof Component))
            return Memory.NULL;

        if (cachedTarget != null)
            return cachedTarget;
        return cachedTarget = new ObjectMemory(UIElement.of(env, (Component) event.getSource()));
    }
}
