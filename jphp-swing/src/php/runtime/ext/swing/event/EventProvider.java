package php.runtime.ext.swing.event;

import php.runtime.env.Environment;
import php.runtime.ext.swing.ComponentProperties;
import php.runtime.ext.swing.classes.events.*;
import php.runtime.ext.swing.misc.EventList;
import php.runtime.memory.ObjectMemory;

import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

abstract public class EventProvider<T extends Component> {

    abstract public Class<T> getComponentClass();

    abstract public void register(final Environment env, T component, ComponentProperties properties);
    abstract public boolean isAllowedEventType(Component component, String code);

    public static void triggerSimple(Environment env, ComponentProperties properties, String name, EventObject e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(new WrapSimpleEvent(env, e)));
    }

    public static void triggerMouse(Environment env, ComponentProperties properties, String name, MouseEvent e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(WrapMouseEvent.of(env, e)));
    }

    public static void triggerMouseWheel(Environment env, ComponentProperties properties, String name, MouseEvent e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(WrapMouseWheelEvent.of(env, e)));
    }

    public static void triggerFocus(Environment env, ComponentProperties properties, String name, FocusEvent e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(WrapFocusEvent.of(env, e)));
    }

    public static void triggerKey(Environment env, ComponentProperties properties, String name, KeyEvent e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(WrapKeyEvent.of(env, e)));
    }

    public static void triggerCaret(Environment env, ComponentProperties properties, String name, CaretEvent e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(new WrapCaretEvent(env, e)));
    }

    public static void triggerWindow(Environment env, ComponentProperties properties, String name, WindowEvent e){
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(WrapWindowEvent.of(env, e)));
    }

    public static void triggerItem(Environment env, ComponentProperties properties, String name, ItemEvent e) {
        EventList list = properties.eventContainer.get(name);
        if (list != null && !list.isEmpty())
            properties.triggerEvent(name, new ObjectMemory(new WrapItemEvent(env, e)));
    }
}
