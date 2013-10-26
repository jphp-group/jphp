package ru.regenix.jphp.compiler.jvm.runtime.memory;

abstract public class Memory {
    public enum Type { NULL, BOOL, INT, DOUBLE, STRING, ARRAY, OBJECT, REFERENCE }

    public static final Memory NULL = NullMemory.INSTANCE;
    public static final Memory FALSE = FalseMemory.INSTANCE;
    public static final Memory TRUE = TrueMemory.INSTANCE;
    public static final Memory CONST_INT_0 = new LongMemory(0);
    public static final Memory CONST_INT_1 = new LongMemory(1);
    public static final Memory CONST_DOUBLE_0 = new DoubleMemory(0.0);
    public static final Memory CONST_DOUBLE_1 = new DoubleMemory(1.0);

    abstract public Type getType();
    abstract public long toLong();
    abstract public double toDouble();
    abstract public boolean toBoolean();
    abstract public Memory toNumeric();
    abstract public String toString();

    abstract public Memory plus(Memory memory);
    abstract public Memory minus(Memory memory);
    abstract public Memory mul(Memory memory);
    abstract public Memory div(Memory memory);
    abstract public Memory mod(Memory memory);
    abstract public Memory concat(Memory memory);

    public Memory assign(Memory memory){
        return this;
    }

    public Memory assignRef(Memory memory){
        return this;
    }

    public Memory toImmutable(){
        return this;
    }

    public boolean isImmutable(){
        return true;
    }
}
