package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

import java.math.BigDecimal;

public class BigDecimalOperation extends MemoryOperation<BigDecimal> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { BigDecimal.class };
    }

    @Override
    public BigDecimal convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return new BigDecimal(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, BigDecimal arg) throws Throwable {
        return StringMemory.valueOf(arg.toString());
    }
}
