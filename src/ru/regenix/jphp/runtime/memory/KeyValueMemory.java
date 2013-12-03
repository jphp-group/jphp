package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.memory.support.Memory;

public class KeyValueMemory extends ReferenceMemory {
    public final Memory key;

    public KeyValueMemory(Memory key, Memory value) {
        super(value);
        this.key = key;
    }
}
