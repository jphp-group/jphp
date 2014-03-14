package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.support.JEditorPaneX;
import org.develnext.jphp.swing.support.RootTextElement;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIEditorArea")
public class UIEditorArea extends UITextElement {
    protected JEditorPaneX component;

    public UIEditorArea(Environment env, JEditorPaneX component) {
        super(env);
        this.component = component;
    }

    public UIEditorArea(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public RootTextElement getTextComponent() {
        return component;
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JEditorPaneX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JEditorPaneX();
    }

    @Signature
    protected Memory __getContentType(Environment env, Memory... args) {
        return new StringMemory(component.getContent().getContentType());
    }

    @Signature(@Arg("value"))
    protected Memory __setContentType(Environment env, Memory... args) {
        component.getContent().setContentType(args[0].toString());
        return Memory.NULL;
    }
}
