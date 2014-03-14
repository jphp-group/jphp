package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapColor;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIColorChooser")
public class UIColorChooser extends UIContainer {
    protected JColorChooser component;

    public UIColorChooser(Environment env, JColorChooser component) {
        super(env);
        this.component = component;
    }

    public UIColorChooser(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JColorChooser) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JColorChooser();
    }

    @Signature
    protected Memory __getColor(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getColor()));
    }

    @Signature(@Arg("value"))
    protected Memory __setColor(Environment env, Memory... args) {
        component.setColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getDragEnabled(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getDragEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setDragEnabled(Environment env, Memory... args) {
        component.setDragEnabled(args[0].toBoolean());
        return Memory.NULL;
    }
}
