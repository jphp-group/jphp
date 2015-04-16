package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.TrueMemory;
import php.runtime.memory.support.MemoryOperation;

public class BooleanMemoryOperation extends MemoryOperation<Boolean> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Boolean.class, Boolean.TYPE };
    }

    @Override
    public Boolean convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toBoolean();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Boolean arg) throws Throwable {
        return TrueMemory.valueOf(arg);
    }
}
