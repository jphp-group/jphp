package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class WindowEventProvider extends EventProvider<Window> {
    public Handler showingHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnShowing(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnShowing();
            }
        };
    }

    public Handler showHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnShown(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnShown();
            }
        };
    }

    public Handler hidingHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnHiding(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnHiding();
            }
        };
    }

    public Handler hideHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnHidden(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnHidden();
            }
        };
    }

    public Handler closeHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.setOnCloseRequest(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getOnCloseRequest();
            }
        };
    }

    public Handler keypressHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnKeyTyped(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnKeyTyped();
            }
        };
    }

    public Handler keydownHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnKeyPressed(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnKeyPressed();
            }
        };
    }

    public Handler keyupHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnKeyReleased(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnKeyReleased();
            }
        };
    }

    public Handler globalmousemoveHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnMouseMoved(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnMouseMoved();
            }
        };
    }

    public Handler scrollHandler() {
        return new Handler() {
            @Override
            public void set(Window target, EventHandler eventHandler) {
                target.getScene().setOnScroll(eventHandler);
            }

            @Override
            public EventHandler get(Window target) {
                return target.getScene().getOnScroll();
            }
        };
    }

    @Override
    public Class<Window> getTargetClass() {
        return Window.class;
    }
}
