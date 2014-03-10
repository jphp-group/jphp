package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIMenuBar")
public class UIMenuBar extends UIContainer {
    protected JMenuBar component;

    public UIMenuBar(Environment env, JMenuBar component) {
        super(env);
        this.component = component;
    }

    public UIMenuBar(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JMenuBar) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JMenuBar();
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
}
