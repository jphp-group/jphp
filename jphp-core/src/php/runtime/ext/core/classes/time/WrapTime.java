package php.runtime.ext.core.classes.time;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.ArrayMemory;
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
public class WrapTime extends BaseObject implements IComparableObject<WrapTime> {
    protected final static TimeZone UTC = TimeZone.getTimeZone("UTC");
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

    public Date getDate() {
        return date;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Calendar getCalendar() {
        return calendar;
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
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*time").assign(date.getTime());
        r.refOfIndex("*timeZone").assign(timeZone.getID());
        return r.toConstant();
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
        return LongMemory.valueOf(calendar.get(Calendar.MONTH) + 1);
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
    public Memory hour(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.HOUR));
    }

    @Signature
    public Memory hourOfDay(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    @Signature
    public Memory minute(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.MINUTE));
    }

    @Signature
    public Memory second(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.SECOND));
    }

    @Signature
    public Memory millisecond(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.get(Calendar.MILLISECOND));
    }

    @Signature
    public Memory getTimeZone(Environment env, Memory... args) {
        return new ObjectMemory(new WrapTimeZone(env, timeZone));
    }

    @Signature(@Arg(value = "time", nativeType = WrapTime.class))
    public Memory compare(Environment env, Memory... args) {
        return LongMemory.valueOf(calendar.compareTo(args[0].toObject(WrapTime.class).calendar));
    }

    @Signature(@Arg(value = "timeZone", nativeType = WrapTimeZone.class, optional = @Optional("null")))
    public static Memory now(Environment env, Memory... args) {
        TimeZone zone = WrapTimeZone.getTimeZone(env, args[0]);
        return new ObjectMemory(new WrapTime(env, Calendar.getInstance(zone).getTime(), zone));
    }

    @Signature(@Arg(value = "timeZone", nativeType = WrapTimeZone.class))
    public Memory withTimezone(Environment env, Memory... args) {
        TimeZone zone = WrapTimeZone.getTimeZone(env, args[0]);
        return new ObjectMemory(new WrapTime(env, date, zone));
    }

    @Signature(@Arg(value = "args", type = HintType.ARRAY))
    public Memory add(Environment env, Memory... args) {
        ArrayMemory arg = args[0].toValue(ArrayMemory.class);
        Memory year  = arg.getByScalar("year");
        Memory month = arg.getByScalar("month");
        Memory day   = arg.getByScalar("day");
        Memory hour  = arg.getByScalar("hour");
        Memory min   = arg.getByScalar("min");
        Memory sec   = arg.getByScalar("sec");
        Memory millis = arg.getByScalar("millis");

        Calendar calendar1 = (Calendar) calendar.clone();
        if (year != null)
            calendar1.add(Calendar.YEAR, year.toInteger());
        if (month != null)
            calendar1.add(Calendar.MONTH, month.toInteger());
        if (day != null)
            calendar1.add(Calendar.DAY_OF_MONTH, day.toInteger());
        if (hour != null)
            calendar1.add(Calendar.HOUR_OF_DAY, hour.toInteger());
        if (min != null)
            calendar1.add(Calendar.MINUTE, min.toInteger());
        if (sec != null)
            calendar1.add(Calendar.SECOND, sec.toInteger());
        if (millis != null)
            calendar1.add(Calendar.MILLISECOND, millis.toInteger());

        return new ObjectMemory(new WrapTime(env, calendar1.getTime(), timeZone));
    }

    @Signature
    public Memory __toString(Environment env, Memory... args) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(timeZone);
        return new StringMemory(isoFormat.format(date));
    }

    @Signature(@Arg("format"))
    public Memory toString(Environment env, Memory... args) {
        SimpleDateFormat format = new SimpleDateFormat(args[0].toString());
        format.setTimeZone(timeZone);
        return StringMemory.valueOf(format.format(date));
    }

    @Signature({
            @Arg(value = "args", type = HintType.ARRAY)
    })
    public Memory replace(Environment env, Memory... args) {
        ArrayMemory arg = args[0].toValue(ArrayMemory.class);
        Memory year  = arg.getByScalar("year");
        Memory month = arg.getByScalar("month");
        Memory day   = arg.getByScalar("day");
        Memory hour  = arg.getByScalar("hour");
        Memory min   = arg.getByScalar("min");
        Memory sec   = arg.getByScalar("sec");
        Memory millis = arg.getByScalar("millis");

        Calendar calendar1 = (Calendar) calendar.clone();
        if (year != null)
            calendar1.set(Calendar.YEAR, year.toInteger());

        if (month != null)
            calendar1.set(Calendar.MONTH, month.toInteger() - 1);

        if (day != null)
            calendar1.set(Calendar.DATE, day.toInteger());

        if (hour != null)
            calendar1.set(Calendar.HOUR_OF_DAY, hour.toInteger());

        if (min != null)
            calendar1.set(Calendar.MINUTE, min.toInteger());

        if (sec != null)
            calendar1.set(Calendar.SECOND, sec.toInteger());

        if (millis != null)
            calendar1.set(Calendar.MILLISECOND, millis.toInteger());

        return new ObjectMemory(new WrapTime(env, calendar1.getTime(), calendar1.getTimeZone()));
    }

    @FastMethod
    @Signature
    public static Memory millis(Environment env, Memory... args) {
        return LongMemory.valueOf(System.currentTimeMillis());
    }

    @FastMethod
    @Signature
    public static Memory seconds(Environment env, Memory... args) {
        return LongMemory.valueOf(System.currentTimeMillis() / 1000);
    }

    @FastMethod
    @Signature
    public static Memory nanos(Environment env, Memory... args) {
        return LongMemory.valueOf(System.nanoTime());
    }

    @Signature(@Arg(value = "timeZone", nativeType = WrapTimeZone.class, optional = @Optional("NULL")))
    public static Memory today(Environment env, Memory... args) {
        Date date1 = new Date();
        Calendar calendar = Calendar.getInstance(WrapTimeZone.getTimeZone(env, args[0]));
        calendar.setTime(date1);
        calendar.set(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new ObjectMemory(new WrapTime(env, calendar.getTime(), calendar.getTimeZone()));
    }

    @Signature({
            @Arg(value = "args", type = HintType.ARRAY),
            @Arg(value = "timeZone", nativeType = WrapTimeZone.class, optional = @Optional("null"))
    })
    public static Memory of(Environment env, Memory... args) {
        Calendar calendar = Calendar.getInstance(WrapTimeZone.getTimeZone(env, args[1]));
        Memory arg = args[0];
        int year  = arg.valueOfIndex("year").toInteger();
        Memory m = arg.toValue(ArrayMemory.class).getByScalar("month");
        int month = 1;
        if (m != null)
            month = m.toInteger();

        int day   = arg.valueOfIndex("day").toInteger();
        if (day < 1)
            day = 1;

        int hour  = arg.valueOfIndex("hour").toInteger();
        int min   = arg.valueOfIndex("min").toInteger();
        int sec   = arg.valueOfIndex("sec").toInteger();

        calendar.set(year, month - 1, day, hour, min, sec);
        calendar.set(Calendar.MILLISECOND, arg.valueOfIndex("millis").toInteger());

        return new ObjectMemory(new WrapTime(env, calendar.getTime(), calendar.getTimeZone()));
    }

    @Override
    public boolean __equal(WrapTime iObject) {
        return calendar.compareTo(iObject.calendar) == 0;
    }

    @Override
    public boolean __identical(WrapTime iObject) {
        return calendar.compareTo(iObject.calendar) == 0 && timeZone.getID().equals(iObject.timeZone.getID());
    }

    @Override
    public boolean __greater(WrapTime iObject) {
        return calendar.compareTo(iObject.calendar) > 0;
    }

    @Override
    public boolean __greaterEq(WrapTime iObject) {
        return calendar.compareTo(iObject.calendar) >= 0;
    }

    @Override
    public boolean __smaller(WrapTime iObject) {
        return calendar.compareTo(iObject.calendar) < 0;
    }

    @Override
    public boolean __smallerEq(WrapTime iObject) {
        return calendar.compareTo(iObject.calendar) <= 0;
    }

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }
}
