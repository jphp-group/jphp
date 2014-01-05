package ru.regenix.jphp.runtime.memory.helper;

import ru.regenix.jphp.runtime.memory.NullMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class UndefinedMemory extends NullMemory {

    public static final UndefinedMemory INSTANCE = new UndefinedMemory();

    @Override
    public Memory toImmutable() {
        return NULL;
    }

    @Override
    public boolean isUndefined() {
        return true;
    }
}
