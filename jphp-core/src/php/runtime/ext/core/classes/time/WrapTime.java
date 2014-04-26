package php.runtime.ext.core.classes.time;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\time\\Time")
public class WrapTime extends BaseObject implements IComparableObject<WrapTime>, ICloneableObject<WrapTime> {
    protected final static TimeZone UTC = TimeZone.getTimeZone("UTC");
    protected final static Calendar utc_calendar = Calendar.getInstance(UTC);
    protected Date date;
    protected TimeZone timeZone;
    protected Calendar calendar;

    public WrapTime(Environment env, Date date) {
        super(env);
        this.date = date;
        timeZone = UTC;
        calendar = Calendar.getInstance(UTC);
        calendar.setTime(date);
    }

    public WrapTime(Environment env, Date date, TimeZone timeZone) {
        super(env);
        this.date = date;
        this.timeZone = timeZone;
        calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
    }

    public WrapTime(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "date"),
            @Arg(value = "timeZone", nativeType = WrapTimeZone.class, optional = @Optional("null"))
    })
    public Memory __construct(Environment env, Memory... args) {
        TimeZone zone = WrapTimeZone.getTimeZone(env, args[1]);

        this.date = new Date(args[0].toLong());
        this.timeZone = zone;
        return Memory.NULL;
    }

    @Signature
    public Memory getTime(Environment env, Memory... args) {
        return LongMemory.valueOf(date.getTime());
    }

    @Signature
    public Memory year(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.YEAR));
    }

    @Signature
    public Memory month(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.MONTH));
    }

    @Signature
    public Memory week(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    @Signature
    public Memory weekOfMonth(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.WEEK_OF_MONTH));
    }

    @Signature
    public Memory day(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
    }

    @Signature
    public Memory dayOfMonth(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Signature
    public Memory dayOfWeek(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Signature
    public Memory dayOfWeekInMonth(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
    }

    @Signature
    public Memory getTimeZone(Environment env, Memory... args) {
        return new ObjectMemory(new WrapTimeZone(env, timeZone));
    }

    @Signature(@Arg(value = "timeZone", nativeType = WrapTimeZone.class, optional = @Optional("null")))
    public static Memory now(Environment env, Memory... args) {
        TimeZone zone = WrapTimeZone.getTimeZone(env, args[0]);
        if (zone == UTC) {
            return new ObjectMemory(new WrapTime(env, utc_calendar.getTime(), UTC));
        } else {
            return new ObjectMemory(new WrapTime(env, Calendar.getInstance(zone).getTime(), zone));
        }
    }

    @Signature(@Arg("count"))
    public Memory plusDays(Environment env, Memory... args) {
        Calendar calendar1 = (Calendar) calendar.clone();
        calendar1.add(Calendar.DATE, args[0].toInteger());

        return new ObjectMemory(new WrapTime(env, calendar1.getTime(), timeZone));
    }

    @Signature(@Arg("count"))
    public Memory plusYears(Environment env, Memory... args) {
        Calendar calendar1 = (Calendar) calendar.clone();
        calendar1.add(Calendar.YEAR, args[0].toInteger());

        return new ObjectMemory(new WrapTime(env, calendar1.getTime(), timeZone));
    }

    @Signature(@Arg("count"))
    public Memory plusMonths(Environment env, Memory... args) {
        Calendar calendar1 = (Calendar) calendar.clone();
        calendar1.add(Calendar.MONTH, args[0].toInteger());

        return new ObjectMemory(new WrapTime(env, calendar1.getTime(), timeZone));
    }

    @FastMethod
    @Signature
    public static Memory millis(Environment env, Memory... args) {
        return LongMemory.valueOf(System.currentTimeMillis());
    }

    @FastMethod
    @Signature
    public static Memory nanos(Environment env, Memory... args) {
        return LongMemory.valueOf(System.nanoTime());
    }

    @Signature
    public Memory __toString(Environment env, Memory... args) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(timeZone);
        return new StringMemory(isoFormat.format(date));
    }

    @Signature({
            @Arg(value = "args", type = HintType.ARRAY),
            @Arg(value = "timeZone", nativeType = WrapTimeZone.class)
    })
    public static Memory of(Environment env, Memory... args) {
        Calendar calendar = Calendar.getInstance(WrapTimeZone.getTimeZone(env, args[1]));
        Memory arg = args[0];
        int year  = arg.valueOfIndex("year").toInteger();
        int month = arg.valueOfIndex("month").toInteger();

        int day   = arg.valueOfIndex("day").toInteger();
        if (day < 1)
            day = 1;

        int hour  = arg.valueOfIndex("hour").toInteger();
        int min   = arg.valueOfIndex("min").toInteger();
        int sec   = arg.valueOfIndex("sec").toInteger();

        calendar.set(year, month + 1, day, hour, min, sec);
        return new ObjectMemory(new WrapTime(env, calendar.getTime(), calendar.getTimeZone()));
    }

    @Override
    public boolean __equal(WrapTime iObject) {
        return date.compareTo(iObject.date) == 0;
    }

    @Override
    public boolean __identical(WrapTime iObject) {
        return date.compareTo(iObject.date) == 0 && timeZone.getID().equals(iObject.timeZone.getID());
    }

    @Override
    public boolean __greater(WrapTime iObject) {
        return date.compareTo(iObject.date) > 0;
    }

    @Override
    public boolean __greaterEq(WrapTime iObject) {
        return date.compareTo(iObject.date) >= 0;
    }

    @Override
    public boolean __smaller(WrapTime iObject) {
        return date.compareTo(iObject.date) < 0;
    }

    @Override
    public boolean __smallerEq(WrapTime iObject) {
        return date.compareTo(iObject.date) <= 0;
    }

    @Override
    public WrapTime __clone(Environment env, TraceInfo trace) {
        return new WrapTime(env, date, timeZone);
    }
}
