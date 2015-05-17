package org.develnext.jphp.debug.impl;

import java.util.HashMap;
import java.util.Map;

public class IdeFeatures {
    protected final Map<String, String> values;

    public IdeFeatures() {
        values = new HashMap<>();
    }

    public void set(String name, String value) {
        values.put(name, value);
    }

    public boolean has(String name) {
        return values.containsKey(name);
    }

    public int getInteger(String name) {
        if (values.containsKey(name)) {
            try {
                return Integer.parseInt(values.get(name));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }
}
