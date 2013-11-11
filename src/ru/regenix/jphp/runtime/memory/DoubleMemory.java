package ru.regenix.jphp.runtime.memory;

public class DoubleMemory extends Memory {

    double value;

    public DoubleMemory(double value) {
        super(Type.DOUBLE);
        this.value = value;
    }

    public static Memory valueOf(double value){
        return new DoubleMemory(value);
    }

    public static Memory valueOf(float value){
        return new DoubleMemory(value);
    }

    @Override
    public long toLong() {
        return (long)value;
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return value != 0.0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Memory inc() {
        return new DoubleMemory(value + 1);
    }

    @Override
    public Memory dec() {
        return new DoubleMemory(value - 1);
    }

    @Override
    public Memory negative() {
        return new DoubleMemory(- value);
    }

    @Override
    public Memory toNumeric(){
        return this;
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(value + ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value + ((DoubleMemory)memory).value);
            case REFERENCE: return plus(memory.toImmutable());
            default: return plus(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(value - ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toImmutable());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(value * ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value * ((DoubleMemory)memory).value);
            case REFERENCE: return mul(memory.toImmutable());
            default: return mul(memory.toNumeric());
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.type){
            case INT:
                if (((LongMemory)memory).value == 0)
                    return FALSE;
                return new DoubleMemory(value / ((LongMemory)memory).value);

            case DOUBLE:
                if (((DoubleMemory)memory).value == 0)
                    return FALSE;

                return new DoubleMemory(value / ((DoubleMemory)memory).value);
            case REFERENCE: return div(memory.toImmutable());
            default: return div(memory.toNumeric());
        }
    }

    @Override
    public Memory mod(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(value % ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value % ((DoubleMemory)memory).value);
            case REFERENCE: return mod(memory.toImmutable());
            default: return mod(memory.toNumeric());
        }
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case INT: return almostEqual(value, ((LongMemory)memory).value);
            case DOUBLE: return almostEqual(value, ((DoubleMemory)memory).value);
            case REFERENCE: return equal(memory.toImmutable());
            default: return almostEqual(value, memory.toDouble());
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public String concat(Memory memory) {
        switch (memory.type){
            case STRING: return toString() + ((StringMemory)memory).value;
            default: return toString() + memory.toString();
        }
    }

    @Override
    public boolean smaller(Memory memory) {
        switch (memory.type){
            case DOUBLE: return value < ((DoubleMemory)memory).value;
            case INT: return value < ((LongMemory)memory).value;
            case REFERENCE: return smaller(memory.toImmutable());
            default:
                return smaller(memory.toDouble());
        }
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return smaller(memory) || equal(memory);
    }

    @Override
    public boolean greater(Memory memory) {
        return memory.smaller(this);
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return memory.smallerEq(this);
    }

    @Override
    public Memory minus(long value) {
        return new DoubleMemory(this.value - value);
    }

    @Override
    public Memory plus(long value) {
        return new DoubleMemory(this.value + value);
    }

    @Override
    public Memory mul(long value) {
        return new DoubleMemory(this.value * value);
    }

    @Override
    public Memory mul(boolean value) {
        if (value)
            return this;
        else
            return CONST_DOUBLE_0;
    }

    @Override
    public Memory plus(boolean value) {
        if (value)
            return new DoubleMemory(this.value + 1);
        else
            return this;
    }

    @Override
    public Memory div(long value) {
        if (value == 0)
            return FALSE;
        return new DoubleMemory(this.value / value);
    }

    @Override
    public Memory div(double value) {
        if (value == 0.0)
            return FALSE;
        else
            return new DoubleMemory(this.value / value);
    }

    @Override
    public Memory div(boolean value) {
        if (!value)
            return FALSE;
        else
            return this;
    }

    public static boolean almostEqual(double o1 , double o2, double eps){
        return Math.abs(o1 - o2) < eps;
    }

    public static boolean almostEqual(double o1, double o2){
        return almostEqual(o1, o2, 0.0000000001);
    }

    @Override
    public int hashCode() {
        long temp = (long)value;
        return (int) (temp ^ (temp >>> 32));
    }
}
