package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.memory.support.MemoryStringUtils;

import java.nio.charset.Charset;
import java.util.Locale;

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

    public static Memory valueOf(Number number) {
        return number instanceof Double || number instanceof Float
                ? new DoubleMemory(number.doubleValue())
                : LongMemory.valueOf(number.longValue());
    }

    @Override
    public long toLong() {
        if (value == Double.POSITIVE_INFINITY) {
            return 0;
        }

        return (long) value;
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
        long longValue = (long) value;

        double abs = value < 0 ? - value : value;
        int exp = (int) Math.log10(abs);
        // php/0c02
        if (longValue == value && exp < 18) {
            return longValue > Integer.MAX_VALUE ? Double.toString(longValue) : String.valueOf(longValue);
        }

        if (-5 < exp && exp < 18) {
            int pr = 13;

            char[] tmp = Double.toString(value).toCharArray();
            for(int i = 0; i < tmp.length; i++){
                switch (tmp[i]){
                    case '-': if (i == 0) continue; else break;
                    case '0': pr += 1;
                    case '.': continue;
                }
                break;
            }

            int digits = pr - exp;

            if (digits > pr) {
                digits = pr;
            } else if (digits < 0) {
                digits = 0;
            }

            String v = String.format(Locale.ENGLISH, "%." + digits + "f", value);

            int len = v.length();
            int nonzero = -1;
            boolean dot = false;

            int i = len - 1;
            for(; i >= 0; i--){
                char ch = v.charAt(i);
                if (ch == '.')
                    dot = true;

                if (ch != '0' && nonzero < 0){
                    nonzero = ch == '.' ? i - 1 : i;
                }
            }
            if (dot && nonzero > 0) {
                return v.substring(0, nonzero + 1);
            } else {
                return v;
            }
        } else {
            return String.format(Locale.ENGLISH, "%.13E", value);
        }
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
            case REFERENCE: return plus(memory.toValue());
            default: return plus(memory.toNumeric());
        }
    }

    @Override
    public Memory minus(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(value - ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value - ((DoubleMemory)memory).value);
            case REFERENCE: return minus(memory.toValue());
            default: return minus(memory.toNumeric());
        }
    }

    @Override
    public Memory mul(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(value * ((LongMemory)memory).value);
            case DOUBLE: return new DoubleMemory(value * ((DoubleMemory)memory).value);
            case REFERENCE: return mul(memory.toValue());
            default: return mul(memory.toNumeric());
        }
    }

    @Override
    public Memory pow(Memory memory) {
        switch (memory.type){
            case INT: return new DoubleMemory(Math.pow(value, ((LongMemory)memory).value));
            case DOUBLE: return new DoubleMemory(Math.pow(value, ((LongMemory)memory).value));
            case REFERENCE: return mul(memory.toImmutable());
            default: return pow(memory.toNumeric());
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
            case REFERENCE: return div(memory.toValue());
            default: return div(memory.toNumeric());
        }
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case INT: return almostEqual(value, ((LongMemory)memory).value);
            case DOUBLE: return almostEqual(value, ((DoubleMemory)memory).value);
            case REFERENCE: return equal(memory.toValue());
            default: return almostEqual(value, memory.toDouble());
        }
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.getRealType() == Type.DOUBLE && (memory.toValue(DoubleMemory.class)).value == value;
    }

    @Override
    public boolean equal(double value) {
        return almostEqual(this.value, value);
    }

    @Override
    public boolean equal(long value) {
        return almostEqual(this.value, value);
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public boolean not() {
        return almostEqual(this.value, 0.0);
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
            case REFERENCE: return smaller(memory.toValue());
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DoubleMemory that = (DoubleMemory) o;

        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = (long) value;
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return MemoryStringUtils.getBinaryBytes(this);
    }
}
