package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UICombobox")
public class UICombobox extends UIContainer {
    protected JComboBox<String> component;

    public UICombobox(Environment env, JComboBox component) {
        super(env);
        this.component = component;
    }

    public UICombobox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JComboBox)component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JComboBox();
    }

    @Signature
    protected Memory __getReadOnly(Environment env, Memory... args) {
        return component.isEditable() ? Memory.FALSE : Memory.TRUE;
    }

    @Signature(@Arg("value"))
    protected Memory __setReadOnly(Environment env, Memory... args) {
        component.setEditable(!args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getPopupVisible(Environment env, Memory... args) {
        return component.isPopupVisible() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setPopupVisible(Environment env, Memory... args) {
        component.setPopupVisible(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getLightweightPopup(Environment env, Memory... args) {
        return component.isLightWeightPopupEnabled() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setLightweightPopup(Environment env, Memory... args) {
        component.setLightWeightPopupEnabled(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getItemCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getItemCount());
    }

    @Signature
    protected Memory __getSelectedIndex(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getSelectedIndex());
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectedIndex(Environment env, Memory... args) {
        component.setSelectedIndex(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMaxRowCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMaximumRowCount());
    }

    @Signature(@Arg("value"))
    protected Memory __setMaxRowCount(Environment env, Memory... args) {
        component.setMaximumRowCount(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    public Memory addItem(Environment env, Memory... args) {
        component.addItem(args[0].toString());
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    public Memory getItem(Environment env, Memory... args) {
        String v = component.getItemAt(args[0].toInteger());
        return v == null ? Memory.NULL : new StringMemory(v);
    }

    @Signature({@Arg("index"), @Arg("value")})
    public Memory insertItem(Environment env, Memory... args) {
        component.insertItemAt(args[1].toString(), args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory removeItem(Environment env, Memory... args) {
        component.removeItemAt(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory removeAllItems(Environment env, Memory... args) {
        component.removeAllItems();
        return Memory.NULL;
    }

    @Signature(@Arg(value = "items", type = HintType.ARRAY))
    public Memory setItems(Environment env, Memory... args) {
        component.removeAllItems();
        ArrayMemory arr = args[0].toValue(ArrayMemory.class);
        String[] items = new String[arr.size()];

        ForeachIterator iterator = arr.foreachIterator(false, false);
        int i = 0;
        while (iterator.next()) {
            items[i] = iterator.getValue().toString();
            i++;
        }

        component.setModel(new DefaultComboBoxModel<String>(items));
        return Memory.NULL;
    }
}
