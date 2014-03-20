package org.develnext.jphp.swing.support;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

abstract public class JScrollableComponent<T extends JComponent> extends JScrollPanel {

    protected T component;

    public JScrollableComponent() {
        super();
        setLayout(new ScrollPaneLayout());

        component = newComponent();
        setViewportView(component);
        setWheelScrollingEnabled(true);
    }

    protected abstract T newComponent();

    public T getContent() {
        return component;
    }

    @Override
    public void setForeground(Color fg) {
        if (component != null)
            component.setForeground(fg);
    }

    @Override
    public Color getForeground() {
        return component == null ? null : component.getForeground();
    }

    @Override
    public void setBackground(Color bg) {
        if (component != null)
            component.setBackground(bg);
    }

    @Override
    public Color getBackground() {
        return component == null ? null : component.getBackground();
    }

    @Override
    public void setFont(Font font) {
        if (component != null)
            component.setFont(font);
    }

    @Override
    public Font getFont() {
        return component == null ? null : component.getFont();
    }

    @Override
    public void setFocusable(boolean value) {
        component.setFocusable(value);
    }

    @Override
    public boolean isFocusable() {
        return component.isFocusable();
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        component.setEnabled(value);
    }

    @Override
    public void setComponentPopupMenu(JPopupMenu popupMenu) {
        component.setComponentPopupMenu(popupMenu);
    }

    @Override
    public JPopupMenu getComponentPopupMenu() {
        return component.getComponentPopupMenu();
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (component != null)
            component.addMouseListener(l);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        if (component != null)
            component.addKeyListener(l);
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        if (component != null)
            component.addFocusListener(l);
    }

    @Override
    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        if (component != null)
            component.addMouseWheelListener(l);
        else
            super.addMouseWheelListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        if (component != null)
            component.addMouseMotionListener(l);
    }

    @Override
    public synchronized void addInputMethodListener(InputMethodListener l) {
        if (component != null)
            component.addInputMethodListener(l);
    }
}
