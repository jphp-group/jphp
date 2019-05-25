package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.zone.ZoneOffsetTransition;
import java.util.NoSuchElementException;
import java.util.Set;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Name("DateTime")
public class DateTime extends BaseDateTime {

    private ObjectMemory $this;

    public DateTime(Environment env) {
        super(env);
    }

    public DateTime(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    /**
     * @param env
     * @param nativeDateTime
     *
     * @return
     */
    public static Memory of(Environment env, ZonedDateTime nativeDateTime) {
        DateTime dateTime = new DateTime(env);
        dateTime.nativeDateTime = nativeDateTime;
        dateTime.$this = new ObjectMemory(dateTime);

        return dateTime.$this;
    }

    /**
     * @param env
     * @param memory
     *
     * @return
     */
    public static Memory of(Environment env, Memory memory) {
        if (!memory.isObject())
            return Memory.UNDEFINED;

        DateTime dateTime = new DateTime(env);
        dateTime.$this = new ObjectMemory(dateTime);

        ObjectMemory objectMemory = memory.toValue(ObjectMemory.class);
        if (objectMemory.value instanceof BaseDateTime) {
            dateTime.nativeDateTime = ((BaseDateTime) objectMemory.value).nativeDateTime;
        }

        return dateTime.$this;
    }

    @Signature(value = {
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "time", type = HintType.STRING),
            @Arg(value = "timezone", type = HintType.OBJECT, optional = @Optional("NULL"))
    }, result = @Arg(type = HintType.OBJECT))
    public static Memory createFromFormat(Environment env, TraceInfo traceInfo,
                                          Memory format,
                                          Memory time,
                                          Memory timezone) {
        try {
            ZonedDateTime parse = DateFormat.createFromFormat(format.toString(), time.toString(),
                    ZonedDateTime.now(toZoneId(env, traceInfo, timezone)));
            DateTime dateTime = new DateTime(env);
            dateTime.nativeDateTime = parse;
            dateTime.$this = new ObjectMemory(dateTime);
            return dateTime.$this;
        } catch (DateTimeException | NoSuchElementException | IllegalArgumentException e) {
            return Memory.FALSE;
        }
    }

