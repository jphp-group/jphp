package org.develnext.jphp.zend.ext.standard.date;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
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

    @Signature(value = {
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "time", type = HintType.STRING),
            @Arg(value = "timezone", type = HintType.OBJECT, optional = @Reflection.Optional("NULL"))
    }, result = @Arg(type = HintType.OBJECT))
    public static Memory createFromFormat(Environment env, TraceInfo traceInfo,
                                          Memory format,
                                          Memory time,
                                          Memory timezone) {
        try {
            ZonedDateTime parse = DateFormat.createFromFormat(env, traceInfo, format.toString(), time.toString(),
                    ZonedDateTime.now(toZoneId(env, traceInfo, timezone)));
            DateTimeImmutable dateTime = new DateTimeImmutable(env);
            dateTime.nativeDateTime = parse;
            return new ObjectMemory(dateTime);
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

    @Signature(value = {
            @Arg(value = "time", type = HintType.STRING, optional = @Reflection.Optional("now")),
            @Arg(value = "timezone", type = HintType.OBJECT, optional = @Reflection.Optional("NULL"))
    }, result = @Arg(type = HintType.OBJECT))
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

    @Signature(value = {
            @Arg(value = "time", type = HintType.STRING, optional = @Reflection.Optional("now"))
    }, result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, Memory time) {
        return __construct(env, traceInfo, time, Memory.NULL);
    }

    @Signature(result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo) {
        return __construct(env, traceInfo, StringMemory.valueOf("now"), Memory.NULL);
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
