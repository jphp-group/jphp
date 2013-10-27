package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class NullMemory extends Memory {

    public static NullMemory INSTANCE = new NullMemory();

    protected NullMemory() {
        super(Type.NULL);
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
    public Memory toNumeric(){
        return Memory.CONST_INT_0;
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT:
            case DOUBLE: return memory;
            default: return memory.toNumeric();
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(-((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(-((DoubleMemory)memory).value);
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return Memory.CONST_INT_0;
            case DOUBLE: return Memory.CONST_DOUBLE_0;
            case STRING: return mul(memory.toNumeric());
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
            case STRING: return div(memory.toNumeric());
        }
        return CONST_INT_0;
    }

    @Override
    public Memory mod(Memory memory) {
        return div(memory);
    }

    @Override
    public Memory concat(Memory memory) {
        switch (memory.type){
            case STRING: return memory;
            default:
                return new StringMemory(memory.toString());
        }
    }

    @Override
    public Memory smaller(Memory memory) {
        switch (memory.type){
            case INT: return 0 < ((LongMemory)memory).value ? TRUE : FALSE;
            case DOUBLE: return 0 < ((DoubleMemory)memory).value ? TRUE : FALSE;
            case BOOL: return 0 < memory.toLong() ? TRUE : FALSE;
            case NULL: return FALSE;
            default:
                return smaller(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(long value) {
        return new LongMemory(- value);
    }
}
