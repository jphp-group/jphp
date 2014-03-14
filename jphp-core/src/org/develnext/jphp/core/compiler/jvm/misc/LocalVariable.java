package org.develnext.jphp.core.compiler.jvm.misc;

import org.objectweb.asm.tree.LabelNode;
import php.runtime.Memory;

import java.util.ArrayList;
import java.util.List;

public class LocalVariable {
    public final String name;
    public final int index;
    public final LabelNode label;
    private LabelNode endLabel;
    private Class clazz;

    private boolean isImmutable;
    private boolean isReference;
    private int level;
    private List<Memory> values;
    private List<StackFrame> frames;

    public LocalVariable(String name, int index, LabelNode label, Class clazz){
        this.name = name;
        this.index = index;
        this.label = label;
        this.clazz = clazz;
        this.level = 0;
        this.isImmutable = true;
        this.values = new ArrayList<Memory>();
        this.frames = new ArrayList<StackFrame>();
    }

    public void addStackFrame(StackFrame frame){
        this.frames.add(frame);
    }

    public List<StackFrame> getStackFrames() {
        return frames;
    }

    public Class getClazz() {
        return clazz;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public void pushLevel(){
        level += 1;
    }

    public int popLevel(){
        if (values.size() >= level + 1) // if we set var inside, delete value of it
            values.remove(values.size() - 1);

        level -= 1;
        setValue(null); // fix for break value
        return level + 1;
    }

    public int getLevel(){
        return level;
    }

    public void setValue(Memory value){
        /*if (isReference)
            value = null;*/

        if (values.size() < level){
            values.add(value);
        } else if (values.size() > 0) {
            values.set(values.size() - 1, value);
        }
    }

    public Memory getValue(){
        return values.isEmpty() ? null : values.get(values.size() - 1);
    }

    public boolean isImmutable() {
        return isImmutable;
    }

    public void setImmutable(boolean immutable) {
        isImmutable = immutable;
    }

    public LabelNode getEndLabel() {
        return endLabel;
    }

    public void setEndLabel(LabelNode endLabel) {
        this.endLabel = endLabel;
    }
}
