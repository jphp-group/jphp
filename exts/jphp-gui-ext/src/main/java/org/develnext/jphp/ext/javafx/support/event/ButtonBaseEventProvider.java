package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class ButtonBaseEventProvider extends EventProvider<ButtonBase> {
    public ButtonBaseEventProvider() {
        setHandler("action", new Handler() {
            @Override
            public void set(ButtonBase target, EventHandler eventHandler) {
                target.setOnAction(eventHandler);
            }

            @Override
            public EventHandler get(ButtonBase target) {
                return target.getOnAction();
            }
        });
    }

    @Override
    public Class<ButtonBase> getTargetClass() {
        return ButtonBase.class;
    }
}
