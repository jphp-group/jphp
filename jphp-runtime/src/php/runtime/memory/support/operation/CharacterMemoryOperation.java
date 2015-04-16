package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

public class CharacterMemoryOperation extends MemoryOperation<Character> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Character.class, Character.TYPE };
    }

    @Override
    public Character convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toChar();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Character arg) throws Throwable {
        return StringMemory.valueOf(arg);
    }
}
