package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class MenuEventProvider extends EventProvider<Menu> {
    public Handler showingHandler() {
        return new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnShowing(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnShowing();
            }
        };
    }

    public Handler showHandler() {
        return new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnShown(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnShown();
            }
        };
    }

    public Handler hidingHandler() {
        return new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnHiding(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnHiding();
            }
        };
    }

    public Handler hideHandler() {
        return new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnHidden(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnHidden();
            }
        };
    }

    @Override
    public Class<Menu> getTargetClass() {
        return Menu.class;
    }
}
