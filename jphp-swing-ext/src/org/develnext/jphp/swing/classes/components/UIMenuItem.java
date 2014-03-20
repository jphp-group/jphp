package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIAbstractButton;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIMenuItem")
public class UIMenuItem extends UIAbstractButton {
    protected JMenuItem component;

    public UIMenuItem(Environment env, JMenuItem component) {
        super(env);
        this.component = component;
    }

    public UIMenuItem(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected AbstractButton getAbstractButton() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JMenuItem) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JMenuItem();
    }
}
