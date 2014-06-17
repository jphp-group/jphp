package org.develnext.jphp.swing.event;

import org.develnext.jphp.swing.ComponentProperties;
import php.runtime.env.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JMenuItemEventProvider extends EventProvider<JMenuItem> {
    @Override
    public Class<JMenuItem> getComponentClass() {
        return JMenuItem.class;
    }

    @Override
    public void register(final Environment env, JMenuItem component, final ComponentProperties properties) {
        component.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerSimple(env, properties, "click", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return "click".equalsIgnoreCase(code);
    }
}
