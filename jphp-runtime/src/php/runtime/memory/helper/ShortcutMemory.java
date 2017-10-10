package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.memory.ReferenceMemory;

public class ShortcutMemory extends ReferenceMemory {

    public ShortcutMemory(ReferenceMemory value) {
        super(value);
    }

    @Override
    public Memory toImmutable() {
        return getValue();
    }

    @Override
    public boolean isShortcut() {
        return true;
    }
}
