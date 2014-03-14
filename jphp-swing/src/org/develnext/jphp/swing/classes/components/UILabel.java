package org.develnext.jphp.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapImage;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UILabel")
public class UILabel extends UIContainer {
    protected JLabel component;

    public UILabel(Environment env, JLabel component) {
        super(env);
        this.component = component;
    }

    public UILabel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void onInit(Environment env, Memory... args){
        component = new JLabel();
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JLabel)component;
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Signature
    protected Memory __getText(Environment env, Memory... args){
        return new StringMemory(component.getText());
    }

    @Signature(@Arg("value"))
    protected Memory __setText(Environment env, Memory... args){
        component.setText(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getIconTextGap(Environment env, Memory... args){
        return new LongMemory(component.getIconTextGap());
    }

    @Signature(@Arg("value"))
    protected Memory __setIconTextGap(Environment env, Memory... args){
        component.setIconTextGap(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVerAlignment(Environment env, Memory... args){
        return SwingExtension.fromDirection(component.getVerticalAlignment());
    }

    @Signature(@Arg("value"))
    protected Memory __setVerAlignment(Environment env, Memory... args){
        component.setVerticalAlignment(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getHorAlignment(Environment env, Memory... args){
        return SwingExtension.fromDirection(component.getHorizontalAlignment());
    }

    @Signature(@Arg("value"))
    protected Memory __setHorAlignment(Environment env, Memory... args){
        component.setHorizontalAlignment(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVerPosition(Environment env, Memory... args){
        return SwingExtension.fromDirection(component.getVerticalTextPosition());
    }

    @Signature(@Arg("value"))
    protected Memory __setVerPosition(Environment env, Memory... args){
        component.setVerticalTextPosition(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getHorPosition(Environment env, Memory... args){
        return SwingExtension.fromDirection(component.getHorizontalTextPosition());
    }

    @Signature(@Arg("value"))
    protected Memory __setHorPosition(Environment env, Memory... args){
        component.setHorizontalTextPosition(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            component.setIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            component.setIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setDisabledIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            component.setDisabledIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            component.setIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(
            @Arg(value = "component", typeClass = SwingExtension.NAMESPACE + "UIElement", optional = @Optional("NULL"))
    )
    public Memory setLabelFor(Environment env, Memory... args){
        if (args[0].isNull())
            component.setLabelFor(null);
        else {
            UIElement element = args[0].toObject(UIElement.class);
            component.setLabelFor(element.getComponent());
        }
        return Memory.NULL;
    }
}
