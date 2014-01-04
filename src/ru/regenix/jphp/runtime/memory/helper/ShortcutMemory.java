package ru.regenix.jphp.runtime.memory.helper;

import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

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
