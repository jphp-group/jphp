package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.memory.support.MemoryStringUtils;

import java.nio.charset.Charset;

public class LongMemory extends Memory {
    private final static int MAX_CACHE_STRING = 10000;
    private final static int MAX_NEG_CACHE = Short.MAX_VALUE / 7;
    private final static int MAX_POS_CACHE = Short.MAX_VALUE - MAX_NEG_CACHE;

    private final static String[] STRING_VALUES;
    private final static LongMemory[] CACHE;

    static {
        STRING_VALUES = new String[MAX_CACHE_STRING];
        CACHE = new LongMemory[MAX_POS_CACHE + MAX_NEG_CACHE];
    }

    public long value;

    public LongMemory(long value) {
        super(Type.INT);
        this.value = value;
    }

    public static Memory valueOf(long value) {
        if (value >= -MAX_NEG_CACHE && value < MAX_POS_CACHE) {
            LongMemory result = CACHE[(int) value + MAX_NEG_CACHE];

            if (result == null) {
                result = CACHE[(int) value + MAX_NEG_CACHE] = new LongMemory(value);
            }

            return result;
        } else {
            return new LongMemory(value);
        }
    }

    public static Memory valueOf(int value){
        if (value >= -MAX_NEG_CACHE && value <= MAX_NEG_CACHE) {
            LongMemory result = CACHE[value + MAX_NEG_CACHE];

            if (result == null) {
                result = CACHE[value + MAX_NEG_CACHE] = new LongMemory(value);
            }

            return result;
        } else {
            return new LongMemory(value);
        }
    }

    public static Memory valueOf(byte value){
        return valueOf((int)value);
    }

    public static Memory valueOf(short value){
        return valueOf((int)value);
    }

    public static Memory valueOf(boolean value){
        return value ? TRUE : FALSE;
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
        if (value >= 0 && value < MAX_CACHE_STRING) {
            String value = STRING_VALUES[(int) this.value];

            if (value == null) {
                value = STRING_VALUES[(int) this.value] = String.valueOf(this.value);
            }

            return value;
        }

        return String.valueOf(value);
    }

    @Override
    public Memory inc() {
        return new LongMemory(value + 1);
    }

    @Override
    public Memory dec() {
        return new LongMemory(value - 1);
    }

    @Override
    public Memory negative() {
        return new LongMemory(- value);
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(value + ((LongMemory) memory).value);
            case DOUBLE: return new DoubleMemory(value + ((DoubleMemory)memory).value);
            case REFERENCE: return plus(memory.toValue());
            default: return plus(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(value - ((LongMemory) memory).value);
            case DOUBLE: return new DoubleMemory(value - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toValue());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(value * ((LongMemory) memory).value);
            case DOUBLE: return new DoubleMemory(value * ((DoubleMemory)memory).value);
            case REFERENCE: return mul(memory.toValue());
            default: return mul(memory.toNumeric());
        }
    }

    @Override
    public Memory pow(Memory memory) {
        switch (memory.type){
            case INT: return pow(((LongMemory) memory).value);
            case DOUBLE: return pow(((DoubleMemory) memory).value);
            case REFERENCE: return pow(memory.toImmutable());
            default: return pow(memory.toNumeric());
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.type){
            case INT: {
                long tmp = ((LongMemory)memory).value;
                if (tmp == 0) return FALSE;

                if (value % tmp == 0)
                    return LongMemory.valueOf(value / tmp);
                else
                    return new DoubleMemory(value / (double)tmp);
            }
            case DOUBLE:
                if (((DoubleMemory)memory).value == 0) return FALSE;
                return new DoubleMemory(value / ((DoubleMemory)memory).value);
            case REFERENCE: return div(memory.toValue());
            default: return div(memory.toNumeric());
        }
    }

    @Override
    public Memory div(long value) {
        if (value == 0)
            return FALSE;

        if (this.value % value == 0)
            return LongMemory.valueOf(this.value / value);
        else
            return new DoubleMemory(this.value / (double)value);
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.getRealType() == Type.INT && memory.toValue(LongMemory.class).value == value;
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case INT: return ((LongMemory)memory).value == value;
            case DOUBLE: return ((DoubleMemory)memory).value == value;
            case STRING: return equal(memory.toNumeric());
            case REFERENCE: return equal(memory.toValue());
            default:
                return value == memory.toLong();
        }
    }

    @Override
    public boolean equal(long value) {
        return this.value == value;
    }

    @Override
    public boolean equal(double value) {
        return this.value == value;
    }

    @Override
    public boolean notEqual(Memory memory) {
        switch (memory.type){
            case INT: return ((LongMemory)memory).value != value;
            case DOUBLE: return ((DoubleMemory)memory).value != value;
            case STRING: return notEqual(memory.toNumeric());
            case REFERENCE: return smaller(memory.toValue());
            default:
                return value != toLong();
        }
    }

    @Override
    public String concat(Memory memory) {
        return toString().concat(memory.toString());
    }

    @Override
    public boolean smaller(Memory memory) {
        switch (memory.type){
            case DOUBLE: return value < ((DoubleMemory)memory).value;
            case INT: return value < ((LongMemory)memory).value;
            case STRING: return smaller(memory.toNumeric());
            case REFERENCE: return smaller(memory.toValue());
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
            case REFERENCE: return smallerEq(memory.toValue());
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
        return memory.smallerEq(this);
    }

    @Override
    public Memory minus(long value) {
        return LongMemory.valueOf(this.value - value);
    }

    @Override
    public Memory plus(long value) {
        return LongMemory.valueOf(this.value + value);
    }

    @Override
    public Memory div(boolean value) {
        if (!value)
            return FALSE;
        return this;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof LongMemory)) return false;

        LongMemory that = (LongMemory) o;
        return value == that.value;
    }

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return MemoryStringUtils.getBinaryBytes(this);
    }
}
