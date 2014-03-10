package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.support.JScrollPanel;
import php.runtime.ext.swing.support.JTextAreaX;
import php.runtime.ext.swing.support.RootTextElement;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITextArea")
public class UITextArea extends UITextElement {
    protected JTextAreaX component;

    public UITextArea(Environment env, JTextAreaX component) {
        super(env);
        this.component = component;
    }

    public UITextArea(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public RootTextElement getTextComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JTextAreaX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JTextAreaX();
    }

    @Signature
    protected Memory __getLineWrap(Environment env, Memory... args) {
        return component.getContent().getLineWrap() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setLineWrap(Environment env, Memory... args) {
        component.getContent().setLineWrap(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getWrapStyleWord(Environment env, Memory... args) {
        return component.getContent().getWrapStyleWord() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setWrapStyleWord(Environment env, Memory... args) {
        component.getContent().setWrapStyleWord(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getColumns(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getColumns());
    }

    @Signature(@Arg("value"))
    protected Memory __setColumns(Environment env, Memory... args) {
        component.getContent().setColumns(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRows(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getRows());
    }

    @Signature(@Arg("value"))
    protected Memory __setRows(Environment env, Memory... args) {
        component.getContent().setRows(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getTabSize(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getTabSize());
    }

    @Signature(@Arg("value"))
    protected Memory __setTabSize(Environment env, Memory... args) {
        component.getContent().setTabSize(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getLineCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getContent().getLineCount());
    }

    @Signature
    protected Memory __getHorScrollPolicy(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getHorScrollPolicy().name());
    }

    @Signature(@Arg("value"))
    protected Memory __setHorScrollPolicy(Environment env, Memory... args) {
        component.setHorScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVerScrollPolicy(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getHorScrollPolicy().name());
    }

    @Signature(@Arg("value"))
    protected Memory __setVerScrollPolicy(Environment env, Memory... args) {
        component.setHorScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }
}
