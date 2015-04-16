package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class LongArrayMemoryOperation extends MemoryOperation<long[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { long[].class };
    }

    @Override
    public long[] convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toValue(ArrayMemory.class).toLongArray();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, long[] arg) throws Throwable {
        return ArrayMemory.ofLongs(arg).toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
