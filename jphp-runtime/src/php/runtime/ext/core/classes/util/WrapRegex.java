package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\util\\Regex")
final public class WrapRegex extends BaseObject implements Iterator {
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
    protected String input;
    protected Memory current;
    protected Memory key;
    protected boolean valid = true;

    public WrapRegex(Environment env, Matcher matcher, String input) {
        super(env);
        this.matcher = matcher;
        this.input = input;
    }

    public WrapRegex(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg("pattern"),
            @Arg(value = "flags", optional = @Reflection.Optional("0")),
            @Arg(value = "string", optional = @Reflection.Optional(""))
    })
    public Memory __construct(Environment env, Memory... args) {
        int flags = convertFlags(args[1]);
        Pattern pattern = Pattern.compile(args[0].toString(), flags);
        matcher = pattern.matcher(input = args[2].toString());

        return Memory.NULL;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public String getInput() {
        return input;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*pattern").assign(matcher.pattern().toString());
        r.refOfIndex("*flags").assign(matcher.pattern().flags());
        r.refOfIndex("*input").assign(input);
        return r.toConstant();
    }

    @Signature(@Arg("string"))
    public Memory with(Environment env, Memory... args) {
        Matcher matcher1 = matcher.pattern().matcher(args[0].toString());
        return new ObjectMemory(new WrapRegex(env, matcher1, args[0].toString()));
    }

    @Signature(@Arg(value = "start", optional = @Reflection.Optional("null")))
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

    @FastMethod
    @Signature(@Arg("string"))
    public Memory test(Environment env, Memory... args) {
        Matcher matcher1 = matcher.pattern().matcher(args[0].toString());
        return TrueMemory.valueOf(matcher1.matches());
    }

    @Signature(@Arg(value = "string", optional = @Reflection.Optional("null")))
    public Memory reset(Environment env, Memory... args) {
        if (args[0].isNull())
            matcher.reset();
        else
            matcher.reset(args[0].toString());

        return new ObjectMemory(this);
    }

    @Signature(@Arg(value = "group", optional = @Reflection.Optional("null")))
    public Memory end(Environment env, Memory... args) {
        if (args[0].isNull())
            return LongMemory.valueOf(matcher.end());
        else
            return LongMemory.valueOf(matcher.end(args[0].toInteger()));
    }

    @Signature(@Arg(value = "group", optional = @Reflection.Optional("null")))
    public Memory start(Environment env, Memory... args) {
        if (args[0].isNull())
            return LongMemory.valueOf(matcher.start());
        else
            return LongMemory.valueOf(matcher.start(args[0].toInteger()));
    }

    @FastMethod
    @Signature
    public Memory getGroupCount(Environment env, Memory... args) {
        return LongMemory.valueOf(matcher.groupCount());
    }

    @FastMethod
    @Signature
    public Memory hitEnd(Environment env, Memory... args) {
        return matcher.hitEnd() ? Memory.TRUE : Memory.FALSE;
    }

    @FastMethod
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

    @FastMethod
    @Signature
    public Memory regionStart(Environment env, Memory... args) {
        return LongMemory.valueOf(matcher.regionStart());
    }

    @FastMethod
    @Signature
    public Memory regionEnd(Environment env, Memory... args) {
        return LongMemory.valueOf(matcher.regionEnd());
    }

    @Signature(@Arg(value = "start", optional = @Reflection.Optional("null")))
    public Memory all(Environment env, Memory... args) {
        int start = args[0].toInteger();

        int count = 0;

        ArrayMemory r = new ArrayMemory();

        while ((count == 0 ? matcher.find(start) : matcher.find())) {
            count++;

            r.add(groups(env));
        }

        return r.toConstant();
    }

    @Signature(@Arg(value = "start", optional = @Reflection.Optional("null")))
    public Memory last(Environment env, Memory... args) {
        int count = 0, index = args[0].toInteger();

        while ((count == 0 ? matcher.find(index) : matcher.find())) {
            index = matcher.start();

            count++;
        }

        if (count > 0) {
            matcher.find(index);
            return groups(env);
        } else {
            return Memory.NULL;
        }
    }

    @Signature(@Arg(value = "start", optional = @Reflection.Optional("null")))
    public Memory first(Environment env, Memory... args) {
        return one(env, args);
    }

    @Signature(@Arg(value = "start", optional = @Reflection.Optional("null")))
    public Memory one(Environment env, Memory... args) {
        int start = args[0].toInteger();

        if (matcher.find(start)) {
            return groups(env);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory groups(Environment env, Memory... args) {
        int count = matcher.groupCount();

        ArrayMemory r = new ArrayMemory();

        r.add(matcher.group());

        for (int i = 0; i < count; i++) {
            r.add(matcher.group(i + 1));
        }

        return r.toConstant();
    }

    @Signature(@Arg(value = "group", optional = @Reflection.Optional("null")))
    public Memory group(Environment env, Memory... args) {
        Memory group = args[0];

        if (group.isNull())
            return StringMemory.valueOf(matcher.group());
        else {
            if (group.isString() || group.isObject()) {
                Memory longMemory = StringMemory.toLong(group.toString(), false);

                if (longMemory == null) {
                    return StringMemory.valueOf(matcher.group(group.toString()));
                }
            }

            return StringMemory.valueOf(matcher.group(group.toInteger()));
        }
    }

    @Signature
    public Memory groupNames() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Integer> namedGroups = getNamedGroups(matcher.pattern());

        ArrayMemory r = new ArrayMemory();

        for (String s : namedGroups.keySet()) {
            r.put(s, StringMemory.valueOf(s));
        }

        return r.toConstant();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Integer> getNamedGroups(Pattern regex)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
        namedGroupsMethod.setAccessible(true);

        Map<String, Integer> namedGroups = null;
        return (Map<String, Integer>) namedGroupsMethod.invoke(regex);
    }

    @Signature(@Arg("replacement"))
    public Memory replace(Environment env, Memory... args) {
        try {
            return StringMemory.valueOf(matcher.replaceAll(args[0].toString()));
        } catch (IllegalStateException | IndexOutOfBoundsException e) {
            throw new RegexException(env, e);
        }
    }

    @Signature(@Arg("replacement"))
    public Memory replaceFirst(Environment env, Memory... args) {
        return StringMemory.valueOf(matcher.replaceFirst(args[0].toString()));
    }

    @Signature({@Arg("group"), @Arg("replacement")})
    public Memory replaceGroup(Environment env, Memory... args) {
        int group = args[0].toInteger();

        matcher.reset();

        if (matcher.find()) {
            String what = matcher.group(group);
            return StringMemory.valueOf(input.replace(what, args[1].toString()));
        }

        return StringMemory.valueOf(input);
    }

    @Signature(@Arg(value = "callback", type = HintType.CALLABLE))
    public Memory replaceWithCallback(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[0]);
        StringBuffer sb = new StringBuffer();
        ObjectMemory self = new ObjectMemory(this);

        int i = 0;

        while (matcher.find()) {
            Memory r = invoker.callNoThrow(self, LongMemory.valueOf(i));
            if (r.toValue() == Memory.FALSE)
                break;

            matcher.appendReplacement(sb, r.toString());
            i++;
        }
        matcher.appendTail(sb);
        return StringMemory.valueOf(sb.toString());
    }

    @Signature
    public Memory getPattern(Environment env, Memory... args) {
        return StringMemory.valueOf(matcher.pattern().pattern());
    }

    @Signature
    public Memory getInput(Environment env, Memory... args) {
        return StringMemory.valueOf(getInput());
    }

    @Signature
    public Memory getFlags(Environment env, Memory... args) {
        return LongMemory.valueOf(matcher.pattern().flags());
    }

    @Signature({
            @Arg("pattern"),
            @Arg(value = "flags", optional = @Reflection.Optional("0")),
            @Arg(value = "string", optional = @Reflection.Optional(""))
    })
    public static Memory of(Environment env, Memory... args) {
        int flags = convertFlags(args[1]);
        Pattern pattern = Pattern.compile(args[0].toString(), flags);
        Matcher matcher = pattern.matcher(args[2].toString());

        return ObjectMemory.valueOf(new WrapRegex(env, matcher, ""));
    }

    @Signature(@Arg(value = "flags"))
    public Memory withFlags(Environment env, Memory... args) {
        int flags = convertFlags(args[0]);
        Pattern pattern = Pattern.compile(matcher.pattern().pattern(), flags);
        Matcher matcher1 = pattern.matcher(input);

        return ObjectMemory.valueOf(new WrapRegex(env, matcher1, input));
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        return current == null ? Memory.NULL : current;
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return key == null ? Memory.NULL : key;
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

    @Signature({
            @Arg("pattern"),
            @Arg("string"),
            @Arg(value = "flags", optional = @Optional("0"))
    })
    public static Memory match(Environment env, Memory... args) {
        Pattern pattern = Pattern.compile(args[0].toString(), convertFlags(args[2]));

        return pattern.matcher(args[1].toString()).find() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({
            @Arg("pattern"),
            @Arg("string"),
            @Arg(value = "limit", optional = @Reflection.Optional("0"))
    })
    public static Memory split(Environment env, Memory... args) {
        String[] r;
        int limit = args[2].toInteger();

        if (limit <= 0)
            r = args[1].toString().split(args[0].toString());
        else
            r = args[1].toString().split(args[0].toString(), limit);

        return ArrayMemory.ofStrings(r).toConstant();
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

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }

    private static int convertFlags(Memory _flags) {
        int result = 0;

        if (_flags.isNumber()) {
            return _flags.toInteger();
        }

        String flags = _flags.toString();

        if (StringMemory.toLong(flags) != null) {
            return _flags.toInteger();
        }

        for (int i = 0; i < flags.length(); i++) {
            char c = flags.charAt(i);

            switch (c) {
                case 'i':
                    result |= CASE_INSENSITIVE;
                    break;
                case 'm':
                    result |= MULTILINE;
                    break;
                case 'L':
                    result |= LITERAL;
                    break;
                case 'd':
                    result |= UNIX_LINES;
                    break;
                case 'u':
                    result |= UNICODE_CASE;
                    break;
                case 'U':
                    result |= Pattern.UNICODE_CHARACTER_CLASS;
                    break;
                case 'x':
                    result |= COMMENTS;
                    break;
                case 's':
                    result |= DOTALL;
                    break;
            }
        }

        return result;
    }

    @Name("php\\util\\RegexException")
    public static class RegexException extends JavaException {
        public RegexException(Environment env, Throwable throwable) {
            super(env, throwable);
        }

        public RegexException(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }
    }
}
