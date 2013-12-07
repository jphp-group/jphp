package ru.regenix.jphp.runtime.memory.helper;

import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class ArrayKeyMemory extends ReferenceMemory {
    private ArrayMemory array;

    public ArrayKeyMemory(ArrayMemory array, Memory key) {
        super(key);
        this.array = array;
    }

    @Override
    public Memory assign(Memory memory) {
        array.renameKey(value, memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(long memory) {
        array.renameKey(value, LongMemory.valueOf(memory));
        return super.assign(memory);
    }

    @Override
    public Memory assign(String memory) {
        array.renameKey(value, new StringMemory(memory));
        return super.assign(memory);
    }

    @Override
    public Memory assign(boolean memory) {
        return assign(memory ? 1 : 0);
    }

    @Override
    public Memory assign(double memory) {
        return assign((long)memory);
    }
}
