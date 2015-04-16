package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.support.MemoryOperation;

public class VoidMemoryOperation extends MemoryOperation {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { void.class, Void.class };
    }

    @Override
    public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return null;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
        return Memory.NULL;
    }
}
