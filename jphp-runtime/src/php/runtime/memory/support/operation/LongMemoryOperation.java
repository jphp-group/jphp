package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.support.MemoryOperation;

public class LongMemoryOperation extends MemoryOperation<Long> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Long.class, Long.TYPE };
    }

    @Override
    public Long convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toLong();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Long arg) throws Throwable {
        return LongMemory.valueOf(arg);
    }
}
