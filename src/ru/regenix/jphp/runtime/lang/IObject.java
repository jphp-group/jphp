package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

@Reflection.Ignore
public interface IObject {
    ClassEntity getReflection();
    ArrayMemory getProperties();
    int getPointer();
}
