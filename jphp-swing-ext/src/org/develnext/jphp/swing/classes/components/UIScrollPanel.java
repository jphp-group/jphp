package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.support.JScrollPanel;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIScrollPanel")
public class UIScrollPanel extends UIContainer {
    protected JScrollPanel component;

    public UIScrollPanel(Environment env, JScrollPanel component) {
        super(env);
        this.component = component;
    }

    public UIScrollPanel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JScrollPanel) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JScrollPanel();
        component.setLayout(new XYLayout());
    }

    @Reflection.Signature
    protected Memory __getHorScrollPolicy(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getHorScrollPolicy().name());
    }

    @Reflection.Signature(@Reflection.Arg("value"))
    protected Memory __setHorScrollPolicy(Environment env, Memory... args) {
        component.setHorScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }

    @Reflection.Signature
    protected Memory __getVerScrollPolicy(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getHorScrollPolicy().name());
    }

    @Reflection.Signature(@Reflection.Arg("value"))
    protected Memory __setVerScrollPolicy(Environment env, Memory... args) {
        component.setHorScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }
}
