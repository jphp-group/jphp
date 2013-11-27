package ru.regenix.jphp.runtime.memory;

public class BinaryMemory extends StringMemory {
    private byte[] bytes;

    public BinaryMemory(String value) {
        super(value);
        bytes = value.getBytes();
    }

    public BinaryMemory(char ch) {
        super(ch);
        bytes = String.valueOf(ch).getBytes();
    }

    public BinaryMemory(byte[] bytes){
        super(new String(bytes));
        this.bytes = bytes;
    }

    @Override
    public byte[] getBinaryBytes() {
        return bytes;
    }
}
