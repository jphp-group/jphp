package org.develnext.jphp.zend.ext.standard.date;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Name("DateTimeImmutable")
public class DateTimeImmutable extends BaseDateTime {
    public DateTimeImmutable(Environment env) {
        super(env);
    }

    public DateTimeImmutable(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature(value = {
            @Reflection.Arg(value = "format", type = HintType.STRING),
            @Reflection.Arg(value = "time", type = HintType.STRING),
            @Reflection.Arg(value = "timezone", type = HintType.OBJECT, optional = @Reflection.Optional("NULL"))
    }, result = @Reflection.Arg(type = HintType.OBJECT))
    public static Memory createFromFormat(Environment env, TraceInfo traceInfo,
                                          Memory format,
                                          Memory time,
                                          Memory timezone) {
        try {
            ZonedDateTime parse = DateFormat.createFromFormat(format.toString(), time.toString(),
                    ZonedDateTime.now(toZoneId(env, traceInfo, timezone)));
            DateTimeImmutable dateTime = new DateTimeImmutable(env);
            dateTime.nativeDateTime = parse;
            return new ObjectMemory(dateTime);
        } catch (DateTimeException | NoSuchElementException | IllegalArgumentException e) {
            return Memory.FALSE;
        }
    }

    @Reflection.Signature(value = {
            @Reflection.Arg(value = "format", type = HintType.STRING),
            @Reflection.Arg(value = "time", type = HintType.STRING)
    }, result = @Reflection.Arg(type = HintType.OBJECT))
    public static Memory createFromFormat(Environment env, TraceInfo traceInfo,
                                          Memory format,
                                          Memory time) {
        return createFromFormat(env, traceInfo, format, time, Memory.NULL);
    }

    @Reflection.Signature(value = {
            @Reflection.Arg(value = "time", type = HintType.STRING, optional = @Reflection.Optional("now")),
            @Reflection.Arg(value = "timezone", type = HintType.OBJECT, optional = @Reflection.Optional("NULL"))
    }, result = @Reflection.Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, Memory time, Memory timeZone) {
        try {
            DateTimeParseResult result = parse(env, traceInfo, time, timeZone, ZonedDateTime.now());
            this.nativeDateTime = result.dateTime();
            this.parsedZone = result.parsedZone();
        } catch (DateTimeParserException e) {
            return Memory.FALSE;
        }

        return new ObjectMemory(this);
    }

    @Reflection.Signature(value = {
            @Reflection.Arg(value = "time", type = HintType.STRING, optional = @Reflection.Optional("now"))
    }, result = @Reflection.Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, Memory time) {
        return __construct(env, traceInfo, time, Memory.NULL);
    }

    @Reflection.Signature(result = @Reflection.Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo) {
        return __construct(env, traceInfo, StringMemory.valueOf("now"), Memory.NULL);
    }

    @Override
    @Reflection.Signature(value = {
            @Reflection.Arg(value = "datetime2", type = HintType.OBJECT, typeClass = "DateTimeInterface"),
            @Reflection.Arg(value = "absolute", type = HintType.BOOLEAN, optional = @Reflection.Optional("FALSE"))
    }, result = @Reflection.Arg(type = HintType.OBJECT, typeClass = "DateInterval"))
    public Memory diff(Environment env, TraceInfo t, Memory dateTimeInterface) {
        return super.diff(env, t, dateTimeInterface);
    }

    @Override
    @Reflection.Signature(value = {@Reflection.Arg(value = "format", type = HintType.STRING)}, result = @Reflection.Arg(type = HintType.STRING))
    public Memory format(Environment env, TraceInfo traceInfo, String format) {
        return super.format(env, traceInfo, format);
    }

    @Override
    @Reflection.Signature(result = @Reflection.Arg(type = HintType.INT))
    public Memory getOffset(Environment env, TraceInfo traceInfo) {
        return super.getOffset(env, traceInfo);
    }

    @Override
    @Reflection.Signature(result = @Reflection.Arg(type = HintType.INT))
    public Memory getTimestamp(Environment env, TraceInfo traceInfo) {
        return super.getTimestamp(env, traceInfo);
    }

    @Override
    @Reflection.Signature(result = @Reflection.Arg(type = HintType.VOID))
    public Memory __wakeup(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }
}
