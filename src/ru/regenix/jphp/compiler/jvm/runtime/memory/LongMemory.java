package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class LongMemory extends Memory {

    protected long value;

    public LongMemory(long value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.INT;
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
        return String.valueOf(value);
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.getType()){
            case INT: return new LongMemory(value + ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value + ((DoubleMemory)memory).value);
            default: return plus(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.getType()){
            case INT: return new LongMemory(value - ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value - ((DoubleMemory)memory).value);
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.getType()){
            case INT: return new LongMemory(value * ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value * ((DoubleMemory)memory).value);
            default: return mul(memory.toNumeric());
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.getType()){
            case INT: {
                long tmp = ((LongMemory)memory).value;
                if (value % tmp == 0)
                    return new LongMemory(value / tmp);
                else
                    return new DoubleMemory(value / (double)tmp);
            }
            case DOUBLE: return new DoubleMemory(value / ((DoubleMemory)memory).value);
            default: return div(memory.toNumeric());
        }
    }

    @Override
    public Memory mod(Memory memory) {
        switch (memory.getType()){
            case INT: return new LongMemory(value % ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value % ((DoubleMemory)memory).value);
            default: return new LongMemory(value % memory.toLong());
        }
    }

    @Override
    public Memory concat(Memory memory) {
        switch (memory.getType()){
            case STRING: return new StringMemory(toString() + ((StringMemory)memory).value);
            default: return new StringMemory(toString() + memory.toString());
        }
    }
}
