package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;


@Reflection.Name("Exception")
public class BaseException implements IObject {
    public final ArrayMemory __dynamicProperties__;
    public final ClassEntity __class__;

    public BaseException(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory();
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        return __dynamicProperties__;
    }

    @Override
    final public int getPointer() {
        return super.hashCode();
    }
}
