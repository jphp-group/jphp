package php.runtime.ext.core.classes.time;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.util.WrapLocale;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static php.runtime.annotation.Reflection.*;

@Name("php\\time\\TimeFormat")
public class WrapTimeFormat extends BaseObject {
    protected String format;
    protected DateFormat dateFormat;

    public WrapTimeFormat(Environment env, DateFormat dateFormat) {
        super(env);
        this.dateFormat = dateFormat;
    }

    public WrapTimeFormat(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public String getFormat() {
        return format;
    }

    public DateFormat getDateFormat(WrapTime time) {
        if (dateFormat.getTimeZone().equals(time.timeZone))
            return dateFormat;
        else {
            DateFormat dt = (DateFormat)dateFormat.clone();
            dt.setTimeZone(time.timeZone);
            return dt;
        }
    }

    public DateFormat getDateFormat(TimeZone zone) {
        if (dateFormat.getTimeZone().equals(zone))
            return dateFormat;
        else {
            DateFormat dt = (DateFormat)dateFormat.clone();
            dt.setTimeZone(zone);
            return dt;
        }
    }

    @Signature({
            @Arg("format"),
            @Arg(value = "locale", nativeType = WrapLocale.class, optional = @Optional("null")),
            @Arg(value = "formatSymbols", type = HintType.ARRAY, optional = @Optional("null"))
    })
    public Memory __construct(Environment env, Memory... args) {
        format = args[0].toString();
        if (args[2].isNull()) {
            if (args[1].isNull())
                dateFormat = new SimpleDateFormat(args[0].toString(), WrapLocale.getDefault(env));
            else
                dateFormat = new SimpleDateFormat(args[0].toString(), args[1].toObject(WrapLocale.class).getLocale());
        } else {
            final ArrayMemory symbols = args[2].toValue(ArrayMemory.class);

            dateFormat = new SimpleDateFormat(args[0].toString(),
                    new DateFormatSymbols(WrapLocale.getDefault(env, args[1])){{

                    Memory months = symbols.getByScalar("months");
                    if (months != null && months.isArray()) {
                        this.setMonths(months.toValue(ArrayMemory.class).toStringArray());
                    }

                    Memory shortMonths = symbols.getByScalar("short_months");
                    if (shortMonths != null && shortMonths.isArray()) {
                        this.setShortMonths(shortMonths.toValue(ArrayMemory.class).toStringArray());
                    }

                    Memory eras = symbols.getByScalar("eras");
                    if (eras != null && eras.isArray()) {
                        this.setEras(eras.toValue(ArrayMemory.class).toStringArray());
                    }

                    Memory weekdays = symbols.getByScalar("weekdays");
                    if (weekdays != null && weekdays.isArray()) {
                        this.setWeekdays(weekdays.toValue(ArrayMemory.class).toStringArray());
                    }

                    Memory shortWeekdays = symbols.getByScalar("short_weekdays");
                    if (shortWeekdays != null && shortWeekdays.isArray()) {
                        this.setShortWeekdays(shortWeekdays.toValue(ArrayMemory.class).toStringArray());
                    }

                    Memory amPm = symbols.getByScalar("am_pm");
                    if (amPm != null && amPm.isArray()) {
                        this.setAmPmStrings(amPm.toValue(ArrayMemory.class).toStringArray());
                    }

                    Memory localPatternChars = symbols.getByScalar("local_pattern_chars");
                    if (localPatternChars != null) {
                        this.setLocalPatternChars(localPatternChars.toString());
                    }
            }});
        }

        return Memory.NULL;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*format").assign(format);

        return r.toConstant();
    }

    @Signature(@Arg(value = "time", nativeType = WrapTime.class))
    public Memory format(Environment env, Memory... args) {
        WrapTime time = args[0].toObject(WrapTime.class);
        DateFormat df = getDateFormat(time);

        return StringMemory.valueOf(df.format(time.date));
    }

    @Signature({
            @Arg("string"),
            @Arg(value = "timeZone", nativeType = WrapTimeZone.class, optional = @Optional("null"))
    })
    public Memory parse(Environment env, Memory... args) {
        try {
            TimeZone timeZone = WrapTimeZone.getTimeZone(env, args[1]);
            Date date = getDateFormat(timeZone).parse(args[0].toString());

            return new ObjectMemory(new WrapTime(env, date, timeZone));
        } catch (ParseException e) {
            return Memory.NULL;
        }
    }

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }
}
