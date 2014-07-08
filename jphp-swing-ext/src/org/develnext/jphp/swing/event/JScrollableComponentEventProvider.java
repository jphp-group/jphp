package org.develnext.jphp.swing.event;

import org.develnext.jphp.swing.ComponentProperties;
import org.develnext.jphp.swing.support.JScrollableComponent;
import php.runtime.env.Environment;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class JScrollableComponentEventProvider extends EventProvider<JScrollableComponent> {
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
    public Class<JScrollableComponent> getComponentClass() {
        return JScrollableComponent.class;
    }

    @Override
    public void register(final Environment env, final JScrollableComponent component,
                         final ComponentProperties properties) {
        component.getContent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                e.setSource(component);
                triggerKey(env, properties, "keypress", e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                e.setSource(component);
                triggerKey(env, properties, "keydown", e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                e.setSource(component);
                triggerKey(env, properties, "keyup", e);
            }
        });

        component.getContent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                e.setSource(component);
                triggerFocus(env, properties, "focus", e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.setSource(component);
                triggerFocus(env, properties, "blur", e);
            }
        });

        component.getContent().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "mousedrag", e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "mousemove", e);
            }
        });

        component.getContent().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                e.setSource(component);
                triggerMouseWheel(env, properties, "mousewheel", e);
            }
        });

        component.getContent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "click", e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "mousepress", e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "mouserelease", e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "mouseenter", e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.setSource(component);
                triggerMouse(env, properties, "mouseexit", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return allowedEventTypes.contains(code.toLowerCase());
    }
}
