package php.runtime.ext.swing.classes.events;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import static php.runtime.annotation.Reflection.Signature;

@Reflection.Name(SwingExtension.NAMESPACE + "event\\KeyEvent")
public class WrapKeyEvent extends WrapComponentEvent {
    protected KeyEvent event;

    public WrapKeyEvent(Environment env) {
        super(env);
    }

    public WrapKeyEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEvent(KeyEvent event) {
        this.event = event;
    }

    @Override
    protected ComponentEvent getEvent() {
        return event;
    }

    @Signature
    public Memory __getKeyChar(Environment env, Memory... args){
        return new StringMemory(event.getKeyChar());
    }

    @Signature
    public Memory __getKeyCode(Environment env, Memory... args){
        return LongMemory.valueOf(event.getKeyCode());
    }

    @Signature
    public Memory __getKeyLocation(Environment env, Memory... args){
        return LongMemory.valueOf(event.getKeyLocation());
    }

    @Signature
    public Memory __getActionKey(Environment env, Memory... args){
        return TrueMemory.valueOf(event.isActionKey());
    }

    public static WrapKeyEvent of(Environment env, KeyEvent event){
        WrapKeyEvent mouseEvent = new WrapKeyEvent(env);
        mouseEvent.setEvent(event);
        return mouseEvent;
    }
}
