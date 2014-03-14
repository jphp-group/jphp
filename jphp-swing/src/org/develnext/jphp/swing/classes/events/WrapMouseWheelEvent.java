package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;

@Reflection.Name(SwingExtension.NAMESPACE + "event\\MouseWheelEvent")
public class WrapMouseWheelEvent extends WrapMouseEvent {
    protected MouseWheelEvent event;

    public WrapMouseWheelEvent(Environment env) {
        super(env);
    }

    public WrapMouseWheelEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEvent(MouseWheelEvent event) {
        this.event = event;
    }

    @Override
    protected ComponentEvent getEvent() {
        return event;
    }

    @Reflection.Signature
    public Memory __getScrollAmount(Environment env, Memory... args){
        return LongMemory.valueOf(event.getScrollAmount());
    }

    @Reflection.Signature
    public Memory __getScrollType(Environment env, Memory... args){
        return LongMemory.valueOf(event.getScrollType());
    }

    @Reflection.Signature
    public Memory __getWheelRotation(Environment env, Memory... args){
        return LongMemory.valueOf(event.getWheelRotation());
    }

    @Reflection.Signature
    public Memory __getUnitsToScroll(Environment env, Memory... args){
        return LongMemory.valueOf(event.getUnitsToScroll());
    }

    public static WrapMouseWheelEvent of(Environment env, MouseWheelEvent event){
        WrapMouseWheelEvent mouseEvent = new WrapMouseWheelEvent(env);
        mouseEvent.setEvent(event);
        return mouseEvent;
    }
}
