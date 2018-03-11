package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.control.ListViewEx;

public class ListViewEventProvider extends EventProvider<ListViewEx> {
    public ListViewEventProvider() {
        setHandler("action", new Handler() {
            @Override
            public void set(ListViewEx target, EventHandler eventHandler) {
                target.setOnAction(eventHandler);
            }

            @Override
            public EventHandler get(ListViewEx target) {
                return target.getOnAction();
            }
        });
    }

    @Override
    public Class<ListViewEx> getTargetClass() {
        return ListViewEx.class;
    }
}
