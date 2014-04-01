package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * bcmath extension
 * http://www.php.net/manual/ru/book.bc.php
 */
public class BCMathFunctions extends FunctionsContainer {

    private static final BigDecimal DECIMAL_TWO = new BigDecimal(2);
    private static final BigInteger INTEGER_MAX = new BigInteger(Integer.MAX_VALUE + "");

    private static int getScale(Environment env) {
        Memory value = env.getConfigValue("bcmath.scale");
        return value == null ? 0 : value.toInteger();
    }

    private static BigDecimal toBigDecimal(Memory value) {
        try {
            switch (value.type){
                case DOUBLE: return BigDecimal.valueOf(value.toDouble());
                case INT: return BigDecimal.valueOf(value.toLong());
                default:
                    return new BigDecimal(value.toString());
            }
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        } catch (IllegalArgumentException ex) {
            return BigDecimal.ZERO;
        }
    }

    public static Memory bcadd(Environment env, Memory left, Memory right, int scale) {
        BigDecimal bd1 = toBigDecimal(left);
        BigDecimal bd2 = toBigDecimal(right);

        BigDecimal bd = bd1.add(bd2);
        bd = bd.setScale(scale, RoundingMode.DOWN);

        return new StringMemory(bd.toPlainString());
    }

    public static Memory bcadd(Environment env, Memory left, Memory right) {
        return bcadd(env, left, right, getScale(env));
    }

    public static int bccomp(Environment env, Memory left, Memory right, int scale){
        BigDecimal bd1 = toBigDecimal(left);
        BigDecimal bd2 = toBigDecimal(right);

        bd1 = bd1.setScale(scale, RoundingMode.DOWN);
        bd2 = bd2.setScale(scale, RoundingMode.DOWN);

        return bd1.compareTo(bd2);
    }

    public static int bccomp(Environment env, Memory left, Memory right){
        return bccomp(env, left, right, getScale(env));
    }

    public static Memory bcdiv(Environment env, Memory left, Memory right, int scale){
        BigDecimal bd1 = toBigDecimal(left);
        BigDecimal bd2 = toBigDecimal(right);

        if (bd2.compareTo(BigDecimal.ZERO) == 0) {
            return Memory.NULL;
        }

        BigDecimal result;

        if (scale > 0)
            result = bd1.divide(bd2, scale + 2, RoundingMode.DOWN);
        else
            result = bd1.divide(bd2, 2, RoundingMode.DOWN);

        result = result.setScale(scale, RoundingMode.DOWN);
        return new StringMemory(result.toPlainString());
    }

    public static Memory bcdiv(Environment env, Memory left, Memory right) {
        return bcdiv(env, left, right, getScale(env));
    }


    public static Memory bcmod(Memory left, Memory modus){
        BigDecimal base = toBigDecimal(left).setScale(0, RoundingMode.DOWN);
        BigDecimal mod = toBigDecimal(modus).setScale(0, RoundingMode.DOWN);

        if (mod.compareTo(BigDecimal.ZERO) == 0) {
            return Memory.NULL;
        }

        return new StringMemory(base.remainder(mod, MathContext.UNLIMITED).toString());
    }

    public static Memory bcmul(Environment env, Memory left, Memory right, int scale){
        BigDecimal bd1 = toBigDecimal(left);
        BigDecimal bd2 = toBigDecimal(right);

        BigDecimal bd = bd1.multiply(bd2);

        // odd php special case for 0, scale is ignored:
        if (bd.compareTo(BigDecimal.ZERO) == 0) {
            if (scale > 0)
                return new StringMemory("0.0");
            else
                return new StringMemory("0");
        }

        bd = bd.setScale(scale, RoundingMode.DOWN);
        bd = bd.stripTrailingZeros();

        return new StringMemory(bd.toPlainString());
    }

    public static Memory bcmul(Environment env, Memory left, Memory right) {
        return bcmul(env, left, right, getScale(env));
    }

    public static boolean bcscale(Environment env, int scale) {
        env.getConfigValue("bcmath.scale", LongMemory.valueOf(scale));
        return true;
    }

    public static String bcpow(Environment env, Memory base, Memory exp, int scale) {
        BigDecimal baseD = toBigDecimal(base);
        BigDecimal expD = toBigDecimal(exp);

        BigInteger expI = expD.toBigInteger();
        return bcpowImpl(baseD, expI, scale).toPlainString();
    }

