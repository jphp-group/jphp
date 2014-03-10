package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UISlider")
public class UISlider extends UIContainer {
    protected JSlider component;

    public UISlider(Environment env, JSlider component) {
        super(env);
        this.component = component;
    }

    public UISlider(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JSlider) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JSlider();
    }

    @Signature
    protected Memory __getMin(Environment env, Memory... args) {
        return LongMemory.valueOf(this.component.getMinimum());
    }

    @Signature(@Arg("value"))
    protected Memory __setMin(Environment env, Memory... args) {
        this.component.setMinimum(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMax(Environment env, Memory... args) {
        return LongMemory.valueOf(this.component.getMaximum());
    }

    @Signature(@Arg("value"))
    protected Memory __setMax(Environment env, Memory... args) {
        this.component.setMaximum(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getValue(Environment env, Memory... args) {
        return LongMemory.valueOf(this.component.getValue());
    }

    @Signature(@Arg("value"))
    protected Memory __setValue(Environment env, Memory... args) {
        this.component.setValue(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getExtent(Environment env, Memory... args) {
        return LongMemory.valueOf(this.component.getExtent());
    }

    @Signature(@Arg("value"))
    protected Memory __setExtent(Environment env, Memory... args) {
        this.component.setExtent(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMajorTickSpacing(Environment env, Memory... args) {
        return LongMemory.valueOf(this.component.getMajorTickSpacing());
    }

    @Signature(@Arg("value"))
    protected Memory __setMajorTickSpacing(Environment env, Memory... args) {
        this.component.setMajorTickSpacing(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMinorTickSpacing(Environment env, Memory... args) {
        return LongMemory.valueOf(this.component.getMinorTickSpacing());
    }

    @Signature(@Arg("value"))
    protected Memory __setMinorTickSpacing(Environment env, Memory... args) {
        this.component.setMinorTickSpacing(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getValueIsAdjusting(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.component.getValueIsAdjusting());
    }

    @Signature(@Arg("value"))
    protected Memory __setValueIsAdjusting(Environment env, Memory... args) {
        this.component.setValueIsAdjusting(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getInverted(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.component.getInverted());
    }

    @Signature(@Arg("value"))
    protected Memory __setInverted(Environment env, Memory... args) {
        this.component.setInverted(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getPaintLabels(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.component.getPaintLabels());
    }

    @Signature(@Arg("value"))
    protected Memory __setPaintLabels(Environment env, Memory... args) {
        this.component.setPaintLabels(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getPaintTicks(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.component.getPaintTicks());
    }

    @Signature(@Arg("value"))
    protected Memory __setPaintTicks(Environment env, Memory... args) {
        this.component.setPaintTicks(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getPaintTrack(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.component.getPaintTrack());
    }

    @Signature(@Arg("value"))
    protected Memory __setPaintTrack(Environment env, Memory... args) {
        this.component.setPaintTrack(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSnapToTicks(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.component.getSnapToTicks());
    }

    @Signature(@Arg("value"))
    protected Memory __setSnapToTicks(Environment env, Memory... args) {
        this.component.setSnapToTicks(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getLabelTable(Environment env, Memory... args) {
        Dictionary dictionary = component.getLabelTable();
        ArrayMemory result = new ArrayMemory();

        for(Object key : Collections.list(dictionary.keys())) {
            JLabel label = (JLabel)dictionary.get(key);
            result.refOfIndex((Integer)key).assign(label.getText());
        }

        return result.toConstant();
    }

    @Signature(@Arg(value = "value", type = HintType.ARRAY))
    protected Memory __setLabelTable(Environment env, Memory... args) {
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();

        ForeachIterator iterator = args[0].getNewIterator(env, false, false);
        while (iterator.next()) {
            int index = iterator.getMemoryKey().toInteger();
            table.put(index, new JLabel(iterator.getValue().toString()));
        }

        component.setLabelTable(table);
        return Memory.NULL;
    }
}
