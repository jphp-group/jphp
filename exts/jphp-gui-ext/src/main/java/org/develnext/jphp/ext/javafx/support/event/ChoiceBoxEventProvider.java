package org.develnext.jphp.ext.javafx.support.event;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import org.develnext.jphp.ext.javafx.classes.UXChoiceBox;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class ChoiceBoxEventProvider extends EventProvider<ChoiceBox> {
    public ChoiceBoxEventProvider() {
        setHandler("action", new Handler() {
            @Override
            public void set(ChoiceBox target, final EventHandler eventHandler) {
                ChangeListener<Number> listener = new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        eventHandler.handle(null);
                    }
                };

                if (target instanceof UXChoiceBox.WrapChoiceBox) {
                    ((UXChoiceBox.WrapChoiceBox) target).setActionListener(listener);
                } else {
                    if (eventHandler != null) {
                        target.getSelectionModel().selectedIndexProperty().addListener(listener);
                    }
                }
            }

            @Override
            public EventHandler get(ChoiceBox target) {
                return null;
            }
        });
    }

    @Override
    public Class<ChoiceBox> getTargetClass() {
        return ChoiceBox.class;
    }
}
