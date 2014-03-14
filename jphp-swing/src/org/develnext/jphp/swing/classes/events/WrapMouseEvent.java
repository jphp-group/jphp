package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

@Reflection.Name(SwingExtension.NAMESPACE + "event\\MouseEvent")
public class WrapMouseEvent extends WrapComponentEvent {
    protected MouseEvent event;

    public WrapMouseEvent(Environment env) {
        super(env);
    }

    public WrapMouseEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEvent(MouseEvent event) {
        this.event = event;
    }

    @Override
    protected ComponentEvent getEvent() {
        return event;
    }

    @Reflection.Signature
    public Memory __getX(Environment env, Memory... args){
        return LongMemory.valueOf(event.getX());
    }

    @Reflection.Signature
    public Memory __getY(Environment env, Memory... args){
        return LongMemory.valueOf(event.getY());
    }

    @Reflection.Signature
    public Memory __getScreenX(Environment env, Memory... args){
        return LongMemory.valueOf(event.getXOnScreen());
    }

    @Reflection.Signature
    public Memory __getScreenY(Environment env, Memory... args){
        return LongMemory.valueOf(event.getYOnScreen());
    }

    @Reflection.Signature
    public Memory __getButton(Environment env, Memory... args){
        return LongMemory.valueOf(event.getButton());
    }

    @Reflection.Signature
    public Memory __getClickCount(Environment env, Memory... args){
        return LongMemory.valueOf(event.getClickCount());
    }

    @Reflection.Signature
    public Memory __getPopupTrigger(Environment env, Memory... args){
        return event.isPopupTrigger() ? Memory.TRUE : Memory.FALSE;
    }


    public static WrapMouseEvent of(Environment env, MouseEvent event){
        WrapMouseEvent mouseEvent = new WrapMouseEvent(env);
        mouseEvent.setEvent(event);
        return mouseEvent;
    }
}
