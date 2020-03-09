package org.develnext.jphp.zend.ext.standard.date;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.develnext.jphp.zend.ext.standard.DateFunctions;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("DatePeriod")
public class DatePeriod extends BaseObject implements Traversable {
    @Property
    public static final int EXCLUDE_START_DATE = 1;
    static final Pattern RECURRENCE_PATTERN = Pattern.compile("R([1-9]+)");
    @Property
    public Memory recurrences = Memory.NULL;
    @Property
    public boolean include_start_date;
    @Property
    public Memory start = Memory.NULL;
    @Property
    public Memory current = Memory.NULL;
    @Property
    public Memory end = Memory.NULL;
    @Property
    public Memory interval = Memory.NULL;

    public DatePeriod(Environment env) {
        super(env);
    }

    public DatePeriod(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Name("__set_state")
    @Signature(value = @Arg(type = HintType.ARRAY))
    public static Memory __set_state(Environment env, TraceInfo traceInfo, Memory array) {
        DatePeriod datePeriod = new DatePeriod(env);
        datePeriod.recurrences = array.refOfIndex(traceInfo, "recurrences");
        datePeriod.include_start_date = array.refOfIndex(traceInfo, "include_start_date").toBoolean();
        datePeriod.start = array.refOfIndex(traceInfo, "start");
        datePeriod.interval = array.refOfIndex(traceInfo, "interval");
        datePeriod.end = array.refOfIndex(traceInfo, "end");
        datePeriod.current = array.refOfIndex(traceInfo, "current");

        return new ObjectMemory(datePeriod);
    }

    @Signature(value = {
            @Arg(value = "start", type = HintType.OBJECT, nativeType = DateTimeInterface.class),
            @Arg(value = "interval", type = HintType.OBJECT, nativeType = DateInterval.class),
            @Arg(value = "end", type = HintType.OBJECT, nativeType = DateTimeInterface.class),
    })
    public Memory __construct(Environment env, Memory start, Memory interval, Memory endOrRecurrences) {
        return __construct(env, start, interval, endOrRecurrences, Memory.CONST_INT_0);
    }

    @Signature(value = {
            @Arg(value = "start", type = HintType.INT)
    })
    public Memory __construct(Environment env, Memory start, Memory interval, Memory end, Memory options) {
        this.start = start;
        this.interval = interval;
        this.include_start_date = options.toLong() != EXCLUDE_START_DATE;
        if (end.isNumber()) {
            this.recurrences = include_start_date ? end.inc() : end;
        } else {
            this.end = end;
        }
        return new ObjectMemory(this);
    }

    @Signature(@Arg(value = "isoString", type = HintType.STRING))
    public Memory __construct(Environment env, TraceInfo traceInfo, String isoString, int options) {
        String[] parts = Arrays.stream(isoString.split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);

        Messages.Item badFormat = new Messages.Item("DatePeriod::__construct(): Unknown or bad format (%s)");
        Messages.Item notContain = new Messages.Item("DatePeriod::__construct(): The ISO interval '%s' did not contain %s.");
        if (parts.length == 0 || parts.length > 3) {
            env.exception(traceInfo, badFormat.fetch(isoString));
        }

        Matcher matcher = RECURRENCE_PATTERN.matcher(parts[0]);
        if (!matcher.matches())
            env.exception(traceInfo, badFormat.fetch(isoString));

        if (parts.length == 1)
            env.exception(traceInfo, notContain.fetch(isoString, "a start date"));

        try {
            ZonedDateTime parse = ZonedDateTime.parse(parts[1]);
            if (parts.length == 2) {
                env.exception(traceInfo, notContain.fetch(isoString, "an interval"));
            }

            try {
                this.interval = new DateInterval(env).__construct(env, traceInfo, parts[2]);
                this.start = DateTime.of(env, parse);
                this.include_start_date = options != EXCLUDE_START_DATE;
                long rec = Long.parseLong(matcher.group(1));
                this.recurrences = LongMemory.valueOf(include_start_date ? ++rec : rec);
            } catch (Exception e) {
                env.exception(traceInfo, badFormat.fetch(isoString));
            }

        } catch (DateTimeParseException e) {
            env.exception(traceInfo, badFormat.fetch(isoString));
        }

        return new ObjectMemory(this);
    }

    @Signature(@Arg(value = "isoString", type = HintType.STRING))
    public Memory __construct(Environment env, TraceInfo traceInfo, String isoString) {
        return __construct(env, traceInfo, isoString, 0);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        if (recurrences.isNotNull()) {
            Memory startCopy = DateTime.of(env, start);
            Memory initial = include_start_date ? startCopy : DateFunctions.date_add(getEnvironment(), null, startCopy, interval);

            DateTimeRecurrenceIterable iterable = new DateTimeRecurrenceIterable(initial);
            return ForeachIterator.of(env, iterable);
        }

        return null;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return getNewIterator(env, false, false);
    }

    @Signature
    public Memory getStartDate() {
        return start;
    }

    @Signature
    public Memory getEndDate() {
        return end;
    }

    @Signature
    public Memory getDateInterval() {
        return interval;
    }

    @Signature
    public Memory getRecurrences() {
        return recurrences;
    }

    @Override
    public ArrayMemory getProperties() {
        ArrayMemory properties = super.getProperties();
        properties.refOfIndex("start").assign(start);
        properties.refOfIndex("current").assign(current);
        properties.refOfIndex("end").assign(end);
        properties.refOfIndex("interval").assign(interval);
        properties.refOfIndex("recurrences").assign(recurrences);
        properties.refOfIndex("include_start_date").assign(include_start_date);
        return properties;
    }

    private class DateTimeRecurrenceIterable implements Iterable<Memory> {
        private Memory next;

        private DateTimeRecurrenceIterable(Memory initialMemory) {
            this.next = initialMemory;
        }

        @Override
        public Iterator<Memory> iterator() {
            return new Iterator<Memory>() {
                private int cr = DatePeriod.this.recurrences.toInteger();

                @Override
                public boolean hasNext() {
                    boolean next = include_start_date ? cr > 0 : cr >= 0;
                    if (!next)
                        current = Memory.NULL;

                    return next;
                }

                @Override
                public Memory next() {
                    cr--;
                    current = current.isNull() ? next : DateFunctions.date_add(getEnvironment(), null, next, interval);

                    return current;
                }
            };
        }
    }
}
