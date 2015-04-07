package org.jphp.std;


import static php.runtime.annotation.Reflection.*;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;
import java.lang.Math;

@Name("php\\util\\Math")
@Final
public class MathUtils extends BaseObject {
    public MathUtils(Environment env) {
        super(env);
    }

    public MathUtils(Environment env, ClassEntity cls) {
        super(env, cls);
    }


    final public static double MAX_VALUE    = Double.MAX_VALUE;
    final public static double MIN_VALUE    = Double.MIN_VALUE;
    final public static double MAX_EXPONENT = Double.MAX_EXPONENT;
    final public static double MIN_EXPONENT = Double.MIN_EXPONENT;

    final public static double E            = Math.E;
    final public static double PI           = Math.PI;
    final public static double LN2          = 0.6931471805599453;
    final public static double LN10         = 2.302585092994046;
    final public static double LOG2E        = 1.4426950408889634;
    final public static double LOG10E       = 0.4342944819032518;
    final public static double SQRT1_2      = 0.7071067811865476;
    final public static double SQRT2        = 1.4142135623730951;


    /**
     * Static class
     */
    @Signature
    private void __construct() {}


    /**
     * Sines
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory sin(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.sin(value));
    }


    /**
     * Arcsines
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory asin(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.asin(value));
    }


    /**
     * Cosines
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory cos(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.cos(value));
    }

    /**
     * Arccosines
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory acos(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.acos(value));
    }


    /**
     * Tangent
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory tan(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.tan(value));
    }

    /**
     * Arctangent
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory atan(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.atan(value));
    }


    /**
     * Pow^2
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory sqr(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.pow(value, 2.0));
    }

    /**
     * Pow^$exp
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x"),
            @Arg(value = "exponent")
    })
    public static Memory pow(Environment env, Memory... args) {
        return DoubleMemory.valueOf(Math.pow(
                args[0].toDouble(),
                args[1].toDouble()
        ));
    }

    /**
     * Square root (x2)
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory sqrt(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.sqrt(value));
    }

    /**
     * Cube root (x3)
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory cbrt(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.cbrt(value));
    }

    /**
     * e^$x (e == Euler's number)
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory exp(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.exp(value));
    }

    /**
     * Logarithm
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory log(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.log(value));
    }

    /**
     * Logarithm10
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory log10(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.log10(value));
    }


    /**
     * returns max of 2 numbers
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "a"),
            @Arg(value = "b")
    })
    public static Memory max(Environment env, Memory... args) {
        return DoubleMemory.valueOf(Math.max(
                args[0].toDouble(),
                args[1].toDouble()
        ));
    }

    /**
     * returns min of 2 numbers
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "a"),
            @Arg(value = "b")
    })
    public static Memory min(Environment env, Memory... args) {
        return DoubleMemory.valueOf(Math.min(
                args[0].toDouble(),
                args[1].toDouble()
        ));
    }


    /**
     * Returns the closest {@code long} to the argument, with ties
     * rounding to positive infinity.
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory round(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.round(value));
    }

    /**
     * Returns the smallest (closest to negative infinity)
     * {@code double} value that is greater than or equal to the
     * argument and is equal to a mathematical integer.
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory ceil(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.ceil(value));
    }

    /**
     * Returns the largest (closest to positive infinity)
     * {@code double} value that is less than or equal to the
     * argument and is equal to a mathematical integer.
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory floor(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.floor(value));
    }

    /**
     * Returns the absolute value of a {@code double} value.
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "x")
    })
    public static Memory abs(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.abs(value));
    }


    /**
     * Random value from $min to $max
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "min"),
            @Arg(value = "max")
    })
    public static Memory random(Environment env, Memory... args) {
        Long min = args[0].toLong();
        Long max = args[1].toLong();

        return LongMemory.valueOf((long)Math.floor(
            Math.random() * (max - min) + min
        ));
    }

    /**
     * Degrees angle to radians
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "degrees")
    })
    public static Memory toRadians(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.toRadians(value));
    }

    /**
     * Radians to degrees angle
     *
     * @param env
     * @param args
     * @return
     */
    @Signature({
            @Arg(value = "radians")
    })
    public static Memory toDegrees(Environment env, Memory... args) {
        Double value = args[0].toDouble();
        return DoubleMemory.valueOf(Math.toDegrees(value));
    }
}
