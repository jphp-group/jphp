package php.runtime.memory;

import java.nio.charset.StandardCharsets;
import php.runtime.Memory;
import php.runtime.env.TraceInfo;

import java.nio.charset.Charset;

public class BinaryMemory extends StringMemory {
    private byte[] bytes;

    public BinaryMemory(String value) {
        super(null);
        bytes = value.getBytes();
    }

    public BinaryMemory(char ch) {
        super(null);
        bytes = String.valueOf(ch).getBytes();
    }

    public BinaryMemory(byte... bytes){
        super(null);
        this.bytes = bytes;
    }

    public static Memory valueOf(String value) {
        return new BinaryMemory(value);
    }

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return bytes;
    }

    @Override
    public char toChar() {
        return (char)((bytes == null || bytes.length == 0 ? 0 : bytes[0]) & 0xFF);
    }

    @Override
    public String toString() {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public String toBinaryString(){
        StringBuilder sb = new StringBuilder(bytes.length);
        for(byte e : bytes){
            sb.append((char)(e & 0xFF));
        }
        return sb.toString();
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, Memory index) {
        int i = index.toInteger();

        if (i < 0 && Math.abs(i) <= bytes.length) {
            i = bytes.length + i;
        }

        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new BinaryMemory(bytes[i]);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, long index) {
        int i = (int)index;

        if (i < 0 && Math.abs(i) <= bytes.length) {
            i = bytes.length + i;
        }

        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new BinaryMemory(bytes[i]);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, double index) {
        int i = (int) index;

        if (i < 0 && Math.abs(i) <= bytes.length) {
            i = bytes.length + i;
        }

        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new BinaryMemory(bytes[i]);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, boolean index) {
        int i = index ? 1 : 0;
        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new BinaryMemory(bytes[i]);
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, String index) {
        Memory i = StringMemory.toLong(index);
        if (i == null)
            return FALSE;

        int toInteger = i.toInteger();

        if (toInteger < 0 && Math.abs(toInteger) <= bytes.length) {
            toInteger = bytes.length + toInteger;
        }

        if (toInteger < 0 || toInteger >= bytes.length)
            return FALSE;

        return new BinaryMemory(bytes[toInteger]);
    }

    @Override
    public boolean toBoolean() {
        return (bytes != null && bytes.length > 0 && bytes[0] != '0');
    }
}
