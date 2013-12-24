package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

public interface IObject {
    ClassEntity getReflection();
    ArrayMemory getProperties();
    int getPointer();
}
