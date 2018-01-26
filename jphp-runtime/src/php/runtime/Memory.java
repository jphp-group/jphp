package php.runtime;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.DynamicMethodInvoker;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.StdClass;
import php.runtime.lang.exception.BaseArithmeticError;
import php.runtime.lang.exception.BaseDivisionByZeroError;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.*;
import php.runtime.memory.helper.UndefinedMemory;
import php.runtime.memory.helper.VariadicMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.support.ReflectionUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

abstract public class Memory implements Comparable<Memory> {

    public enum Type {
        NULL(null),
        BOOL(Boolean.TYPE),
        INT(Long.TYPE),
        DOUBLE(Double.TYPE),
        STRING(String.class),
        ARRAY(ArrayMemory.class),
        OBJECT(ObjectMemory.class),
        REFERENCE(Memory.class),
        KEY_VALUE(KeyValueMemory.class);

        private final Class<?> clazz;

        Type(Class<?> aClass) {
            clazz = aClass;
        }

        public Class toClass(){
            return clazz;
        }

        private final static Map<Class<?>, Type> map;

        static {
            map = new HashMap<>();

            for (Type type : Type.values()) {
                map.put(type.clazz, type);
            }
        }

        public static Type valueOf(Class clazz){
            Type type = map.get(clazz);

            if (type == null) {
                return REFERENCE;
            } else {
                return type;
            }
        }

        @Override
        public String toString(){
            switch (this){
                case ARRAY: return "array";
                case BOOL: return "boolean";
                case DOUBLE: return "float";
                case INT: return "integer";
                case NULL: return "NULL";
                case OBJECT: return "object";
                case STRING: return "string";
                default:
                    return "unknown";
            }
        }

        protected final static Map<String, Type> TYPE_MAP = new HashMap<String, Type>(){{
            put("array", ARRAY);
            put("bool", BOOL); put("boolean", BOOL);
            put("double", DOUBLE); put("float", DOUBLE);
            put("int", INT); put("integer", INT); put("long", INT);
            put("null", NULL);
            put("string", STRING);
            put("object", OBJECT);
        }};

        public static Type of(String code) {
            return TYPE_MAP.get(code.toLowerCase());
        }
    }

    public final Type type;

    protected Memory(Type type) {
        this.type = type;
    }

    public static final Memory NULL = NullMemory.INSTANCE;
    public static final Memory UNDEFINED = UndefinedMemory.INSTANCE;
    public static final Memory FALSE = FalseMemory.INSTANCE;
    public static final Memory TRUE = TrueMemory.INSTANCE;

    public static final Memory CONST_INT_0 = new LongMemory(0);
    public static final Memory CONST_INT_M1 = new LongMemory(-1);
    public static final Memory CONST_INT_1 = new LongMemory(1);
    public static final Memory CONST_INT_2 = new LongMemory(2);
    public static final Memory CONST_INT_3 = new LongMemory(3);
    public static final Memory CONST_INT_4 = new LongMemory(4);
    public static final Memory CONST_INT_5 = new LongMemory(5);

    public static final Memory CONST_DOUBLE_0 = new DoubleMemory(0.0);
    public static final Memory CONST_DOUBLE_1 = new DoubleMemory(1.0);
    public static final Memory CONST_DOUBLE_NAN = new DoubleMemory(Double.NaN);

    public static final Memory CONST_EMPTY_STRING = new StringMemory("");

    public boolean isNull(){
        return type == Type.NULL;
    }

    public boolean isNotNull() {
        return !isNull();
    }

    public boolean isUndefined(){
        return toValue() == UNDEFINED;
    }

    public boolean isShortcut(){
        return false;
    }

    abstract public long toLong();
    public int toInteger(){ return (int)toLong(); }

    abstract public double toDouble();
    abstract public boolean toBoolean();
    abstract public Memory toNumeric();
    abstract public String toString();
    public Memory toUnset() { return NULL; }

    public String toBinaryString(){
        return toString();
    }

    public Memory toBinary() {
        return new BinaryMemory(toString());
    }

    public float toFloat() { return (float) toDouble(); }

    public Memory toArray() {
        ArrayMemory result = new ArrayMemory();
        result.add(toImmutable());
        return result.toConstant();
    }

