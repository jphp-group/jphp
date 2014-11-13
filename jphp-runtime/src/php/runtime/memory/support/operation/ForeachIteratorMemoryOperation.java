package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class ForeachIteratorMemoryOperation extends MemoryOperation<ForeachIterator> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { ForeachIterator.class };
    }

    @Override
    public ForeachIterator convert(Environment env, TraceInfo trace, Memory arg) {
        return arg.isNull() ? null : arg.getNewIterator(env);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ForeachIterator arg) {
        throw new CriticalException("Unsupported operation");
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
