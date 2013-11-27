package ru.regenix.jphp.runtime;

import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class OperatorUtils {

    public static boolean toBoolean(double value){
        return value != 0.0;
    }

    public static boolean toBoolean(long value){
        return value != 0;
    }

    public static boolean toBoolean(String value){
        return value != null && !value.equals("0") && !value.isEmpty();
    }

    // PLUS
    public static Memory plus(long o1, Memory value) {
        switch (value.type){
            case INT: return LongMemory.valueOf(o1 + value.toLong());
            case DOUBLE: return DoubleMemory.valueOf(o1 + value.toDouble());
        }
        return plus(o1, value.toNumeric());
    }
    public static Memory plus(double o1, Memory value) { return DoubleMemory.valueOf(o1 + value.toDouble()); }
    public static Memory plus(boolean o1, Memory value) {
        return o1 ? Memory.TRUE.plus(value) : Memory.FALSE.plus(value);
    }

    // MINUS
    public static Memory minus(long o1, Memory value) {
        switch (value.type){
            case INT: return LongMemory.valueOf(o1 - value.toLong());
            case DOUBLE: return DoubleMemory.valueOf(o1 - value.toDouble());
        }
        return minus(o1, value.toNumeric());
    }
    public static Memory minus(double o1, Memory value) { return DoubleMemory.valueOf(o1 - value.toDouble()); }
    public static Memory minus(boolean o1, Memory value) {
        return o1 ? Memory.TRUE.minus(value) : Memory.FALSE.minus(value);
    }

    // MUL
    public static Memory mul(long o1, Memory value) {
        switch (value.type){
            case INT: return LongMemory.valueOf(o1 * value.toLong());
            case DOUBLE: return DoubleMemory.valueOf(o1 * value.toDouble());
        }
        return mul(o1, value.toNumeric());
    }
    public static Memory mul(double o1, Memory value) { return DoubleMemory.valueOf(o1 * value.toDouble()); }
    public static Memory mul(boolean o1, Memory value) {
        return o1 ? Memory.TRUE.mul(value) : Memory.FALSE.mul(value);
    }

    // MOD
    public static Memory mod(long o1, Memory value) { return LongMemory.valueOf(o1 % value.toLong()); }
    public static Memory mod(double o1, Memory value) { return LongMemory.valueOf((long)o1 % value.toLong()); }
    public static Memory mod(boolean o1, Memory value) {
        return o1 ? Memory.TRUE.mod(value) : Memory.FALSE.mod(value);
    }
}
