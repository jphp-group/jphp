package php.runtime.memory.support;

import php.runtime.Memory;

import java.io.ByteArrayOutputStream;

public class BinaryStringBuilder {
    private ByteArrayOutputStream buffer;

    public BinaryStringBuilder(int size) {
        buffer = new ByteArrayOutputStream(size);
    }

    public BinaryStringBuilder(){
        buffer = new ByteArrayOutputStream();
    }

    public BinaryStringBuilder(String initValue){
        this(initValue.length());
        append(MemoryStringUtils.getBinaryBytes(initValue));
    }

    public BinaryStringBuilder(Memory initValue){
        this(initValue.toString());
    }

    public void append(byte[] bytes, int off, int len){
        buffer.write(bytes, off, len);
    }

    public void append(byte[] bytes) {
        append(bytes, 0, bytes.length);
    }

    public void append(Memory value){
        append(value.getBinaryBytes());
    }

    public void appendString(String value){
        append(value.getBytes());
    }

    public void appendChar(char ch){
        appendString(String.valueOf(ch));
    }

    public void append(byte b){
        buffer.write(b);
    }

    public byte[] toByteArray(){
        return buffer.toByteArray();
    }

    @Override
    public String toString(){
        return buffer.toString();
    }
}
