package org.develnext.jphp.ext.javafx.support;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumnBase;
import javafx.stage.Window;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.memory.NativeMemory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final public class JavaFxUtils {

    public static ObservableValue findObservable(Object object, final String property) {
        String name = property + "Property";

        Class<?> aClass = object.getClass();

        try {
            Method method;
            try {
                method = aClass.getMethod(name);
            } catch (NoSuchMethodException e) {
                method = aClass.getDeclaredMethod(name);
            }

            ReadOnlyProperty bindProperty = (ReadOnlyProperty) method.invoke(object);

            return bindProperty;
        } catch (NoSuchMethodException | ClassCastException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to find the '" + property + "' property for watching, "  + e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    public static void on(Object target, String event, Invoker invoker) {
        on(target, event, invoker, "general");
    }

    @SuppressWarnings("unchecked")
    public static void on(Object target, String event, Invoker invoker, String group) {
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.on(target, event, group, invoker);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @SuppressWarnings("unchecked")
    public static void off(Object target, String event, @Nullable String group) {
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.off(target, event, group);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    public static void off(Object target, String event) {
        off(target, event, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> void trigger(T object, String event, Event e) {
        EventProvider eventProvider = EventProvider.get(object, event);

        if (eventProvider != null) {
            eventProvider.trigger(object, event, e);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    protected static Memory _data(Object userData, String name) {
        if (userData instanceof UserData) {
            return ((UserData) userData).get(name);
        }

        return Memory.NULL;
    }

    public static Memory data(Node object, String name) {
        return _data(object.getUserData(), name);
    }

    public static Memory data(Window object, String name) {
        return _data(object.getUserData(), name);
    }

    public static Memory data(Tab object, String name) {
        return _data(object.getUserData(), name);
    }

    public static Memory data(TableColumnBase object, String name) {
        return _data(object.getUserData(), name);
    }

    public static Memory data(Environment env, Node object, String name, Memory value) {
        Object userData = object.getUserData();

        if (!(userData instanceof UserData)) {
            object.setUserData(userData = new UserData(Memory.wrap(env, userData)));
        }

        return ((UserData) userData).set(name, value);
    }

    public static Memory data(Environment env, Tab object, String name, Memory value) {
        Object userData = object.getUserData();

        if (!(userData instanceof UserData)) {
            object.setUserData(userData = new UserData(Memory.wrap(env, userData)));
        }

        return ((UserData) userData).set(name, value);
    }

    public static Memory data(Environment env, TableColumnBase object, String name, Memory value) {
        Object userData = object.getUserData();

        if (!(userData instanceof UserData)) {
            object.setUserData(userData = new UserData(Memory.wrap(env, userData)));
        }

        return ((UserData) userData).set(name, value);
    }

    public static Memory data(Environment env, Window object, String name, Memory value) {
        Object userData = object.getUserData();

        if (!(userData instanceof UserData)) {
            object.setUserData(userData = new UserData(Memory.wrap(env, userData)));
        }

        return ((UserData) userData).set(name, value);
    }

    public static Memory userData(Environment env, Object userData) {
        if (userData == null) {
            return null;
        }

        if (userData instanceof UserData) {
            return ((UserData) userData).getValue();
        }

        return Memory.wrap(env, userData);
    }

    public static void saveEventHandler(Node target, String eventId, EventHandler eventHandler) {
        Object userData = target.getUserData();

        if (!(userData instanceof UserData)) {
            target.setUserData(userData = new UserData(Memory.wrap(null, userData)));
        }

        ((UserData) userData).set("event#" + eventId, new NativeMemory<>(eventHandler));
    }

    public static EventHandler loadEventHandler(Node target, String eventId) {
        Object userData = target.getUserData();

        if (!(userData instanceof UserData)) {
            target.setUserData(userData = new UserData(Memory.wrap(null, userData)));
        }

        if (((UserData) userData).has("event#" + eventId)) {
            Memory memory = ((UserData) userData).get("event#" + eventId).toValue();

            if (memory instanceof NativeMemory) {
                return (EventHandler) ((NativeMemory) memory).getObject();
            }
        }

        return null;
    }
}
