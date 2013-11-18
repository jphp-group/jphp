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
            put("cos", getNative(Math.class, "cos", Double.TYPE));
            put("cosh", getNative(Math.class, "cosh", Double.TYPE));
            put("rad2deg", getNative(Math.class, "toDegrees", Double.TYPE));
            put("deg2rad", getNative(Math.class, "toRadians", Double.TYPE));
            put("exp", getNative(Math.class, "exp", Double.TYPE));
            put("expm1", getNative(Math.class, "expm1", Double.TYPE));
            put("floor", getNative(Math.class, "floor", Double.TYPE));
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

    public static long bindec(String binary){
        return Long.parseLong(binary, 2);
    }

    public static String decbin(long value){
        return Long.toString(value, 2);
    }

    public static String dechex(long value){
        return Long.toString(value, 16);
    }

    public static String decoct(long value){
        return Long.toString(value, 8);
    }

    public static double fmod(double x, double y){
        return x % y;
    }
}
