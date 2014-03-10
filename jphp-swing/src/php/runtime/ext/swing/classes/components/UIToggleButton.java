package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIAbstractButton;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIToggleButton")
public class UIToggleButton extends UIAbstractButton {
    protected JToggleButton component;

    public UIToggleButton(Environment env) {
        super(env);
    }

    public UIToggleButton(Environment env, JToggleButton component) {
        super(env);
        this.component = component;
    }

    public UIToggleButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected AbstractButton getAbstractButton() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JToggleButton) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JToggleButton();
    }
}
