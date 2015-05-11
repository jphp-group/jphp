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
}
