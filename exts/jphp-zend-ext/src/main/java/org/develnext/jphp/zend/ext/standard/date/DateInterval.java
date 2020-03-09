package org.develnext.jphp.zend.ext.standard.date;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Name("DateInterval")
public class DateInterval extends BaseObject {
    static final String FAILED = "Failed to parse interval";
    static final String BAD_FORMAT = "Unknown or bad format";
    private static final String MESSAGE = "DateInterval::__construct(): %s (%s)";
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
        if (group == null) {
            return def;
        }

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

    @Signature(value = @Arg(value = "time", type = HintType.STRING), result = @Arg(type = HintType.OBJECT))
    public static Memory createFromDateString(Environment env, TraceInfo traceInfo, Memory arg) {
        try {
            DateTimeParseResult result = new DateTimeParser(arg.toString()).parseResult();
            ArrayMemory referenceMemories = result.toArrayMemory();
            DateInterval dateInterval = new DateInterval(env);
            Memory relative = referenceMemories.refOfIndex("relative");

            dateInterval.y = relative.refOfIndex("year").toValue(LongMemory.class);
            dateInterval.m = relative.refOfIndex("month").toValue(LongMemory.class);
            dateInterval.d = relative.refOfIndex("day").toValue(LongMemory.class);
            dateInterval.h = relative.refOfIndex("hour").toValue(LongMemory.class);
            dateInterval.i = relative.refOfIndex("minute").toValue(LongMemory.class);
            dateInterval.s = relative.refOfIndex("second").toValue(LongMemory.class);
            dateInterval.f = Memory.CONST_DOUBLE_0;
            dateInterval.invert = Memory.CONST_INT_0;
            dateInterval.days = Memory.FALSE;

            return new ObjectMemory(dateInterval);
        } catch (DateTimeParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Memory getKeyOrDefault(ArrayMemory array, String key, Memory def) {
        ReferenceMemory item = array.getByScalar(key);

        if (item == null) {
            return def;
        }

        return item.toNumeric();
    }

    private static Memory unsignedLongMemory(long l) {
        return LongMemory.valueOf(Math.abs(l));
    }

    @Signature(value = @Arg(value = "interval_spec", type = HintType.STRING), result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, String spec) {
        int slashIdx = spec.indexOf('/') == -1 ? spec.indexOf(' ') : spec.indexOf('/');

        if (slashIdx != -1) {
            initWithDates(env, traceInfo, spec, slashIdx);
        } else {
            initWithIsoSpec(env, traceInfo, spec);
        }

        return new ObjectMemory(this);
    }

    private void initWithDates(Environment env, TraceInfo traceInfo, String spec, int slashIdx) {
        String startStr = spec.substring(0, slashIdx);
        String endStr = spec.substring(slashIdx + 1);

        if (endStr.trim().isEmpty()) {
            env.exception(traceInfo, MESSAGE, FAILED, spec);
        }

        try {
            ZonedDateTime start = ZonedDateTime.parse(startStr);
            ZonedDateTime end;

            try {
                end = ZonedDateTime.parse(endStr);
            } catch (DateTimeParseException e) {
                env.exception(traceInfo, MESSAGE, BAD_FORMAT, spec);
                return;
            }

            invert = start.isAfter(end) ? Memory.CONST_INT_1 : Memory.CONST_INT_0;

            if (invert == Memory.CONST_INT_1) {
                ZonedDateTime tmp = start;
                start = end;
                end = tmp;
            }

            ZonedDateTime tempDateTime = ZonedDateTime.from(start);

            y = unsignedLongMemory(tempDateTime.until(end, ChronoUnit.YEARS));
            tempDateTime = tempDateTime.plusYears(y.toLong());

            m = unsignedLongMemory(tempDateTime.until(end, ChronoUnit.MONTHS));
            tempDateTime = tempDateTime.plusMonths(m.toLong());

            d = unsignedLongMemory(tempDateTime.until(end, ChronoUnit.DAYS));
            tempDateTime = tempDateTime.plusDays(d.toLong());

            h = unsignedLongMemory(tempDateTime.until(end, ChronoUnit.HOURS));
            tempDateTime = tempDateTime.plusHours(h.toLong());

            i = unsignedLongMemory(tempDateTime.until(end, ChronoUnit.MINUTES));
            tempDateTime = tempDateTime.plusMinutes(i.toLong());

            s = unsignedLongMemory(tempDateTime.until(end, ChronoUnit.SECONDS));
            tempDateTime = tempDateTime.plusSeconds(s.toLong());

            long abs = Math.abs(tempDateTime.until(end, ChronoUnit.MICROS));
            f = DoubleMemory.valueOf(abs / 1000000.0);

            this.days = LongMemory.valueOf(Math.abs(Duration.between(start, end).toDays()));
        } catch (DateTimeParseException e) {
            env.exception(traceInfo, MESSAGE, FAILED, spec);
        }
    }

    private void initWithIsoSpec(Environment env, TraceInfo traceInfo, String spec) {
        Matcher matcher = PATTERN.matcher(spec);

        if (!matcher.matches()) {

            try {
                ZonedDateTime.parse(spec);
                env.exception(traceInfo, BaseException.class, MESSAGE, FAILED, spec);
            } catch (DateTimeParseException ignore) {
                env.exception(traceInfo, BaseException.class, MESSAGE, BAD_FORMAT, spec);
            }

        }

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
    }

    @Signature(value = @Arg(value = "format", type = HintType.STRING), result = @Arg(type = HintType.STRING))
    public Memory format(Environment env, TraceInfo traceInfo, String format) {
        StringBuilder buff = new StringBuilder();

        for (int i = 0, n = format.length(); i < n; i++) {
            char c = format.charAt(i);

            if (c == '%') {
                if (i + 1 >= n) {
                    continue;
                }

                c = format.charAt(++i);
                switch (c) {
                    case 'Y':
                        min2Digit(buff, y.toLong());
                        break;
                    case 'y':
                        buff.append(y.toLong());
                        break;
                    case 'M':
                        min2Digit(buff, m.toLong());
                        break;
                    case 'm':
                        buff.append(m.toLong());
                        break;
                    case 'D':
                        min2Digit(buff, d.toLong());
                        break;
                    case 'd':
                        buff.append(d.toLong());
                        break;
                    case 'a':
                        if (days == Memory.CONST_INT_M1 || days == Memory.FALSE) {
                            buff.append("(unknown)");
                        } else {
                            buff.append(days.toLong());
                        }
                        break;
                    case 'H':
                        min2Digit(buff, h.toLong());
                        break;
                    case 'h':
                        buff.append(h.toLong());
                        break;
                    case 'I':
                        min2Digit(buff, this.i.toLong());
                        break;
                    case 'i':
                        buff.append(this.i.toLong());
                        break;
                    case 'S':
                        min2Digit(buff, s.toLong());
                        break;
                    case 's':
                        buff.append(s.toLong());
                        break;
                    case 'F':
                        buff.append(String.format(env.getLocale(), "%06d", f.toLong()));
                        break;
                    case 'f':
                        buff.append(f.toLong());
                        break;
                    case 'R':
                        buff.append(invert.toLong() >= 0 ? '+' : '-');
                        break;
                    case 'r':
                        if (invert.toLong() < 0) {
                            buff.append('-');
                        }
                        break;
                    default:
                        buff.append('%');
                        if (c != '%') {
                            buff.append(c);
                        }
                }
            } else {
                buff.append(c);
            }
        }

        return new StringMemory(buff.toString());
    }

    private void min2Digit(StringBuilder buff, long years) {
        if (years < 10) {
            buff.append('0');
        }

        buff.append(years);
    }

    @Override
    public ArrayMemory getProperties() {
        ArrayMemory props = super.getProperties();
        props.checkCopied();

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
