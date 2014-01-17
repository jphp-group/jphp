package php.runtime.ext.core;

import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import php.runtime.memory.*;
import php.runtime.Memory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MathFunctions extends FunctionsContainer {

    private final static MathConstants constants = new MathConstants();
    public static Random MERSENNE_TWISTER = new Random();
    public static Random RANDOM = new Random();

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
            put("sinh", getNative(Math.class, "sinh", Double.TYPE));
            put("tanh", getNative(Math.class, "tanh", Double.TYPE));
            put("tan", getNative(Math.class, "tan", Double.TYPE));
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
            put("sqrt", getNative(Math.class, "sqrt", Double.TYPE));
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

    @Runtime.Immutable
    public static double cos(Memory memory){
        switch (memory.type){
            case DOUBLE: return Math.cos(memory.toDouble());
            case STRING: return cos(memory.toNumeric());
            default:
                return _cos(memory.toLong());
        }
    }

    @Runtime.Immutable
    public static double sin(Memory memory){
        switch (memory.type){
            case DOUBLE: return Math.sin(memory.toDouble());
            case STRING: return sin(memory.toNumeric());
            default:
                return _sin(memory.toLong());
        }
    }

    @Runtime.Immutable
    public static Memory abs(Memory value) {
        switch (value.type){
            case DOUBLE: return new DoubleMemory(Math.abs(value.toDouble()));
            case STRING: return abs(value.toNumeric());
            default: return LongMemory.valueOf(Math.abs(value.toLong()));
        }
    }

    @Runtime.Immutable
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    @Runtime.Immutable
    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x*x - 1.0));
    }

    @Runtime.Immutable
    public static double atanh(double x) {
        return 0.5 * Math.log( (x + 1.0) / (x - 1.0) );
    }

    @Runtime.Immutable
    public static String base_convert(String number, int fromBase, int toBase){
        return new BigInteger(number, fromBase).toString( toBase);
    }

    @Runtime.Immutable
    public static long bindec(String binary){
        try {
            return Long.parseLong(binary, 2);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    @Runtime.Immutable
    public static String decbin(long value){
        return Long.toString(value, 2);
    }

    @Runtime.Immutable
    public static String dechex(long value){
        return Long.toString(value, 16);
    }

    @Runtime.Immutable
    public static String decoct(long value){
        return Long.toString(value, 8);
    }

    @Runtime.Immutable
    public static double fmod(double x, double y){
        return x % y;
    }

    @Runtime.Immutable
    public static long getmaxrand(){
        return Integer.MAX_VALUE;
    }

    @Runtime.Immutable
    public static long hexdec(String hex){
        try {
            return Long.parseLong(hex, 16);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    @Runtime.Immutable
    public static boolean is_finite(double value){
        return !Double.isInfinite(value);
    }

    public static double lcg_value(){
        return Math.random();
    }

    @Runtime.Immutable
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

    @Runtime.Immutable
    public static Memory min(Memory value, Memory... args){
        if (value.isArray() && args == null){
            Memory min = null;
            for (Memory one : (ArrayMemory)value){
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

    @Runtime.Immutable
    public static long mt_getrandmax(){
        return Integer.MAX_VALUE;
    }


    public static long mt_rand(){
        return MERSENNE_TWISTER.nextLong();
    }

    public static Memory mt_rand(long min, long max){
        if (max < min) return Memory.FALSE;
        return LongMemory.valueOf(MERSENNE_TWISTER.nextInt((int)(max - min) + 1) + min);
    }

    public static void mt_srand(){
        MERSENNE_TWISTER = new Random();
    }

    public static void mt_srand(long seed){
        MERSENNE_TWISTER = new Random(seed);
    }

    public static long rand(){
        return RANDOM.nextLong();
    }

    public static Memory rand(long min, long max){
        if (max < min)
            return Memory.FALSE;
        return LongMemory.valueOf(RANDOM.nextInt((int)(max - min) + 1) + min);
    }

    public static void srand(){
        RANDOM = new Random();
    }

    public static void srand(long seed){
        RANDOM = new Random(seed);
    }

    @Runtime.Immutable
    public static Memory octdec(String octalString){
        try {
            return LongMemory.valueOf(Long.parseLong(octalString, 8));
        } catch (NumberFormatException e){
            return Memory.FALSE;
        }
    }

    @Runtime.Immutable
    public static double pi(){
        return constants.M_PI;
    }

    @Runtime.Immutable
    public static Memory pow(Memory base, Memory exp){
        long lExp;
        if (base.type == Memory.Type.INT
                && exp.type == Memory.Type.INT && (lExp = exp.toLong()) >= 0){
            return LongMemory.valueOf( (long)Math.pow(base.toDouble(), lExp) );
        } else {
            return new DoubleMemory(Math.pow(base.toDouble(), exp.toDouble()));
        }
    }

    @Runtime.Immutable
    public static double round(double value){
        return Math.round(value);
    }

    @Runtime.Immutable
    public static double round(double value, int precession){
        return BigDecimal.valueOf(value)
                .round(new MathContext(precession, RoundingMode.UP))
                .doubleValue();
    }

    @Runtime.Immutable
    public static double round(double value, int precession, int mode){
        MathContext context;
        switch (mode){
            case 2: context = new MathContext(precession, RoundingMode.DOWN); break;
            case 3: context = new MathContext(precession, RoundingMode.HALF_EVEN); break;
            case 4:
                if ((long)value % 2 == 0)
                    context = new MathContext(precession, RoundingMode.UP);
                else
                    context = new MathContext(precession, RoundingMode.DOWN);
                break;
            default:
                context = new MathContext(precession, RoundingMode.UP);
        }

        return BigDecimal.valueOf(value)
                .round(context)
                .doubleValue();
    }
}
