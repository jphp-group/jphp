package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;

@Reflection.Name(SwingExtension.NAMESPACE + "event\\FocusEvent")
public class WrapFocusEvent extends WrapComponentEvent {
    protected FocusEvent event;

    public WrapFocusEvent(Environment env) {
        super(env);
    }

    public WrapFocusEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEvent(FocusEvent event) {
        this.event = event;
    }

    @Override
    protected ComponentEvent getEvent() {
        return event;
    }

    @Reflection.Signature
    public Memory __getTemporary(Environment env, Memory... args) {
        return TrueMemory.valueOf(event.isTemporary());
    }

    @Reflection.Signature
    public Memory __getParamString(Environment env, Memory... args){
        return StringMemory.valueOf(event.paramString());
    }

    public static WrapFocusEvent of(Environment env, FocusEvent event){
        WrapFocusEvent e = new WrapFocusEvent(env);
        e.setEvent(event);
        return e;
    }
}
