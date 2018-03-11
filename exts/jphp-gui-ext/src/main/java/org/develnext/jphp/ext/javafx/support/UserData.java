package org.develnext.jphp.ext.javafx.support;

import php.runtime.Memory;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    protected Memory value = Memory.NULL;
    protected final Map<String, Memory> data = new HashMap<>();

    public UserData() {
    }

    public UserData(Memory value) {
        if (value == null) {
            throw new NullPointerException();
        }

        this.value = value;
    }

    public Memory getValue() {
        return value;
    }

    public void setValue(Memory value) {
        this.value = value;
    }

    public Memory set(String name, Memory data) {
        Memory memory = this.data.put(name, data);

        return memory == null ? Memory.NULL : memory;
    }

    public Memory get(String name) {
        Memory memory = this.data.get(name);
        return memory == null ? Memory.NULL : memory;
    }

    public boolean has(String name) {
        return this.data.containsKey(name);
    }
}
