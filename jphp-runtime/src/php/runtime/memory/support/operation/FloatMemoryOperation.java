package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.support.MemoryOperation;

public class FloatMemoryOperation extends MemoryOperation<Float> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Float.class, Float.TYPE };
    }

    @Override
    public Float convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toFloat();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Float arg) throws Throwable {
        return DoubleMemory.valueOf(arg);
    }
}
