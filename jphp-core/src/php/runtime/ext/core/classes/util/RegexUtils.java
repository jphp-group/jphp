package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\regex")
final public class RegexUtils extends BaseObject {
    public RegexUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    @FastMethod
    @Signature({
            @Arg("pattern"),
            @Arg("string"),
    })
    public static Memory match(Environment env, Memory... args) {
        return args[1].toString().matches(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
    @Signature({
            @Arg("pattern"),
            @Arg("string"),
            @Arg(value = "limit", optional = @Optional("0"))
    })
    public static Memory split(Environment env, Memory... args) {
        String[] r;
        int limit = args[2].toInteger();

        if (limit <= 0)
            r = args[1].toString().split(args[0].toString());
        else
            r = args[1].toString().split(args[0].toString(), limit);

        return new ArrayMemory(r).toConstant();
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory quote(Environment env, Memory... args) {
        return StringMemory.valueOf(Pattern.quote(args[0].toString()));
    }

    @FastMethod
    @Signature(@Arg("string"))
    public static Memory quoteReplacement(Environment env, Memory... args) {
        return StringMemory.valueOf(Matcher.quoteReplacement(args[0].toString()));
    }

    @FastMethod
    @Signature({
            @Arg("pattern"),
            @Arg(value = "string", optional = @Optional("")),
            @Arg(value = "flags", optional = @Optional("0"))
    })
    public static Memory of(Environment env, Memory... args) {
        return WrapPattern.of(env, args);
    }

    @Name("php\\lib\\regex\\Pattern")
    public static class WrapPattern extends BaseObject implements Iterator {
        public static final int CANON_EQ = Pattern.CANON_EQ;
        public static final int CASE_INSENSITIVE = Pattern.CASE_INSENSITIVE;
        public static final int UNICODE_CASE = Pattern.UNICODE_CASE;
        public static final int UNICODE_CHARACTER_CLASS = Pattern.UNICODE_CHARACTER_CLASS;
        public static final int COMMENTS = Pattern.COMMENTS;
        public static final int DOTALL = Pattern.DOTALL;
        public static final int LITERAL = Pattern.LITERAL;
        public static final int MULTILINE = Pattern.MULTILINE;
        public static final int UNIX_LINES = Pattern.UNIX_LINES;

        protected Matcher matcher;
        protected Memory current;
        protected Memory key;
        protected boolean valid = true;

        public WrapPattern(Environment env, Matcher matcher) {
            super(env);
            this.matcher = matcher;
        }

        public WrapPattern(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        private Memory __construct(Environment env, Memory... args) {
            return Memory.NULL;
        }

        @Signature(@Arg("string"))
        public Memory with(Environment env, Memory... args) {
            Matcher matcher1 = matcher.pattern().matcher(args[0].toString());
            return new ObjectMemory(new WrapPattern(env, matcher1));
        }

        @Signature(@Arg(value = "start", optional = @Optional("null")))
        public Memory find(Environment env, Memory... args) {
            if (args[0].isNull())
                return matcher.find() ? Memory.TRUE : Memory.FALSE;
            else
                return matcher.find(args[0].toInteger()) ? Memory.TRUE : Memory.FALSE;
        }

        @Signature
        public Memory matches(Environment env, Memory... args) {
            return matcher.matches() ? Memory.TRUE : Memory.FALSE;
        }

        @Signature(@Arg(value = "string", optional = @Optional("null")))
        public Memory reset(Environment env, Memory... args) {
            if (args[0].isNull())
                matcher.reset();
            else
                matcher.reset(args[0].toString());

            return new ObjectMemory(this);
        }

        @Signature(@Arg(value = "group", optional = @Optional("null")))
        public Memory end(Environment env, Memory... args) {
            if (args[0].isNull())
                return LongMemory.valueOf(matcher.end());
            else
                return LongMemory.valueOf(matcher.end(args[0].toInteger()));
        }

        @Signature(@Arg(value = "group", optional = @Optional("null")))
        public Memory start(Environment env, Memory... args) {
            if (args[0].isNull())
                return LongMemory.valueOf(matcher.start());
            else
                return LongMemory.valueOf(matcher.start(args[0].toInteger()));
        }

        @Signature
        public Memory getGroupCount(Environment env, Memory... args) {
            return LongMemory.valueOf(matcher.groupCount());
        }

        @Signature
        public Memory hitEnd(Environment env, Memory... args) {
            return matcher.hitEnd() ? Memory.TRUE : Memory.FALSE;
        }

        @Signature
        public Memory requireEnd(Environment env, Memory... args) {
            return matcher.requireEnd() ? Memory.TRUE : Memory.FALSE;
        }

        @Signature
        public Memory lookingAt(Environment env, Memory... args) {
            return matcher.lookingAt() ? Memory.TRUE : Memory.FALSE;
        }

        @Signature({
                @Arg("start"),
                @Arg("end")
        })
        public Memory region(Environment env, Memory... args) {
            matcher.region(args[0].toInteger(), args[1].toInteger());
            return new ObjectMemory(this);
        }

        @Signature
        public Memory regionStart(Environment env, Memory... args) {
            return LongMemory.valueOf(matcher.regionStart());
        }

        @Signature
        public Memory regionEnd(Environment env, Memory... args) {
            return LongMemory.valueOf(matcher.regionEnd());
        }

        @Signature(@Arg(value = "group", optional = @Optional("null")))
        public Memory group(Environment env, Memory... args) {
            if (args[0].isNull())
                return StringMemory.valueOf(matcher.group());
            else if (args[0].isNumber())
                return StringMemory.valueOf(matcher.group(args[0].toInteger()));
            else {
                Memory l = StringMemory.toLong(args[0].toString());
                if (l != null)
                    return StringMemory.valueOf(matcher.group(l.toInteger()));

                return StringMemory.valueOf(matcher.group(args[0].toString()));
            }
        }

        @Signature(@Arg("replacement"))
        public Memory replace(Environment env, Memory... args) {
            return StringMemory.valueOf(matcher.replaceAll(args[0].toString()));
        }

        @Signature(@Arg("replacement"))
        public Memory replaceFirst(Environment env, Memory... args) {
            return StringMemory.valueOf(matcher.replaceFirst(args[0].toString()));
        }

        @Signature(@Arg(value = "callback", type = HintType.CALLABLE))
        public Memory replaceWithCallback(Environment env, Memory... args) {
            Invoker invoker = Invoker.valueOf(env, null, args[0]);
            StringBuffer sb = new StringBuffer();
            ObjectMemory self = new ObjectMemory(this);
            while (matcher.find()) {
                Memory r = invoker.callNoThrow(self);
                if (r.toValue() == Memory.FALSE)
                    break;

                matcher.appendReplacement(sb, r.toString());
            }
            matcher.appendTail(sb);
            return StringMemory.valueOf(sb.toString());
        }

        @FastMethod
        @Signature({
                @Arg("pattern"),
                @Arg(value = "string", optional = @Optional("")),
                @Arg(value = "flags", optional = @Optional("0"))
        })
        public static Memory of(Environment env, Memory... args) {
            int flags = args[2].toInteger();
            Pattern pattern = Pattern.compile(args[0].toString(), flags);
            Matcher matcher = pattern.matcher(args[1].toString());

            return ObjectMemory.valueOf(new WrapPattern(env, matcher));
        }

        @Override
        @Signature
        public Memory current(Environment env, Memory... args) {
            return current;
        }

        @Override
        @Signature
        public Memory key(Environment env, Memory... args) {
            return key;
        }

        @Override
        @Signature
        public Memory next(Environment env, Memory... args) {
            valid = matcher.find();
            if (valid) {
                current = StringMemory.valueOf(matcher.group());
                key = key.inc();
            } else {
                key = Memory.NULL;
                current = Memory.NULL;
            }

            return Memory.NULL;
        }

        @Override
        @Signature
        public Memory rewind(Environment env, Memory... args) {
            key = Memory.CONST_INT_M1;
            matcher.reset();
            next(env);
            return Memory.NULL;
        }

        @Override
        @Signature
        public Memory valid(Environment env, Memory... args) {
            return valid ? Memory.TRUE : Memory.FALSE;
        }
    }
}
