package org.develnext.jphp.ext.javafx.jfoenix.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.jfoenix.support.JFXTabPaneFixed;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.control.TabPaneEx;

public class JFXTabPaneEventProvider extends EventProvider<JFXTabPaneFixed> {
    public JFXTabPaneEventProvider() {
        setHandler("close", new Handler() {
            @Override
            public void set(JFXTabPaneFixed target, EventHandler eventHandler) {
                target.setOnCloseTab(eventHandler);
            }

            @Override
            public EventHandler get(JFXTabPaneFixed target) {
                return target.getOnCloseTab();
            }
        });

        setHandler("closeRequest", new Handler() {
            @Override
            public void set(JFXTabPaneFixed target, EventHandler eventHandler) {
                target.setOnCloseRequestTab(eventHandler);
            }

            @Override
            public EventHandler get(JFXTabPaneFixed target) {
                return target.getOnCloseRequestTab();
            }
        });

        setHandler("change", new Handler() {
            @Override
            public void set(JFXTabPaneFixed target, EventHandler eventHandler) {
                target.setOnSelectionChange(eventHandler);
            }

            @Override
            public EventHandler get(JFXTabPaneFixed target) {
                return target.getOnSelectionChange();
            }
        });
    }

    @Override
    public Class<JFXTabPaneFixed> getTargetClass() {
        return JFXTabPaneFixed.class;
    }
}
