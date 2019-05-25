package org.develnext.jphp.zend.ext.standard.date;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

import org.develnext.jphp.zend.ext.standard.DateFunctions;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Ignore;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

abstract class BaseDateTime extends BaseObject implements DateTimeInterface, IComparableObject<DateTimeInterface> {
    @Ignore
    static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS]");
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60;
    private static final int SECONDS_IN_DAY = SECONDS_IN_HOUR * 24;
    private static final int SECONDS_IN_MONTH = 31 * SECONDS_IN_DAY;
    private static final int SECONDS_IN_YEAR = 365 * SECONDS_IN_DAY;
    ZonedDateTime nativeDateTime;
    String parsedZone;

    public BaseDateTime(Environment env) {
        super(env);
    }

    public BaseDateTime(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public static DateTimeParseResult parse(Environment env, TraceInfo traceInfo, Memory time, Memory timeZone,
                                            ZonedDateTime baseDateTime) throws DateTimeParserException {
        String timeStr = time.isNull() ? "now": time.toString();
        baseDateTime = baseDateTime.withZoneSameLocal(toZoneId(env, traceInfo, timeZone));
        if ("now".equals(timeStr)) {
            return new DateTimeParseResult(baseDateTime, Collections.emptySet(), null, null);
        } else {
            return new DateTimeParser(timeStr, baseDateTime).parseResult();
        }
    }

    static ZoneId toZoneId(Environment env, TraceInfo traceInfo, Memory timeZone) {
        ZoneId zone;
        if (timeZone.isNull()) {
            zone = ZoneId.of(DateFunctions.date_default_timezone_get(env, traceInfo).toString());
        } else if (timeZone.isString()) {
            zone = ZoneIdFactory.of(timeZone.toString());
        } else {
            zone = ((DateTimeZone) timeZone.toValue(ObjectMemory.class).value).getNativeZone();
        }
        return zone;
    }

    @Override
    public ArrayMemory getProperties() {
        ArrayMemory props = super.getProperties();
        Stream.of("date", "timezone_type", "timezone").forEach(s -> props.refOfIndex(s).assign(Memory.UNDEFINED));

        if (nativeDateTime != null) {
            props.refOfIndex("date").assign(DEFAULT_FORMATTER.format(nativeDateTime));
            Memory timezoneType = props.refOfIndex("timezone_type");
            if (parsedZone == null) {
                timezoneType.assign(DateTimeZone.getTimeZoneType(nativeDateTime.getZone()));
            } else {
                timezoneType.assign(ZoneIdFactory.isAbbreviation(parsedZone) ? 2 : DateTimeZone.getTimeZoneType(ZoneId.of(parsedZone)));
            }

            props.refOfIndex("timezone")
                    .assign(parsedZone == null ? nativeDateTime.getZone().toString() : parsedZone);
        }

        return props;
    }

    @Override
    @Signature(value = {
            @Arg(value = "datetime2", type = HintType.OBJECT, typeClass = "DateTimeInterface"),
            @Arg(value = "absolute", type = HintType.BOOLEAN, optional = @Reflection.Optional("FALSE"))
    }, result = @Arg(type = HintType.OBJECT, typeClass = "DateInterval"))
    public Memory diff(Environment env, TraceInfo t, Memory dateTimeInterface) {
        BaseDateTime dateTime = dateTimeInterface.toObject(BaseDateTime.class);

        long diff = dateTime.nativeDateTime.toEpochSecond() - nativeDateTime.toEpochSecond();
        long diffAbs = Math.abs(diff);
        long offsetDiff = 0;

        if (!dateTime.getOffset(env, t).equals(getOffset(env, t))) {
            offsetDiff = Duration.ofSeconds(dateTime.getOffset(env, t).toLong())
                    .minus(Duration.ofSeconds(getOffset(env, t).toLong()))
                    .getSeconds();

            //diffAbs += offsetDiff;
        }

        long years = diffAbs / SECONDS_IN_YEAR;
        if (years != 0) {
            diffAbs -= (years * SECONDS_IN_YEAR);
        }

        long months = diffAbs / SECONDS_IN_MONTH;
        if (months != 0) {
            diffAbs -= (months * SECONDS_IN_MONTH);
        }

        long days = diffAbs / SECONDS_IN_DAY;
        if (days != 0) {
            diffAbs -= (days * SECONDS_IN_DAY);
        }

        long hours = diffAbs / SECONDS_IN_HOUR;
        if (hours != 0) {
            diffAbs -= hours * SECONDS_IN_HOUR;
        }

        long minutes = diffAbs / SECONDS_IN_MINUTE;
        if (minutes != 0) {
            diffAbs -= minutes * SECONDS_IN_MINUTE;
        }

        DateInterval interval = new DateInterval(env);
        interval.y = LongMemory.valueOf(years);
        interval.m = LongMemory.valueOf(months);
        interval.d = LongMemory.valueOf(days);
        interval.h = LongMemory.valueOf(hours);
        interval.i = LongMemory.valueOf(minutes);
        interval.s = LongMemory.valueOf(diffAbs);
        int f = dateTime.nativeDateTime.get(ChronoField.MICRO_OF_SECOND) - nativeDateTime.get(ChronoField.MICRO_OF_SECOND);
        interval.f = DoubleMemory.valueOf(f * 0.000001);
        interval.days = LongMemory.valueOf(Math.abs(Duration.ofSeconds(diff).toDays()));

        interval.invert = diff > 0 ? Memory.CONST_INT_0 : Memory.CONST_INT_1;
        return new ObjectMemory(interval);
    }

    @Override
    @Signature(value = {@Arg(value = "format", type = HintType.STRING)}, result = @Arg(type = HintType.STRING))
    public Memory format(Environment env, TraceInfo traceInfo, String format) {
        return StringMemory.valueOf(DateFormat.formatForDateFunction(env,
                new DateTimeParseResult(nativeDateTime, Collections.emptySet(), parsedZone, null), format));
    }

    @Override
    @Signature(result = @Arg(type = HintType.INT))
    public Memory getOffset(Environment env, TraceInfo traceInfo) {
        return LongMemory.valueOf(nativeDateTime.getOffset().getTotalSeconds());
    }

    @Override
    @Signature(result = @Arg(type = HintType.INT))
    public Memory getTimestamp(Environment env, TraceInfo traceInfo) {
        return LongMemory.valueOf(nativeDateTime.toEpochSecond());
    }

    @Override
    @Signature(result = @Arg(type = HintType.OBJECT))
    public Memory getTimezone(Environment env, TraceInfo traceInfo) {
        // TODO reuse same object if possible
        DateTimeZone timezoneObject = new DateTimeZone(env);
        StringMemory timezone = StringMemory.valueOf(nativeDateTime.getZone().getId()).toValue(StringMemory.class);
        Memory ret = timezoneObject.__construct(env, traceInfo, timezone);
        return ret;
    }

    @Override
    public Memory __wakeup(Environment env, TraceInfo traceInfo) {
        return Memory.NULL;
    }

    @Override
    public boolean __equal(DateTimeInterface iObject) {
        BaseDateTime dateTime = (BaseDateTime) iObject;
        return Objects.equals(dateTime.nativeDateTime, nativeDateTime);
    }

    @Override
    public boolean __identical(DateTimeInterface iObject) {
        return iObject == this;
    }

    @Override
    public boolean __greater(DateTimeInterface iObject) {
        BaseDateTime dateTime = (BaseDateTime) iObject;
        return dateTime.nativeDateTime.compareTo(nativeDateTime) < 0;
    }

    @Override
    public boolean __greaterEq(DateTimeInterface iObject) {
        BaseDateTime dateTime = (BaseDateTime) iObject;
        return dateTime.nativeDateTime.compareTo(nativeDateTime) <= 0;
    }

    @Override
    public boolean __smaller(DateTimeInterface iObject) {
        BaseDateTime dateTime = (BaseDateTime) iObject;
        return dateTime.nativeDateTime.compareTo(nativeDateTime) > 0;
    }

    @Override
    public boolean __smallerEq(DateTimeInterface iObject) {
        BaseDateTime dateTime = (BaseDateTime) iObject;
        return dateTime.nativeDateTime.compareTo(nativeDateTime) >= 0;
    }
}
