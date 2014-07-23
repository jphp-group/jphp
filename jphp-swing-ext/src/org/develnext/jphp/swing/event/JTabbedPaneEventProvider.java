package org.develnext.jphp.swing.event;

import org.develnext.jphp.swing.ComponentProperties;
import php.runtime.env.Environment;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class JTabbedPaneEventProvider extends EventProvider<JTabbedPane> {
    @Override
    public Class<JTabbedPane> getComponentClass() {
        return JTabbedPane.class;
    }

    @Override
    public void register(final Environment env, JTabbedPane component, final ComponentProperties properties) {
        component.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                triggerSimple(env, properties, "change", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return "change".equalsIgnoreCase(code);
    }
}
