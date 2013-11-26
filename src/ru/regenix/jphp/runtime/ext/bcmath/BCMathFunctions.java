package ru.regenix.jphp.runtime.ext.bcmath;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.StringMemory;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * bcmath extension
 * http://www.php.net/manual/ru/book.bc.php
 *
 *  TODO: add bcsqrt, bcpow, bcpowmod
 */
public class BCMathFunctions extends FunctionsContainer {

    private static MathContext defaultContext = null;

    public static Memory bcadd(String left, String right, int scale) {
        try {
            return new StringMemory(
                    new BigDecimal(left).add(new BigDecimal(right), new MathContext(scale)).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        } catch (IllegalArgumentException e){
            return Memory.NULL;
        }
    }

    public static Memory bcadd(String left, String right) {
        try {
            return new StringMemory(defaultContext == null
                ? new BigDecimal(left).add(new BigDecimal(right)).toString()
                : new BigDecimal(left).add(new BigDecimal(right), defaultContext).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        }
    }

    public static Memory bccomp(String left, String right, int scale){
        try {
            MathContext context = new MathContext(scale);
            return LongMemory.valueOf(
                    new BigDecimal(left, context).compareTo(new BigDecimal(right, context))
            );
        } catch (IllegalArgumentException e){
            return Memory.NULL;
        }
    }

    public static int bccomp(String left, String right){
        if (defaultContext == null)
            return new BigDecimal(left).compareTo(new BigDecimal(right));
        else
            return new BigDecimal(left, defaultContext).compareTo(new BigDecimal(right, defaultContext));
    }

    public static Memory bcdiv(String left, String right, int scale){
        try {
            return new StringMemory(
                    new BigDecimal(left).divide(new BigDecimal(right), new MathContext(scale)).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        } catch (IllegalArgumentException e){
            return Memory.NULL;
        }
    }

    public static Memory bcdiv(String left, String right) {
        try {
            return new StringMemory(defaultContext == null
                    ? new BigDecimal(left).divide(new BigDecimal(right)).toString()
                    : new BigDecimal(left).divide(new BigDecimal(right), defaultContext).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        }
    }


    public static Memory bcmod(String left, String modus){
        try {
            return new StringMemory(
                    new BigDecimal(left).remainder(new BigDecimal(modus)).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        }
    }

    public static Memory bcmul(String left, String right, int scale){
        try {
            return new StringMemory(
                    new BigDecimal(left).multiply(new BigDecimal(right), new MathContext(scale)).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        } catch (IllegalArgumentException e){
            return Memory.NULL;
        }
    }

    public static Memory bcmul(String left, String right) {
        try {
            return new StringMemory(defaultContext == null
                    ? new BigDecimal(left).multiply(new BigDecimal(right)).toString()
                    : new BigDecimal(left).multiply(new BigDecimal(right), defaultContext).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        }
    }

    public static boolean bcscale(int scale){
        if (scale == -1)
            defaultContext = null;
        else if (scale >= 0){
            try {
                defaultContext = new MathContext(scale);
            } catch (IllegalArgumentException e){
                return false;
            }
        } else
            return false;

        return true;
    }


    public static Memory bcsub(String left, String right, int scale){
        try {

            return new StringMemory(
                    new BigDecimal(left).subtract(new BigDecimal(right), new MathContext(scale)).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        } catch (IllegalArgumentException e){
            return Memory.NULL;
        }
    }

    public static Memory bcsub(String left, String right) {
        try {
            return new StringMemory(defaultContext == null
                    ? new BigDecimal(left).subtract(new BigDecimal(right)).toString()
                    : new BigDecimal(left).subtract(new BigDecimal(right), defaultContext).toString()
            );
        } catch (ArithmeticException e){
            return Memory.NULL;
        }
    }
}
