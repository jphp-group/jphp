package org.develnext.jphp.zend.ext.standard.date;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("DateTimeImmutable")
public class DateTimeImmutable extends BaseObject implements DateTimeInterface {
    public DateTimeImmutable(Environment env) {
        super(env);
    }

    public DateTimeImmutable(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Memory diff(Environment env, TraceInfo traceInfo, Memory... args) {
        return null;
    }

    @Override
    public Memory format(Environment env, TraceInfo traceInfo, String format) {
        return null;
    }

    @Override
    public Memory getOffset(Environment env, TraceInfo traceInfo) {
        return null;
    }

    @Override
    public Memory getTimestamp(Environment env, TraceInfo traceInfo) {
        return null;
    }

    @Override
    public Memory getTimezone(Environment env, TraceInfo traceInfo) {
        return null;
    }

    @Override
    public Memory __wakeup(Environment env, TraceInfo traceInfo) {
        return null;
    }
}
