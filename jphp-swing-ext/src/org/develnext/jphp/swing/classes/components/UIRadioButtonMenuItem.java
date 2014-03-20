package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIRadioButtonMenuItem")
public class UIRadioButtonMenuItem extends UIMenuItem {
    protected JRadioButtonMenuItem component;

    public UIRadioButtonMenuItem(Environment env, JRadioButtonMenuItem component) {
        super(env, component);
        this.component = component;
    }

    public UIRadioButtonMenuItem(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JRadioButtonMenuItem();
        super.component = component;
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        this.component = (JRadioButtonMenuItem) component;
    }
}
