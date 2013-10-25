package ru.regenix.jphp.compiler.jvm.runtime;

public class Memory {
    public static final Memory NULL = new Memory(Type.NULL);
    public static final Memory TRUE = new Memory(true);
    public static final Memory FALSE = new Memory(false);

    public enum Type { NULL, BOOL, INT, DOUBLE, STRING, ARRAY, OBJECT }

    protected Type type;

    protected int intValue;
    protected long longValue;
    protected double doubleValue;

    protected Object value;

    public Memory(Type type) {
        this.type = type;
    }

    public Memory(boolean value){
        this(Type.BOOL);
        this.intValue = value ? 1 : 0;
    }

    public Memory(int value){
        this(Type.INT);
        this.longValue = value;
    }

    public Memory(long value){
        this(Type.INT);
        this.longValue = value;
    }

    public Memory(byte value){
        this((long)value);
    }

    public Memory(short value){
        this((long)value);
    }

    public Memory(String value){
        this(Type.STRING);
        this.value = value;
    }
}
