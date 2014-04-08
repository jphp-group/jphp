package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.ext.core.MathFunctions;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\str")
final public class StrUtils extends BaseObject {
    public StrUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("search"),
            @Arg(value = "fromIndex", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory indexOf(Environment env, Memory... args) {
        int fromIndex = args[2].toInteger();
        return LongMemory.valueOf(args[0].toString().indexOf(args[1].toString(), fromIndex));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("search"),
            @Arg(value = "fromIndex", optional = @Optional("NULL"))
    })
    public static Memory lastIndexOf(Environment env, Memory... args) {
        return LongMemory.valueOf(args[2].isNull()
                ? args[0].toString().lastIndexOf(args[1].toString())
                : args[0].toString().lastIndexOf(args[1].toString(), args[2].toInteger())
        );
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
    @Signature({@Arg("string"), @Arg("pattern")})
    public static Memory matches(Environment env, Memory... args) {
        return args[0].toString().matches(args[1].toString()) ? Memory.TRUE : Memory.FALSE;
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
    public static Memory hash(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().hashCode());
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
        int amount = args[0].toInteger();
        if (amount <= 0)
            return Memory.FALSE;

        if (s.length() == 1) {
            return new StringMemory(StringUtils.repeat(s.charAt(0), args[1].toInteger()));
        } else {
            int cnt = args[1].toInteger();
            StringBuilder sb = new StringBuilder(cnt * s.length());
            for(int i = 0; i < cnt; i++) {
                sb.append(s);
            }
            return new StringMemory(sb.toString());
        }
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory trim(Environment env, Memory... args) {
        return new StringMemory(args[0].toString().trim());
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
}
