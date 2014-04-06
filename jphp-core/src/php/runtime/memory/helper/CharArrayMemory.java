package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;

import java.nio.CharBuffer;

public class CharArrayMemory extends StringMemory {
    protected CharBuffer buffer;

    public CharArrayMemory(String value) {
        super("");
        buffer = CharBuffer.allocate(value.length());
        buffer.put(value);
    }

    public CharArrayMemory(char ch) {
        super("");
        buffer = CharBuffer.allocate(1);
        buffer.put(ch);
    }

    @Override
    public Memory toImmutable() {
        return new StringMemory(buffer.toString());
    }

    @Override
    public String toString() {
        return new String(buffer.array());
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, Memory index) {
        int _index = -1;

        switch (index.type){
            case STRING:
                Memory tmp = StringMemory.toLong(index.toString());
                if (tmp != null)
                    _index = tmp.toInteger();
                break;
            case REFERENCE: return valueOfIndex(index.toValue());
            default:
                _index = index.toInteger();
        }

        if (_index < buffer.length() && _index >= 0)
            return getChar(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }


    @Override
    public Memory valueOfIndex(TraceInfo trace, long index) {
        int _index = (int)index;
        if (_index >= 0 && _index < buffer.length())
            return getChar(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, double index) {
        int _index = (int)index;
        if (_index >= 0 && _index < buffer.length())
            return getChar(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, boolean index) {
        int _index = index ? 1 : 0;
        if (_index >= 0 && _index < buffer.length())
            return getChar(buffer.charAt(_index));
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
            return getChar(buffer.charAt(_index));
        else
            return CONST_EMPTY_STRING;
    }

    public int length() {
        return buffer.length();
    }

    public void put(int index, String s) {
        int len = s.length();
        int sLen = buffer.limit();

        if (index < 0)
            return;

        char ch = len == 0 ? '\0' : s.charAt(0);
        if (index < sLen)
            buffer.put(index, ch);
        else {
            int cnt = index - sLen;
            CharBuffer tmp = CharBuffer.allocate(sLen + cnt + 1);
            tmp.put(buffer.array());

            buffer = tmp;
            for(int i = 0; i < cnt; i++) {
                buffer.append('\32');
            }
            buffer.append(ch);
        }
    }

    public char get(int index) {
        return buffer.get(index);
    }
}