    public static String bcpow(Environment env, Memory base, Memory exp){
        return bcpow(env, base, exp, getScale(env));
    }

    private static BigDecimal bcpowImpl(BigDecimal base, BigInteger exp, int scale) {
        if (exp.compareTo(BigInteger.ZERO) == 0)
            return BigDecimal.ONE;

        boolean isNeg;
        if (exp.compareTo(BigInteger.ZERO) < 0) {
            isNeg = true;
            exp = exp.negate();
        }
        else
            isNeg = false;

        BigDecimal result = BigDecimal.ZERO;

        while (exp.compareTo(BigInteger.ZERO) > 0) {
            BigInteger expSub = exp.min(INTEGER_MAX);
            exp = exp.subtract(expSub);

            result = result.add(base.pow(expSub.intValue()));
        }

        if (isNeg)
            result = BigDecimal.ONE.divide(result, scale + 2, RoundingMode.DOWN);

        result = result.setScale(scale, RoundingMode.DOWN);

        if (result.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;

        result = result.stripTrailingZeros();
        return result;
    }

    public static String bcpowmod(Environment env, Memory _base, Memory _exp, Memory _modulus, int scale) {
        BigDecimal base = toBigDecimal(_base);
        BigDecimal exp = toBigDecimal(_exp);
        BigDecimal modulus = toBigDecimal(_modulus);

        if (base.scale() != 0) {
            BigInteger expI = exp.toBigInteger();
            BigDecimal pow = bcpowImpl(base, expI, scale);
            return pow.remainder(modulus, MathContext.UNLIMITED).toString();
        } else {
            BigInteger baseI = base.toBigInteger();
            BigInteger expI = exp.toBigInteger();
            BigInteger modulusI = modulus.toBigInteger();

            BigInteger result = baseI.modPow(expI, modulusI);
            return result.toString();
        }
    }

    public static String bcpowmod(Environment env, Memory _base, Memory _exp, Memory _modulus){
        return bcpowmod(env, _base, _exp, _modulus, getScale(env));
    }

    public static Memory bcsqrt(Environment env, Memory operand, int scale) {
        BigDecimal value = toBigDecimal(operand);

        int compareToZero = value.compareTo(BigDecimal.ZERO);

        if (compareToZero < 0) {
            return Memory.NULL;
        } else if (compareToZero == 0) {
            return new StringMemory("0");
        }

        int compareToOne = value.compareTo(BigDecimal.ONE);

        if (compareToOne == 0)
            return new StringMemory("1");

        int cscale;

        BigDecimal initialGuess;
        if (compareToOne < 1) {
            initialGuess = BigDecimal.ONE;
            cscale = value.scale();
        } else {
            BigInteger integerPart = value.toBigInteger();

            int length = integerPart.toString().length();

            if ((length % 2) == 0)
                length--;

            length /= 2;
            initialGuess = BigDecimal.ONE.movePointRight(length);
            cscale = Math.max(scale, value.scale()) + 2;
        }

        BigDecimal guess = initialGuess;
        BigDecimal lastGuess;

        for (int iteration = 0; iteration < 50; iteration++) {
            lastGuess = guess;
            guess = value.divide(guess, cscale, RoundingMode.DOWN);
            guess = guess.add(lastGuess);
            guess = guess.divide(DECIMAL_TWO, cscale, RoundingMode.DOWN);

            if (lastGuess.equals(guess)) {
                break;
            }
        }

        value = guess;
        value = value.setScale(scale, RoundingMode.DOWN);
        return new StringMemory(value.toPlainString());
    }

    public static Memory bcsqrt(Environment env, Memory operand) {
        return bcsqrt(env, operand, getScale(env));
    }

    public static String bcsub(Environment env, Memory left, Memory right, int scale){
        BigDecimal bd1 = toBigDecimal(left);
        BigDecimal bd2 = toBigDecimal(right);
        return bd1.subtract(bd2).setScale(scale, RoundingMode.DOWN).toPlainString();
    }

    public static String bcsub(Environment env, Memory left, Memory right) {
        return bcsub(env, left, right, getScale(env));
    }
}
