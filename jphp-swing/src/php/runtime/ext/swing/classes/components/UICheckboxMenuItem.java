package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;

import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UICheckboxMenuItem")
public class UICheckboxMenuItem extends UIMenuItem {
    protected JCheckBoxMenuItem component;

    public UICheckboxMenuItem(Environment env, JCheckBoxMenuItem component) {
        super(env, component);
        this.component = component;
    }

    public UICheckboxMenuItem(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JCheckBoxMenuItem();
        super.component = component;
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        this.component = (JCheckBoxMenuItem) component;
    }
}
