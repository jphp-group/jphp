package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class WebEngineEventProvider extends EventProvider<WebEngine> {
    public Handler errorHandler() {
        return new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnError(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnError();
            }
        };
    }

    public Handler alertHandler() {
        return new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnAlert(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnAlert();
            }
        };
    }

    public Handler resizeHandler() {
        return new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnResized(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnResized();
            }
        };
    }

    public Handler statuschangedHandler() {
        return new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnStatusChanged(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnStatusChanged();
            }
        };
    }

    public Handler visibilitychangedHandler() {
        return new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnVisibilityChanged(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnVisibilityChanged();
            }
        };
    }

    @Override
    public Class<WebEngine> getTargetClass() {
        return WebEngine.class;
    }
}
