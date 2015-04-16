package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.support.MemoryOperation;

public class ShortMemoryOperation extends MemoryOperation<Short> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Short.class, Short.TYPE };
    }

    @Override
    public Short convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return (short)arg.toInteger();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Short arg) throws Throwable {
        return LongMemory.valueOf(arg);
    }
}
