package org.develnext.jphp.zend.ext.standard.date;

import java.time.ZoneId;
import java.time.ZoneOffset;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Name("DateTimeZone")
public class DateTimeZone extends BaseObject {
    @Property
    public static final int AFRICA = 1;
    @Property
    public static final int AMERICA = 2;
    @Property
    public static final int ANTARCTICA = 4;
    @Property
    public static final int ARCTIC = 8;
    @Property
    public static final int ASIA = 16;
    @Property
    public static final int ATLANTIC = 32;
    @Property
    public static final int AUSTRALIA = 64;
    @Property
    public static final int EUROPE = 128;
    @Property
    public static final int INDIAN = 256;
    @Property
    public static final int PACIFIC = 512;
    @Property
    public static final int UTC = 1024;
    @Property
    public static final int ALL = 2047;
    @Property
    public static final int ALL_WITH_BC = 4095;
    @Property
    public static final int PER_COUNTRY = 4096;
    @Property("timezone")
    Memory timezone = Memory.UNDEFINED;
    @Property("timezone_type")
    Memory type = Memory.UNDEFINED;
    private ZoneId nativeZone;

    public DateTimeZone(Environment env) {
        super(env);
    }

    public DateTimeZone(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(value = @Arg(value = "array", type = HintType.ARRAY), result = @Arg(type = HintType.OBJECT))
    public static Memory __set_state(Environment env, TraceInfo traceInfo, ArrayMemory arg) {
        DateTimeZone dateTimeZone = new DateTimeZone(env);
        StringMemory timezone = arg.get(StringMemory.valueOf("timezone")).toValue(StringMemory.class);

        return dateTimeZone.__construct(env, traceInfo, timezone);
    }

    @Signature
    public static Memory listAbbreviations(Environment env, TraceInfo traceInfo) {
        return ZoneIdFactory.listAbbreviations();
    }

    @Signature
    public static Memory listIdentifiers(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    static int getTimeZoneType(ZoneId zoneId) {
        if (zoneId == null)
            return -1;

        if (zoneId instanceof ZoneOffset) {
            return 1;
        } else {
            String id = zoneId.getId();

            if (id.contains("/") || id.equals("UTC")) {
                return 3;
            }
        }

        return 2;
    }

    ZoneId getNativeZone() {
        return nativeZone;
    }

    @Signature(value = {
            @Arg(value = "timezone", type = HintType.STRING)
    }, result = @Arg(type = HintType.OBJECT))
    public Memory __construct(Environment env, TraceInfo traceInfo, StringMemory arg) {
        init(arg.toString());
        return new ObjectMemory(this);
    }

    private void init(String zone) {
        this.nativeZone = ZoneId.of(zone);
        this.timezone = StringMemory.valueOf(zone);
        type = LongMemory.valueOf(getTimeZoneType(nativeZone));
    }

    @Signature
    public Memory __wakeup(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Signature
    public Memory getLocation(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Signature
    public Memory getName(Environment env, TraceInfo traceInfo) {
        return timezone;
    }

    @Signature
    public Memory getOffset(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Signature
    public Memory getTransitions(Environment env, TraceInfo traceInfo) {
        return Memory.UNDEFINED;
    }

    @Override
    public ArrayMemory getProperties() {
        ArrayMemory props = super.getProperties();
        props.refOfIndex("timezone_type").assign(type);
        props.refOfIndex("timezone").assign(timezone);

        return props;
    }
}
