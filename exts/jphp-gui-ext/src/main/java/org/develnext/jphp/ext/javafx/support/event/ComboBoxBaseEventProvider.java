package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBoxBase;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class ComboBoxBaseEventProvider extends EventProvider<ComboBoxBase> {
    public Handler showHandler() {
        return new Handler() {
            @Override
            public void set(ComboBoxBase target, EventHandler eventHandler) {
                target.setOnShown(eventHandler);
            }

            @Override
            public EventHandler get(ComboBoxBase target) {
                return target.getOnShown();
            }
        };
    }

    public Handler showingHandler() {
        return new Handler() {
            @Override
            public void set(ComboBoxBase target, EventHandler eventHandler) {
                target.setOnShowing(eventHandler);
            }

            @Override
            public EventHandler get(ComboBoxBase target) {
                return target.getOnShowing();
            }
        };
    }

    public Handler hideHandler() {
        return new Handler() {
            @Override
            public void set(ComboBoxBase target, EventHandler eventHandler) {
                target.setOnHidden(eventHandler);
            }

            @Override
            public EventHandler get(ComboBoxBase target) {
                return target.getOnHidden();
            }
        };
    }

    public Handler hidingHandler() {
        return new Handler() {
            @Override
            public void set(ComboBoxBase target, EventHandler eventHandler) {
                target.setOnHiding(eventHandler);
            }

            @Override
            public EventHandler get(ComboBoxBase target) {
                return target.getOnHiding();
            }
        };
    }

    public Handler actionHandler() {
        return new Handler() {
            @Override
            public void set(ComboBoxBase target, EventHandler eventHandler) {
                target.setOnAction(eventHandler);
            }

            @Override
            public EventHandler get(ComboBoxBase target) {
                return target.getOnAction();
            }
        };
    }

    @Override
    public Class<ComboBoxBase> getTargetClass() {
        return ComboBoxBase.class;
    }
}
