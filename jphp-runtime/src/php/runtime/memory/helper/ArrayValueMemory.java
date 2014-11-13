package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;

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
    public Memory refOfPush(TraceInfo trace) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfPush();
        return super.refOfPush(trace);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, Memory index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(index);
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, long index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(trace, index);
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, double index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(trace, index);
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, String index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(trace, index);
        return super.refOfIndex(trace, index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, boolean index) {
        ArrayMemory dup = array.checkCopied();
        if (dup != null) return dup.get(key).refOfIndex(trace, index);
        return super.refOfIndex(trace, index);
    }

    @Override
    public ReferenceMemory getReference() {
        return this;
    }
}
