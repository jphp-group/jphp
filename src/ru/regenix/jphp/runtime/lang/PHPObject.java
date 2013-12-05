package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

abstract public class PHPObject {
    public final ArrayMemory __dynamicProperties__;
    public final ClassEntity __class__;
    public final Environment __env__;

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
