package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.invoke.Invoker;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class InvokerMemoryOperation extends MemoryOperation<Invoker> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Invoker.class };
    }

    @Override
    public Invoker convert(Environment env, TraceInfo trace, Memory arg) {
        return arg.isNull() ? null : Invoker.valueOf(env, trace, arg);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Invoker arg) {
        throw new CriticalException("Unsupported operation");
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.CALLABLE);
    }
}