    @Signature(value = {
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "time", type = HintType.STRING)
    }, result = @Arg(type = HintType.OBJECT))
    public static Memory createFromFormat(Environment env, TraceInfo traceInfo,
                                          Memory format,
                                          Memory time) {
        return createFromFormat(env, traceInfo, format, time, Memory.NULL);
    }

    @Signature(result = @Arg(type = HintType.ARRAY))
    public static Memory getLastErrors(Environment env, TraceInfo traceInfo) {
        return DateTimeMessageContainer.getMessages();
    }

    @Name("__set_state")
    @Signature(value = @Arg(value = "array", type = HintType.ARRAY),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public static Memory fromState(Environment env, TraceInfo traceInfo, ArrayMemory array) {
        StringMemory date = array.refOfIndex(traceInfo, "date").toValue(StringMemory.class);

        DateTime dateTime = new DateTime(env);
        ZoneId timezone = ZoneIdFactory.of(array.refOfIndex(traceInfo, "timezone").toString());
        dateTime.nativeDateTime = LocalDateTime.parse(date.toString(), DEFAULT_FORMATTER).atZone(timezone);
        return dateTime.$this = new ObjectMemory(dateTime);
    }

    @Signature(value = {
            @Arg(value = "time", type = HintType.STRING, optional = @Optional("now")),
            @Arg(value = "timezone", type = HintType.OBJECT, optional = @Optional("NULL"))
    }, result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, Memory time, Memory timeZone) {
        try {
            DateTimeParseResult result = parse(env, traceInfo, time, timeZone, ZonedDateTime.now());
            this.nativeDateTime = result.dateTime();
            this.parsedZone = result.parsedZone();
        } catch (DateTimeParserException e) {
            return Memory.FALSE;
        }

        return ($this = new ObjectMemory(this));
    }

    @Signature(value = {
            @Arg(value = "time", type = HintType.STRING, optional = @Optional("now"))
    }, result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, Memory time) {
        return __construct(env, traceInfo, time, Memory.NULL);
    }

    @Signature(result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo) {
        return __construct(env, traceInfo, StringMemory.valueOf("now"), Memory.NULL);
    }

    @Signature(value = {
            @Arg(value = "interval", type = HintType.OBJECT, nativeType = DateInterval.class)
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory add(Environment env, TraceInfo traceInfo, DateInterval interval) {
        ZonedDateTime result = nativeDateTime
                .plusYears(interval.y.toLong())
                .plusMonths(interval.m.toLong())
                .plusDays(interval.d.toLong())
                .plusHours(interval.h.toLong())
                .plusMinutes(interval.i.toLong())
                .plusSeconds(interval.s.toLong());

        LocalDateTime localDateTime = result.toLocalDateTime();
        ZoneOffsetTransition transition = result.getZone().getRules().getTransition(localDateTime);
        if (transition != null && transition.isOverlap() &&
                localDateTime.isAfter(transition.getDateTimeAfter()) &&
                localDateTime.isBefore(transition.getDateTimeBefore())) {

            //result = result.plus(transition.getDuration());
        }
        this.nativeDateTime = result;

        return $this;
    }

    @Signature(value = @Arg(value = "modify", type = HintType.STRING),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory modify(Environment env, TraceInfo traceInfo, String modify) {
        try {
            DateTimeParseResult result = new DateTimeParser(modify, nativeDateTime).parseResult();
            Set<TemporalField> modified = result.modified();
            if (modified.contains(MICRO_OF_SECOND) || modified.contains(MILLI_OF_SECOND)) {
                nativeDateTime = result.dateTime();
            } else {
                nativeDateTime = result.dateTime().with(MICRO_OF_SECOND, nativeDateTime.get(MICRO_OF_SECOND));
            }

            if (result.hasParsedZone()) {
                parsedZone = result.parsedZone();
            }
        } catch (DateTimeParserException e) {
            env.warning(traceInfo, "DateTime::modify(): %s: Unexpected character", e.getMessage());
            return Memory.FALSE;
        }
        return $this;
    }

    @Signature(value = {
            @Arg(value = "year", type = HintType.INT),
            @Arg(value = "month", type = HintType.INT),
            @Arg(value = "day", type = HintType.INT),
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setDate(Environment env, TraceInfo traceInfo, int year, int month, int day) {
        nativeDateTime = nativeDateTime.withYear(year);
        if (month > 12) {
            int years = month / 12;
            int monthRem = month % 12;
            if (monthRem > 0)
                ++years;

            nativeDateTime = nativeDateTime.plusYears(Math.max(years - 1, 1)).withMonth(monthRem == 0 ? 12 : monthRem);
        } else if (month <= 0) {
            nativeDateTime = nativeDateTime.withMonth(1).plusMonths(--month);
        } else {
            nativeDateTime = nativeDateTime.withMonth(month);
        }

        if (day > getMonthLength()) {
            nativeDateTime = nativeDateTime.withDayOfMonth(1).plusDays(--day);
        } else if (day <= 0) {
            nativeDateTime = nativeDateTime.minusMonths(1);
            nativeDateTime = nativeDateTime.withDayOfMonth(getMonthLength()).plusDays(day);
        } else {
            nativeDateTime = nativeDateTime.withDayOfMonth(day);
        }

        return $this;
    }

    private int getMonthLength() {
        return nativeDateTime.getMonth().length(Year.isLeap(nativeDateTime.getYear()));
    }

    @Signature(value = {
            @Arg(value = "year", type = HintType.INT),
            @Arg(value = "week", type = HintType.INT),
            @Arg(value = "day", type = HintType.INT, optional = @Optional("1")),
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setISODate(Environment env, TraceInfo traceInfo, int year, int week, int day) {
        ZonedDateTime firstDoy = nativeDateTime.withYear(year).withDayOfYear(1);
        int dayOffset = day + ((week - 1) * 7) + day;
        nativeDateTime = firstDoy.plusDays(dayOffset).with(DAY_OF_WEEK, day);

        return $this;
    }

    @Signature(value = {
            @Arg(value = "year", type = HintType.INT),
            @Arg(value = "week", type = HintType.INT),
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setISODate(Environment env, TraceInfo traceInfo, int year, int week) {
        return setISODate(env, traceInfo, year, week, 1);
    }

    @Signature(result = @Arg(type = HintType.SELF, nativeType = DateTime.class))
    public Memory setTime(Environment env, TraceInfo traceInfo, int hour, int minute, int second, int micosecond) {
        nativeDateTime = nativeDateTime
                .withHour(hour)
                .withMinute(minute)
                .withSecond(second)
                .with(ChronoField.MICRO_OF_SECOND, micosecond);

        return $this;
    }

    @Signature(result = @Arg(type = HintType.SELF, nativeType = DateTime.class))
    public Memory setTime(Environment env, TraceInfo traceInfo, int hour, int minute, int second) {
        return setTime(env, traceInfo, hour, minute, second, 0);
    }

    @Signature(result = @Arg(type = HintType.SELF, nativeType = DateTime.class))
    public Memory setTime(Environment env, TraceInfo traceInfo, int hour, int minute) {
        return setTime(env, traceInfo, hour, minute, 0, 0);
    }

    @Signature(value = @Arg(value = "unixtimestamp", type = HintType.INT),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setTimestamp(Environment env, TraceInfo traceInfo, long timestamp) {
        nativeDateTime = Instant.ofEpochSecond(timestamp).atZone(nativeDateTime.getZone());
        return $this;
    }

    @Signature(value = @Arg(value = "timezone", type = HintType.OBJECT, nativeType = DateTimeZone.class),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setTimezone(Environment env, TraceInfo traceInfo, DateTimeZone timezone) {
        nativeDateTime = nativeDateTime.withZoneSameInstant(timezone.getNativeZone());
        parsedZone = null;
        return $this;
    }

    @Signature(value = @Arg(value = "interval", type = HintType.OBJECT, nativeType = DateInterval.class),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory sub(Environment env, TraceInfo traceInfo, DateInterval interval) {
        ZonedDateTime result = nativeDateTime
                .minusYears(interval.y.toLong())
                .minusMonths(interval.m.toLong())
                .minusDays(interval.d.toLong())
                .minusHours(interval.h.toLong())
                .minusMinutes(interval.i.toLong())
                .minusSeconds(interval.s.toLong());

        if (!nativeDateTime.getOffset().equals(result.getOffset())) {
            parsedZone = null;
        }

        nativeDateTime = result;
        return $this;
    }

    @Override
    @Signature(value = {
            @Arg(value = "datetime2", type = HintType.OBJECT, typeClass = "DateTimeInterface"),
            @Arg(value = "absolute", type = HintType.BOOLEAN, optional = @Reflection.Optional("FALSE"))
    }, result = @Arg(type = HintType.OBJECT, typeClass = "DateInterval"))
    public Memory diff(Environment env, TraceInfo t, Memory dateTimeInterface) {
        return super.diff(env, t, dateTimeInterface);
    }

    @Override
    @Signature(value = {@Arg(value = "format", type = HintType.STRING)}, result = @Arg(type = HintType.STRING))
    public Memory format(Environment env, TraceInfo traceInfo, String format) {
        return super.format(env, traceInfo, format);
    }

    @Override
    @Signature(result = @Arg(type = HintType.INT))
    public Memory getOffset(Environment env, TraceInfo traceInfo) {
        return super.getOffset(env, traceInfo);
    }

    @Override
    @Signature(result = @Arg(type = HintType.INT))
    public Memory getTimestamp(Environment env, TraceInfo traceInfo) {
        return super.getTimestamp(env, traceInfo);
    }

    @Override
    @Signature(result = @Arg(type = HintType.VOID))
    public Memory __wakeup(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }
}
