package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class ShortArrayMemoryOperation extends MemoryOperation<short[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { short[].class };
    }

    @Override
    public short[] convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        short[] result = new short[((ArrayMemory)arg).size()];

        int i = 0;
        for (Memory el : (ArrayMemory)arg) {
            result[i++] = (short) el.toInteger();
        }

        return result;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, short[] arg) throws Throwable {
        return ArrayMemory.ofShorts(arg).toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
