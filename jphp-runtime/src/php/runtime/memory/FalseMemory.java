package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;

import java.nio.charset.Charset;

public class FalseMemory extends Memory {

    public final static FalseMemory INSTANCE = new FalseMemory();

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
        return this;
    }

    @Override
    public Memory dec(){
        return this;
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
    public boolean identical(Memory memory) {
        return memory.toValue() == FALSE;
    }

    @Override
    public boolean equal(long value) {
        return value == 0;
    }

    @Override
    public boolean equal(double value) {
        return value == 0.0;
    }

    @Override
    public boolean equal(boolean value) {
        return !value;
    }

    @Override
    public boolean equal(String value) {
        return value.isEmpty() || value.equals("0");
    }

    @Override
    public boolean notEqual(long value) {
        return value != 0;
    }

    @Override
    public boolean notEqual(double value) {
        return value != 0.0;
    }

    @Override
    public boolean notEqual(boolean value) {
        return value;
    }

    @Override
    public boolean notEqual(String value) {
        return !value.isEmpty() && !value.equals("0");
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type){
            case INT:
            case DOUBLE: return memory;
            case REFERENCE: return plus(memory.toValue());
            default: return memory.toNumeric();
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new LongMemory(-((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(-((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toValue());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return Memory.CONST_INT_0;
            case DOUBLE: return Memory.CONST_DOUBLE_0;
            case STRING: return mul(memory.toNumeric());
            case REFERENCE: return mul(memory.toValue());
            default: return Memory.CONST_INT_0;
        }
    }

    @Override
    public Memory pow(Memory memory) {
        switch (memory.type){
            case DOUBLE: return Memory.CONST_INT_0;
            case REFERENCE: return pow(memory.toImmutable());
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
            case REFERENCE: return div(memory.toValue());
            case STRING: return div(memory.toNumeric());
        }
        return CONST_INT_0;
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case INT: return (((LongMemory)memory).value == 0);
            case NULL: return true;
            case REFERENCE: return equal(memory.toValue());
            default:
                return !memory.toBoolean();
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public String concat(Memory memory) {
        switch (memory.type){
            case REFERENCE: return concat(memory.toValue());
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
            case REFERENCE: return smaller(memory.toValue());
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
            case REFERENCE: return smallerEq(memory.toValue());
            default:
                return smallerEq(memory.toBoolean());
        }
    }

    @Override
    public boolean greater(Memory memory) {
        switch (memory.type){
            case STRING:
                String str = memory.toString();
                if (str.isEmpty())
                    return false;

                Memory value = StringMemory.toNumeric(str, true, null);
                return value != null && 0 > value.toLong();
            case REFERENCE:
                return greater(memory.toValue());
            default:
                return 0 > memory.toLong();
        }
    }

    @Override
    public boolean greaterEq(Memory memory) {
        switch (memory.type){
            case STRING:
                String str = memory.toString();
                if (str.isEmpty())
                    return true;

                Memory value = StringMemory.toNumeric(str, true, null);
                return value != null && 0 >= value.toLong();
            case REFERENCE:
                return greater(memory.toValue());
            default:
                return 0 >= memory.toLong();
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

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return new byte[]{};
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
        return !value;
    }

    @Override
    public boolean identical(String value) {
        return false;
    }

    @Override
    public Invoker toInvoker(Environment env) {
        return null;
    }
}
