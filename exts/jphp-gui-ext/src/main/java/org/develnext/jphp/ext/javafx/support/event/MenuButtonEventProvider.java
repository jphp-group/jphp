package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuButton;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import org.develnext.jphp.ext.javafx.support.UserData;

public class MenuButtonEventProvider extends EventProvider<MenuButton> {
    public MenuButtonEventProvider() {
        setHandler("showing", new Handler() {
            @Override
            public void set(MenuButton target, EventHandler eventHandler) {
                target.addEventHandler(MenuButton.ON_SHOWING, eventHandler);
                JavaFxUtils.saveEventHandler(target, "showing", eventHandler);
            }

            @Override
            public EventHandler get(MenuButton target) {
                return JavaFxUtils.loadEventHandler(target, "showing");
            }
        });

        setHandler("show", new Handler() {
            @Override
            public void set(MenuButton target, EventHandler eventHandler) {
                target.addEventHandler(MenuButton.ON_SHOWN, eventHandler);
                JavaFxUtils.saveEventHandler(target, "show", eventHandler);
            }

            @Override
            public EventHandler get(MenuButton target) {
                return JavaFxUtils.loadEventHandler(target, "show");
            }
        });

        setHandler("hiding", new Handler() {
            @Override
            public void set(MenuButton target, EventHandler eventHandler) {
                target.addEventHandler(MenuButton.ON_HIDING, eventHandler);
                JavaFxUtils.saveEventHandler(target, "hiding", eventHandler);
            }

            @Override
            public EventHandler get(MenuButton target) {
                return JavaFxUtils.loadEventHandler(target, "hiding");
            }
        });

        setHandler("hide", new Handler() {
            @Override
            public void set(MenuButton target, EventHandler eventHandler) {
                target.addEventHandler(MenuButton.ON_HIDDEN, eventHandler);
                JavaFxUtils.saveEventHandler(target, "hide", eventHandler);
            }

            @Override
            public EventHandler get(MenuButton target) {
                return JavaFxUtils.loadEventHandler(target, "hide");
            }
        });
    }

    @Override
    public Class<MenuButton> getTargetClass() {
        return MenuButton.class;
    }
}
