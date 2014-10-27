package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.invoke.Invoker;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.support.MemoryOperation;

public class InvokerMemoryOperation extends MemoryOperation<Invoker> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Invoker.class };
    }

    @Override
    public Invoker convert(Environment env, TraceInfo trace, Memory arg) {
        return Invoker.valueOf(env, trace, arg);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Invoker arg) {
        throw new CriticalException("Unsupported operation");
    }
}
