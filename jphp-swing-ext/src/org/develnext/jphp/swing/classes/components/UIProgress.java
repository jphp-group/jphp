package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIProgress")
public class UIProgress extends UIElement {
    protected JProgressBar component;

    public UIProgress(Environment env, JProgressBar component) {
        super(env);
        this.component = component;
    }

    public UIProgress(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JProgressBar) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JProgressBar();
    }

    @Signature
    protected Memory __getMax(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMaximum());
    }

    @Signature
    protected Memory __getMin(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMinimum());
    }

    @Signature
    protected Memory __getValue(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getValue());
    }

    @Signature(@Arg("value"))
    protected Memory __setMax(Environment env, Memory... args) {
        component.setMaximum(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setMin(Environment env, Memory... args) {
        component.setMinimum(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setValue(Environment env, Memory... args) {
        component.setValue(args[0].toInteger());
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
    protected Memory __getIndeterminate(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isIndeterminate());
    }

    @Signature(@Arg("value"))
    protected Memory __setIndeterminate(Environment env, Memory... args) {
        component.setIndeterminate(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getTextPainted(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isStringPainted());
    }

    @Signature(@Arg("value"))
    protected Memory __setTextPainted(Environment env, Memory... args) {
        component.setStringPainted(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getText(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getString());
    }

    @Signature(@Arg("value"))
    protected Memory __setText(Environment env, Memory... args) {
        component.setString(args[0].toString());
        return Memory.NULL;
    }

}
