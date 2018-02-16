package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.common.DigestUtils;
import php.runtime.common.GrammarUtils;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.ext.core.MathFunctions;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ParameterEntity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\Str")
public class StrUtils extends BaseObject {

    public StrUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("search"),
            @Arg(value = "fromIndex", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory pos(Environment env, Memory... args) {
        int fromIndex = args[2].toInteger();
        return LongMemory.valueOf(args[0].toString().indexOf(args[1].toString(), fromIndex));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("search"),
            @Arg(value = "fromIndex", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory posIgnoreCase(Environment env, Memory... args) {
        int fromIndex = args[2].toInteger();
        return LongMemory.valueOf(StringUtils.indexOfIgnoreCase(args[0].toString(), args[1].toBinaryString(), fromIndex));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("search"),
            @Arg(value = "fromIndex", optional = @Optional("NULL"))
    })
    public static Memory lastPos(Environment env, Memory... args) {
        return LongMemory.valueOf(args[2].isNull()
                ? args[0].toString().lastIndexOf(args[1].toString())
                : args[0].toString().lastIndexOf(args[1].toString(), args[2].toInteger())
        );
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("search"),
            @Arg(value = "fromIndex", optional = @Optional("NULL"))
    })
    public static Memory lastPosIgnoreCase(Environment env, Memory... args) {
        String string = args[0].toString();
        int from = args[2].isNull() ? string.length() : args[2].toInteger();
        return LongMemory.valueOf(StringUtils.lastIndexOfIgnoreCase(string, args[1].toString(), from));
    }

    @FastMethod
    @Signature({@Arg("string"), @Arg("beginIndex"), @Arg(value = "endIndex", optional = @Optional("NULL"))})
    public static Memory sub(Environment env, Memory... args) {
        String string = args[0].toString();
        int len = string.length();
        int begin = args[1].toInteger();
        int finish;

        if (args.length < 3 || args[2].isNull())
            finish = len;
        else {
            finish = args[2].toInteger();
            if (finish > len)
                return Memory.FALSE;
        }

        if (begin > finish || begin < 0 || begin > len - 1)
            return Memory.FALSE;

        return StringMemory.valueOf(string.substring(begin, finish));
    }

    @FastMethod
    @Signature({@Arg("string1"), @Arg("string2")})
    public static Memory compare(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().compareTo(args[1].toString()));
    }

