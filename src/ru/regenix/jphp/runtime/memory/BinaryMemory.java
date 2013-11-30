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
    public String toString() {
        return new String(bytes);
    }
}
