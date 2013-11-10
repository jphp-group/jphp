package ru.regenix.jphp.compiler.common.misc;

import org.objectweb.asm.Label;
import ru.regenix.jphp.runtime.memory.Memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
* User: Dim-S (dz@dim-s.net)
* Date: 10.11.13
*/
public class LocalVariable {
    public final String name;
    public final int index;
    public final Label label;
    private Class clazz;

    private boolean isImmutable;
    private boolean isReference;
    private int level;
    private List<Memory> values;

    public LocalVariable(String name, int index, Label label, Class clazz){
        this.name = name;
        this.index = index;
        this.label = label;
        this.clazz = clazz;
        this.level = 0;
        this.isImmutable = true;
        this.values = new ArrayList<Memory>();
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
        return level + 1;
    }

    public int getLevel(){
        return level;
    }

    public void setValue(Memory value){
        if (isReference)
            value = null;

        if (values.size() < level){
            values.add(value);
        } else {
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
}
