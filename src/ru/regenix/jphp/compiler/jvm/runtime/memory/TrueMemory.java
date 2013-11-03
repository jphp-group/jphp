package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class TrueMemory extends Memory {

    public final static TrueMemory INSTANCE = new TrueMemory();

    protected TrueMemory() {
        super(Type.BOOL);
    }

    @Override
    public long toLong() {
        return 1;
    }

    @Override
    public double toDouble() {
        return 1;
    }

    @Override
    public boolean toBoolean() {
        return true;
    }

    @Override
    public String toString() {
        return "1";
    }

    @Override
    public Memory inc(Memory memory) {
        switch (memory.type){
            case INT: return inc(1);
            case DOUBLE: return inc(1.0);
            case STRING: return inc(memory.toNumeric());
            case REFERENCE: return inc(memory.toImmutable());
            default:
                return inc(memory.toLong());
        }
    }

    @Override
    public Memory negative() {
        return new LongMemory(-1);
    }

    @Override
    public Memory toNumeric(){
        return Memory.CONST_INT_1;
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(1 + ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(1 + ((DoubleMemory)memory).value);
            case REFERENCE: return plus(memory.toImmutable());
            default: return memory.toNumeric().plus(CONST_INT_1);
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(1 - ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(1 - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toImmutable());
            case BOOL: return new LongMemory(1 - memory.toLong());
            default: return CONST_INT_1.minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT:
            case DOUBLE: return memory;
            case REFERENCE: return mul(memory.toImmutable());
            default: return memory.toNumeric();
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.type){
            case DOUBLE: return new DoubleMemory(1 / ((DoubleMemory)memory).value);
            case REFERENCE: return div(memory.toImmutable());
            default:
                return CONST_INT_1.div(memory.toNumeric());
        }
    }

    @Override
    public Memory divRight(boolean value) {
        if (!value)
            return CONST_INT_0;
        else
            return CONST_INT_1;
    }

    @Override
    public Memory divRight(long value) {
        if (value == 0)
            return CONST_INT_0;
        else
            return new LongMemory(value);
    }

    @Override
    public Memory divRight(double value) {
        return new DoubleMemory(value);
    }

    @Override
    public Memory mod(Memory memory) {
        return mod(memory.toLong());
    }

    @Override
    public boolean equal(Memory memory) {
        return toBoolean();
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !toBoolean();
    }

    @Override
    public String concat(Memory memory) {
        switch (memory.type){
            case STRING: return toString() + ((StringMemory)memory).value;
            default:
                return toString() + memory.toString();
        }
    }

    @Override
    public boolean smaller(Memory memory) {
        return !memory.toBoolean();
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return memory.toBoolean();
    }

    @Override
    public boolean greater(Memory memory) {
        return false;
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return memory.toBoolean();
    }

    @Override
    public Memory minus(long value) {
        return new LongMemory(1 - value);
    }
}
