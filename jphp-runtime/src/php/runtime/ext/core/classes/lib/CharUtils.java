package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\Char")
public class CharUtils extends BaseObject {
    private final static char CHAR_UNDEFINED = 0xFFFF;

    public CharUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    protected static boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                c != CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    protected static char chr(Memory arg) {
        String s = arg.toString();
        if (s.length() > 0)
            return s.charAt(0);
        else
            return '\0';
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory ord(Environment env, Memory... args) {
        return LongMemory.valueOf((int) args[0].toChar());
    }

    @FastMethod
    @Signature(@Arg("code"))
    public static Memory of(Environment env, Memory... args) {
        return StringMemory.valueOf((char) (args[0].toInteger()));
    }

    @FastMethod
    @Signature(@Arg("code"))
    public static Memory count(Environment env, Memory... args) {
        return LongMemory.valueOf(Character.charCount(args[0].toInteger()));
    }

    @FastMethod
    @Signature({@Arg("char1"), @Arg("char2")})
    public static Memory compare(Environment env, Memory... args) {
        return LongMemory.valueOf(chr(args[0]) - chr(args[1]));
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory upper(Environment env, Memory... args) {
        return StringMemory.valueOf(Character.toUpperCase(chr(args[0])));
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory lower(Environment env, Memory... args) {
        return StringMemory.valueOf(Character.toLowerCase(chr(args[0])));
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory title(Environment env, Memory... args) {
        return StringMemory.valueOf(Character.toTitleCase(chr(args[0])));
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isSpace(Environment env, Memory... args) {
        return Character.isSpaceChar(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isDigit(Environment env, Memory... args) {
        return Character.isDigit(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isLetter(Environment env, Memory... args) {
        return Character.isLetter(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isLetterOrDigit(Environment env, Memory... args) {
        return Character.isLetterOrDigit(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isLower(Environment env, Memory... args) {
        return Character.isLowerCase(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isUpper(Environment env, Memory... args) {
        return Character.isUpperCase(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isTitle(Environment env, Memory... args) {
        return Character.isTitleCase(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isWhitespace(Environment env, Memory... args) {
        return Character.isWhitespace(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isISOControl(Environment env, Memory... args) {
        return Character.isISOControl(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isDefined(Environment env, Memory... args) {
        return Character.isDefined(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isMirrored(Environment env, Memory... args) {
        return Character.isMirrored(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isLowSurrogate(Environment env, Memory... args) {
        return Character.isLowSurrogate(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isHighSurrogate(Environment env, Memory... args) {
        return Character.isHighSurrogate(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory isPrintable(Environment env, Memory... args) {
        return isPrintableChar(chr(args[0])) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("char"))
    public static Memory number(Environment env, Memory... args) {
        return LongMemory.valueOf(Character.getNumericValue(chr(args[0])));
    }
}
