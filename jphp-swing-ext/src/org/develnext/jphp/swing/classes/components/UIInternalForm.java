package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIInternalForm")
public class UIInternalForm extends UIContainer {
    protected JInternalFrame frame;

    public UIInternalForm(Environment env, JInternalFrame frame) {
        super(env);
        this.frame = frame;
    }

    public UIInternalForm(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return frame;
    }

    @Override
    public void setComponent(Component component) {
        frame = (JInternalFrame)component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        frame = new JInternalFrame();
    }

    @Signature(@Arg(value = "value", nativeType = UIDesktopPanel.class))
    public Memory setLayeredPanel(Environment env, Memory... args) {
        frame.setLayeredPane(args[0].toObject(UIDesktopPanel.class).getPane());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "content", nativeType = UIContainer.class))
    public Memory setContent(Environment env, Memory... args) {
        frame.setContentPane(args[0].toObject(UIContainer.class).getContainer());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setSelected(Environment env, Memory... args) throws PropertyVetoException {
        frame.setSelected(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelected(Environment env, Memory... args) {
        return frame.isSelected() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setResizable(Environment env, Memory... args) throws PropertyVetoException {
        frame.setResizable(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getResizable(Environment env, Memory... args) {
        return frame.isResizable() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setTitle(Environment env, Memory... args) throws PropertyVetoException {
        frame.setTitle(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getTitle(Environment env, Memory... args) {
        return StringMemory.valueOf(frame.getTitle());
    }
}
