package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class DoubleArrayMemoryOperation extends MemoryOperation<double[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { double[].class };
    }

    @Override
    public double[] convert(Environment env, TraceInfo trace, Memory arg) {
        return arg.toValue(ArrayMemory.class).toDoubleArray();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, double[] arg) {
        return new ArrayMemory(arg).toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
