package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIPanel")
public class UIPanel extends UIContainer {
    protected JPanel component;

    public UIPanel(Environment env, JPanel component) {
        super(env);
        this.component = component;
    }

    public UIPanel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JPanel)component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JPanel(new XYLayout());
    }
}
