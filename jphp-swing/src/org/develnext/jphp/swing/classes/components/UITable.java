package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapColor;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.support.JTableX;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITable")
public class UITable extends UIContainer {
    protected JTableX component;

    public UITable(Environment env, JTableX component) {
        super(env);
        this.component = component;
    }

    public UITable(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JTableX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JTableX();
    }

    @Signature
    protected Memory __getDragEnabled(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.getContent().getDragEnabled());
    }

    @Signature(@Arg("value"))
    protected Memory __setDragEnabled(Environment env, Memory... args) {
        component.getContent().setDragEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectionBackground(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getContent().getSelectionForeground()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionBackground(Environment evn, Memory... args) {
        component.getContent().setSelectionBackground(WrapColor.of(args[0]));
        return Memory.NULL;
    }
}
