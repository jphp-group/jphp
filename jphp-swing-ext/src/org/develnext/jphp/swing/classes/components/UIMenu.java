package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIMenu")
public class UIMenu extends UIMenuItem {
    protected JMenu component;

    public UIMenu(Environment env, JMenu component) {
        super(env, component);
        this.component = component;
    }

    public UIMenu(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JMenu();
        super.component = component;
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
        this.component = (JMenu) component;
    }

    @Signature
    protected Memory __getItemCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getItemCount());
    }

    @Signature
    protected Memory __getDelay(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getDelay());
    }

    @Signature(@Arg("value"))
    protected Memory __setDelay(Environment env, Memory... args) {
        component.setDelay(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory getItem(Environment env, Memory... args) {
        JMenuItem item = component.getItem(args[0].toInteger());
        return new ObjectMemory(new UIMenuItem(env, item));
    }

    @Signature
    public Memory addSeparator(Environment env, Memory... args) {
        component.addSeparator();
        return Memory.NULL;
    }

    @Signature
    protected Memory __getPopupVisible(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isPopupMenuVisible());
    }

    @Signature(@Arg("value"))
    protected Memory __setPopupVisible(Environment env, Memory... args) {
        component.setPopupMenuVisible(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y")})
    public Memory setMenuPosition(Environment env, Memory... args) {
        component.setMenuLocation(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory isTopLevelMenu(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isTopLevelMenu());
    }

    @Signature
    public Memory isTearOff(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isTearOff());
    }
}
