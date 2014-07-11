package org.develnext.jphp.swing.event;

import org.develnext.jphp.swing.ComponentProperties;
import php.runtime.env.Environment;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public class JPopupMenuEventProvider extends EventProvider<JPopupMenu> {
    @Override
    public Class<JPopupMenu> getComponentClass() {
        return JPopupMenu.class;
    }

    @Override
    public void register(final Environment env, JPopupMenu component, final ComponentProperties properties) {
        component.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                triggerSimple(env, properties, "open", e);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                triggerSimple(env, properties, "close", e);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                triggerSimple(env, properties, "cancel", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return "open".equalsIgnoreCase(code)
                || "close".equalsIgnoreCase(code)
                || "cancel".equalsIgnoreCase(code);
    }
}
