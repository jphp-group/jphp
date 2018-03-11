package org.develnext.jphp.ext.javafx.jfoenix.support;

import com.jfoenix.controls.JFXTabPane;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.Tab;

public class JFXTabPaneFixed extends JFXTabPane {
    protected EventHandler<Event> onCloseTab;
    protected EventHandler<Event> onCloseRequestTab;
    protected EventHandler<Event> onSelectionChange;

    public JFXTabPaneFixed() {
        super();

        getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> doSelectionChange(newValue.intValue()));

        getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Tab tab : c.getAddedSubList()) {
                        final EventHandler<Event> closeRequest = tab.getOnCloseRequest();

                        tab.setOnClosed(this::doCloseTab);
                        tab.setOnCloseRequest(this::doCloseRequestTab);
                    }
                }
            }
        });
    }

    private void doCloseTab(Event event) {
        if (onCloseTab != null) {
            event = event.copyFor(this, (EventTarget) event.getSource());
            onCloseTab.handle(event);
        }
    }

    private void doCloseRequestTab(Event event) {
        if (onCloseRequestTab != null) {
            Event newEvent = event.copyFor(this, (EventTarget) event.getSource());
            onCloseRequestTab.handle(newEvent);

            if (newEvent.isConsumed()) {
                event.consume();
            }
        }
    }

    private void doSelectionChange(int index) {
        if (onSelectionChange != null) {
            Tab tab = getTabs().get(index);

            Event event = new Event(this, tab, EventType.ROOT);
            onSelectionChange.handle(event);
        }
    }

    public EventHandler<Event> getOnCloseTab() {
        return onCloseTab;
    }

    public void setOnCloseTab(EventHandler<Event> onCloseTab) {
        this.onCloseTab = onCloseTab;
    }

    public EventHandler<Event> getOnCloseRequestTab() {
        return onCloseRequestTab;
    }

    public void setOnCloseRequestTab(EventHandler<Event> onCloseRequestTab) {
        this.onCloseRequestTab = onCloseRequestTab;
    }

    public EventHandler<Event> getOnSelectionChange() {
        return onSelectionChange;
    }

    public void setOnSelectionChange(EventHandler<Event> onSelectionChange) {
        this.onSelectionChange = onSelectionChange;
    }
}
