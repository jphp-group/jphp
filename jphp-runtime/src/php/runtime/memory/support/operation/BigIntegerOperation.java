package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

import java.math.BigInteger;

public class BigIntegerOperation extends MemoryOperation<BigInteger> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { BigInteger.class };
    }

    @Override
    public BigInteger convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return new BigInteger(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, BigInteger arg) throws Throwable {
        return StringMemory.valueOf(arg.toString());
    }
}
