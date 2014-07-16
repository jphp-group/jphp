package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIDesktopPanel")
public class UIDesktopPanel extends UIContainer {
    protected JDesktopPane pane;

    public UIDesktopPanel(Environment env, JDesktopPane pane) {
        super(env);
        this.pane = pane;
    }

    public UIDesktopPanel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public JDesktopPane getPane() {
        return pane;
    }

    @Override
    public Container getContainer() {
        return pane;
    }

    @Override
    public void setComponent(Component component) {
        pane = (JDesktopPane) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        pane = new JDesktopPane();
        pane.setLayout(new XYLayout());
        pane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
    }
}
