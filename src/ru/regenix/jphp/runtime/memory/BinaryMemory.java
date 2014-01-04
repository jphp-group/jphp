package ru.regenix.jphp.runtime.memory;

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

    public BinaryMemory(byte[] bytes){
        super(null);
        this.bytes = bytes;
    }

    @Override
    public byte[] getBinaryBytes() {
        return bytes;
    }

    @Override
    public char toChar() {
        return (char)((bytes == null || bytes.length == 0 ? 0 : bytes[0]) & 0xFF);
    }

    @Override
    public String toString() {
        char[] tmp = new char[bytes.length];
        int length = bytes.length;
        for(int i = 0; i < length; i++){
            tmp[i] = (char)(bytes[i] & 0xFF);
        }
        return new String(tmp);
    }
}
