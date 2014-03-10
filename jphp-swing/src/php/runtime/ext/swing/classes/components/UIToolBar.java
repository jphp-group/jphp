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

@Name(SwingExtension.NAMESPACE + "UIToolBar")
public class UIToolBar extends UIContainer {
    protected JToolBar component;

    public UIToolBar(Environment env, JToolBar component) {
        super(env);
        this.component = component;
    }

    public UIToolBar(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JToolBar) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JToolBar();
    }

    @Signature
    protected Memory __getVertical(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getOrientation() == SwingConstants.VERTICAL);
    }

    @Signature(@Arg("value"))
    protected Memory __setVertical(Environment env, Memory... args) {
        component.setOrientation(args[0].toBoolean() ? SwingConstants.VERTICAL : SwingConstants.HORIZONTAL);
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
    protected Memory __getFloatable(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isFloatable());
    }

    @Signature(@Arg("value"))
    protected Memory __setFloatable(Environment env, Memory... args) {
        component.setFloatable(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRollover(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isRollover());
    }

    @Signature(@Arg("value"))
    protected Memory __setRollover(Environment env, Memory... args) {
        component.setRollover(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "w", optional = @Optional("NULL")),
            @Arg(value = "h", optional = @Optional("NULL"))
    })
    public Memory addSeparator(Environment env, Memory... args) {
        if (args[0].isNull() && args[1].isNull())
            component.addSeparator();
        else
            component.addSeparator(new Dimension(args[0].toInteger(), args[1].toInteger()));
        return Memory.NULL;
    }
}
