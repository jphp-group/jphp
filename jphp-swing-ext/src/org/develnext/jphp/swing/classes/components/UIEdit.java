package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.support.JTextFieldX;
import org.develnext.jphp.swing.support.RootTextElement;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIEdit")
public class UIEdit extends UITextElement {
    protected JTextFieldX component;

    public UIEdit(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void onInit(Environment env, Memory... args){
        component = new JTextFieldX();
    }

    @Override
    public RootTextElement getTextComponent() {
        return component;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JTextFieldX)component;
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Signature
    protected Memory __getColumns(Environment env, Memory... args){
        return LongMemory.valueOf(component.getColumns());
    }

    @Signature(@Arg("value"))
    protected Memory __setColumns(Environment env, Memory... args){
        component.setColumns(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getScrollOffset(Environment env, Memory... args){
        return LongMemory.valueOf(component.getScrollOffset());
    }

    @Signature(@Arg("value"))
    protected Memory __setScrollOffset(Environment env, Memory... args){
        component.setScrollOffset(args[0].toInteger());
        return Memory.NULL;
    }
}
