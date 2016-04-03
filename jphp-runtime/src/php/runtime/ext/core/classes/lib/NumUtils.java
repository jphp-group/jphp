package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\num")
public class NumUtils extends BaseObject {
    public NumUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    protected final static DecimalFormatSymbols DEFAULT_DECIMAL_FORMAT_SYMBOLS;

    @FastMethod
    @Signature({
            @Arg("number"),
            @Arg("pattern"),
            @Arg(value = "decSep", optional = @Optional(".")),
            @Arg(value = "groupSep", optional = @Optional(","))
    })
    public static Memory format(Environment env, Memory... args) {
        try {
            char decSep = args[2].toChar();
            char groupSep = args[3].toChar();

            DecimalFormat decimalFormat;
            if (decSep == '.' && groupSep == ',')
                decimalFormat = new DecimalFormat(args[1].toString(), DEFAULT_DECIMAL_FORMAT_SYMBOLS);
            else {
                DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
                formatSymbols.setZeroDigit('0');
                formatSymbols.setDecimalSeparator(decSep);
                formatSymbols.setGroupingSeparator(groupSep);

                decimalFormat = new DecimalFormat(args[1].toString(), formatSymbols);
            }
            return new StringMemory(decimalFormat.format(args[0].toDouble()));
        } catch (IllegalArgumentException e) {
            return Memory.FALSE;
        }
    }

    @FastMethod
    @Signature(@Arg("value"))
    public static Memory toBin(Environment env, Memory... args) {
        return StringMemory.valueOf(Long.toBinaryString(args[0].toLong()));
    }

    @FastMethod
    @Signature({
            @Arg("num1"), @Arg("num2")
    })
    public static Memory compare(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toNumeric().compareTo(args[1].toNumeric()));
    }

    @FastMethod
    @Signature(@Arg("value"))
    public static Memory toOctal(Environment env, Memory... args) {
        return StringMemory.valueOf(Long.toOctalString(args[0].toLong()));
    }

    @FastMethod
    @Signature(@Arg("value"))
    public static Memory toHex(Environment env, Memory... args) {
        return StringMemory.valueOf(Long.toHexString(args[0].toLong()));
    }

    @FastMethod
    @Signature({@Arg("value"), @Arg("radix")})
    public static Memory toString(Environment env, Memory... args) {
        return StringMemory.valueOf(Long.toString(args[0].toLong(), args[1].toInteger()));
    }

    @FastMethod
    @Signature(@Arg("value"))
    public static Memory decode(Environment env, Memory... args) {
        try {
            return LongMemory.valueOf(Long.decode(args[0].toString()));
        } catch (NumberFormatException e) {
            return Memory.FALSE;
        }
    }

    @FastMethod
    @Signature(@Arg("number"))
    public static Memory reverse(Environment env, Memory... args) {
        return LongMemory.valueOf(Long.reverse(args[0].toLong()));
    }

    static {
        DEFAULT_DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setDecimalSeparator('.');
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setGroupingSeparator(',');
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setZeroDigit('0');
    }
}