    public Memory toObject(Environment env) {
        StdClass stdClass = new StdClass(env);
        stdClass.getProperties().refOfIndex("scalar").assign(toImmutable());
        return new ObjectMemory(stdClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends IObject> T toObject(Class<T> clazz) {
        try {
            return clazz.cast( toValue(ObjectMemory.class).value );
        } catch (ClassCastException e) {
            if (!(this instanceof ObjectMemory)) {
                throw new ClassCastException(
                        "Cannot convert '" + toString() + "' to an instance of " + ReflectionUtils.getClassName(clazz) + " class"
                );
            } else {
                throw new ClassCastException(
                        "Cannot convert instance of " + toValue(ObjectMemory.class).getReflection().getName() + " class"
                        + " to an instance of " + ReflectionUtils.getClassName(clazz)
                );
            }
        }
    }

    public <T extends Enum> T toEnum(Class<T> clazz) {
        return (T) Enum.valueOf(clazz, toString());
    }

    public Invoker toInvoker(Environment env) {
        Invoker invoker = Invoker.valueOf(env, null, this);

        if (invoker != null) {
            invoker.setTrace(env.trace());
            invoker.setMemory(this);
            return invoker;
        }

        return null;
    }

    public Memory clone(Environment env, TraceInfo trace) throws Throwable {
        env.error(trace, "__clone method called on non-object");
        return NULL;
    }

    public Type getRealType(){
        return type;
    }

    public char toChar(){
        switch (type){
            case STRING:
                String tmp = toString();
                if (tmp.isEmpty())
                    return '\0';
                else
                    return tmp.charAt(0);
            default:
                return (char)toLong();
        }
    }

    public int getPointer(boolean absolute){
        return super.hashCode();
    }

    public int getPointer(){
        return super.hashCode();
    }

    public Memory newKeyValue(Memory memory){ return new KeyValueMemory(this.toValue(), memory); }
    public Memory newKeyValue(long memory){ return new KeyValueMemory(this.toValue(), LongMemory.valueOf(memory)); }
    public Memory newKeyValue(double memory){ return new KeyValueMemory(this.toValue(), new DoubleMemory(memory)); }
    public Memory newKeyValue(boolean memory){ return new KeyValueMemory(this.toValue(), memory ? TRUE : FALSE); }
    public Memory newKeyValue(String memory){ return new KeyValueMemory(this.toValue(), new StringMemory(memory)); }

    public Memory newKeyValueRight(Memory memory){ return new KeyValueMemory(memory, this.toValue()); }
    public Memory newKeyValueRight(long memory){ return new KeyValueMemory(LongMemory.valueOf(memory), this.toValue()); }
    public Memory newKeyValueRight(double memory){ return new KeyValueMemory(new DoubleMemory(memory), this.toValue()); }
    public Memory newKeyValueRight(boolean memory){ return new KeyValueMemory(memory ? TRUE : FALSE, this.toValue()); }
    public Memory newKeyValueRight(String memory){ return new KeyValueMemory(new StringMemory(memory), this.toValue()); }

    public boolean isObject() { return type == Type.OBJECT; }
    public boolean isClosure() { return false; }
    public boolean isResource() { return false; }
    public boolean isArray(){ return type == Type.ARRAY; }
    public boolean isTraversable() { return isArray() || instanceOf("Traversable", "traversable"); }
    public boolean isString() { return type == Type.STRING; }
    public boolean isNumber() { return type == Type.INT || type == Type.DOUBLE; }
    public boolean isReference() { return false; }
    // <value>[index]

    final public Memory valueOfIndex(Memory index) { return valueOfIndex(null, index); }
    public Memory valueOfIndex(TraceInfo trace, Memory index) { return NULL; }

    public Memory valueOfIndex(TraceInfo trace, long index) { return NULL; }
    final public Memory valueOfIndex(long index) { return valueOfIndex(null, index); }

    public Memory valueOfIndex(TraceInfo trace, double index) { return NULL; }
    final public Memory valueOfIndex(double index) { return valueOfIndex(null, index); }

    public Memory valueOfIndex(TraceInfo trace, String index) { return NULL; }
    final public Memory valueOfIndex(String index) { return valueOfIndex(null, index); }

    public Memory valueOfIndex(TraceInfo trace, boolean index) { return NULL; }
    final public Memory valueOfIndex(boolean index) { return valueOfIndex(null, index); }

    final public Memory refOfIndex(Memory index){
        return refOfIndex(null, index);
    }

    public Memory refOfIndex(TraceInfo trace, Memory index) {
        return NULL;
    }
    public Memory refOfIndexAsShortcut(TraceInfo trace, Memory index) { return refOfIndex(trace, index); }

    public Memory refOfIndex(TraceInfo trace, long index) { return NULL; }
    final public Memory refOfIndex(long index) { return refOfIndex(null, index); }

    public Memory refOfIndex(TraceInfo trace, double index) { return NULL; }
    final public Memory refOfIndex(double index) { return refOfIndex(null, index); }

    public Memory refOfIndex(TraceInfo trace, String index) { return NULL; }
    final public Memory refOfIndex(String index) { return refOfIndex(null, index); }

    public Memory refOfIndex(TraceInfo trace, boolean index) { return NULL; }
    final public Memory refOfIndex(boolean index) { return refOfIndex(null, index); }

    public Memory refOfPush(TraceInfo trace) { return new ReferenceMemory(); }
    final public Memory refOfPush() { return refOfPush(null); }
    public void unsetOfIndex(TraceInfo trace, Memory index) { }
    public Memory issetOfIndex(TraceInfo trace, Memory index) { return NULL; }
    public Memory emptyOfIndex(TraceInfo trace, Memory index) { return issetOfIndex(trace, index); }

    // INC DEC
    abstract public Memory inc();
    abstract public Memory dec();

    // NEGATIVE
    abstract public Memory negative();

    // CONCAT
    public String concat(Memory memory){  return toString() + memory.toString(); }
    public String concat(long value) { return toString() + value; }
    public String concat(double value) { return toString() + new DoubleMemory(value).toString(); }
    public String concat(boolean value) { return toString() + boolToString(value); }
    public String concat(String value) { return toString() + value; }

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
    public Memory mul(boolean value){ return LongMemory.valueOf(toLong() * (value ? 1 : 0));}
    public Memory mul(String value){ return mul(StringMemory.toNumeric(value)); }

    // POW
    abstract public Memory pow(Memory memory);
    public Memory pow(long value) {
        Memory real = toNumeric();
        if (real instanceof LongMemory) {
            double result = Math.pow(real.toLong(), value);
            if (result > Long.MAX_VALUE) {
                return new DoubleMemory(result);
            }
            return new LongMemory((long) result);
        }
        return new DoubleMemory(Math.pow(real.toDouble(), value));
    }
    public Memory pow(double value) { return new DoubleMemory(Math.pow(toDouble(), value)); }
    public Memory pow(boolean value) {
        Memory real = toNumeric();
        if (real instanceof LongMemory) {
            return value ? real.toImmutable() : Memory.CONST_INT_1;
        }
        return value ? real.toImmutable() : Memory.CONST_DOUBLE_1;
    }
    public Memory pow(String value) { return pow(StringMemory.toNumeric(value)); }

    // DIV
    abstract public Memory div(Memory memory);
    public Memory div(long value){ if(value==0) return FALSE; return new DoubleMemory(toDouble() / value); }
    public Memory div(double value){ if(value==0.0) return FALSE; return new DoubleMemory(toDouble() / value); }
    public Memory div(boolean value){ if(!value) return FALSE; return LongMemory.valueOf(toLong()); }
    public Memory div(String value){ return div(StringMemory.toNumeric(value)); }

    // MOD
    protected static Memory _divByZero() {
        return UNDEFINED;
    }

    public Memory modCheckResult(Environment env, TraceInfo trace) {
        if (this == UNDEFINED) {
            env.exception(trace, BaseDivisionByZeroError.class, "Modulo by zero");
        }

        return this;
    }

    public Memory mod(Memory memory) {
        long t = memory.toLong();
        if (t == 0)
            return _divByZero();

        return LongMemory.valueOf(toLong() % t);
    }

    public Memory mod(long value){ if (value==0) return _divByZero(); return LongMemory.valueOf(toLong() % value); }
    public Memory mod(double value){ return mod((long) value); }
    public Memory mod(boolean value){ if (!value) return _divByZero(); return LongMemory.valueOf(toLong() % 1); }
    public Memory mod(String value){ return mod(StringMemory.toNumeric(value, true, CONST_INT_0)); }

    // NOT
    public boolean not(){ return !toBoolean(); }

    private static boolean _xor(boolean... args) {
        boolean r = false;
        for (boolean b : args) {
            r = r ^ b;
        }
        return r;
    }

    public boolean xor(Memory value) { return _xor(toBoolean(), value.toBoolean()); }
    public boolean xor(long value) { return _xor(toBoolean(), value != 0); }
    public boolean xor(double value) { return _xor(toBoolean(), OperatorUtils.toBoolean(value)); }
    public boolean xor(boolean value) { return _xor(toBoolean(), value); }
    public boolean xor(String value) { return _xor(toBoolean(), OperatorUtils.toBoolean(value)); }

    // SPACESHIP
    public long spaceshipCompare(Memory value) { return equal(value) ? 0 : greater(value) ? 1 : -1; }
    public long spaceshipCompare(long value) { return equal(value) ? 0 : greater(value) ? 1 : -1; }
    public long spaceshipCompare(double value) { return equal(value) ? 0 : greater(value) ? 1 : -1; }
    public long spaceshipCompare(boolean value) { return equal(value) ? 0 : greater(value) ? 1 : -1; }
    public long spaceshipCompare(String value) { return equal(value) ? 0 : greater(value) ? 1 : -1; }

    // EQUAL
    abstract public boolean equal(Memory memory);
    public boolean equal(long value){ return toLong() == value; }
    public boolean equal(double value) { return DoubleMemory.almostEqual(toDouble(), value); }
    public boolean equal(boolean value) { return toBoolean() == value; }
    public boolean equal(String value) { return equal(StringMemory.toNumeric(value)); }

    // IDENTICAL
    abstract public boolean identical(Memory memory);
    public boolean identical(long value) { return type == Type.INT && toLong() == value; }
    public boolean identical(double value) { return type == Type.DOUBLE && DoubleMemory.almostEqual(toDouble(), value); }
    public boolean identical(boolean value) { return type == Type.BOOL && value ? toImmutable() == TRUE : toImmutable() == FALSE; }
    public boolean identical(String value) { return type == Type.STRING && toString().equals(value); }

    // NOT EQUAL
    abstract public boolean notEqual(Memory memory);
    public boolean notEqual(long value){ return toLong() != value; }
    public boolean notEqual(double value) { return toDouble() != value; }
    public boolean notEqual(boolean value) { return toBoolean() != value; }
    public boolean notEqual(String value) { return !toString().equals(value); }

    // NOT IDENTICAL
    public boolean notIdentical(Memory memory) { return !identical(memory); }
    public boolean notIdentical(long memory) { return !identical(memory); }
    public boolean notIdentical(double memory) { return !identical(memory); }
    public boolean notIdentical(boolean memory) { return !identical(memory); }
    public boolean notIdentical(String memory) { return !identical(memory); }

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


    // BIT &
    public Memory bitAnd(Memory memory) { return LongMemory.valueOf(toLong() & memory.toLong()); }
    public Memory bitAnd(long memory) { return LongMemory.valueOf( toLong() & memory ); }
    public Memory bitAnd(double memory) { return LongMemory.valueOf( toLong() & (long)memory ); }
    public Memory bitAnd(boolean memory) { return LongMemory.valueOf( toLong() & (memory ? 1 : 0) ); }
    public Memory bitAnd(String memory) { return LongMemory.valueOf( toLong() & StringMemory.toNumeric(memory).toLong() ); }

    // BIT |
    public Memory bitOr(Memory memory) { return LongMemory.valueOf( toLong() | memory.toLong() ); }
    public Memory bitOr(long memory) { return LongMemory.valueOf( toLong() | memory ); }
    public Memory bitOr(double memory) { return LongMemory.valueOf( toLong() | (long)memory ); }
    public Memory bitOr(boolean memory) { return LongMemory.valueOf( toLong() | (memory ? 1 : 0) ); }
    public Memory bitOr(String memory) { return LongMemory.valueOf( toLong() | StringMemory.toNumeric(memory).toLong() ); }

    // BIT XOR ^
    public Memory bitXor(Memory memory) { return LongMemory.valueOf( toLong() ^ memory.toLong() ); }
    public Memory bitXor(long memory) { return LongMemory.valueOf( toLong() ^ memory ); }
    public Memory bitXor(double memory) { return LongMemory.valueOf( toLong() ^ (long)memory ); }
    public Memory bitXor(boolean memory) { return LongMemory.valueOf( toLong() ^ (memory ? 1 : 0) ); }
    public Memory bitXor(String memory) { return LongMemory.valueOf( toLong() ^ StringMemory.toNumeric(memory).toLong() ); }

    // BIT not ~
    public Memory bitNot(){ return LongMemory.valueOf(~toLong()); }

    protected static Memory _negativeShift() {
        return UNDEFINED;
    }

    public Memory bitwiseCheckResult(Environment env, TraceInfo trace) {
        if (this == UNDEFINED) {
            env.exception(trace, BaseArithmeticError.class, "Bit shift by negative number");
        }

        return this;
    }

    // SHR >>
    public Memory bitShr(Memory memory) {
        long l = memory.toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf( toLong() >> l); }
    public Memory bitShr(long memory) { return memory < 0 ? _negativeShift() : LongMemory.valueOf( toLong() >> memory ); }
    public Memory bitShr(double memory) { return memory < 0 ? _negativeShift() : LongMemory.valueOf( toLong() >> (long)memory ); }
    public Memory bitShr(boolean memory) { return LongMemory.valueOf( toLong() >> (memory ? 1 : 0) ); }
    public Memory bitShr(String memory) {
        long l = StringMemory.toNumeric(memory).toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf( toLong() >> l);
    }

    // SHL <<
    public Memory bitShl(Memory memory) {
        long l = memory.toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf( toLong() << l);
    }

    public Memory bitShl(long memory) { return memory < 0 ? _negativeShift() : LongMemory.valueOf( toLong() << memory ); }
    public Memory bitShl(double memory) { return memory < 0 ? _negativeShift() : LongMemory.valueOf( toLong() << (long)memory ); }
    public Memory bitShl(boolean memory) { return LongMemory.valueOf( toLong() << (memory ? 1 : 0) ); }
    public Memory bitShl(String memory) {
        long l = StringMemory.toNumeric(memory).toLong();
        return l < 0 ? FALSE : LongMemory.valueOf( toLong() << l);
    }


    // ASSIGN
    public Memory assign(Memory memory){ throw new RuntimeException("Invalid assign `memory` to " + type); }
    public Memory assign(long value){ throw new RuntimeException("Invalid assign `long` to " + type); }
    public Memory assign(double value) { throw new RuntimeException("Invalid assign `double` to " + type); }
    public Memory assign(boolean value) { throw new RuntimeException("Invalid assign `bool` to " + type); }
    public Memory assign(String value){ throw new RuntimeException("Invalid assign `string` to " + type); }
    public Memory assignRef(Memory memory){ throw new RuntimeException("Invalid assignRef `memory` to " + type); }

    public Memory assignRight(Memory memory) { return memory.assign(this); }
    public Memory assignRefRight(Memory memory) { return memory.assignRef(this); }

    public Memory assign(IObject object) { return this.assign(new ObjectMemory(object)); }

    public Memory assignConcat(Memory memory) { return assign(concat(memory)); }
    public Memory assignConcat(long memory) { return assign(concat(memory)); }
    public Memory assignConcat(double memory) { return assign(concat(memory)); }
    public Memory assignConcat(boolean memory) { return assign(concat(memory)); }
    public Memory assignConcat(String memory) { return assign(concat(memory)); }
    public Memory assignConcatRight(Memory memory) { return memory.assign(memory.concat(this)); }

    public Memory assignPlus(Memory memory) { return assign(plus(memory)); }
    public Memory assignPlus(long memory) { return assign(plus(memory)); }
    public Memory assignPlus(double memory) { return assign(plus(memory)); }
    public Memory assignPlus(boolean memory) { return assign(plus(memory)); }
    public Memory assignPlus(String memory) { return assign(plus(memory)); }
    public Memory assignPlusRight(Memory memory) { return memory.assign(memory.plus(this)); }

    public Memory assignMinus(Memory memory) { return assign(minus(memory)); }
    public Memory assignMinus(long memory) { return assign(minus(memory)); }
    public Memory assignMinus(double memory) { return assign(minus(memory)); }
    public Memory assignMinus(boolean memory) { return assign(minus(memory)); }
    public Memory assignMinus(String memory) { return assign(minus(memory)); }
    public Memory assignMinusRight(Memory memory) { return memory.assign(memory.minus(this)); }

    public Memory assignMul(Memory memory) { return assign(mul(memory)); }
    public Memory assignMul(long memory) { return assign(mul(memory)); }
    public Memory assignMul(double memory) { return assign(mul(memory)); }
    public Memory assignMul(boolean memory) { return assign(mul(memory)); }
    public Memory assignMul(String memory) { return assign(mul(memory)); }
    public Memory assignMulRight(Memory memory) { return memory.assign(memory.mul(this)); }

    public Memory assignPow(Memory memory) { return assign(pow(memory)); }
    public Memory assignPow(long memory) { return assign(pow(memory)); }
    public Memory assignPow(double memory) { return assign(pow(memory)); }
    public Memory assignPow(boolean memory) { return assign(pow(memory)); }
    public Memory assignPow(String memory) { return assign(pow(memory)); }
    public Memory assignPowRight(Memory memory) { return memory.assign(memory.pow(this)); }

    public Memory assignDiv(Memory memory) { return assign(div(memory)); }
    public Memory assignDiv(long memory) { return assign(div(memory)); }
    public Memory assignDiv(double memory) { return assign(div(memory)); }
    public Memory assignDiv(boolean memory) { return assign(div(memory)); }
    public Memory assignDiv(String memory) { return assign(div(memory)); }
    public Memory assignDivRight(Memory memory) { return memory.assign(memory.div(this)); }

    public Memory assignMod(Memory memory) { return assign(mod(memory)); }
    public Memory assignMod(long memory) { return assign(mod(memory)); }
    public Memory assignMod(double memory) { return assign(mod(memory)); }
    public Memory assignMod(boolean memory) { return assign(mod(memory)); }
    public Memory assignMod(String memory) { return assign(mod(memory)); }
    public Memory assignModRight(Memory memory) { return memory.assign(memory.mod(this)); }

    public Memory assignBitShr(Memory memory) { return assign(bitShr(memory)); }
    public Memory assignBitShr(long memory) { return assign(bitShr(memory)); }
    public Memory assignBitShr(double memory) { return assign(bitShr(memory)); }
    public Memory assignBitShr(boolean memory) { return assign(bitShr(memory)); }
    public Memory assignBitShr(String memory) { return assign(bitShr(memory)); }
    public Memory assignBitShrRight(Memory memory) { return memory.assign(memory.bitShr(this)); }

    public Memory assignBitShl(Memory memory) { return assign(bitShl(memory)); }
    public Memory assignBitShl(long memory) { return assign(bitShl(memory)); }
    public Memory assignBitShl(double memory) { return assign(bitShl(memory)); }
    public Memory assignBitShl(boolean memory) { return assign(bitShl(memory)); }
    public Memory assignBitShl(String memory) { return assign(bitShl(memory)); }
    public Memory assignBitShlRight(Memory memory) { return memory.assign(memory.bitShl(this)); }

    public Memory assignBitAnd(Memory memory) { return assign(bitAnd(memory)); }
    public Memory assignBitAnd(long memory) { return assign(bitAnd(memory)); }
    public Memory assignBitAnd(double memory) { return assign(bitAnd(memory)); }
    public Memory assignBitAnd(boolean memory) { return assign(bitAnd(memory)); }
    public Memory assignBitAnd(String memory) { return assign(bitAnd(memory)); }
    public Memory assignBitAndRight(Memory memory) { return memory.assign(memory.bitAnd(this)); }

    public Memory assignBitOr(Memory memory) { return assign(bitOr(memory)); }
    public Memory assignBitOr(long memory) { return assign(bitOr(memory)); }
    public Memory assignBitOr(double memory) { return assign(bitOr(memory)); }
    public Memory assignBitOr(boolean memory) { return assign(bitOr(memory)); }
    public Memory assignBitOr(String memory) { return assign(bitOr(memory)); }
    public Memory assignBitOrRight(Memory memory) { return memory.assign(memory.bitOr(this)); }

    public Memory assignBitXor(Memory memory) { return assign(bitXor(memory)); }
    public Memory assignBitXor(long memory) { return assign(bitXor(memory)); }
    public Memory assignBitXor(double memory) { return assign(bitXor(memory)); }
    public Memory assignBitXor(boolean memory) { return assign(bitXor(memory)); }
    public Memory assignBitXor(String memory) { return assign(bitXor(memory)); }
    public Memory assignBitXorRight(Memory memory) { return memory.assign(memory.bitXor(this)); }

    public void unset(){  }
    public void manualUnset(Environment env) { }

    public Memory toImmutable(){
        return this;
    }

    public Memory toImmutable(Environment env, TraceInfo trace){
        return toImmutable();
    }

    @SuppressWarnings("unchecked")
    public <T extends Memory> T toValue(Class<T> clazz){
        return (T) this;
    }

    public Memory toValue(){
        return this;
    }

    public boolean isImmutable(){
        return true;
    }

    /********** RIGHT ******************/
    // SPACESHIP
    public long spaceshipCompareRight(Memory value) { return equal(value) ? 0 : greater(value) ? -1 : 1; }
    public long spaceshipCompareRight(long value) { return equal(value) ? 0 : greater(value) ? -1 : 1; }
    public long spaceshipCompareRight(double value) { return equal(value) ? 0 : greater(value) ? -1 : 1; }
    public long spaceshipCompareRight(boolean value) { return equal(value) ? 0 : greater(value) ? -1 : 1; }
    public long spaceshipCompareRight(String value) { return equal(value) ? 0 : greater(value) ? -1 : 1; }

    public Memory minusRight(Memory value){ return value.minus(this); }
    public Memory minusRight(long value){ return LongMemory.valueOf(value).minus(this); }
    public Memory minusRight(double value){ return new DoubleMemory(value).minus(this); }
    public Memory minusRight(boolean value){ return LongMemory.valueOf((value ? 1 : 0)).minus(this); }
    public Memory minusRight(String value){ return StringMemory.toNumeric(value).minus(this); }

    public Memory divRight(Memory value){ return value.div(this); }
    public Memory divRight(long value){ return LongMemory.valueOf(value).div(this); }
    public Memory divRight(double value){ return new DoubleMemory(value).div(this); }
    public Memory divRight(boolean value){ if(!value) return CONST_INT_0; else return TRUE.div(this); }
    public Memory divRight(String value){ return StringMemory.toNumeric(value).div(this); }

    public Memory modRight(Memory value){ return value.mod(this); }
    public Memory modRight(long value){ return LongMemory.valueOf(value).mod(this); }
    public Memory modRight(double value){ return new DoubleMemory(value).mod(this); }
    public Memory modRight(boolean value){ return LongMemory.valueOf((value ? 1 : 0)).mod(this); }
    public Memory modRight(String value){ return StringMemory.toNumeric(value).mod(this); }

    public Memory powRight(Memory value){ return value.pow(this); }
    public Memory powRight(long value){ return LongMemory.valueOf(value).pow(this); }
    public Memory powRight(double value){ return new DoubleMemory(value).pow(this); }
    public Memory powRight(boolean value){ return LongMemory.valueOf((value ? 1 : 0)).pow(this); }
    public Memory powRight(String value){ return StringMemory.toNumeric(value).pow(this); }

    public String concatRight(Memory value) { return value.concat(this); }
    public String concatRight(long value) { return value + toString(); }
    public String concatRight(double value) { return value + toString(); }
    public String concatRight(boolean value) { return boolToString(value) + toString(); }
    public String concatRight(String value) { return value + toString(); }

    public boolean smallerRight(Memory value) { return value.smaller(this); }
    public boolean smallerRight(long value) { return this.greaterEq(value); }
    public boolean smallerRight(double value) { return this.greaterEq(value); }
    public boolean smallerRight(boolean value) { return this.greaterEq(value); }
    public boolean smallerRight(String value) { return this.greaterEq(value); }

    public boolean smallerEqRight(Memory value) { return value.smallerEq(this); }
    public boolean smallerEqRight(long value) { return this.greater(value); }
    public boolean smallerEqRight(double value) { return this.greater(value); }
    public boolean smallerEqRight(boolean value) { return this.greater(value); }
    public boolean smallerEqRight(String value) { return this.greater(value); }

    public boolean greaterRight(Memory value) { return value.greater(this); }
    public boolean greaterRight(long value) { return this.smallerEq(value); }
    public boolean greaterRight(double value) { return this.smallerEq(value); }
    public boolean greaterRight(boolean value) { return this.smallerEq(value); }
    public boolean greaterRight(String value) { return this.smallerEq(value); }

    public boolean greaterEqRight(Memory value) { return value.greaterEq(this); }
    public boolean greaterEqRight(long value) { return this.smaller(value); }
    public boolean greaterEqRight(double value) { return this.smaller(value); }
    public boolean greaterEqRight(boolean value) { return this.smaller(value); }
    public boolean greaterEqRight(String value) { return this.smaller(value); }



    public Memory bitShrRight(Memory value){ return value.bitShr(this); }
    public Memory bitShrRight(long value){
        long l = toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf(value >> l);
    }
    public Memory bitShrRight(double value){
        long l = toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf((long)value >> l);
    }
    public Memory bitShrRight(boolean value){ return LongMemory.valueOf((value ? 1 : 0) >> toLong()); }
    public Memory bitShrRight(String value){ return StringMemory.toNumeric(value).bitShr(this); }


    public Memory bitShlRight(Memory value){ return value.bitShl(this); }
    public Memory bitShlRight(long value) {
        long l = toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf(value << l);
    }
    public Memory bitShlRight(double value){
        long l = toLong();
        return l < 0 ? _negativeShift() : LongMemory.valueOf((long)value << l);
    }
    public Memory bitShlRight(boolean value){ return LongMemory.valueOf((value ? 1 : 0) << toLong()); }
    public Memory bitShlRight(String value){ return StringMemory.toNumeric(value).bitShl(this); }

    public Memory unpack() { return new VariadicMemory(this); }

    /****************************************************************/
    /** Static *****/
    public static Memory assignRight(Memory value, Memory memory){ return memory.assign(value); }
    public static Memory assignRight(long value, Memory memory){ return memory.assign(value); }
    public static Memory assignRight(double value, Memory memory){ return memory.assign(value); }
    public static Memory assignRight(boolean value, Memory memory){ return memory.assign(value); }
    public static Memory assignRight(String value, Memory memory){ return memory.assign(value); }

    public static Memory assignRefRight(Memory value, Memory memory) { return memory.assignRef(value); }


    public static Memory assignConcatRight(Memory value, Memory memory){ return memory.assignConcat(value); }
    public static Memory assignConcatRight(long value, Memory memory){ return memory.assignConcat(value); }
    public static Memory assignConcatRight(double value, Memory memory){ return memory.assignConcat(value); }
    public static Memory assignConcatRight(boolean value, Memory memory){ return memory.assignConcat(value); }
    public static Memory assignConcatRight(String value, Memory memory){ return memory.assignConcat(value); }

    public static Memory assignPlusRight(Memory value, Memory memory){ return memory.assignPlus(value); }
    public static Memory assignPlusRight(long value, Memory memory){ return memory.assignPlus(value); }
    public static Memory assignPlusRight(double value, Memory memory){ return memory.assignPlus(value); }
    public static Memory assignPlusRight(boolean value, Memory memory){ return memory.assignPlus(value); }
    public static Memory assignPlusRight(String value, Memory memory){ return memory.assignPlus(value); }

    public static Memory assignMinusRight(Memory value, Memory memory){ return memory.assignMinus(value); }
    public static Memory assignMinusRight(long value, Memory memory){ return memory.assignMinus(value); }
    public static Memory assignMinusRight(double value, Memory memory){ return memory.assignMinus(value); }
    public static Memory assignMinusRight(boolean value, Memory memory){ return memory.assignMinus(value); }
    public static Memory assignMinusRight(String value, Memory memory){ return memory.assignMinus(value); }

    public static Memory assignMulRight(Memory value, Memory memory){ return memory.assignMul(value); }
    public static Memory assignMulRight(long value, Memory memory){ return memory.assignMul(value); }
    public static Memory assignMulRight(double value, Memory memory){ return memory.assignMul(value); }
    public static Memory assignMulRight(boolean value, Memory memory){ return memory.assignMul(value); }
    public static Memory assignMulRight(String value, Memory memory){ return memory.assignMul(value); }

    public static Memory assignPowRight(Memory value, Memory memory){ return memory.assignPow(value); }
    public static Memory assignPowRight(long value, Memory memory){ return memory.assignPow(value); }
    public static Memory assignPowRight(double value, Memory memory){ return memory.assignPow(value); }
    public static Memory assignPowRight(boolean value, Memory memory){ return memory.assignPow(value); }
    public static Memory assignPowRight(String value, Memory memory){ return memory.assignPow(value); }

    public static Memory assignDivRight(Memory value, Memory memory){ return memory.assignDiv(value); }
    public static Memory assignDivRight(long value, Memory memory){ return memory.assignDiv(value); }
    public static Memory assignDivRight(double value, Memory memory){ return memory.assignDiv(value); }
    public static Memory assignDivRight(boolean value, Memory memory){ return memory.assignDiv(value); }
    public static Memory assignDivRight(String value, Memory memory){ return memory.assignDiv(value); }

    public static Memory assignModRight(Memory value, Memory memory){ return memory.assignMod(value); }
    public static Memory assignModRight(long value, Memory memory){ return memory.assignMod(value); }
    public static Memory assignModRight(double value, Memory memory){ return memory.assignMod(value); }
    public static Memory assignModRight(boolean value, Memory memory){ return memory.assignMod(value); }
    public static Memory assignModRight(String value, Memory memory){ return memory.assignMod(value); }

    public static Memory assignBitShrRight(Memory value, Memory memory){ return memory.assignBitShr(value); }
    public static Memory assignBitShrRight(long value, Memory memory){ return memory.assignBitShr(value); }
    public static Memory assignBitShrRight(double value, Memory memory){ return memory.assignBitShr(value); }
    public static Memory assignBitShrRight(boolean value, Memory memory){ return memory.assignBitShr(value); }
    public static Memory assignBitShrRight(String value, Memory memory){ return memory.assignBitShr(value); }

    public static Memory assignBitShlRight(Memory value, Memory memory){ return memory.assignBitShl(value); }
    public static Memory assignBitShlRight(long value, Memory memory){ return memory.assignBitShl(value); }
    public static Memory assignBitShlRight(double value, Memory memory){ return memory.assignBitShl(value); }
    public static Memory assignBitShlRight(boolean value, Memory memory){ return memory.assignBitShl(value); }
    public static Memory assignBitShlRight(String value, Memory memory){ return memory.assignBitShl(value); }

    public static Memory assignBitAndRight(Memory value, Memory memory){ return memory.assignBitAnd(value); }
    public static Memory assignBitAndRight(long value, Memory memory){ return memory.assignBitAnd(value); }
    public static Memory assignBitAndRight(double value, Memory memory){ return memory.assignBitAnd(value); }
    public static Memory assignBitAndRight(boolean value, Memory memory){ return memory.assignBitAnd(value); }
    public static Memory assignBitAndRight(String value, Memory memory){ return memory.assignBitAnd(value); }

    public static Memory assignBitOrRight(Memory value, Memory memory){ return memory.assignBitOr(value); }
    public static Memory assignBitOrRight(long value, Memory memory){ return memory.assignBitOr(value); }
    public static Memory assignBitOrRight(double value, Memory memory){ return memory.assignBitOr(value); }
    public static Memory assignBitOrRight(boolean value, Memory memory){ return memory.assignBitOr(value); }
    public static Memory assignBitOrRight(String value, Memory memory){ return memory.assignBitOr(value); }

    public static Memory assignBitXorRight(Memory value, Memory memory){ return memory.assignBitXor(value); }
    public static Memory assignBitXorRight(long value, Memory memory){ return memory.assignBitXor(value); }
    public static Memory assignBitXorRight(double value, Memory memory){ return memory.assignBitXor(value); }
    public static Memory assignBitXorRight(boolean value, Memory memory){ return memory.assignBitXor(value); }
    public static Memory assignBitXorRight(String value, Memory memory){ return memory.assignBitXor(value); }
    ////

    public static Object unwrap(Environment env, Memory memory) {
        return unwrap(env, memory, false);
    }

    public static Object unwrap(Environment env, Memory memory, boolean arrayToMap) {
        if (memory.isObject()) {
            IObject iObject = memory.toValue(ObjectMemory.class).value;

            if (iObject instanceof BaseWrapper) {
                return ((BaseWrapper) iObject).getWrappedObject();
            } else {
                return iObject;
            }
        } else {
            switch (memory.getRealType()) {
                case BOOL:
                    return memory.toBoolean();
                case INT:
                    return memory.toLong();
                case DOUBLE:
                    return memory.toDouble();
                case STRING:
                    return memory.toString();
                case NULL:
                    return null;
                case ARRAY:
                    if (arrayToMap) {
                        return memory.toValue(ArrayMemory.class).toMapOrList(env);
                    } else {
                        return memory.toValue(ArrayMemory.class);
                    }
            }
            return memory;
        }
    }

    @SuppressWarnings("unchecked")
    public static Memory wrap(Environment env, Object o) {
        if (o == null) {
            return NULL;
        }

        MemoryOperation operation = MemoryOperation.get(o.getClass(), null);

        if (operation != null) {
            return operation.unconvertNoThow(env, env == null ? TraceInfo.UNKNOWN : env.trace(), o);
        } else {
            return NULL;
        }
    }

    public static String boolToString(boolean value){
        return value ? "1" : "";
    }

    abstract public byte[] getBinaryBytes(Charset charset);

    @Deprecated
    final public byte[] getBinaryBytes() {
        return getBinaryBytes(Charset.defaultCharset());
    }

    public ForeachIterator getNewIterator(Environment env,
                                          boolean getReferences, boolean getKeyReferences){ return null; }

    final public ForeachIterator getNewIterator(Environment env) {
        return getNewIterator(env, false, false);
    }

    public boolean instanceOf(String className, String lowerClassName){
        return false;
    }

    public boolean instanceOf(Class<? extends IObject> clazz){
        return instanceOf(ReflectionUtils.getClassName(clazz));
    }

    public boolean instanceOf(String name){
        return false;
    }

    @Override
    public int compareTo(Memory o) {
        if (greater(o))
            return 1;
        else if (smaller(o))
            return -1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Memory memory = (Memory) o;

        return equal(memory);
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}
