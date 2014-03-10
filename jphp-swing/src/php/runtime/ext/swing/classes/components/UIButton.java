package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIAbstractButton;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Optional;

@Name(SwingExtension.NAMESPACE + "UIButton")
public class UIButton extends UIAbstractButton {
    protected JButton component;

    public UIButton(Environment env, JButton component) {
        super(env);
        this.component = component;
    }

    public UIButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Optional
    public void onInit(Environment env, Memory... args){
        component = new JButton();
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JButton)component;
    }

    @Override
    protected AbstractButton getAbstractButton() {
        return component;
    }
}
