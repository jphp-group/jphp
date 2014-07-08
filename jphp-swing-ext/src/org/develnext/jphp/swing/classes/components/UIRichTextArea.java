package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.text.WrapStyle;
import org.develnext.jphp.swing.support.JRichTextAreaX;
import org.develnext.jphp.swing.support.JScrollPanel;
import org.develnext.jphp.swing.support.RootTextElement;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIRichTextArea")
public class UIRichTextArea extends UITextElement {
    protected JRichTextAreaX component;

    public UIRichTextArea(Environment env, JRichTextAreaX component) {
        super(env);
        this.component = component;
    }

    public UIRichTextArea(Environment env, ClassEntity clazz) {
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
        this.component = (JRichTextAreaX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JRichTextAreaX();
    }

    @Signature(@Arg(value = "style", nativeType = WrapStyle.class))
    public Memory __setLogicalStyle(Environment env, Memory... args) {
        component.getContent().setLogicalStyle(args[0].toObject(WrapStyle.class).getStyle());
        return Memory.NULL;
    }

    @Signature
    public Memory __getLogicalStyle(Environment env, Memory... args) {
        if (component.getContent().getLogicalStyle() == null)
            return Memory.NULL;

        return new ObjectMemory(new WrapStyle(env, component.getContent().getLogicalStyle()));
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

    @Signature({
            @Arg("name"),
            @Arg(value = "parent", nativeType = WrapStyle.class, optional = @Optional("null"))
    })
    public Memory addStyle(Environment env, Memory... args) {
        Style style = component.getContent().addStyle(
                args[0].toString(), args[1].isNull() ? null : args[1].toObject(WrapStyle.class).getStyle()
        );
        return new ObjectMemory(new WrapStyle(env, style));
    }

    @Signature(@Arg("name"))
    public Memory getStyle(Environment env, Memory... args) {
        Style style = component.getContent().getStyle(args[0].toString());
        if (style == null)
            return Memory.NULL;
        return new ObjectMemory(new WrapStyle(env, style));
    }

    @Signature({
            @Arg("text"),
            @Arg(value = "style", nativeType = WrapStyle.class)
    })
    public Memory appendText(Environment env, Memory... args) throws BadLocationException {
        StyledDocument doc = component.getContent().getStyledDocument();

        doc.insertString(doc.getLength(),
                args[0].toString(), args[1].toObject(WrapStyle.class).getStyle()
        );
        return Memory.NULL;
    }
}
