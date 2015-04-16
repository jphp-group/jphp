package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

public class CharSequenceMemoryOperation extends MemoryOperation<CharSequence> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { CharSequence.class };
    }

    @Override
    public CharSequence convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toString();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, CharSequence arg) throws Throwable {
        return StringMemory.valueOf(arg.toString());
    }
}
