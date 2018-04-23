package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class MenuItemEventProvider extends EventProvider<MenuItem> {
    public Handler actionHandler() {
        return new Handler() {
            @Override
            public void set(MenuItem target, EventHandler eventHandler) {
                target.setOnAction(eventHandler);
            }

            @Override
            public EventHandler get(MenuItem target) {
                return target.getOnAction();
            }
        };
    }

    public Handler menuvalidationHandler() {
        return new Handler() {
            @Override
            public void set(MenuItem target, EventHandler eventHandler) {
                target.setOnMenuValidation(eventHandler);
            }

            @Override
            public EventHandler get(MenuItem target) {
                return target.getOnMenuValidation();
            }
        };
    }

    @Override
    public Class<MenuItem> getTargetClass() {
        return MenuItem.class;
    }
}
