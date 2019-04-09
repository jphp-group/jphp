package org.develnext.jphp.zend.ext.standard.date;

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
}
