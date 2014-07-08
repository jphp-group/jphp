package org.develnext.jphp.swing.event;

import org.develnext.jphp.swing.support.JScrollableComponent;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.ComponentProperties;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class ComponentEventProvider extends EventProvider<Component> {

    private final Set<String> allowedEventTypes = new HashSet<String>(){{
        add("keypress");
        add("keydown");
        add("keyup");
        add("focus");
        add("blur");
        add("mousedrag");
        add("mousemove");
        add("mousewheel");
        add("click");
        add("mousepress");
        add("mouserelease");
        add("mouseenter");
        add("mouseexit");
    }};

    @Override
    public Class<Component> getComponentClass() {
        return Component.class;
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return allowedEventTypes.contains(code.toLowerCase());
    }

    @Override
    public void register(final Environment env, Component component, final ComponentProperties properties) {
        if (component instanceof JScrollableComponent)
            return;

        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                triggerKey(env, properties, "keypress", e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                triggerKey(env, properties, "keydown", e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                triggerKey(env, properties, "keyup", e);
            }
        });

        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                triggerFocus(env, properties, "focus", e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                triggerFocus(env, properties, "blur", e);
            }
        });

        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                triggerMouse(env, properties, "mousedrag", e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                triggerMouse(env, properties, "mousemove", e);
            }
        });

        component.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                triggerMouseWheel(env, properties, "mousewheel", e);
            }
        });

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                triggerMouse(env, properties, "click", e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                triggerMouse(env, properties, "mousepress", e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                triggerMouse(env, properties, "mouserelease", e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                triggerMouse(env, properties, "mouseenter", e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                triggerMouse(env, properties, "mouseexit", e);
            }
        });
    }
}
