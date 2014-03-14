package org.develnext.jphp.swing.event;

import php.runtime.env.Environment;
import org.develnext.jphp.swing.ComponentProperties;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;

public class WindowEventProvider extends EventProvider<Window> {

    private final Set<String> allowedEventTypes = new HashSet<String>(){{
        add("windowopen");
        add("windowclosing");
        add("windowclose");
        add("windowactive");
        add("windowdeactive");
    }};

    @Override
    public Class<Window> getComponentClass() {
        return Window.class;
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return allowedEventTypes.contains(code.toLowerCase());
    }

    @Override
    public void register(final Environment env, Window wnd, final ComponentProperties properties) {
        wnd.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                triggerWindow(env, properties, "windowopen", e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                triggerWindow(env, properties, "windowclosing", e);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                triggerWindow(env, properties, "windowclose", e);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {
                triggerWindow(env, properties, "windowactive", e);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                triggerWindow(env, properties, "windowdeactive", e);
            }
        });
    }
}
