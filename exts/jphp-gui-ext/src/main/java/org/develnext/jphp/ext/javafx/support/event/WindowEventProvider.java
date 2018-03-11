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

        setHandler("keyPress", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnKeyTyped(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnKeyTyped();
            }
        });

        setHandler("keyDown", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnKeyPressed(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnKeyPressed();
            }
        });

        setHandler("keyUp", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnKeyReleased(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnKeyReleased();
            }
        });

        setHandler("globalMouseMove", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnMouseMoved(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnMouseMoved();
            }
        });

        setHandler("scroll", new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnScroll(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnScroll();
            }
        });
    }

    @Override
    public Class<Window> getTargetClass() {
        return Window.class;
    }
}
