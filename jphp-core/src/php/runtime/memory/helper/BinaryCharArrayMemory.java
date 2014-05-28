package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.env.TraceInfo;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.StringMemory;

public class BinaryCharArrayMemory extends CharArrayMemory {
    protected final static Memory CONST_EMPTY_STRING = new BinaryMemory();

    public BinaryCharArrayMemory(String value) {
        super(value);
        buffer.position(0);
    }

    public BinaryCharArrayMemory(char ch) {
        super(ch);
        buffer.position(0);
    }

    @Override
    public Memory toImmutable() {
        return new BinaryMemory(buffer.toString());
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, long index) {
        int _index = (int)index;
        if (_index >= 0 && _index < buffer.length()) {
            return new BinaryMemory(buffer.charAt(_index));
        } else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, double index) {
        int _index = (int)index;
        if (_index >= 0 && _index < buffer.length())
            return new BinaryMemory(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, boolean index) {
        int _index = index ? 1 : 0;
        if (_index >= 0 && _index < buffer.length())
            return new BinaryMemory(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, String index) {
        int _index = -1;

        Memory tmp = StringMemory.toLong(index);
        if (tmp != null)
            _index = tmp.toInteger();

        if (_index >= 0 && _index < buffer.length())
            return new BinaryMemory(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public char toChar() {
        return toImmutable().toChar();
    }
}
