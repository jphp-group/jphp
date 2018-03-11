package org.develnext.jphp.ext.javafx.classes.data;

import javafx.css.Styleable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Data extends MetaNode implements Map<String, String>, Styleable {
    private final HashMap<String, String> map;
    private final String idKey;

    public Data() {
        super();
        this.idKey = "id";

        map = new HashMap<>();
        setManaged(false);
    }

    /* Map <String, String> */
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public String get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public String put(String key, String value) {
        if (key.equals(idKey)) idProperty().setValue(value);
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        map.putAll(m);
    }

    @Override
    public String remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<String> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return map.entrySet();
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public String putIfAbsent(String key, String value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public boolean replace(String key, String oldValue, String newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public String replace(String key, String value) {
        return map.replace(key, value);
    }
}
