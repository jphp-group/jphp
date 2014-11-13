package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.memory.NullMemory;

public class UndefinedMemory extends NullMemory {

    public static final UndefinedMemory INSTANCE = new UndefinedMemory();

    @Override
    public Memory toImmutable() {
        return UNDEFINED;
    }

    @Override
    public boolean isUndefined() {
        return true;
    }
}
