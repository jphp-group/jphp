package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class LongMemory extends Memory {

    protected final static int MAX_CACHE_STRING = 10000;
    protected final static String[] STRING_VALUES;

    static {
        STRING_VALUES = new String[MAX_CACHE_STRING];
        for(int i = 0; i < MAX_CACHE_STRING; i++){
            STRING_VALUES[i] = String.valueOf(i);
        }
    }

    protected long value;

    public LongMemory(long value) {
        super(Type.INT);
        this.value = value;
    }

    public static Memory valueOf(long value){
        return new LongMemory(value);
    }

    @Override
    public long toLong() {
        return value;
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return value != 0;
    }

    @Override
    public Memory toNumeric() {
        return this;
    }

    @Override
    public String toString() {
        if (value >= 0 && value < MAX_CACHE_STRING)
            return STRING_VALUES[(int)value];

        return String.valueOf(value);
    }

    @Override
    public Memory inc(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory( value + ((LongMemory)memory).value );
            case DOUBLE: return new DoubleMemory( value + ((DoubleMemory)memory).value );
            case STRING: return inc(memory.toNumeric());
            case REFERENCE: return inc(memory.toImmutable());
            default:
                return new LongMemory(value + memory.toLong());
        }
    }

    @Override
    public Memory negative() {
        return new LongMemory(- value);
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(value + ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value + ((DoubleMemory)memory).value);
            case REFERENCE: return plus(memory.toImmutable());
            default: return plus(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(value - ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toImmutable());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(value * ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value * ((DoubleMemory)memory).value);
            case REFERENCE: return mul(memory.toImmutable());
            default: return mul(memory.toNumeric());
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.type){
            case INT: {
                long tmp = ((LongMemory)memory).value;
                if (tmp == 0) return FALSE;

                if (value % tmp == 0)
                    return new LongMemory(value / tmp);
                else
                    return new DoubleMemory(value / (double)tmp);
            }
            case DOUBLE:
                if (((DoubleMemory)memory).value == 0) return FALSE;
                return new DoubleMemory(value / ((DoubleMemory)memory).value);
            case REFERENCE: return div(memory.toImmutable());
            default: return div(memory.toNumeric());
        }
    }

    @Override
    public Memory div(long value) {
        if (value == 0)
            return FALSE;

        if (this.value % value == 0)
            return new LongMemory(this.value / value);
        else
            return new DoubleMemory(this.value / (double)value);
    }

    @Override
    public Memory mod(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(value % ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value % ((DoubleMemory)memory).value);
            case STRING: return mod(memory.toNumeric());
            case REFERENCE: return mod(memory.toImmutable());
            default: return new LongMemory(value % memory.toLong());
        }
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case INT: return ((LongMemory)memory).value == value;
            case DOUBLE: return ((DoubleMemory)memory).value == value;
            case STRING: return equal(memory.toNumeric());
            case REFERENCE: return equal(memory.toImmutable());
            default:
                return value == toLong();
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        switch (memory.type){
            case INT: return ((LongMemory)memory).value != value;
            case DOUBLE: return ((DoubleMemory)memory).value != value;
            case STRING: return notEqual(memory.toNumeric());
            case REFERENCE: return smaller(memory.toImmutable());
            default:
                return value != toLong();
        }
    }

    @Override
    public String concat(Memory memory) {
        switch (memory.type){
            case STRING: return String.valueOf(value) + ((StringMemory)memory).value;
            default: return toString() + memory.toString();
        }
    }

    @Override
    public boolean smaller(Memory memory) {
        switch (memory.type){
            case DOUBLE: return value < ((DoubleMemory)memory).value;
            case INT: return value < ((LongMemory)memory).value;
            case STRING: return smaller(memory.toNumeric());
            case REFERENCE: return smaller(memory.toImmutable());
            default:
                return value < toLong();
        }
    }

    @Override
    public boolean smallerEq(Memory memory) {
        switch (memory.type){
            case DOUBLE: return value <= ((DoubleMemory)memory).value;
            case INT: return value <= ((LongMemory)memory).value;
            case STRING: return smallerEq(memory.toNumeric());
            case REFERENCE: return smallerEq(memory.toImmutable());
            default:
                return value <= toLong();
        }
    }

    @Override
    public boolean greater(Memory memory) {
        return memory.smaller(this);
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return memory.smallerEq(memory);
    }

    @Override
    public Memory minus(long value) {
        return new LongMemory(this.value - value);
    }

    @Override
    public Memory plus(long value) {
        return new LongMemory(this.value + value);
    }

    @Override
    public Memory div(boolean value) {
        if (!value)
            return FALSE;
        return this;
    }
}
