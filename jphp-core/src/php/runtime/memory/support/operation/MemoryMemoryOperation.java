package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.support.MemoryOperation;

public class MemoryMemoryOperation extends MemoryOperation<Memory> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Memory.class };
    }

    @Override
    public Memory convert(Environment env, TraceInfo trace, Memory arg) {
        return arg;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Memory arg) {
        return arg;
    }
}
