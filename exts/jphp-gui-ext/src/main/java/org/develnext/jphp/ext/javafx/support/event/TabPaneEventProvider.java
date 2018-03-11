package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.control.TabPaneEx;

public class TabPaneEventProvider extends EventProvider<TabPaneEx> {
    public TabPaneEventProvider() {
        setHandler("close", new Handler() {
            @Override
            public void set(TabPaneEx target, EventHandler eventHandler) {
                target.setOnCloseTab(eventHandler);
            }

            @Override
            public EventHandler get(TabPaneEx target) {
                return target.getOnCloseTab();
            }
        });

        setHandler("closeRequest", new Handler() {
            @Override
            public void set(TabPaneEx target, EventHandler eventHandler) {
                target.setOnCloseRequestTab(eventHandler);
            }

            @Override
            public EventHandler get(TabPaneEx target) {
                return target.getOnCloseRequestTab();
            }
        });

        setHandler("change", new Handler() {
            @Override
            public void set(TabPaneEx target, EventHandler eventHandler) {
                target.setOnSelectionChange(eventHandler);
            }

            @Override
            public EventHandler get(TabPaneEx target) {
                return target.getOnSelectionChange();
            }
        });
    }

    @Override
    public Class<TabPaneEx> getTargetClass() {
        return TabPaneEx.class;
    }
}
