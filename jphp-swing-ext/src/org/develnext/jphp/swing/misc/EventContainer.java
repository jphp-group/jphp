package org.develnext.jphp.swing.misc;


import php.runtime.invoke.Invoker;

import java.util.HashMap;
import java.util.Map;

public class EventContainer {
    protected Map<String, EventList> events;

    public EventContainer() {
        this.events = new HashMap<String, EventList>();
    }

    public EventList get(String event){
       return events.get(event);
    }

    public Invoker addEvent(String event, String group, Invoker handler){
        EventList list = events.get(event);
        if (list == null)
            events.put(event, list = new EventList());

        return list.put(group, handler);
    }

    public Invoker clearEvent(String event, String group){
        EventList list = events.get(event);
        if (list == null)
            return null;

        return list.remove(group);
    }

    public EventList clearEvent(String event){
        EventList list = events.get(event);
        if (list == null)
            return null;

        return events.remove(event);
    }
}
