package org.develnext.jphp.zend.ext.standard.date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseException;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;

@Name("DateInterval")
public class DateInterval extends BaseObject {
    /**
     * The pattern for parsing.
     */
    private static final Pattern PATTERN =
            Pattern.compile("P(?:([0-9]+)Y)?(?:([0-9]+)M)?(?:([0-9]+)W)?(?:([0-9]+)D)?" +
                    "(T(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", Pattern.CASE_INSENSITIVE);

    @Property
    public Memory y;
    @Property
    public Memory m;
    @Property
    public Memory d;
    @Property
    public Memory h;
    @Property
    public Memory i;
    @Property
    public Memory s;
    @Property
    public Memory f;
    @Property
    public Memory invert;
    @Property
    public Memory days;

    public DateInterval(Environment env) {
        super(env);
    }

    public DateInterval(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    private static Memory parseLongMemory(String group, Memory def) {
        if (group == null)
            return def;

        return LongMemory.valueOf(Long.parseLong(group));
    }

    @Name("__set_state")
    @Signature(value = @Arg(value = "array", type = HintType.ARRAY), result = @Arg(type = HintType.OBJECT))
    public static Memory createFromArray(Environment env, Memory arg) {
        DateInterval interval = new DateInterval(env);
        ArrayMemory array = arg.toValue(ArrayMemory.class);

        interval.y = getKeyOrDefault(array, "y", Memory.CONST_INT_M1);
        interval.m = getKeyOrDefault(array, "m", Memory.CONST_INT_M1);
        interval.d = getKeyOrDefault(array, "d", Memory.CONST_INT_M1);
        interval.h = getKeyOrDefault(array, "h", Memory.CONST_INT_M1);
        interval.i = getKeyOrDefault(array, "i", Memory.CONST_INT_M1);
        interval.s = getKeyOrDefault(array, "s", Memory.CONST_INT_M1);
        interval.i = getKeyOrDefault(array, "i", Memory.CONST_INT_M1);
        interval.f = array.getByScalar("f") == null ? Memory.CONST_DOUBLE_M1 : DoubleMemory.valueOf(array.getByScalar("f").toDouble());
        interval.invert = getKeyOrDefault(array, "invert", Memory.CONST_INT_0);
        interval.days = getKeyOrDefault(array, "days", Memory.CONST_INT_M1);

        return new ObjectMemory(interval);
    }

    private static Memory getKeyOrDefault(ArrayMemory array, String key, Memory def) {
        ReferenceMemory item = array.getByScalar(key);

        if (item == null)
            return def;

        return item.toNumeric();
    }

    @Signature(value = @Arg(value = "interval_spec", type = HintType.STRING), result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, String spec) {
        Matcher matcher = PATTERN.matcher(spec);

        if (!matcher.matches())
            env.exception(traceInfo, BaseException.class, "DateInterval::__construct(): Unknown or bad format (" + spec + ")");

        y = parseLongMemory(matcher.group(1), Memory.CONST_INT_0);
        m = parseLongMemory(matcher.group(2), Memory.CONST_INT_0);

        long weeks = matcher.group(3) != null ? Long.parseLong(matcher.group(3)) : 0;

        if (weeks != 0) {
            d = LongMemory.valueOf(weeks * 7);
        }

        if (d == null) { // week not specified
            d = parseLongMemory(matcher.group(4), Memory.CONST_INT_0);
        } else if (matcher.group(4) != null) {
            d = parseLongMemory(matcher.group(4), Memory.CONST_INT_0);
        }

        h = parseLongMemory(matcher.group(6), Memory.CONST_INT_0);
        i = parseLongMemory(matcher.group(7), Memory.CONST_INT_0);
        s = parseLongMemory(matcher.group(8), Memory.CONST_INT_0);

        f = Memory.CONST_DOUBLE_0;
        invert = Memory.CONST_INT_0;
        days = Memory.FALSE;

        return new ObjectMemory(this);
    }

    @Override
    public ArrayMemory getProperties() {
        ArrayMemory props = ArrayMemory.createHashed(9);

        props.putAsKeyString("y", y);
        props.putAsKeyString("m", m);
        props.putAsKeyString("d", d);
        props.putAsKeyString("h", h);
        props.putAsKeyString("i", i);
        props.putAsKeyString("s", s);
        props.putAsKeyString("f", f);
        props.putAsKeyString("invert", invert);
        props.putAsKeyString("days", days);

        return props;
    }
}
