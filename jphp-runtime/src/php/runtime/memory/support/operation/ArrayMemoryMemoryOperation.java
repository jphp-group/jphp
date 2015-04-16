package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class ArrayMemoryMemoryOperation extends MemoryOperation<ArrayMemory> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { ArrayMemory.class };
    }

    @Override
    public ArrayMemory convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return arg.toValue(ArrayMemory.class);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ArrayMemory arg) throws Throwable {
        return arg == null ? Memory.NULL : arg;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
