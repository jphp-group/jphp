package ru.regenix.jphp.runtime.memory.helper;

import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class ArrayValueMemory extends ReferenceMemory {
    private Memory key;
    private ArrayMemory array;

    public ArrayValueMemory(Memory key, ArrayMemory array, ReferenceMemory value) {
        super(value);
        this.key   = key;
        this.array = array;
    }

    @Override
    public Memory assign(Memory memory) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).assign(memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(long memory) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).assign(memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(String memory) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).assign(memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(boolean memory) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).assign(memory);
        return super.assign(memory);
    }

    @Override
    public Memory assign(double memory) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).assign(memory);
        return super.assign(memory);
    }

    @Override
    public Memory assignRef(Memory reference) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).assignRef(reference);
        return super.assignRef(reference);
    }

    @Override
    public Memory refOfPush() {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfPush();
        return super.refOfPush();
    }

    @Override
    public Memory refOfIndex(Memory index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(index);
        return super.refOfIndex(index);
    }

    @Override
    public Memory refOfIndex(long index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(index);
        return super.refOfIndex(index);
    }

    @Override
    public Memory refOfIndex(double index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(index);
        return super.refOfIndex(index);
    }

    @Override
    public Memory refOfIndex(String index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(index);
        return super.refOfIndex(index);
    }

    @Override
    public Memory refOfIndex(boolean index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(index);
        return super.refOfIndex(index);
    }

    @Override
    public ReferenceMemory getReference() {
        return this;
    }
}
