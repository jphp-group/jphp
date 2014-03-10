package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.WrapColor;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.ext.swing.support.JListbox;
import php.runtime.ext.swing.support.JScrollPanel;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIListbox")
public class UIListbox extends UIContainer {
    protected JListbox component;

    public UIListbox(Environment env, JListbox component) {
        super(env);
        this.component = component;
    }

    public UIListbox(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JListbox)component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JListbox();
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
    protected Memory __getVisibleRowCount(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getVisibleRowCount());
    }

    @Signature(@Arg("value"))
    protected Memory __setVisibleRowCount(Environment env, Memory... args) {
        component.setVisibleRowCount(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMaxSelectionIndex(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMaxSelectionIndex());
    }

    @Signature
    protected Memory __getMinSelectionIndex(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getMinSelectionIndex());
    }

    @Signature
    protected Memory __getMultiple(Environment env, Memory... args) {
        return component.isMultiple() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setMultiple(Environment env, Memory... args) {
        component.setMultiple(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("item"))
    public Memory addItem(Environment env, Memory... args) {
        DefaultListModel listModel = (DefaultListModel)component.getModel();
        listModel.addElement(args[0].toString());
        return Memory.NULL;
    }

    @Signature({@Arg("index"), @Arg("item")})
    public Memory insertItem(Environment env, Memory... args) {
        DefaultListModel listModel = (DefaultListModel)component.getModel();
        listModel.insertElementAt(args[1].toString(), args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("index")})
    public Memory getItem(Environment env, Memory... args) {
        DefaultListModel listModel = (DefaultListModel)component.getModel();
        return new StringMemory((String)listModel.getElementAt(args[0].toInteger()));
    }

    @Signature({@Arg("index")})
    public Memory removeItem(Environment env, Memory... args) {
        DefaultListModel listModel = (DefaultListModel)component.getModel();
        listModel.removeElementAt(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory removeAllItems(Environment env, Memory... args) {
        DefaultListModel listModel = (DefaultListModel)component.getModel();
        listModel.removeAllElements();
        return Memory.NULL;
    }

    @Signature(@Arg(value = "items", type = HintType.ARRAY))
    public Memory setItems(Environment env, Memory... args) {
        DefaultListModel listModel = (DefaultListModel)component.getModel();
        listModel.clear();

        ArrayMemory arr = args[0].toValue(ArrayMemory.class);

        ForeachIterator iterator = arr.foreachIterator(false, false);
        int i = 0;
        while (iterator.next()) {
            listModel.addElement(iterator.getValue().toString());
            i++;
        }

        return Memory.NULL;
    }

    @Signature(@Arg(value = "indexes", type = HintType.ARRAY))
    protected Memory __setSelectedIndexes(Environment env, Memory... args) {
        ArrayMemory arr = args[0].toValue(ArrayMemory.class);
        int[] items = new int[arr.size()];

        ForeachIterator iterator = arr.foreachIterator(false, false);
        int i = 0;
        while (iterator.next()) {
            items[i] = iterator.getValue().toInteger();
            i++;
        }

        component.setSelectedIndices(items);
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedIndexes(Environment env, Memory... args) {
        ArrayMemory arr = new ArrayMemory();
        for(int index : component.getSelectedIndices()){
            arr.add(LongMemory.valueOf(index));
        }
        return arr.toConstant();
    }

    @Signature
    protected Memory __getSelectionBackground(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getSelectionBackground()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionBackground(Environment env, Memory... args) {
        component.setSelectionBackground(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectionForeground(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, component.getSelectionForeground()));
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectionForeground(Environment env, Memory... args) {
        component.setSelectionForeground(WrapColor.of(args[0]));
        return Memory.NULL;
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
