package ru.regenix.jphp.compiler.jvm.runtime.memory;

abstract public class Memory {
    public enum Type {
        NULL, BOOL, INT, DOUBLE, STRING, ARRAY, OBJECT, REFERENCE;

        public Class toClass(){
            if (this == DOUBLE)
                return Double.TYPE;
            else if (this == INT)
                return Long.TYPE;
            else if (this == STRING)
                return String.class;
            else if (this == BOOL)
                return Boolean.class;
            else if (this == REFERENCE)
                return Memory.class;

            return null;
        }

        public boolean isConstant(){
            return this != REFERENCE && this != ARRAY && this != OBJECT;
        }
    }

    public final Type type;

    protected Memory(Type type) {
        this.type = type;
    }

    public static final Memory NULL = NullMemory.INSTANCE;
    public static final Memory FALSE = FalseMemory.INSTANCE;
    public static final Memory TRUE = TrueMemory.INSTANCE;

    public static final Memory CONST_INT_0 = new LongMemory(0);
    public static final Memory CONST_INT_1 = new LongMemory(1);
    public static final Memory CONST_INT_2 = new LongMemory(2);
    public static final Memory CONST_INT_3 = new LongMemory(3);
    public static final Memory CONST_INT_4 = new LongMemory(4);
    public static final Memory CONST_INT_5 = new LongMemory(5);

    public static final Memory CONST_DOUBLE_0 = new DoubleMemory(0.0);
    public static final Memory CONST_DOUBLE_1 = new DoubleMemory(1.0);

    abstract public long toLong();
    abstract public double toDouble();
    abstract public boolean toBoolean();
    abstract public Memory toNumeric();
    abstract public String toString();

    // CONCAT
    abstract public Memory concat(Memory memory);
    public Memory concat(long value) { return new StringMemory(toString() + value); }
    public Memory concat(double value) { return new StringMemory(toString() + value); }
    public Memory concat(boolean value) { return new StringMemory(toString() + boolToString(value)); }
    public Memory concat(String value) { return new StringMemory(toString() + value); }

    public String concatScalar(Memory memory){  return toString() + memory.toString(); }
    public String concatScalar(long value) { return toString() + value; }
    public String concatScalar(double value) { return toString() + value; }
    public String concatScalar(boolean value) { return toString() + boolToString(value); }
    public String concatScalar(String value) { return toString() + value; }

    // PLUS
    abstract public Memory plus(Memory memory);
    public Memory plus(long value){ return new LongMemory(toLong() + value); }
    public Memory plus(double value){ return new DoubleMemory(toDouble() + value); }
    public Memory plus(boolean value){ return new LongMemory(toLong() + (value ? 1 : 0)); }
    public Memory plus(String value){ return plus(StringMemory.toNumeric(value)); }

    // MINUS
    abstract public Memory minus(Memory memory);
    public Memory minus(long value){ return new LongMemory(toLong() - value); }
    public Memory minus(double value){ return new DoubleMemory(toDouble() - value); }
    public Memory minus(boolean value){ return new LongMemory(toLong() - (value ? 1 : 0)); }
    public Memory minus(String value){ return minus(StringMemory.toNumeric(value)); }

    // MUL
    abstract public Memory mul(Memory memory);
    public Memory mul(long value){ return new LongMemory(toLong() * value); }
    public Memory mul(double value){ return new DoubleMemory(toDouble() * value); }
    public Memory mul(boolean value){ return new LongMemory(toLong() * (value ? 1 : 0)); }
    public Memory mul(String value){ return mul(StringMemory.toNumeric(value)); }

    // DIV
    abstract public Memory div(Memory memory);
    public Memory div(long value){ return new DoubleMemory(toDouble() / value); }
    public Memory div(double value){ return new DoubleMemory(toDouble() / value); }
    public Memory div(boolean value){ return new DoubleMemory(toDouble() / (value ? 1 : 0)); }
    public Memory div(String value){ return div(StringMemory.toNumeric(value)); }

    // MOD
    abstract public Memory mod(Memory memory);
    public Memory mod(long value){ return new LongMemory(toLong() % value); }
    public Memory mod(double value){ return new DoubleMemory(toDouble() % value); }
    public Memory mod(boolean value){ return new LongMemory(toLong() % (value ? 1 : 0)); }
    public Memory mod(String value){ return div(StringMemory.toNumeric(value)); }

    // EQUAL
    abstract public boolean equal(Memory memory);
    public boolean equal(long value){ return toLong() == value; }
    public boolean equal(double value) { return toDouble() == value; }
    public boolean equal(boolean value) { return toBoolean() == value; }
    public boolean equal(String value) { return toString().equals(value); }

    // NOT EQUAL
    abstract public boolean notEqual(Memory memory);
    public boolean notEqual(long value){ return toLong() != value; }
    public boolean notEqual(double value) { return toDouble() != value; }
    public boolean notEqual(boolean value) { return toBoolean() != value; }
    public boolean notEqual(String value) { return !toString().equals(value); }

