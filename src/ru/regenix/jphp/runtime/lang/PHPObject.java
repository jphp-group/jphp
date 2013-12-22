package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

abstract public class PHPObject {
    public final ArrayMemory __dynamicProperties__;
    public final Environment __env__;
    public final ClassEntity __class__;

    protected PHPObject(ClassEntity entity) {
        this.__class__ = entity;
        this.__dynamicProperties__ = null;
        this.__env__ = null;
    }

    protected PHPObject(ArrayMemory __dynamicProperties__, Environment __env__, ClassEntity __class__) {
        this.__dynamicProperties__ = __dynamicProperties__;
        this.__env__ = __env__;
        this.__class__ = __class__;
    }

    public PHPObject(Environment env, ClassEntity clazz) {
        this.__env__   = env;
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory();
    }

    final public int getPointer(){
        return super.hashCode();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
