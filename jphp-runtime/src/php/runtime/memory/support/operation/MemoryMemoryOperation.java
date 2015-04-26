package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.*;
import php.runtime.memory.helper.CharArrayMemory;
import php.runtime.memory.helper.UndefinedMemory;
import php.runtime.memory.support.MemoryOperation;

public class MemoryMemoryOperation extends MemoryOperation<Memory> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] {
                Memory.class,
                NullMemory.class,
                UndefinedMemory.class,
                StringMemory.class,
                LongMemory.class,
                DoubleMemory.class,
                TrueMemory.class,
                FalseMemory.class,
                KeyValueMemory.class,
                BinaryMemory.class,
                StringBuilderMemory.class,
                CharArrayMemory.class,
                CharMemory.class,
                ObjectMemory.class,
                ReferenceMemory.class
        };
    }

    @Override
    public Memory convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg == null ? Memory.NULL : arg;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg == null ? Memory.NULL : arg;
    }
}
