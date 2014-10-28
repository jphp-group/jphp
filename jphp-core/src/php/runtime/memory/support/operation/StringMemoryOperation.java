package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.memory.support.MemoryOperation;

public class StringMemoryOperation extends MemoryOperation<String> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { String.class };
    }

    @Override
    public String convert(Environment env, TraceInfo trace, Memory arg) {
        return arg.toString();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, String arg) {
        return StringMemory.valueOf(arg);
    }
}
