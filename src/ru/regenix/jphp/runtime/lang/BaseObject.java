package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

@Reflection.Ignore
abstract public class BaseObject implements IObject {
    public final ArrayMemory __dynamicProperties__;
    public final ClassEntity __class__;

    protected BaseObject(ClassEntity entity) {
        this.__class__ = entity;
        this.__dynamicProperties__ = null;
    }

    protected BaseObject(ArrayMemory __dynamicProperties__, Environment __env__, ClassEntity __class__) {
        this.__dynamicProperties__ = __dynamicProperties__;
        this.__class__ = __class__;
    }

    public BaseObject(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory(true);
    }

    @Override
    final public int getPointer(){
        return super.hashCode();
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        return __dynamicProperties__;
    }
}
