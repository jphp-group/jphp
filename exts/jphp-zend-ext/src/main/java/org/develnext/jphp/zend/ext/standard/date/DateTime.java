package org.develnext.jphp.zend.ext.standard.date;

import java.time.ZonedDateTime;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

@Name("DateTime")
public class DateTime extends BaseObject implements DateTimeInterface {
    private ObjectMemory $this;
    private ZonedDateTime dateTime;

    public DateTime(Environment env) {
        super(env);
    }

    public DateTime(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(value = {
            @Arg(value = "format", type = HintType.STRING),
            @Arg(value = "time", type = HintType.STRING),
            @Arg(value = "timezone", type = HintType.OBJECT, optional = @Optional("NULL"))
    }, result = @Arg(type = HintType.OBJECT))
    public static Memory createFromFormat(Environment env, TraceInfo traceInfo, Memory... args) {
        return new ObjectMemory(null);
    }

    @Signature(result = @Arg(type = HintType.ARRAY))
    public static Memory getLastErrors(Environment env, TraceInfo traceInfo) {
        return ArrayMemory.of();
    }

    @Name("__set_state")
    @Signature(value = @Arg(value = "array", type = HintType.ARRAY),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public static Memory fromState(Environment env, TraceInfo traceInfo) {
        return new ObjectMemory();
    }

    @Signature(value = {
            @Arg(value = "time", type = HintType.STRING, optional = @Optional("now")),
            @Arg(value = "timezone", type = HintType.OBJECT, optional = @Optional("NULL"))
    }, result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, Memory... args) {
        DateTimeParser parser = new DateTimeParser(args[0].toString());
        dateTime = parser.parse();

        return ($this = new ObjectMemory(this));
    }

    @Signature(value = {
            @Arg(value = "interval", type = HintType.OBJECT, nativeType = DateInterval.class)
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory add(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = @Arg(value = "modify", type = HintType.STRING),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory modify(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = {
            @Arg(value = "year", type = HintType.INT),
            @Arg(value = "month", type = HintType.INT),
            @Arg(value = "day", type = HintType.INT),
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setDate(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = {
            @Arg(value = "year", type = HintType.INT),
            @Arg(value = "week", type = HintType.INT),
            @Arg(value = "day", type = HintType.INT, optional = @Optional("1")),
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setISODate(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = {
            @Arg(value = "hour", type = HintType.INT),
            @Arg(value = "minute", type = HintType.INT),
            @Arg(value = "second", type = HintType.INT, optional = @Optional("0")),
            @Arg(value = "micosecond", type = HintType.INT, optional = @Optional("0")),
    }, result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setTime(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = @Arg(value = "unixtimestamp", type = HintType.INT),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setTimestamp(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = @Arg(value = "timezone", type = HintType.OBJECT, nativeType = DateTimeZone.class),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory setTimezone(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Signature(value = @Arg(value = "interval", type = HintType.OBJECT, nativeType = DateInterval.class),
            result = @Arg(type = HintType.OBJECT, nativeType = DateTime.class))
    public Memory sub(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Override
    @Signature(value = {
            @Arg(value = "datetime2", type = HintType.OBJECT, typeClass = "DateTimeInterface"),
            @Arg(value = "absolute", type = HintType.BOOLEAN, optional = @Optional("FALSE"))
    }, result = @Arg(type = HintType.OBJECT, typeClass = "DateInterval"))
    public Memory diff(Environment env, TraceInfo traceInfo, Memory... args) {
        return $this;
    }

    @Override
    @Signature(value = {@Arg(value = "format", type = HintType.STRING)}, result = @Arg(type = HintType.STRING))
    public Memory format(Environment env, TraceInfo traceInfo, String format) {
        return Memory.UNDEFINED;
    }

    @Override
    @Signature(result = @Arg(type = HintType.INT))
    public Memory getOffset(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Override
    @Signature(result = @Arg(type = HintType.INT))
    public Memory getTimestamp(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Override
    @Signature(result = @Arg(type = HintType.OBJECT))
    public Memory getTimezone(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Override
    @Signature(result = @Arg(type = HintType.VOID))
    public Memory __wakeup(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }
}
