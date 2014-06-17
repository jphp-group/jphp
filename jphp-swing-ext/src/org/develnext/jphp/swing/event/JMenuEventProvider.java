package org.develnext.jphp.swing.event;

import org.develnext.jphp.swing.ComponentProperties;
import php.runtime.env.Environment;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class JMenuEventProvider extends EventProvider<JMenu> {
    @Override
    public Class<JMenu> getComponentClass() {
        return JMenu.class;
    }

    @Override
    public void register(final Environment env, JMenu component, final ComponentProperties properties) {
        component.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(final MenuEvent e) {
                triggerSimple(env, properties, "select", e);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                triggerSimple(env, properties, "deselect", e);
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                triggerSimple(env, properties, "cancel", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return "select".equalsIgnoreCase(code)
                || "deselect".equalsIgnoreCase(code)
                || "cancel".equalsIgnoreCase(code);
    }
}
