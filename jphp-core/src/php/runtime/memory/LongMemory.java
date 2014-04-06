package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.memory.support.MemoryStringUtils;

public class LongMemory extends Memory {

    protected final static int MAX_CACHE_STRING = 10000;
    protected final static int MAX_NEG_CACHE = Short.MAX_VALUE / 7;
    protected final static int MAX_POS_CACHE = Short.MAX_VALUE - MAX_NEG_CACHE;

    protected final static String[] STRING_VALUES;
    protected final static LongMemory[] CACHE;

    static {
        STRING_VALUES = new String[MAX_CACHE_STRING];
        for(int i = 0; i < MAX_CACHE_STRING; i++){
            STRING_VALUES[i] = String.valueOf(i);
        }

        CACHE = new LongMemory[MAX_POS_CACHE + MAX_NEG_CACHE];
        for(int i = -MAX_NEG_CACHE; i < MAX_POS_CACHE; i++){
            CACHE[i + MAX_NEG_CACHE] = new LongMemory(i);
        }
    }

    public long value;

    public LongMemory(long value) {
        super(Type.INT);
        this.value = value;
    }

    public static Memory valueOf(long value){
        if (value >= -MAX_NEG_CACHE && value < MAX_POS_CACHE)
            return CACHE[(int)value + MAX_NEG_CACHE];
        else
            return new LongMemory(value);
    }

    public static Memory valueOf(int value){
        if (value >= -MAX_NEG_CACHE && value <= MAX_NEG_CACHE)
            return CACHE[value + MAX_NEG_CACHE];
        else
            return new LongMemory(value);
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
        if (value >= 0 && value < MAX_CACHE_STRING)
            return STRING_VALUES[(int)value];

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
            case REFERENCE: return plus(memory.toImmutable());
            default: return plus(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(value - ((LongMemory) memory).value);
            case DOUBLE: return new DoubleMemory(value - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toImmutable());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(value * ((LongMemory) memory).value);
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
                    return LongMemory.valueOf(value / tmp);
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
            return LongMemory.valueOf(this.value / value);
        else
            return new DoubleMemory(this.value / (double)value);
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.getRealType() == Type.INT && ((LongMemory)memory).value == value;
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
            case REFERENCE: return smaller(memory.toImmutable());
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
        if (value != that.value) return false;
        return true;
    }

    @Override
    public byte[] getBinaryBytes() {
        return MemoryStringUtils.getBinaryBytes(this);
    }
}
