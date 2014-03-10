package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.WrapColor;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.ext.swing.support.RootTextElement;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.awt.print.PrinterException;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITextElement")
abstract public class UITextElement extends UIContainer {

    protected UITextElement(Environment env) {
        super(env);
    }

    public UITextElement(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    abstract public RootTextElement getTextComponent();

    @Signature
    protected Memory __getText(Environment env, Memory... args){
        return new StringMemory(getTextComponent().getText());
    }

    @Signature(@Arg("value"))
    protected Memory __setText(Environment env, Memory... args){
        getTextComponent().setText(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getCaretPos(Environment env, Memory... args){
        return LongMemory.valueOf(getTextComponent().getCaretPosition());
    }

    @Signature(@Arg("value"))
    protected Memory __setCaretPos(Environment env, Memory... args){
        getTextComponent().setCaretPosition(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getCaretColor(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, getTextComponent().getCaretColor()));
    }

    @Signature(@Arg("value"))
    protected Memory __setCaretColor(Environment env, Memory... args){
        getTextComponent().setCaretColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setReadOnly(Environment env, Memory... args){
        getTextComponent().setEditable(!args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getReadOnly(Environment env, Memory... args){
        return TrueMemory.valueOf(!getTextComponent().isEditable());
    }

    @Signature
    protected Memory __getSelectedStart(Environment env, Memory... args){
        return LongMemory.valueOf(getTextComponent().getSelectionStart());
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectedStart(Environment env, Memory... args){
        getTextComponent().setSelectionStart(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedEnd(Environment env, Memory... args){
        return LongMemory.valueOf(getTextComponent().getSelectionEnd());
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectedEnd(Environment env, Memory... args){
        getTextComponent().setSelectionStart(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedText(Environment env, Memory... args){
        return StringMemory.valueOf(getTextComponent().getSelectedText());
    }

    @Signature
    protected Memory __getSelectionColor(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, getTextComponent().getSelectionColor()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionColor(Environment env, Memory... args){
        getTextComponent().setSelectionColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectionTextColor(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, getTextComponent().getSelectedTextColor()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionTextColor(Environment env, Memory... args){
        getTextComponent().setSelectedTextColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getDisabledTextColor(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, getTextComponent().getDisabledTextColor()));
    }

    @Signature(@Arg("value"))
    protected Memory __setDisabledTextColor(Environment env, Memory... args){
        getTextComponent().setDisabledTextColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    public Memory copy(Environment env, Memory... args){
        getTextComponent().copy();
        return Memory.NULL;
    }

    @Signature
    public Memory cut(Environment env, Memory... args){
        getTextComponent().cut();
        return Memory.NULL;
    }

    @Signature
    public Memory paste(Environment env, Memory... args){
        getTextComponent().paste();
        return Memory.NULL;
    }

    @Signature({@Arg("start"), @Arg("end")})
    public Memory select(Environment env, Memory... args){
        getTextComponent().select(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory selectAll(Environment env, Memory... args){
        getTextComponent().selectAll();
        return Memory.NULL;
    }

    @Signature(@Arg("content"))
    public Memory replaceSelection(Environment env, Memory... args){
        getTextComponent().replaceSelection(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    public Memory printDialog(Environment env, Memory... args) throws PrinterException {
        return getTextComponent().print() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "margin", type = HintType.ARRAY))
    public Memory __setMargin(Environment env, Memory... args) throws PrinterException {
        ArrayMemory arr = args[0].toValue(ArrayMemory.class);
        if (arr.size() < 4)
            throw new IllegalArgumentException("Argument 1 must be an array with size > 3");

        Memory[] a = arr.values();
        getTextComponent().setMargin(new Insets(
                a[0].toInteger(), a[1].toInteger(),
                a[2].toInteger(), a[3].toInteger()
        ));
        return Memory.NULL;
    }

    @Signature
    public Memory __getMargin(Environment env, Memory... args) {
        Insets insets = getTextComponent().getMargin();
        return new ArrayMemory(insets.top, insets.left, insets.bottom, insets.right);
    }
}
