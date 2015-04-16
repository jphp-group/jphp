package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.support.MemoryOperation;

public class ObjectMemoryOperation extends MemoryOperation<Object> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Object.class };
    }

    @Override
    public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return Memory.unwrap(env, arg);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
        return Memory.wrap(env, arg);
    }
}
