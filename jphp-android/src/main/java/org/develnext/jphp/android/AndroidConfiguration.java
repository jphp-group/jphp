package org.develnext.jphp.android;

import java.util.Properties;

public class AndroidConfiguration extends Properties {
    public int getInteger(String name) {
        return getInteger(name, 0);
    }

    public int getInteger(String name, int def) {
        try {
            return Integer.parseInt(getProperty(name), def);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public boolean getBoolean(String name) {
        String value = getProperty(name, "").trim();
        return value.equals("1") || value.equalsIgnoreCase("on");
    }
}
