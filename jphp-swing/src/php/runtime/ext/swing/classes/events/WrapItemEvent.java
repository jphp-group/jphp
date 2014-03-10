package php.runtime.ext.swing.classes.events;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.ext.swing.classes.components.support.UIElement;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.awt.event.ItemEvent;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(SwingExtension.NAMESPACE + "event\\ItemEvent")
public class WrapItemEvent extends RootObject {
    public ObjectMemory cachedTarget;
    protected ItemEvent event;

    public WrapItemEvent(Environment env, ItemEvent event) {
        super(env);
        this.event = event;
    }

    public WrapItemEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected Memory __getItem(Environment env, Memory... args) {
        return new StringMemory(event.getItem().toString());
    }

    @Signature
    public Memory isSelected(Environment env, Memory... args) {
        return event.getStateChange() == ItemEvent.SELECTED ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDeselected(Environment env, Memory... args) {
        return event.getStateChange() == ItemEvent.DESELECTED ? Memory.TRUE : Memory.FALSE;
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
