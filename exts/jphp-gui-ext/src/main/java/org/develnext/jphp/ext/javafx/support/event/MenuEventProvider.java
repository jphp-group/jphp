package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class MenuEventProvider extends EventProvider<Menu> {
    public MenuEventProvider() {
        setHandler("showing", new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnShowing(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnShowing();
            }
        });

        setHandler("show", new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnShown(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnShown();
            }
        });

        setHandler("hiding", new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnHiding(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnHiding();
            }
        });

        setHandler("hide", new Handler() {
            @Override
            public void set(Menu target, EventHandler eventHandler) {
                target.setOnHidden(eventHandler);
            }

            @Override
            public EventHandler get(Menu target) {
                return target.getOnHidden();
            }
        });
    }

    @Override
    public Class<Menu> getTargetClass() {
        return Menu.class;
    }
}
