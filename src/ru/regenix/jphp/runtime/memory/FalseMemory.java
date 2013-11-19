package ru.regenix.jphp.runtime.memory;

public class FalseMemory extends Memory {

    public final static FalseMemory INSTANCE = new FalseMemory();

    boolean value;

    protected FalseMemory() {
        super(Type.BOOL);
    }

    FalseMemory(Type type){
        super(type);
    }

    @Override
    public long toLong() {
        return 0;
    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public boolean toBoolean() {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Memory inc() {
        return CONST_INT_1;
    }

    @Override
    public Memory dec(){
        return LongMemory.valueOf(-1);
    }

    @Override
    public Memory negative() {
        return CONST_INT_0;
    }

    @Override
    public Memory toNumeric(){
        return Memory.CONST_INT_0;
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT:
            case DOUBLE: return memory;
            case REFERENCE: return plus(memory.toImmutable());
            default: return memory.toNumeric();
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(-((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(-((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toImmutable());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return Memory.CONST_INT_0;
            case DOUBLE: return Memory.CONST_DOUBLE_0;
            case STRING: return mul(memory.toNumeric());
            case REFERENCE: return mul(memory.toImmutable());
            default: return Memory.CONST_INT_0;
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.type){
            case DOUBLE: return CONST_DOUBLE_0;
            case INT: {
                if (((LongMemory)memory).value == 0)
                    throw new RuntimeException("Zero division denied");

                return CONST_INT_0;
            }
            case REFERENCE: return div(memory.toImmutable());
            case STRING: return div(memory.toNumeric());
        }
        return CONST_INT_0;
    }

    @Override
    public Memory mod(Memory memory) {
        return div(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case INT: return (((LongMemory)memory).value == 0);
            case NULL: return true;
            case REFERENCE: return equal(memory.toImmutable());
            default:
                return !toBoolean();
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public String concat(Memory memory) {
        switch (memory.type){
            case REFERENCE: return concat(memory.toImmutable());
            default:
                return memory.toString();
        }
    }

    @Override
    public boolean smaller(Memory memory) {
        switch (memory.type){
            case INT: return 0 < ((LongMemory)memory).value;
            case DOUBLE: return 0 < ((DoubleMemory)memory).value;
            case BOOL: return 0 < memory.toLong();
            case NULL: return false;
            case REFERENCE: return smaller(memory.toImmutable());
            default:
                return smaller(memory.toBoolean());
        }
    }

    @Override
    public boolean smallerEq(Memory memory) {
        switch (memory.type){
            case INT: return 0 <= ((LongMemory)memory).value;
            case DOUBLE: return 0 <= ((DoubleMemory)memory).value;
            case BOOL: return 0 <= memory.toLong();
            case NULL: return true;
            case REFERENCE: return smallerEq(memory.toImmutable());
            default:
                return smallerEq(memory.toBoolean());
        }
    }

    @Override
    public boolean greater(Memory memory) {
        switch (memory.type){
            case INT: return 0 > ((LongMemory)memory).value;
            case DOUBLE: return 0 > ((DoubleMemory)memory).value;
            case BOOL: return 0 > memory.toLong();
            case NULL: return false;
            case REFERENCE: return greater(memory.toImmutable());
            default:
                return greater(memory.toBoolean());
        }
    }

    @Override
    public boolean greaterEq(Memory memory) {
        switch (memory.type){
            case INT: return 0 >= ((LongMemory)memory).value;
            case DOUBLE: return 0 >= ((DoubleMemory)memory).value;
            case BOOL: return 0 >= memory.toLong();
            case NULL: return true;
            case REFERENCE: return greaterEq(memory.toImmutable());
            default:
                return greater(memory.toBoolean());
        }
    }

    @Override
    public Memory minus(long value) {
        return new LongMemory(- value);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
