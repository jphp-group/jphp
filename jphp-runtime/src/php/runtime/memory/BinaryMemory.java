package php.runtime.memory;

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
        return new String(bytes, Charset.forName("UTF-8"));
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
        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new StringMemory((char)(bytes[i] & 0xFF));
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, long index) {
        int i = (int)index;
        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new StringMemory((char)(bytes[i] & 0xFF));
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, double index) {
        int i = (int)index;
        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new StringMemory((char)(bytes[i]  & 0xFF));
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, boolean index) {
        int i = index ? 1 : 0;
        if (i < 0 || i >= bytes.length)
            return FALSE;

        return new StringMemory((char)(bytes[i] & 0xFF));
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, String index) {
        Memory i = StringMemory.toLong(index);
        if (i == null)
            return FALSE;

        if (i.toInteger() < 0 || i.toInteger() >= bytes.length)
            return FALSE;

        return new StringMemory((char)(bytes[i.toInteger()] & 0xFF));
    }

    @Override
    public boolean toBoolean() {
        return (bytes != null && bytes.length > 0 && bytes[0] != '0');
    }
}
