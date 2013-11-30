package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

abstract public class PHPObject {
    public final ArrayMemory __dynamicProperties__;
    public final ClassEntity __class__;

    public PHPObject(ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory();
    }

    final public int getPointer(){
        return super.hashCode();
    }
}
