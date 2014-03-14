package org.develnext.jphp.swing.classes.components.support;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.ComponentProperties;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapImage;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIAbstractButton")
abstract public class UIAbstractButton extends UIContainer {

    protected UIAbstractButton(Environment env) {
        super(env);
    }

    public UIAbstractButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    abstract protected AbstractButton getAbstractButton();

    @Override
    public Container getContainer() {
        return getAbstractButton();
    }

    @Signature
    protected Memory __getText(Environment env, Memory... args){
        return new StringMemory(getAbstractButton().getText());
    }

    @Signature(@Arg("value"))
    protected Memory __setText(Environment env, Memory... args){
        getAbstractButton().setText(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVerAlignment(Environment env, Memory... args){
        return SwingExtension.fromDirection(getAbstractButton().getVerticalAlignment());
    }

    @Signature(@Arg("value"))
    protected Memory __setVerAlignment(Environment env, Memory... args){
        getAbstractButton().setVerticalAlignment(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getHorAlignment(Environment env, Memory... args){
        return SwingExtension.fromDirection(getAbstractButton().getHorizontalAlignment());
    }

    @Signature(@Arg("value"))
    protected Memory __setHorAlignment(Environment env, Memory... args){
        getAbstractButton().setHorizontalAlignment(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getIconTextGap(Environment env, Memory... args){
        return new LongMemory(getAbstractButton().getIconTextGap());
    }

    @Signature(@Arg("value"))
    protected Memory __setIconTextGap(Environment env, Memory... args){
        getAbstractButton().setIconTextGap(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVerPosition(Environment env, Memory... args){
        return SwingExtension.fromDirection(getAbstractButton().getVerticalTextPosition());
    }

    @Signature(@Arg("value"))
    protected Memory __setVerPosition(Environment env, Memory... args){
        getAbstractButton().setVerticalTextPosition(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getHorPosition(Environment env, Memory... args){
        return SwingExtension.fromDirection(getAbstractButton().getHorizontalTextPosition());
    }

    @Signature(@Arg("value"))
    protected Memory __setHorPosition(Environment env, Memory... args){
        getAbstractButton().setHorizontalTextPosition(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setDisabledIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setDisabledIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setDisabledIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setSelectedIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setSelectedIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setSelectedIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setPressedIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setPressedIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setPressedIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setRolloverIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setRolloverIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setRolloverIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setDisabledSelectedIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setDisabledSelectedIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setDisabledSelectedIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature(@Arg("icon"))
    public Memory setRolloverSelectedIcon(Environment env, Memory... args){
        if (args[0].instanceOf(WrapImage.class)){
            getAbstractButton().setRolloverSelectedIcon(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));
        } else {
            getAbstractButton().setRolloverSelectedIcon(new ImageIcon(args[0].toString()));
        }
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelected(Environment env, Memory... args) {
        return getAbstractButton().isSelected() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setSelected(Environment env, Memory... args) {
        getAbstractButton().setSelected(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getBorderPainted(Environment env, Memory... args) {
        return getAbstractButton().isBorderPainted() ? Memory.TRUE : Memory.FALSE;
    }


    @Signature(@Arg("value"))
    protected Memory __setBorderPainted(Environment env, Memory... args) {
        getAbstractButton().setBorderPainted(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getFocusPainted(Environment env, Memory... args) {
        return getAbstractButton().isFocusPainted() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setFocusPainted(Environment env, Memory... args) {
        getAbstractButton().setFocusPainted(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getRolloverEnabled(Environment env, Memory... args) {
        return getAbstractButton().isRolloverEnabled() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setRolloverEnabled(Environment env, Memory... args) {
        getAbstractButton().setRolloverEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getContentAreaFilled(Environment env, Memory... args) {
        return getAbstractButton().isContentAreaFilled() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setContentAreaFilled(Environment env, Memory... args) {
        getAbstractButton().setContentAreaFilled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "pressTime", optional = @Optional(value = "68", type = HintType.INT)))
    public Memory doClick(Environment env, Memory... args) {
        getAbstractButton().doClick(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getButtonGroup(Environment env, Memory... args) {
        ComponentProperties properties = SwingExtension.getProperties(getComponent());
        String val = properties.getData("buttonGroup", String.class);
        if (val == null)
            return Memory.CONST_EMPTY_STRING;

        return new StringMemory(val);
    }

    @Signature(@Arg("value"))
    protected Memory __setButtonGroup(Environment env, Memory... args) {
        ComponentProperties properties = SwingExtension.getProperties(getComponent());
        properties.setData("buttonGroup", args[0].toString());

        ButtonGroup group = SwingExtension.getOrCreateButtonGroup(args[0].toString());
        group.add(getAbstractButton());

        return Memory.NULL;
    }

    @Signature(@Arg("buttonGroup"))
    public static Memory getButtons(Environment env, Memory... args) {
        ButtonGroup group = SwingExtension.getOrCreateButtonGroup(args[0].toString());
        if (group == null)
            return new ArrayMemory().toConstant();

        ArrayMemory result = new ArrayMemory();
        for(AbstractButton btn : Collections.list(group.getElements())) {
            result.add(new ObjectMemory(UIElement.of(env, btn)));
        }

        return result.toConstant();
    }

    @Signature(@Arg("buttonGroup"))
    public static Memory getSelectedButtons(Environment env, Memory... args) {
        ButtonGroup group = SwingExtension.getOrCreateButtonGroup(args[0].toString());
        if (group == null)
            return new ArrayMemory().toConstant();

        Object[] selected = group.getSelection().getSelectedObjects();
        ArrayMemory result = new ArrayMemory();

        if (selected != null)
        for(Object btn : selected) {
            result.add(new ObjectMemory(UIElement.of(env, (Component)btn)));
        }

        return result.toConstant();
    }
}
