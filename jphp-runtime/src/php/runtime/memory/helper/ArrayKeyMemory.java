package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;

public class ArrayKeyMemory extends ReferenceMemory {
    private ArrayMemory array;

    public ArrayKeyMemory(ArrayMemory array, Memory key) {
        super(key);
        this.array = array;
    }

    @Override
    public Memory assign(Memory memory) {
        array.renameKey(getValue(), memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(long memory) {
        array.renameKey(getValue(), LongMemory.valueOf(memory));
        return super.assign(memory);
    }

    @Override
    public Memory assign(String memory) {
        array.renameKey(getValue(), new StringMemory(memory));
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
