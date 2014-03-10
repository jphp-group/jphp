package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;

import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIRadioButton")
public class UIRadioButton extends UIToggleButton {
    public JRadioButton component;

    public UIRadioButton(Environment env, JRadioButton component1) {
        super(env, component1);
        component = component1;
    }

    public UIRadioButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        this.component = (JRadioButton)component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JRadioButton();
        super.component = component;
    }
}
