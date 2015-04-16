package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class FloatArrayMemoryOperation extends MemoryOperation<float[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { float[].class };
    }

    @Override
    public float[] convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toValue(ArrayMemory.class).toFloatArray();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, float[] arg) throws Throwable {
        return ArrayMemory.ofFloats(arg).toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
