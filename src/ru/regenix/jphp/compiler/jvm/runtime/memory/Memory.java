package ru.regenix.jphp.compiler.jvm.runtime.memory;

abstract public class Memory {
    public enum Type {
        NULL, BOOL, INT, DOUBLE, STRING, ARRAY, OBJECT, REFERENCE;

        public Class toClass(){
            if (this == DOUBLE)
                return Double.TYPE;
            else if (this == INT)
                return Long.TYPE;
            else if (this == STRING)
                return String.class;
            else if (this == BOOL)
                return Boolean.class;
            else if (this == REFERENCE)
                return Memory.class;

            return null;
        }
    }

    public final Type type;

    protected Memory(Type type) {
        this.type = type;
    }

    public static final Memory NULL = NullMemory.INSTANCE;
    public static final Memory FALSE = FalseMemory.INSTANCE;
    public static final Memory TRUE = TrueMemory.INSTANCE;

    public static final Memory CONST_INT_0 = new LongMemory(0);
    public static final Memory CONST_INT_1 = new LongMemory(1);
    public static final Memory CONST_INT_2 = new LongMemory(2);
    public static final Memory CONST_INT_3 = new LongMemory(3);
    public static final Memory CONST_INT_4 = new LongMemory(4);
    public static final Memory CONST_INT_5 = new LongMemory(5);

    public static final Memory CONST_DOUBLE_0 = new DoubleMemory(0.0);
    public static final Memory CONST_DOUBLE_1 = new DoubleMemory(1.0);

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
    abstract public Memory smaller(Memory memory);

    public Memory concat(String value) {
        return new StringMemory(toString() + value);
    }

    public Memory plus(long value){
        return new LongMemory(toLong() + value);
    }

    public Memory plus(double value){
        return new DoubleMemory(toDouble() + value);
    }

    public Memory plus(String value){
        return plus(new StringMemory(value));
    }

    public Memory plusRight(long value){
        return new LongMemory(value + toLong());
    }

    public Memory plusRight(double value){
        return new DoubleMemory(value + toDouble());
    }

    public Memory plusRight(String value){
        return new StringMemory(value).toNumeric().plus(this);
    }

    public Memory minus(long value){
        return new LongMemory(toLong() - value);
    }

    public Memory minusRight(long value){
        return new LongMemory(value - toLong());
    }

    public Memory minusRight(double value){
        return new DoubleMemory(value - toDouble());
    }

    public Memory mul(long value){
        return new LongMemory(toLong() * value);
    }

    public Memory mul(double value){
        return new DoubleMemory(toDouble() * value);
    }

    public Memory assign(Memory memory){
        return this;
    }

    public Memory assign(long value){
        return this;
    }

    public Memory assign(String value){
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

    public static Memory plusRight(long value, Memory memory){
        return memory.plusRight(value);
    }

    public static Memory plusRight(double value, Memory memory){
        return memory.plusRight(value);
    }

    public static Memory plusRight(String value, Memory memory){
        return memory.plusRight(value);
    }
}
