package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.control.TabPaneEx;

public class TabPaneEventProvider extends EventProvider<TabPaneEx> {
    public Handler closeHandler() {
        return new Handler() {
            @Override
            public void set(TabPaneEx target, EventHandler eventHandler) {
                target.setOnCloseTab(eventHandler);
            }

            @Override
            public EventHandler get(TabPaneEx target) {
                return target.getOnCloseTab();
            }
        };
    }

    public Handler closerequestHandler() {
        return new Handler() {
            @Override
            public void set(TabPaneEx target, EventHandler eventHandler) {
                target.setOnCloseRequestTab(eventHandler);
            }

            @Override
            public EventHandler get(TabPaneEx target) {
                return target.getOnCloseRequestTab();
            }
        };
    }

    public Handler changeHandler() {
        return new Handler() {
            @Override
            public void set(TabPaneEx target, EventHandler eventHandler) {
                target.setOnSelectionChange(eventHandler);
            }

            @Override
            public EventHandler get(TabPaneEx target) {
                return target.getOnSelectionChange();
            }
        };
    }

    @Override
    public Class<TabPaneEx> getTargetClass() {
        return TabPaneEx.class;
    }
}
