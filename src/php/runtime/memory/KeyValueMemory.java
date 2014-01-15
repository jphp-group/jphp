package php.runtime.memory;

import php.runtime.Memory;

public class KeyValueMemory extends ReferenceMemory {
    public final Memory key;

    public KeyValueMemory(Memory key, Memory value) {
        super(value);
        this.key = key;
    }
}
