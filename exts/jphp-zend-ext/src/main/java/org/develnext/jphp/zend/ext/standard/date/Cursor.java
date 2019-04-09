package org.develnext.jphp.zend.ext.standard.date;

import java.util.Objects;
import java.util.StringJoiner;

class Cursor {
    private int value;

    void inc() {
        value++;
    }

    void dec() {
        value--;
    }

    int value() {
        return value;
    }

    void setValue(int cursor) {
        value = cursor;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cursor.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cursor)) return false;
        Cursor cursor = (Cursor) o;
        return value == cursor.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
