package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.Memory;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MathFunctions extends FunctionsContainer {

    @Override
    protected Map<String, Method> getNativeFunctions() {
        return new HashMap<String, Method>(){{
            put("acos", getNative(Math.class, "acos", Double.TYPE));
            put("asin", getNative(Math.class, "asin", Double.TYPE));
            put("atan2", getNative(Math.class, "atan2", Double.TYPE, Double.TYPE));
            put("atan", getNative(Math.class, "atan", Double.TYPE));
            put("ceil", getNative(Math.class, "ceil", Double.TYPE));
        }};
    }

    public static Memory abs(Memory value) {
        switch (value.type){
            case DOUBLE: return new DoubleMemory(Math.abs(value.toDouble()));
            case STRING: return abs(value.toNumeric());
            default: return LongMemory.valueOf(Math.abs(value.toLong()));
        }
    }

    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x*x - 1.0));
    }

    public static double atanh(double x) {
        return 0.5 * Math.log( (x + 1.0) / (x - 1.0) );
    }

    public static String base_convert(String number, int fromBase, int toBase){
        try {
            return Long.toString(Long.valueOf(number, fromBase), toBase);
        } catch (NumberFormatException e){
            return new BigInteger(number, fromBase).toString( toBase);
        }
    }
}
