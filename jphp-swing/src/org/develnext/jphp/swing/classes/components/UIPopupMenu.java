package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIPopupMenu")
public class UIPopupMenu extends UIContainer {
    protected JPopupMenu component;

    public UIPopupMenu(Environment env, JPopupMenu component) {
        super(env);
        this.component = component;
    }

    public UIPopupMenu(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public JPopupMenu getComponent() {
        return component;
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JPopupMenu) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JPopupMenu();
    }

    @Signature
    public Memory addSeparator(Environment env, Memory... args) {
        component.addSeparator();
        return Memory.NULL;
    }

    @Signature
    protected Memory __getBorderPainted(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isBorderPainted());
    }

    @Signature(@Arg("value"))
    protected Memory __setBorderPainted(Environment env, Memory... args) {
        component.setBorderPainted(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getLightweightPopup(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isLightWeightPopupEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setLightweightPopup(Environment env, Memory... args) {
        component.setLightWeightPopupEnabled(args[0].toBoolean());
        return Memory.NULL;
    }


    @Signature
    protected Memory __getLabel(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getLabel());
    }

    @Signature(@Arg("value"))
    protected Memory __setLabel(Environment env, Memory... args) {
        component.setLabel(args[0].toString());
        return Memory.NULL;
    }

    @Signature({@Arg("w"), @Arg("h")})
    public Memory setPopupSize(Environment env, Memory... args) {
        component.setPopupSize(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg(value = "component", typeClass = SwingExtension.NAMESPACE + "UIElement")})
    public Memory setSelected(Environment env, Memory... args) {
        component.setSelected(
                args[0].toObject(UIElement.class).getComponent()
        );
        return Memory.NULL;
    }
}
