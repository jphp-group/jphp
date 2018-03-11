package org.develnext.jphp.ext.javafx.support.event;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class WebEngineEventProvider extends EventProvider<WebEngine> {
    public WebEngineEventProvider() {
        setHandler("error", new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnError(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnError();
            }
        });

        setHandler("alert", new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnAlert(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnAlert();
            }
        });

        setHandler("resize", new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnResized(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnResized();
            }
        });

        setHandler("statusChanged", new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnStatusChanged(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnStatusChanged();
            }
        });

        setHandler("visibilityChanged", new Handler() {
            @Override
            public void set(WebEngine target, EventHandler eventHandler) {
                target.setOnVisibilityChanged(eventHandler);
            }

            @Override
            public EventHandler get(WebEngine target) {
                return target.getOnVisibilityChanged();
            }
        });
    }

    @Override
    public Class<WebEngine> getTargetClass() {
        return WebEngine.class;
    }
}
