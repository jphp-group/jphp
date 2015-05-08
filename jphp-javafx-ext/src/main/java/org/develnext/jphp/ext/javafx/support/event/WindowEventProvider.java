package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class WindowEventProvider extends EventProvider<Window> {

    public WindowEventProvider() {
        setHandler("showing", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnShowing(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnShowing();
            }
        });

        setHandler("show", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnShown(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnShown();
            }
        });

        setHandler("hiding", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnHiding(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnHiding();
            }
        });

        setHandler("hide", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnHidden(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnHidden();
            }
        });

        setHandler("close", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnCloseRequest(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnCloseRequest();
            }
        });
    }

    @Override
    public Class<Window> getTargetClass() {
        return Window.class;
    }
}
