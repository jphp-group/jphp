package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.support.MemoryOperation;

public class IntegerMemoryOperation extends MemoryOperation<Integer> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Integer.class, Integer.TYPE };
    }

    @Override
    public Integer convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toInteger();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Integer arg) throws Throwable {
        return LongMemory.valueOf(arg);
    }
}
