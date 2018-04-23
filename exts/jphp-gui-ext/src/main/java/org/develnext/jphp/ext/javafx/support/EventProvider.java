package org.develnext.jphp.ext.javafx.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.event.Event;
import javafx.event.EventHandler;
import php.runtime.exceptions.CriticalException;
import php.runtime.invoke.Invoker;

import java.util.HashMap;
import java.util.Map;

abstract public class EventProvider<T> {
    protected final static Map<Class<?>, EventProvider> eventProviderMap = new HashMap<>(200);

    abstract protected class Handler {
        abstract public void set(T target, EventHandler eventHandler);
        abstract public EventHandler get(T target);
    }

    protected Map<String, Handler> handlers = new HashMap<>();

    public EventProvider() {
        // ...
    }

    abstract public Class<T> getTargetClass();


    protected Handler fetchHandler(String code) {
        code = code.toLowerCase();
        Handler handler = handlers.get(code);

        if (handler == null) {
            try {
                Method getMethod = this.getClass().getDeclaredMethod(code + "Handler");
                getMethod.setAccessible(true);
                handler = (Handler) getMethod.invoke(this);
                handlers.put(code, handler);
            } catch (NoSuchMethodException|IllegalAccessException e1) {
                // nop.
            } catch (InvocationTargetException e1) {
                throw new CriticalException(e1);
            }
        }

        return handler;
    }


    public void trigger(T target, String code, Event e) {
        Handler handler = fetchHandler(code);

        if (handler != null) {
            EventHandler eventHandler = handler.get(target);

            if (eventHandler != null) {
                eventHandler.handle(e);
            }
        } else {

        }
    }

    public void on(T target, String code, String group, Invoker invoker) {
        Handler handler = fetchHandler(code);

        if (handler != null) {
            EventHandler eventHandler = handler.get(target);
            if (!(eventHandler instanceof ScriptEventHandler)) {
                eventHandler = new ScriptEventHandler(invoker.getEnvironment());

                handler.set(target, eventHandler);
            }

            ScriptEventHandler scriptEventHandler = (ScriptEventHandler) eventHandler;
            scriptEventHandler.set(invoker, group);
        }
    }

    public void off(T target, String code, String group) {
        Handler handler = fetchHandler(code);

        if (handler != null) {
            if (group == null) {
                handler.set(target, null);
            } else {
                EventHandler eventHandler = handler.get(target);
                if (eventHandler != null && eventHandler instanceof ScriptEventHandler) {
                    ((ScriptEventHandler) eventHandler).unset(group);
                }
            }
        }
    }

    @Deprecated
    protected void setHandler(String event, Handler handler) {
        handlers.put(event, handler);
    }

    public boolean hasHandler(String event) {
        fetchHandler(event);
        return handlers.containsKey(event.toLowerCase());
    }

    public static void register(EventProvider eventProvider) {
        eventProviderMap.put(eventProvider.getTargetClass(), eventProvider);
    }

    public static EventProvider get(Object object, String event) {
        if (object == null) {
            return null;
        }

        Class<?> cls = object.getClass();

        while (cls != null) {
            EventProvider eventProvider = eventProviderMap.get(cls);

            if (eventProvider != null && eventProvider.hasHandler(event)) {
                return eventProvider;
            }

            cls = cls.getSuperclass();
        }

        return null;
    }
}
