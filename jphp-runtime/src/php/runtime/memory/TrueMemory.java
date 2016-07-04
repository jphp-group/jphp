package php.runtime.memory;

import php.runtime.Memory;

import java.nio.charset.Charset;

public class TrueMemory extends Memory {

    public final static TrueMemory INSTANCE = new TrueMemory();

    protected TrueMemory() {
        super(Type.BOOL);
    }

    public static Memory valueOf(boolean value){
        return value ? TRUE : FALSE;
    }

    public static Memory valueOf(int value){
        return value != 0 ? TRUE : FALSE;
    }

    public static Memory valueOf(long value){
        return value != 0 ? TRUE : FALSE;
    }

    public static Memory valueOf(short value){
        return value != 0 ? TRUE : FALSE;
    }

    public static Memory valueOf(byte value){
        return value != 0 ? TRUE : FALSE;
    }

    public static Memory valueOf(double value){
        return value == 0.0 ? FALSE : TRUE;
    }

    public static Memory valueOf(float value){
        return value == 0.0f ? FALSE : TRUE;
    }

    public static Memory valueOf(String value){
        return value == null || value.isEmpty() || "0".equals(value) ? FALSE : TRUE;
    }

    public static Memory valueOf(Memory memory){
        return memory.toBoolean() ? TRUE : FALSE;
    }

    @Override
    public boolean equal(long value) {
        return value != 0;
    }

    @Override
    public boolean equal(double value) {
        return value != 0.0;
    }

    @Override
    public boolean equal(boolean value) {
        return value;
    }

    @Override
    public boolean equal(String value) {
        return !value.isEmpty();
    }

    @Override
    public boolean notEqual(long value) {
        return value == 0;
    }

    @Override
    public boolean notEqual(double value) {
        return value == 0.0;
    }

    @Override
    public boolean notEqual(boolean value) {
        return !value;
    }

    @Override
    public boolean notEqual(String value) {
        return value.isEmpty();
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
    public Memory inc() {
        return this;
    }

    @Override
    public Memory dec() {
        return this;
    }

    @Override
    public Memory negative() {
        return LongMemory.valueOf(-1);
    }

    @Override
    public Memory toNumeric(){
        return Memory.CONST_INT_1;
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(1 + ((LongMemory) memory).value);
            case DOUBLE: return new DoubleMemory(1 + ((DoubleMemory)memory).value);
            case REFERENCE: return plus(memory.toValue());
            default: return memory.toNumeric().plus(CONST_INT_1);
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return LongMemory.valueOf(1 - ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(1 - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toValue());
            case BOOL: return LongMemory.valueOf(1 - memory.toLong());
            default: return CONST_INT_1.minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT:
            case DOUBLE: return memory;
            case REFERENCE: return mul(memory.toValue());
            default: return memory.toNumeric();
        }
    }

    @Override
    public Memory pow(Memory memory) {
        switch (memory.type) {
            case DOUBLE: return Memory.CONST_DOUBLE_1;
            case REFERENCE: return pow(memory.toImmutable());
            default: return Memory.CONST_INT_1;
        }
    }

    @Override
    public Memory div(Memory memory) {
        switch (memory.type){
            case DOUBLE: return new DoubleMemory(1 / ((DoubleMemory)memory).value);
            case REFERENCE: return div(memory.toValue());
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
    public boolean equal(Memory memory) {
        return memory.toBoolean();
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !memory.toBoolean();
    }

    @Override
    public String concat(Memory memory) {
        return toString().concat(memory.toString());
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
        switch (memory.type){
            case STRING:
                String str = memory.toString();
                if (str.isEmpty())
                    return true;

                Memory value = StringMemory.toNumeric(str, true, null);
                return value != null && 1 > value.toLong();
            case REFERENCE:
                return greater(memory.toValue());
            default:
                return 1 > memory.toLong();
        }
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return 1 >= memory.toLong();
    }

    @Override
    public Memory minus(long value) {
        return new LongMemory(1 - value);
    }

    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return new byte[]{ 1 };
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.toValue() == TRUE;
    }

    @Override
    public boolean identical(long value) {
        return false;
    }

    @Override
    public boolean identical(double value) {
        return false;
    }

    @Override
    public boolean identical(boolean value) {
        return value;
    }

    @Override
    public boolean identical(String value) {
        return false;
    }
}
