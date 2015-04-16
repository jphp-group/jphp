package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class BooleanArrayMemoryOperation extends MemoryOperation<boolean[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { boolean[].class };
    }

    @Override
    public boolean[] convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toValue(ArrayMemory.class).toBoolArray();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, boolean[] arg) throws Throwable {
        return ArrayMemory.ofBooleans(arg).toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
