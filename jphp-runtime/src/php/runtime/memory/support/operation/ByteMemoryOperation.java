package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.support.MemoryOperation;

public class ByteMemoryOperation extends MemoryOperation<Byte> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Byte.class, Byte.TYPE };
    }

    @Override
    public Byte convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return (byte)arg.toInteger();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Byte arg) throws Throwable {
        return LongMemory.valueOf(arg);
    }
}
