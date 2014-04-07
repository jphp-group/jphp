package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\util\\Str")
final public class StrUtils extends BaseObject {
    public StrUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    @FastMethod
    @Signature({@Arg("string"), @Arg("search")})
    public static Memory pos(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().indexOf(args[1].toString()));
    }

    @FastMethod
    @Signature({@Arg("string"), @Arg("search")})
    public static Memory lastPos(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().lastIndexOf(args[1].toString()));
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
    public static Memory match(Environment env, Memory... args) {
        return args[0].toString().matches(args[1].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature({@Arg("string1"), @Arg("string2")})
    public static Memory compare(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().compareTo(args[1].toString()));
    }

    @FastMethod
    @Signature({@Arg("string1"), @Arg("string2")})
    public static Memory compareInsensitive(Environment env, Memory... args) {
        return LongMemory.valueOf(args[0].toString().compareToIgnoreCase(args[1].toString()));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg("prefix"),
            @Arg(value = "offset", optional = @Optional(value = "0", type = HintType.INT))
    })
    public static Memory startWith(Environment env, Memory... args) {
        return args[0].toString().startsWith(args[1].toString(), args[2].toInteger()) ? Memory.TRUE : Memory.FALSE;
    }
}
