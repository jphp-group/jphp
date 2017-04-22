package php.runtime.ext.core.classes.time;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Timer;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\time\\TimeZone")
public class WrapTimeZone extends BaseObject implements IComparableObject<WrapTimeZone> {
    protected final static TimeZone UTC = TimeZone.getTimeZone("UTC");

    protected TimeZone timeZone;

    public WrapTimeZone(Environment env, TimeZone timeZone) {
        super(env);
        this.timeZone = timeZone;
    }

    public WrapTimeZone(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg("rawOffset"),
            @Arg("ID"),
            @Arg(value = "properties", type = HintType.ARRAY, optional = @Optional("null"))
    })
    public Memory __construct(Environment env, Memory... args) {
        if (args[2].isNull()) {
            timeZone = new SimpleTimeZone(args[0].toInteger(), args[1].toString());
        } else {
            ArrayMemory props = args[2].toValue(ArrayMemory.class);
            int startMonth = props.valueOfIndex("start_month").toInteger();
            int startDay   = props.valueOfIndex("start_day").toInteger();
            int startDayOfWeek = props.valueOfIndex("start_day_of_week").toInteger();
            int startTime  = props.valueOfIndex("start_time").toInteger();
            int endMonth   = props.valueOfIndex("end_month").toInteger();
            int endDay     = props.valueOfIndex("end_day").toInteger();
            int endDayOfWeek = props.valueOfIndex("end_day_of_week").toInteger();
            int endTime    = props.valueOfIndex("end_time").toInteger();

            timeZone = new SimpleTimeZone(args[0].toInteger(), args[1].toString(),
                    startMonth,
                    startDay,
                    startDayOfWeek,
                    startTime,
                    endMonth,
                    endDay,
                    endDayOfWeek,
                    endTime
            );
        }
        return Memory.NULL;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*id").assign(timeZone.getID());
        r.refOfIndex("*rawOffset").assign(timeZone.getRawOffset());
        r.refOfIndex("*name").assign(timeZone.getDisplayName());
        return r.toConstant();
    }

    @Signature
    public Memory getId(Environment env, Memory... args) {
        return StringMemory.valueOf(timeZone.getID());
    }

    @Signature
    public Memory getRawOffset(Environment env, Memory... args) {
        return LongMemory.valueOf(timeZone.getRawOffset());
    }

    @Signature
    public Memory getDisplayName(Environment env, Memory... args) {
        return StringMemory.valueOf(timeZone.getDisplayName());
    }

    @FastMethod
    @Signature
    public static Memory UTC(final Environment env, Memory... args) {
        Memory r = env.getUserValue(WrapTimeZone.class.getName() + "#UTC", Memory.class);
        if (r == null) {
            r = new ObjectMemory(new WrapTimeZone(env, UTC));
            env.setUserValue(WrapTimeZone.class.getName() + "#UTC", r);
        }

        return r;
    }

    @FastMethod
    @Signature
    public static Memory of(Environment env, Memory... args) {
        return new ObjectMemory(new WrapTimeZone(env, TimeZone.getTimeZone(args[0].toString())));
    }

    public static TimeZone getTimeZone(Environment env, Memory arg) {
        if (arg.isNull()) {
            TimeZone zone = env.getUserValue(WrapTime.class.getName() + "#def_time_zone", TimeZone.class);
            if (zone == null)
                return TimeZone.getDefault();
            else
                return zone;
        } else if (arg.instanceOf(WrapTimeZone.class)) {
            WrapTimeZone timeZone = arg.toObject(WrapTimeZone.class);
            return timeZone.timeZone;
        } else
            throw new IllegalArgumentException();
    }

    @Signature({
            @Arg(value = "timeZone", nativeType = WrapTimeZone.class),
            @Arg(value = "globally", optional = @Optional("false"))
    })
    public static Memory setDefault(Environment env, Memory... args) {
        if (args[1].toBoolean())
            TimeZone.setDefault(args[0].toObject(WrapTimeZone.class).timeZone);
        else
            env.setUserValue(WrapTime.class.getName() + "#def_time_zone", args[0].toObject(WrapTimeZone.class).timeZone);
        return Memory.NULL;
    }

    @Signature(@Arg(value = "globally", optional = @Optional("false")))
    public static Memory getDefault(Environment env, Memory... args) {
        return new ObjectMemory(new WrapTimeZone(env,
                args[0].toBoolean() ? TimeZone.getDefault() : getTimeZone(env, Memory.NULL)));
    }

    @Signature(@Arg(value = "rawOffset", optional = @Optional("null")))
    public static Memory getAvailableIDs(Environment env, Memory... args) {
        if (args[0].isNull())
            return ArrayMemory.ofStrings(TimeZone.getAvailableIDs()).toConstant();
        else
            return ArrayMemory.ofStrings(TimeZone.getAvailableIDs(args[0].toInteger())).toConstant();
    }

    @Override
    public boolean __equal(WrapTimeZone iObject) {
        return this.timeZone.getID().equals(iObject.timeZone.getID());
    }

    @Override
    public boolean __identical(WrapTimeZone iObject) {
        return this.timeZone == iObject.timeZone;
    }

    @Override
    public boolean __greater(WrapTimeZone iObject) {
        return this.timeZone.getRawOffset() > iObject.timeZone.getRawOffset();
    }

    @Override
    public boolean __greaterEq(WrapTimeZone iObject) {
        return this.timeZone.getRawOffset() >= iObject.timeZone.getRawOffset();
    }

    @Override
    public boolean __smaller(WrapTimeZone iObject) {
        return this.timeZone.getRawOffset() < iObject.timeZone.getRawOffset();
    }

    @Override
    public boolean __smallerEq(WrapTimeZone iObject) {
        return this.timeZone.getRawOffset() <= iObject.timeZone.getRawOffset();
    }

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }
}
