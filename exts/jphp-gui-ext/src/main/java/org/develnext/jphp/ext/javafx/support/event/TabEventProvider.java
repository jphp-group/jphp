package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class TabEventProvider extends EventProvider<Tab> {
    public TabEventProvider() {
        setHandler("close", new Handler() {
            @Override
            public void set(Tab target, EventHandler eventHandler) {
                target.setOnClosed(eventHandler);
            }

            @Override
            public EventHandler get(Tab target) {
                return target.getOnClosed();
            }
        });

        setHandler("closeRequest", new Handler() {
            @Override
            public void set(Tab target, EventHandler eventHandler) {
                target.setOnCloseRequest(eventHandler);
            }

            @Override
            public EventHandler get(Tab target) {
                return target.getOnCloseRequest();
            }
        });

        setHandler("change", new Handler() {
            @Override
            public void set(Tab target, EventHandler eventHandler) {
                target.setOnSelectionChanged(eventHandler);
            }

            @Override
            public EventHandler get(Tab target) {
                return target.getOnSelectionChanged();
            }
        });
    }

    @Override
    public Class<Tab> getTargetClass() {
        return Tab.class;
    }
}
