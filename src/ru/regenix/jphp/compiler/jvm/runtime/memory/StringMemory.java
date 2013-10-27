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

    @Override
    public Memory toNumeric(){
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
    public Memory minus(long value) {
        return toNumeric().minus(value);
    }

    @Override
    public Memory concat(Memory memory) {
        switch (memory.type){
            case STRING: return new StringMemory(value + ((StringMemory)memory).value);
            default:
                return new StringMemory(value + memory.toString());
        }
    }

    @Override
    public Memory smaller(Memory memory) {
        return toNumeric().smaller(memory);
    }

    @Override
    public Memory concat(String value) {
        return new StringMemory(value + value);
    }
}
