package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class StringMemory extends Memory {

    String value = "";

    public StringMemory(String value) {
        super(Type.STRING);
        this.value = value;
    }

    public static Memory valueOf(String value){
        return new StringMemory(value);
    }

    @Override
    public long toLong() {
        return toNumeric().toLong();
    }

    @Override
    public double toDouble() {
        return toNumeric().toDouble();
    }

    @Override
    public boolean toBoolean() {
        return (value != null && !value.isEmpty() && !value.equals("0"));
    }

    @Override
    public String toString() {
        return value;
    }

    public static Memory toNumeric(String value){
        int len = value.length();
        boolean real = false;
        int i = 0;
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (!('9' >= ch && ch >= '0')){
                if (ch == '.'){
                    if (real)
                        break;
                    real = true;
                    continue;
                }
                if (i == 0)
                    return CONST_INT_0;
                else
                    break;
            }
        }
        if (real) {
            if (len == i)
                return new DoubleMemory(Double.parseDouble(value));
            else
                return new DoubleMemory(Double.parseDouble(value.substring(0, i)));
        } else {
            if (len == i)
                return new LongMemory(Long.parseLong(value));
            else
                return new LongMemory(Long.parseLong(value.substring(0, i)));
        }
    }

    @Override
    public Memory toNumeric(){
        return toNumeric(value);
    }

    @Override
    public Memory plus(Memory memory) {
        return toNumeric().plus(memory);
    }

    @Override
    public Memory minus(Memory memory) {
        return toNumeric().minus(memory);
    }

    @Override
    public Memory mul(Memory memory) {
        return toNumeric().mul(memory);
    }

    @Override
    public Memory div(Memory memory) {
        return toNumeric().div(memory);
    }

    @Override
    public Memory mod(Memory memory) {
        return toNumeric().mod(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case NULL: return value.equals("");
            case DOUBLE:
            case INT: return toNumeric().equal(memory);
            case STRING: return value.equals(((StringMemory)memory).value);
            case OBJECT:
            case ARRAY: return false;
            default: return equal(memory.toImmutable());
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public Memory minus(long value) {
        return toNumeric().minus(value);
    }

    @Override
    public String concat(Memory memory) {
        switch (memory.type){
            case STRING: return value.concat(((StringMemory)memory).value);
            default:
                return (value + memory.toString());
        }
    }

    @Override
    public boolean smaller(Memory memory) {
        return toNumeric().smaller(memory);
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return toNumeric().smallerEq(memory);
    }

    @Override
    public boolean greater(Memory memory) {
        return toNumeric().greater(memory);
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return toNumeric().greaterEq(memory);
    }

    @Override
    public String concat(String value) {
        return this.value.concat(value);
    }
}
