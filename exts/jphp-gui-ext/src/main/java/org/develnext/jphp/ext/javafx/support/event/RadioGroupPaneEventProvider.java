package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.control.ListViewEx;
import org.develnext.jphp.ext.javafx.support.control.RadioGroupPane;

public class RadioGroupPaneEventProvider extends EventProvider<RadioGroupPane> {
    public RadioGroupPaneEventProvider() {
        setHandler("action", new Handler() {
            @Override
            public void set(RadioGroupPane target, EventHandler eventHandler) {
                target.setOnAction(eventHandler);
            }

            @Override
            public EventHandler get(RadioGroupPane target) {
                return target.getOnAction();
            }
        });
    }

    @Override
    public Class<RadioGroupPane> getTargetClass() {
        return RadioGroupPane.class;
    }
}
