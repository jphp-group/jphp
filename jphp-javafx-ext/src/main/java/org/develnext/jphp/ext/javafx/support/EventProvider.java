package org.develnext.jphp.ext.javafx.support;

import javafx.event.Event;
import javafx.event.EventHandler;
import php.runtime.invoke.Invoker;

import java.util.HashMap;
import java.util.Map;

abstract public class EventProvider<T> {

    protected static Map<Class<?>, EventProvider> eventProviderMap = new HashMap<Class<?>, EventProvider>();

    abstract protected class Handler {
        abstract public void set(T target, EventHandler eventHandler);
        abstract public EventHandler get(T target);
    }

    protected Map<String, Handler> handlers = new HashMap<String, Handler>();

    public EventProvider() {
        // ...
    }

    abstract public Class<T> getTargetClass();


    public void trigger(T target, String code, Event e) {
        Handler handler = handlers.get(code.toLowerCase());

        if (handler != null) {
            EventHandler eventHandler = handler.get(target);

            if (eventHandler != null) {
                eventHandler.handle(e);
            }
        }
    }

    public void on(T target, String code, String group, Invoker invoker) {
        Handler handler = handlers.get(code.toLowerCase());

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
        Handler handler = handlers.get(code.toLowerCase());

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

    protected void setHandler(String event, Handler handler) {
        handlers.put(event.toLowerCase(), handler);
    }

    public boolean hasHandler(String event) {
        return handlers.containsKey(event.toLowerCase());
    }

    public static void register(EventProvider eventProvider) {
        eventProviderMap.put(eventProvider.getTargetClass(), eventProvider);
    }

    public static EventProvider get(Object object, String event) {
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
