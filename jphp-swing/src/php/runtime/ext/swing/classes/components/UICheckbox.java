package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UICheckbox")
public class UICheckbox extends UIToggleButton {
    protected JCheckBox component;

    public UICheckbox(Environment env, JCheckBox component) {
        super(env, component);
        this.component = component;
    }

    public UICheckbox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected AbstractButton getAbstractButton() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JCheckBox) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JCheckBox();
    }
}
