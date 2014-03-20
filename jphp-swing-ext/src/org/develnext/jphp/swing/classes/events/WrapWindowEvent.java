package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIWindow;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(SwingExtension.NAMESPACE + "event\\WindowEvent")
public class WrapWindowEvent extends WrapComponentEvent {
    protected WindowEvent event;

    public WrapWindowEvent(Environment env) {
        super(env);
    }

    public WrapWindowEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEvent(WindowEvent event) {
        this.event = event;
    }

    @Override
    protected ComponentEvent getEvent() {
        return event;
    }

    @Signature
    protected Memory __getNewState(Environment env, Memory... args) {
        return LongMemory.valueOf(event.getNewState());
    }

    @Signature
    protected Memory __getOldState(Environment env, Memory... args) {
        return LongMemory.valueOf(event.getOldState());
    }

    @Signature
    protected Memory __getOppositeWindow(Environment env, Memory... args) {
        return event.getOppositeWindow() == null
                ? Memory.NULL
                : new ObjectMemory(UIWindow.of(env, event.getOppositeWindow()));
    }

    public static WrapWindowEvent of(Environment env, WindowEvent event){
        WrapWindowEvent mouseEvent = new WrapWindowEvent(env);
        mouseEvent.setEvent(event);
        return mouseEvent;
    }
}