    @FastMethod
    @Signature({@Arg("string1"), @Arg("string2")})
    public static Memory compareIgnoreCase(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().compareToIgnoreCase(args[1].toString()));
    }

    @FastMethod
    @Signature({@Arg("string1"), @Arg("string2")})
    public static Memory equalsIgnoreCase(Environment env, Memory... args) {
        return TrueMemory.valueOf(args[0].toString().equalsIgnoreCase(args[1].toString()));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("prefix"),
            @Arg(value = "offset", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory startsWith(Environment env, Memory... args) {
        return args[0].toString().startsWith(args[1].toString(), args[2].toInteger()) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("suffix")
    })
    public static Memory endsWith(Environment env, Memory... args) {
        return args[0].toString().endsWith(args[1].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature({@Arg("string")})
    public static Memory lower(Environment env, Memory... args) {
        return StringMemory.valueOf(args[0].toString().toLowerCase());
    }

    @FastMethod
    @Signature({@Arg("string")})
    public static Memory upper(Environment env, Memory... args) {
        return StringMemory.valueOf(args[0].toString().toUpperCase());
    }

    @FastMethod
    @Signature({@Arg("string")})
    public static Memory length(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().length());
    }

    @FastMethod
    @Signature({@Arg("string"), @Arg("target"), @Arg("replacement")})
    public static Memory replace(Environment env, Memory... args) {
        String target = args[1].toString();
        String replacement = args[2].toString();
        if (target.length() == 1 && replacement.length() == 1) {
            return StringMemory.valueOf(args[0].toString().replace(target.charAt(0), replacement.charAt(0)));
        } else {
            return StringMemory.valueOf(args[0].toString().replace(target, replacement));
        }
    }

    @FastMethod
    @Signature({@Arg("string"), @Arg("amount")})
    public static Memory repeat(Environment env, Memory... args) {
        String s = args[0].toString();
        int amount = args[1].toInteger();
        if (amount <= 0)
            return Memory.FALSE;

        if (s.length() == 1) {
            return new StringMemory(StringUtils.repeat(s.charAt(0), amount));
        } else {
            int cnt = args[1].toInteger();
            StringBuilder sb = new StringBuilder(cnt * s.length());
            for (int i = 0; i < cnt; i++) {
                sb.append(s);
            }
            return new StringMemory(sb.toString());
        }
    }

    protected static String trimStringByString(String text, String trimBy, boolean toLeft, boolean toRight) {
        int len = text.length();

        int left = 0;
        if (toLeft) {
            while (left < len && trimBy.indexOf(text.charAt(left)) > -1) {
                left++;
            }
        }

        int right = len - 1;
        if (toRight) {
            while (right > 0 && trimBy.indexOf(text.charAt(right)) > -1) {
                right--;
            }
        }

        if (toLeft && toRight) {
            if (left == 0 && right == len - 1)
                return text;
           /* if (left < right - 1)
                return "";*/
            if (right == 0 && left > 0)
                return text.substring(left);

            return text.substring(left, right + 1);
        } else if (toLeft) {
            if (left == len)
                return "";
            return text.substring(left);
        } else if (toRight) {
            if (right == 0)
                return "";
            return text.substring(0, right + 1);
        } else
            throw new IllegalArgumentException();
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg(value = "charlist", optional = @Optional(" \t\n\r\0\11"))
    })
    public static Memory trim(Environment env, Memory... args) {
        String trimBy = args[1].toString();
        return StringMemory.valueOf(trimStringByString(args[0].toString(), trimBy, true, true));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg(value = "charlist", optional = @Optional(" \t\n\r\0\11"))
    })
    public static Memory trimRight(Environment env, Memory... args) {
        String trimBy = args[1].toString();
        return StringMemory.valueOf(trimStringByString(args[0].toString(), trimBy, false, true));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg(value = "charlist", optional = @Optional(" \t\n\r\0\11"))
    })
    public static Memory trimLeft(Environment env, Memory... args) {
        String trimBy = args[1].toString();
        return StringMemory.valueOf(trimStringByString(args[0].toString(), trimBy, true, false));
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory reverse(Environment env, Memory... args) {
        return new StringMemory(StringUtils.reverse(args[0].toString()));
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory shuffle(Environment env, Memory... args) {
        char[] chars = args[0].toString().toCharArray();

        int length = chars.length;
        for (int i = 0; i < length; i++) {
            int rand = MathFunctions.RANDOM.nextInt(length);

            char temp = chars[rand];
            chars[rand] = chars[i];
            chars[i] = temp;
        }
        return new StringMemory(new String(chars));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("separator"),
            @Arg(value = "limit", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory split(Environment env, Memory... args) {
        String string = args[0].toString();

        if (string.isEmpty()) {
            return new ArrayMemory().toConstant();
        }

        String separator = args[1].toString();
        int limit = args[2].toInteger();

        String[] strings = StringUtils.split(string, separator, limit);
        return ArrayMemory.ofStrings(strings).toConstant();
    }

    @FastMethod
    @Signature({
            @Arg("collection"),
            @Arg("separator"),
            @Arg(value = "limit", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory join(Environment env, Memory... args) {
        String separator = args[1].toString();
        StringBuilder builder = new StringBuilder();
        int limit = args[2].toInteger();

        int i = 0;
        if (args[0].isArray()) {
            ArrayMemory array = args[0].toValue(ArrayMemory.class);
            int size = array.size();
            if (limit > 0 && limit < size)
                size = limit;

            for (Memory el : array) {
                builder.append(el);
                if (i != size - 1)
                    builder.append(separator);
                i++;
                if (i == size) break;
            }

            return new StringMemory(builder.toString());
        } else {
            ParameterEntity.validateTypeHinting(env, 1, args, HintType.TRAVERSABLE, false);

            ForeachIterator iterator = args[0].getNewIterator(env);
            while (iterator.next()) {
                builder.append(iterator.getValue());
                builder.append(separator);
                i++;

                if (limit > 0 && i == limit) break;
            }

            int length = builder.length();
            if (length > 0) {
                builder.delete(length - separator.length(), length);
            } else
                return Memory.CONST_EMPTY_STRING;

            return new StringMemory(builder.toString());
        }
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("charset")
    })
    public static Memory encode(Environment env, Memory... args) {
        Charset charset;

        try {
            charset = Charset.forName(args[1].toString());
        } catch (Exception e) {
            return Memory.FALSE;
        }

        ByteBuffer buffer = charset.encode(args[0].toString());

        return new BinaryMemory(Arrays.copyOf(buffer.array(), buffer.limit()));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("charset")
    })
    public static Memory decode(Environment env, Memory... args) {
        Charset charset;

        try {
            charset = Charset.forName(args[1].toString());
        } catch (Exception e) {
            return Memory.FALSE;
        }

        CharBuffer charBuffer = charset.decode(ByteBuffer.wrap(args[0].getBinaryBytes(env.getDefaultCharset())));
        return StringMemory.valueOf(charBuffer.toString());
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg(value = "bigNumbers", optional = @Optional("true"))
    })
    public static Memory isNumber(Environment env, Memory... args) {
        return StringMemory.toLong(args[0].toString(), args[1].toBoolean()) != null ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({
            @Arg(value = "length", optional = @Optional("16")),
            @Arg(value = "set", optional = @Optional("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789"))
    })
    public static Memory random(Environment env, Memory... args) {
        String set = args[1].toString();
        int resultLen = args[0].toInteger();
        int len = set.length();

        if (len < 1) {
            return Memory.FALSE;
        }

        StringBuilder sb = new StringBuilder(resultLen);

        for (int i = 0; i < resultLen; i++) {
            sb.append(set.charAt(MathFunctions.RANDOM.nextInt(len)));
        }

        return StringMemory.valueOf(sb.toString());
    }

    protected interface CharChecker {
        boolean check(char ch);
    }

    protected static Memory _checkChars(Memory string, CharChecker checker) {
        String s = string.toString();

        int length = s.length();

        if (length == 0) {
            return Memory.FALSE;
        }

        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);

            if (!checker.check(ch)) {
                return Memory.FALSE;
            }
        }

        return Memory.TRUE;
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory isLower(Environment env, Memory... args) {
        String s = args[0].toString();

        int length = s.length();

        if (length == 0) {
            return Memory.FALSE;
        }

        boolean letterExists = false;

        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);

            if (!Character.isLetter(ch)) continue;

            if (!Character.isLowerCase(ch)) {
                return Memory.FALSE;
            }

            letterExists = true;
        }

        return letterExists ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory isUpper(Environment env, Memory... args) {
        String s = args[0].toString();

        int length = s.length();

        if (length == 0) {
            return Memory.FALSE;
        }

        boolean letterExists = false;

        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);

            if (!Character.isLetter(ch)) continue;

            if (!Character.isUpperCase(ch)) {
                return Memory.FALSE;
            }

            letterExists = true;
        }

        return letterExists ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature({@Arg("string")})
    public static Memory upperFirst(Environment env, Memory... args) {
        String s = args[0].toString();

        if (s.length() > 0) {
            s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }

        return StringMemory.valueOf(s);
    }

    @FastMethod
    @Signature({@Arg("string")})
    public static Memory lowerFirst(Environment env, Memory... args) {
        String s = args[0].toString();

        if (s.length() > 0) {
            s = Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }

        return StringMemory.valueOf(s);
    }

    @FastMethod
    @Signature({
            @Arg("string"), @Arg("subString"), @Arg(value = "offset", optional = @Optional("0"))
    })
    public static Memory count(Environment env, Memory... args) {
        String s = args[0].toString();
        String sub = args[1].toString();
        int offset = args[2].toInteger();

        int length = sub.length();
        int count = 0;

        int sLength = s.length();

        if (length == 1) {
            char subChar = sub.charAt(0);

            for (int i = offset; i < sLength; i++) {
                if (s.charAt(i) == subChar) {
                    count++;
                }
            }
        } else if (length > 1) {
            for (int i = offset; i < sLength; i++) {
                if (s.startsWith(sub, i)) {
                    count++;
                    i += length;
                }
            }
        }

        return LongMemory.valueOf(count);
    }

    @FastMethod
    @Signature({@Arg("string"), @Arg("search")})
    public static Memory contains(Environment env, Memory... args) {
        String s = args[0].toString();

        return s.contains(args[1].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("string"), @Arg("format"), @Arg(value = "flags", optional = @Optional("-1"))})
    public static Memory parseAs(Environment env, Memory... args) throws Throwable {
        return env.invokeMethod(
                WrapProcessor.createByCode(env, args[1].toString(), args[2].toInteger()), "parse", args[0]
        );
    }

    @Signature({@Arg("memory"), @Arg("format"), @Arg(value = "flags", optional = @Optional("-1"))})
    public static Memory formatAs(Environment env, Memory... args) throws Throwable {
        return env.invokeMethod(
                WrapProcessor.createByCode(env, args[1].toString(), args[2].toInteger()), "format", args[0]
        );
    }

    @Signature({@Arg("string")})
    public static Memory format(Environment env, Memory... args) {
        String s = args[0].toString();

        Object[] _args = new Object[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            Memory arg = args[i];
            Object _arg = arg;

            switch (arg.getRealType()) {
                case INT:
                    _arg = arg.toLong();
                    break;
                case DOUBLE:
                    _arg = arg.toDouble();
                    break;
                case BOOL:
                    _arg = arg.toBoolean();
                    break;
            }

            _args[i - 1] = _arg;
        }

        return StringMemory.valueOf(String.format(s, _args));
    }

    @Signature({
            @Arg(value = "value", optional = @Optional("null"))
    })
    public static Memory uuid(Environment env, Memory... args) {
        Memory value = args[0];

        String s;

        if (value.isNotNull()) {
            s = UUID.fromString(value.toString()).toString();
        } else {
            s = UUID.randomUUID().toString();
        }

        return StringMemory.valueOf(s);
    }

    @Signature({
            @Arg("value"),
            @Arg(value = "algorithm", optional = @Optional("SHA-1"))
    })
    public static Memory hash(Environment env, Memory... args) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(args[1].toString());

        messageDigest.update(args[0].getBinaryBytes(env.getDefaultCharset()));
        return StringMemory.valueOf(DigestUtils.bytesToHex(messageDigest.digest()));
    }

    @Signature({
            @Arg(value = "string"),
            @Arg(value = "removeEmpty", optional = @Optional("false"))
    })
    public static Memory lines(Environment env, Memory... args) {
        boolean removeEmpty = args[1].toBoolean();

        Scanner scanner = new Scanner(args[0].toString());

        ArrayMemory result = new ArrayMemory();

        while (scanner.hasNextLine()) {
            String value = scanner.nextLine();

            if (removeEmpty) {
                value = value.trim();

                if (value.isEmpty()) {
                    continue;
                }
            }

            result.add(value);
        }

        return result.toConstant();
    }
}
