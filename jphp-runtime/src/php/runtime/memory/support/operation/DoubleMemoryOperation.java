package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.support.MemoryOperation;

public class DoubleMemoryOperation extends MemoryOperation<Double> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Double.class, Double.TYPE };
    }

    @Override
    public Double convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toDouble();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Double arg) throws Throwable {
        return DoubleMemory.valueOf(arg);
    }
}
