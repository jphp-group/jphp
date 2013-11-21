package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.Memory;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MathFunctions extends FunctionsContainer {

    private final static double[] COS_CACHE = new double[Short.MAX_VALUE * 5];
    private final static double[] SIN_CACHE = new double[Short.MAX_VALUE * 5];

    private final static int MAX_COS_NEG = COS_CACHE.length / 7;
    private final static int MAX_SIN_NEG = SIN_CACHE.length / 7;

    private final static int MAX_COS = COS_CACHE.length - MAX_COS_NEG;
    private final static int MAX_SIN = SIN_CACHE.length - MAX_SIN_NEG;

    static {
        for(int i = -MAX_COS_NEG; i < MAX_COS; i++){
            COS_CACHE[i + MAX_COS_NEG] = Math.cos(i);
        }

        for(int i = -MAX_SIN_NEG; i < MAX_SIN; i++){
            SIN_CACHE[i + MAX_SIN_NEG] = Math.cos(i);
        }
    }

    @Override
    protected Map<String, Method> getNativeFunctions() {
        return new HashMap<String, Method>(){{
            put("acos", getNative(Math.class, "acos", Double.TYPE));
            put("asin", getNative(Math.class, "asin", Double.TYPE));
            put("atan2", getNative(Math.class, "atan2", Double.TYPE, Double.TYPE));
            put("atan", getNative(Math.class, "atan", Double.TYPE));
            put("ceil", getNative(Math.class, "ceil", Double.TYPE));
            put("cosh", getNative(Math.class, "cosh", Double.TYPE));
            put("rad2deg", getNative(Math.class, "toDegrees", Double.TYPE));
            put("deg2rad", getNative(Math.class, "toRadians", Double.TYPE));
            put("exp", getNative(Math.class, "exp", Double.TYPE));
            put("expm1", getNative(Math.class, "expm1", Double.TYPE));
            put("floor", getNative(Math.class, "floor", Double.TYPE));
            put("hypot", getNative(Math.class, "hypot", Double.TYPE, Double.TYPE));
            put("is_infinite", getNative(Double.class, "isInfinite", Double.TYPE));
            put("is_nan", getNative(Double.class, "isNaN", Double.TYPE));
            put("log10", getNative(Math.class, "log10", Double.TYPE));
            put("log1p", getNative(Math.class, "log1p", Double.TYPE));
            put("log", getNative(Math.class, "log", Double.TYPE));
        }};
    }

    private static double _cos(long value){
        if (value >= -MAX_COS_NEG && value < MAX_COS)
            return COS_CACHE[(int)value + MAX_COS_NEG];
        else
            return Math.cos(value);
    }

    private static double _sin(long value){
        if (value >= -MAX_SIN_NEG && value < MAX_SIN)
            return SIN_CACHE[(int)value + MAX_SIN_NEG];
        else
            return Math.sin(value);
    }

    public static double cos(Memory memory){
        switch (memory.type){
            case DOUBLE: return Math.cos(memory.toDouble());
            case STRING: return cos(memory.toNumeric());
            default:
                return _cos(memory.toLong());
        }
    }

    public static double sin(Memory memory){
        switch (memory.type){
            case DOUBLE: return Math.sin(memory.toDouble());
            case STRING: return sin(memory.toNumeric());
            default:
                return _sin(memory.toLong());
        }
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
        try {
            return Long.parseLong(binary, 2);
        } catch (NumberFormatException e){
            return 0;
        }
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

    public static long getmaxrand(){
        return Long.MAX_VALUE;
    }

    public static long hexdec(String hex){
        try {
            return Long.parseLong(hex, 16);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public static boolean is_finite(double value){
        return !Double.isInfinite(value);
    }

    public static double lcg_value(){
        return Math.random();
    }

    public static Memory max(Memory value, Memory... args){
        if (value.isArray() && args == null){
            Memory max = null;
            for (Memory one : (ArrayMemory)value){
                if (max == null || one.greater(max))
                    max = one;
            }
            return max == null ? Memory.NULL : max.toImmutable();
        } else {
            Memory max = value;
            if (args != null)
                for(Memory one : args){
                    if (one.greater(max))
                        max = one;
                }
            return max.toImmutable();
        }
    }

    public static Memory min(Memory value, Memory... args){
        if (value.isArray() && args == null){
            Memory min = null;
            for (Memory one : (ArrayMemory)args[0]){
                if (min == null || one.smaller(min))
                    min = one;
            }
            return min == null ? Memory.NULL : min.toImmutable();
        } else {
            Memory min = value;
            for(Memory one : args){
                if (one.smaller(min))
                    min = one;
            }
            return min.toImmutable();
        }
    }
}
