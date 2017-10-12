package php.runtime.memory;

import php.runtime.Memory;

public class KeyValueMemory extends ReferenceMemory {
    public final Memory key;
    private Object arrayKey;

    public KeyValueMemory(Memory key, Memory value) {
        super(value);
        this.key = key;
    }

    public static Memory valueOf(Memory key, Memory value){
        return new KeyValueMemory(key, value);
    }

    public static Memory valueOf(Memory key, Memory value, String arrayKey) {
        KeyValueMemory memory = new KeyValueMemory(key, value);
        memory.arrayKey = arrayKey;
        return memory;
    }

    public static Memory valueOf(Memory key, Memory value, Memory arrayKey) {
        KeyValueMemory memory = new KeyValueMemory(key, value);
        memory.arrayKey = arrayKey;
        return memory;
    }

    @Override
    public Memory toImmutable() {
        return this;
    }

    public Object getArrayKey() {
        if (arrayKey == null) {
            arrayKey = ArrayMemory.toKey(key);
        }

        return arrayKey;
    }


}
