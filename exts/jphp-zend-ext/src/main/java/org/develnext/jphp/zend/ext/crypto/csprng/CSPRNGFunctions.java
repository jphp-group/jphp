package org.develnext.jphp.zend.ext.crypto.csprng;

import java.security.SecureRandom;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.lang.exception.BaseError;
import php.runtime.memory.BinaryMemory;

/**
 * https://www.php.net/manual/en/ref.csprng.php
 */
public class CSPRNGFunctions extends FunctionsContainer {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static Memory random_bytes(Environment env, TraceInfo trace, int length) {
        if (length < 1) {
            env.exception(trace, BaseError.class, "Length must be greater than 0");
        }

        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);

        return new BinaryMemory(bytes);
    }

    public static long random_int(Environment env, TraceInfo trace, long min, long max) {
        if (max < min) {
            env.exception(trace, BaseError.class, "Minimum value must be less than or equal to the maximum value");
        }

        return nextLong((max - min) + 1) + min;
    }

    private static long nextLong(long bound) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (SECURE_RANDOM.nextLong() << 1) >>> 1;
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0L);
        return val;
    }
}