    // SMALLER
    abstract public boolean smaller(Memory memory);
    public boolean smaller(long value) { return toDouble() < value; }
    public boolean smaller(double value) { return toDouble() < value; }
    public boolean smaller(boolean value) { return toDouble() < (value ? 1 : 0); }
    public boolean smaller(String value) { return this.smaller(StringMemory.toNumeric(value)); }

    // SMALLER EQ
    abstract public boolean smallerEq(Memory memory);
    public boolean smallerEq(long value) { return toDouble() <= value; }
    public boolean smallerEq(double value) { return toDouble() <= value; }
    public boolean smallerEq(boolean value) { return toDouble() <= (value ? 1 : 0); }
    public boolean smallerEq(String value) { return this.smallerEq(StringMemory.toNumeric(value)); }

    // GREATER
    abstract public boolean greater(Memory memory);
    public boolean greater(long value) { return toDouble() > value; }
    public boolean greater(double value) { return toDouble() > value; }
    public boolean greater(boolean value) { return toDouble() > (value ? 1 : 0); }
    public boolean greater(String value) { return this.smaller(StringMemory.toNumeric(value)); }

    // GREATER EQ
    abstract public boolean greaterEq(Memory memory);
    public boolean greaterEq(long value) { return toDouble() >= value; }
    public boolean greaterEq(double value) { return toDouble() >= value; }
    public boolean greaterEq(boolean value) { return toDouble() >= (value ? 1 : 0); }
    public boolean greaterEq(String value) { return this.greaterEq(StringMemory.toNumeric(value)); }

    // ASSIGN
    public Memory assign(Memory memory){ return this; }
    public Memory assign(long value){ return this; }
    public Memory assign(double value) { return this; }
    public Memory assign(boolean value) { return this; }
    public Memory assign(String value){ return this; }

    // ASSIGN REF
    public Memory assignRef(Memory memory){ return this; }
    public Memory assignRef(long value){ return this; }
    public Memory assignRef(double value){ return this; }
    public Memory assignRef(boolean value){ return this; }
    public Memory assignRef(String value){ return this; }

    public Memory toImmutable(){
        return this;
    }

    public boolean isImmutable(){
        return true;
    }


    /********** RIGHT ******************/
    public Memory minusRight(long value){ return new LongMemory(value - toLong()); }
    public Memory minusRight(double value){ return new DoubleMemory(value - toDouble()); }
    public Memory minusRight(boolean value){ return new LongMemory((value ? 1 : 0) - toLong()); }
    public Memory minusRight(String value){ return StringMemory.toNumeric(value).minus(this); }

    public Memory divRight(long value){ return new DoubleMemory(value / toDouble()); }
    public Memory divRight(double value){ return new DoubleMemory(value / toDouble()); }
    public Memory divRight(boolean value){ return new DoubleMemory((value ? 1 : 0) / toDouble()); }
    public Memory divRight(String value){ return StringMemory.toNumeric(value).div(this); }

    public Memory modRight(long value){ return new LongMemory(value % toLong()); }
    public Memory modRight(double value){ return new DoubleMemory(value % toDouble()); }
    public Memory modRight(boolean value){ return new LongMemory((value ? 1 : 0) % toLong()); }
    public Memory modRight(String value){ return StringMemory.toNumeric(value).mod(this); }

    public Memory concatRight(long value) { return new StringMemory(value + toString()); }
    public Memory concatRight(double value) { return new StringMemory(value + toString()); }
    public Memory concatRight(boolean value) { return new StringMemory(boolToString(value) + toString()); }
    public Memory concatRight(String value) { return new StringMemory(value + toString()); }

    public String concatScalarRight(long value) { return value + toString(); }
    public String concatScalarRight(double value) { return value + toString(); }
    public String concatScalarRight(boolean value) { return boolToString(value) + toString(); }
    public String concatScalarRight(String value) { return value + toString(); }

    /****************************************************************/
    /** Static *****/

    public static Memory minusRight(long value, Memory memory){ return memory.minusRight(value); }
    public static Memory minusRight(double value, Memory memory){ return memory.minusRight(value); }
    public static Memory minusRight(boolean value, Memory memory){ return memory.minusRight(value); }
    public static Memory minusRight(String value, Memory memory){ return memory.minusRight(value); }

    public static Memory divRight(long value, Memory memory){ return memory.divRight(value); }
    public static Memory divRight(double value, Memory memory){ return memory.divRight(value); }
    public static Memory divRight(boolean value, Memory memory){ return memory.divRight(value); }
    public static Memory divRight(String value, Memory memory){ return memory.divRight(value); }

    public static Memory modRight(long value, Memory memory){ return memory.modRight(value); }
    public static Memory modRight(double value, Memory memory){ return memory.modRight(value); }
    public static Memory modRight(boolean value, Memory memory){ return memory.modRight(value); }
    public static Memory modRight(String value, Memory memory){ return memory.modRight(value); }

    public static Memory concatRight(long value, Memory memory){ return memory.concatRight(value); }
    public static Memory concatRight(double value, Memory memory){ return memory.concatRight(value); }
    public static Memory concatRight(boolean value, Memory memory){ return memory.concatRight(value); }
    public static Memory concatRight(String value, Memory memory){ return memory.concatRight(value); }

    ////

    public static String boolToString(boolean value){
        return value ? "-1" : "";
    }
}
