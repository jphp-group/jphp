package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

public class ForeachIteratorMemoryOperation extends MemoryOperation<ForeachIterator> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { ForeachIterator.class };
    }

    @Override
    public ForeachIterator convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : arg.getNewIterator(env);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ForeachIterator arg) throws Throwable {
        throw new CriticalException("Unsupported operation");
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(TypeChecker.of(HintType.TRAVERSABLE));
    }
}
