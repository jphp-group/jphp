package org.develnext.jphp.ext.javafx.support.event;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import org.develnext.jphp.ext.javafx.support.EventProvider;

public class ContextMenuEventProvider extends EventProvider<ContextMenu> {
    public ContextMenuEventProvider() {
        setHandler("action", new Handler() {
            @Override
            public void set(ContextMenu target, EventHandler eventHandler) {
                target.setOnAction(eventHandler);
            }

            @Override
            public EventHandler get(ContextMenu target) {
                return target.getOnAction();
            }
        });
    }

    @Override
    public Class<ContextMenu> getTargetClass() {
        return ContextMenu.class;
    }
}
