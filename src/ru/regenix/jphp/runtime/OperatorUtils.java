package ru.regenix.jphp.runtime;

import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;

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

    public static Memory plus(long o1, Memory value) {
        switch (value.type){
            case INT: return LongMemory.valueOf(o1 + value.toLong());
            case DOUBLE: return DoubleMemory.valueOf(o1 + value.toDouble());
            case REFERENCE: return plus(o1, ((ReferenceMemory)value).value);
            default: return plus(o1, value.toNumeric());
        }
    }

    public static Memory plus(double o1, Memory value) {
        return DoubleMemory.valueOf(o1 + value.toDouble());
    }
}
