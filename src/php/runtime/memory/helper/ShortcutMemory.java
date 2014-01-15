package php.runtime.memory.helper;

import php.runtime.memory.ReferenceMemory;
import php.runtime.Memory;

public class ShortcutMemory extends ReferenceMemory {

    public ShortcutMemory(ReferenceMemory value) {
        super(value);
    }

    @Override
    public Memory toImmutable() {
        return value;
    }

    @Override
    public boolean isShortcut() {
        return true;
    }
}
