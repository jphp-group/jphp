package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIWindow;
import php.runtime.ext.swing.support.JDialogX;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIDialog")
public class UIDialog extends UIWindow {
    public final static int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public final static int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public final static int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
    public final static int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public final static int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;

    public final static int DEFAULT_OPTION = JOptionPane.DEFAULT_OPTION;
    public final static int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    public final static int OK_CANCEL_OPTION = JOptionPane.OK_CANCEL_OPTION;
    public final static int YES_NO_CANCEL_OPTION = JOptionPane.YES_NO_CANCEL_OPTION;
    public final static int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;
    public final static int OK_OPTION = JOptionPane.OK_OPTION;
    public final static int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;
    public final static int YES_OPTION = JOptionPane.YES_OPTION;
    public final static int NO_OPTION = JOptionPane.NO_OPTION;

    protected JDialogX component;

    public UIDialog(Environment env, JDialogX component) {
        super(env);
        this.component = component;
    }

    public UIDialog(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Window getWindow() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JDialogX)component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        if (args[0].isNull())
            component = new JDialogX();
        else {
            UIWindow window = args[0].toObject(UIWindow.class);
            if (window.getWindow() instanceof Frame)
                component = new JDialogX((Frame)window.getWindow());
            else if (window.getWindow() instanceof Dialog)
                component = new JDialogX((Dialog)window.getWindow());
            else
                throw new IllegalArgumentException("Unregistered window type");
        }
    }

    @Signature(
            @Arg(value = "parent", typeClass = SwingExtension.NAMESPACE + "UIWindow", optional = @Optional("NULL"))
    )
    public Memory __construct(Environment env, Memory... args) {
        return super.__construct(env, args);
    }

    @Signature
    protected Memory __getModal(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isModal());
    }

    @Signature(@Arg("value"))
    protected Memory __setModal(Environment env, Memory... args) {
        component.setModal(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getModalType(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getModalityType().name().toLowerCase());
    }

    @Signature(@Arg("value"))
    protected Memory __setModalType(Environment env, Memory... args) {
        component.setModalityType(Dialog.ModalityType.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }

    @Signature({
            @Arg("message"),
            @Arg(value = "title"),
            @Arg(value = "type", optional = @Optional(value = "1", type = HintType.INT))
    })
    public static Memory message(Environment env, Memory... args){
        JOptionPane.showMessageDialog(null, args[0].toString(), args[1].toString(), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature({
            @Arg("message"),
            @Arg(value = "title"),
            @Arg(value = "optionType", optional = @Optional(value = "3", type = HintType.INT)),
            @Arg(value = "type", optional = @Optional(value = "1", type = HintType.INT))
    })
    public static Memory confirm(Environment env, Memory... args){
        return LongMemory.valueOf(
                JOptionPane.showConfirmDialog(
                        null, args[0].toString(), args[1].toString(), args[2].toInteger(), args[3].toInteger()
                )
        );
    }
}
