package org.develnext.jphp.zend.ext.standard;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.util.Collections;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import org.develnext.jphp.zend.ext.standard.date.DateFormat;
import org.develnext.jphp.zend.ext.standard.date.DateInterval;
import org.develnext.jphp.zend.ext.standard.date.DateTime;
import org.develnext.jphp.zend.ext.standard.date.DateTimeImmutable;
import org.develnext.jphp.zend.ext.standard.date.DateTimeInterface;
import org.develnext.jphp.zend.ext.standard.date.DateTimeParseResult;
import org.develnext.jphp.zend.ext.standard.date.DateTimeParserException;
import org.develnext.jphp.zend.ext.standard.date.DateTimeZone;
import org.develnext.jphp.zend.ext.standard.date.ZoneIdFactory;
import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.lang.exception.BaseTypeError;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

public class DateFunctions extends FunctionsContainer {
    public static final int MSEC_IN_MIN = 60 * 1000;
    private static final Memory TZ_UTC = StringMemory.valueOf("UTC");
    private static final LocalDateTime UNIX_EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    private static final String TWO_DIGIT_INT = "%02d";
    private static final ZoneId ZONE_GMT = ZoneId.of("GMT");
    private static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    public static Memory microtime(boolean getAsFloat) {
        double now = System.currentTimeMillis() / 1000.0;
        int s = (int) now;

        return getAsFloat
                ? new DoubleMemory(now)
                : new StringMemory((Math.round((now - s) * 1000) / 1000) + " " + s);
    }

    public static Memory microtime() {
        return microtime(false);
    }

    public static Memory gettimeofday(boolean getAsFloat) {
        long msec_time = System.currentTimeMillis();

        if (getAsFloat) {
            double now = msec_time / 1000.0;

            return new DoubleMemory(now);
        } else {
            ArrayMemory result = new ArrayMemory();

            TimeZone timeZone = TimeZone.getDefault();
            long sec = msec_time / 1000;
            long usec = (msec_time % 1000) * 1000;
            long minuteswest = -timeZone.getOffset(msec_time) / MSEC_IN_MIN;
            boolean is_dst = timeZone.inDaylightTime(new Date(msec_time));

            result.refOfIndex("sec").assign(sec);
            result.refOfIndex("usec").assign(usec);
            result.refOfIndex("minuteswest").assign(minuteswest);
            result.refOfIndex("dsttime").assign(is_dst ? 1 : 0);

            return result.toConstant();
        }
    }

    public static Memory gettimeofday() {
        return gettimeofday(false);
    }

    public static Memory date_default_timezone_set(Environment env, TraceInfo traceInfo, String tz) {
        try {
            ZoneIdFactory.of(tz);

            env.setConfigValue("date.timezone", StringMemory.valueOf(tz));

            return Memory.TRUE;
        } catch (DateTimeException e) {
            env.notice(traceInfo, "date_default_timezone_set(): Timezone ID '%s' is invalid", tz);
            return Memory.FALSE;
        }
    }

    public static Memory date_default_timezone_get(Environment env, TraceInfo traceInfo) {
        // from "TZ" environment variable
        String tz = (String) env.getUserValue("env", Map.class).get("TZ");

        if (StringUtils.isBlank(tz)) {
            // if "TZ" does not contain value read from ini config
            Memory iniConfig = env.getConfigValue("date.timezone", Memory.UNDEFINED);

            if (iniConfig == Memory.UNDEFINED || StringUtils.isBlank(iniConfig.toString())) {
                // the fallback timezone.
                return TZ_UTC;
            }

            tz = iniConfig.toString();

            try {
                ZoneIdFactory.of(tz);
            } catch (DateTimeException e) {
                env.warning(traceInfo, "date_default_timezone_get(): Invalid date.timezone value '%s'," +
                    " we selected the timezone '%s' for now.", tz, TZ_UTC);
                return TZ_UTC;
            }
        }

        return StringMemory.valueOf(tz);
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo,
                                int hour, int minute, int second, int month, int day, int year) {
        return __mktime(zoneId(date_default_timezone_get(env, traceInfo)), hour, minute, second,
            month, day, year);
    }

    private static Memory __mktime(ZoneId zoneId, int hour, int minute, int second, int month, int day, int year) {
        if (year >= 0 && year <= 69) {
            year += 2000;
        } else if (year >= 70 && year <= 100) {
            year += 1900;
        } else if (year < 0) {
            year = 1970 + year;
        }

        long time = UNIX_EPOCH.plusYears(year - 1970)
            .plusMonths(month - 1)
            .plusDays(day - 1)
            .plusMinutes(minute)
            .plusHours(hour)
            .plusSeconds(second)
            .atZone(zoneId)
            .toEpochSecond();

        return LongMemory.valueOf(time);
    }

    private static ZoneId zoneId(Memory memory) {
        return ZoneIdFactory.of(memory.toString());
    }

    private static ZoneId zoneId(Environment env, TraceInfo traceInfo) {
        return zoneId(date_default_timezone_get(env, traceInfo));
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo,
                                int hour, int minute, int second, int month, int day) {
        return mktime(env, traceInfo, hour, minute, second, month, day, Year.now().getValue());
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo,
                                int hour, int minute, int second, int month) {
        LocalDate date = LocalDate.now();
        return mktime(env, traceInfo, hour, minute, second, month, date.getDayOfMonth(), date.getYear());
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo,
                                int hour, int minute, int second) {
        LocalDate date = LocalDate.now();
        return mktime(env, traceInfo, hour, minute, second, date.getMonthValue(), date.getDayOfMonth(), date.getYear());
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo,
                                int hour, int minute) {
        LocalDateTime date = LocalDateTime.now();
        return mktime(env, traceInfo, hour, minute, date.getSecond(), date.getMonthValue(), date.getDayOfMonth(), date.getYear());
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo,
                                int hour) {
        LocalDateTime date = LocalDateTime.now();
        return mktime(env, traceInfo, hour, date.getMinute(), date.getSecond(), date.getMonthValue(), date.getDayOfMonth(), date.getYear());
    }

    public static Memory mktime(Environment env, TraceInfo traceInfo) {
        env.error(traceInfo, ErrorType.E_DEPRECATED, "mktime(): You should be using the time() function instead");
        LocalDateTime date = LocalDateTime.now();
        return mktime(env, traceInfo, date.getHour(), date.getMinute(), date.getSecond(), date.getMonthValue(), date.getDayOfMonth(), date.getYear());
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo, int hour, int minute, int second, int month,
                                  int day, int year) {
        return __mktime(ZONE_GMT, hour, minute, second, month, day, year);
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo, int hour, int minute, int second, int month,
                                  int day) {
        return __mktime(ZONE_GMT, hour, minute, second, month, day, Year.now().getValue());
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo, int hour, int minute, int second, int month) {
        LocalDate date = LocalDate.now();
        return __mktime(ZONE_GMT, hour, minute, second, month, date.getDayOfMonth(), date.getYear());
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo, int hour, int minute, int second) {
        LocalDate date = LocalDate.now();
        return __mktime(ZONE_GMT, hour, minute, second, date.getMonthValue(), date.getDayOfMonth(), date.getYear());
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo, int hour, int minute) {
        LocalDateTime date = LocalDateTime.now();
        return __mktime(ZONE_GMT, hour, minute, date.getSecond(), date.getMonthValue(), date.getDayOfMonth(),
            date.getYear());
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo, int hour) {
        LocalDateTime date = LocalDateTime.now();
        return __mktime(ZONE_GMT, hour, date.getMinute(), date.getSecond(), date.getMonthValue(), date.getDayOfMonth(),
            date.getYear());
    }

    public static Memory gmmktime(Environment env, TraceInfo traceInfo) {
        env.error(traceInfo, ErrorType.E_DEPRECATED, "gmmktime(): You should be using the time() function instead");
        LocalDateTime date = LocalDateTime.now();
        return __mktime(ZONE_GMT, date.getHour(), date.getMinute(), date.getSecond(), date.getMonthValue(),
            date.getDayOfMonth(), date.getYear());
    }

    public static Memory date(Environment env, TraceInfo traceInfo, String format, long time) {
        ZoneId zoneId = zoneId(date_default_timezone_get(env, traceInfo));
        return __date(env, traceInfo, zoneId, format, time);
    }

    public static Memory date(Environment env, TraceInfo traceInfo, String format) {
        return date(env, traceInfo, format, epochSeconds());
    }

    public static Memory gmdate(Environment env, TraceInfo traceInfo, String format, long time) {
        return __date(env, traceInfo, ZONE_GMT, format, time);
    }

    public static Memory gmdate(Environment env, TraceInfo traceInfo, String format) {
        return gmdate(env, traceInfo, format, epochSeconds());
    }

    private static Memory __date(Environment env, TraceInfo traceInfo, ZoneId zoneId, String format, long time) {
        ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(time), zoneId);
        DateTimeParseResult parseResult = new DateTimeParseResult(date, Collections.emptySet(), null, null);
        return StringMemory.valueOf(DateFormat.formatForDateFunction(env, parseResult, format));
    }

    public static Memory date_format(Environment env, TraceInfo traceInfo, Memory object, String format) {
        return object.toObject(DateTimeInterface.class).format(env, traceInfo, format);
    }

    public static Memory date_get_last_errors(Environment env, TraceInfo traceInfo, Memory object, String format) {
        return DateTime.getLastErrors(env, traceInfo);
    }

    public static Memory strtotime(Environment env, TraceInfo traceInfo, Memory time) {
        return __strtotime(env, traceInfo, time, epochSeconds());
    }

    private static Memory __strtotime(Environment env, TraceInfo traceInfo, Memory time, long now) {
        Memory zoneMemory = date_default_timezone_get(env, traceInfo);
        ZoneId zoneId = zoneId(zoneMemory);
        ZonedDateTime base = Instant.ofEpochSecond(now).atZone(zoneId);
        try {
            ZonedDateTime dateTime = DateTime.parse(env, traceInfo, time, zoneMemory, base).dateTime();
            return LongMemory.valueOf(dateTime.toEpochSecond());
        } catch (Throwable e) {
            return Memory.FALSE;
        }
    }

    public static Memory strtotime(Environment env, TraceInfo traceInfo, Memory time, Memory now) {
        if (!now.isNumber()) {
            env.exception(traceInfo, new BaseTypeError(env, ErrorType.E_ERROR),
                Messages.ERR_WRONG_PARAM_TYPE.fetch("strtotime()", 2, Memory.Type.INT.toString(), now.getRealType().toString()));
        }

        return __strtotime(env, traceInfo, time, now.toLong());
    }

    public static Memory checkdate(int month, int day, int year) {
        // checkdate spec
        if (year < 1 || year > 32767) {
            return Memory.FALSE;
        }

        try {
            LocalDate.of(year, month, day);
            return Memory.TRUE;
        } catch (DateTimeException e) {
            return Memory.FALSE;
        }
    }

    public static Memory localtime(Environment env, TraceInfo traceInfo, long time, boolean isAssociative) {
        ZoneId zone = zoneId(env, traceInfo);

        Instant instant = Instant.ofEpochSecond(time);
        ZonedDateTime dateTime = instant.atZone(zone);

        Memory[] ret = new Memory[9];

        ret[0] = LongMemory.valueOf(dateTime.getSecond());
        ret[1] = LongMemory.valueOf(dateTime.getMinute());
        ret[2] = LongMemory.valueOf(dateTime.getHour());
        ret[3] = LongMemory.valueOf(dateTime.getDayOfMonth());
        ret[4] = LongMemory.valueOf(dateTime.getMonthValue() - 1);
        ret[5] = LongMemory.valueOf(dateTime.getYear() - 1900);
        ret[6] = LongMemory.valueOf(dateTime.getDayOfWeek().getValue());
        ret[7] = LongMemory.valueOf(dateTime.getDayOfYear() - 1);
        Duration ds = zone.getRules().getDaylightSavings(instant);
        ret[8] = ds.isZero() ? Memory.CONST_INT_0 : Memory.CONST_INT_1;

        if (isAssociative) {
            Memory[] struct = LocaltimeStructureHolder.VALUE;
            ArrayMemory array = ArrayMemory.createHashed(ret.length);
            for (int i = 0; i < struct.length; i++) {
                array.put(struct[i], ret[i]);
            }

            return array;
        }

        return ArrayMemory.of(ret);
    }

    public static Memory localtime(Environment env, TraceInfo traceInfo, long time) {
        return localtime(env, traceInfo, time, false);
    }

    public static Memory localtime(Environment env, TraceInfo traceInfo) {
        return localtime(env, traceInfo, epochSeconds(), false);
    }

    public static Memory getdate(Environment env, TraceInfo traceInfo) {
        return getdate(env, traceInfo, epochSeconds());
    }

    @SuppressWarnings("WeakerAccess")
    public static Memory getdate(Environment env, TraceInfo traceInfo, long time) {
        ArrayMemory ret = ArrayMemory.createHashed(10);
        ZonedDateTime dateTime = zonedDateTime(env, traceInfo, time);

        ret.putAsKeyString("seconds", LongMemory.valueOf(dateTime.getSecond()));
        ret.putAsKeyString("minutes", LongMemory.valueOf(dateTime.getMinute()));
        ret.putAsKeyString("hours", LongMemory.valueOf(dateTime.getHour()));
        ret.putAsKeyString("mday", LongMemory.valueOf(dateTime.getDayOfMonth()));
        ret.putAsKeyString("wday", LongMemory.valueOf(dateTime.getDayOfWeek().getValue()));
        ret.putAsKeyString("mon", LongMemory.valueOf(dateTime.getMonthValue()));
        ret.putAsKeyString("year", LongMemory.valueOf(dateTime.getYear()));
        ret.putAsKeyString("yday", LongMemory.valueOf(dateTime.getDayOfYear() - 1));
        ret.putAsKeyString("weekday", StringMemory.valueOf(dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, env.getLocale())));
        ret.putAsKeyString("month", StringMemory.valueOf(dateTime.getMonth().getDisplayName(TextStyle.FULL, env.getLocale())));
        ret.add(time);

        return ret;
    }

    public static Memory gmstrftime(Environment env, TraceInfo traceInfo, String format, long time) {
        if (format.isEmpty()) {
            return Memory.FALSE;
        }

        StringBuilder buff = new StringBuilder();

        return StringMemory.valueOf(
                strftimeImpl(Instant.ofEpochSecond(time).atZone(ZONE_GMT), env.getLocale(), format, buff)
                .toString()
        );
    }

    public static Memory gmstrftime(Environment env, TraceInfo traceInfo, String format) {
        return gmstrftime(env, traceInfo, format, epochSeconds());
    }

    public static Memory date_create(Environment env, TraceInfo traceInfo, Memory... args) {
        DateTime dateTime = new DateTime(env);
        if (args == null || args.length == 0) {
            return dateTime.__construct(env, traceInfo, StringMemory.valueOf("now"), Memory.NULL);
        }
        return dateTime.__construct(env, traceInfo, args[0], args.length == 2 ? args[1] : Memory.NULL);
    }

    public static Memory date_create_from_format(Environment env, TraceInfo traceInfo, Memory... args) {
        if (args.length == 2) {
            return DateTime.createFromFormat(env, traceInfo, args[0], args[1], Memory.NULL);
        } else if (args.length == 3) {
            return DateTime.createFromFormat(env, traceInfo, args[0], args[1], args[2]);
        }

        return Memory.UNDEFINED;
    }

    public static Memory date_create_immutable_from_format(Environment env, TraceInfo traceInfo, Memory... args) {
        if (args.length == 2) {
            return DateTimeImmutable.createFromFormat(env, traceInfo, args[0], args[1], Memory.NULL);
        } else if (args.length == 3) {
            return DateTimeImmutable.createFromFormat(env, traceInfo, args[0], args[1], args[2]);
        }

        return Memory.UNDEFINED;
    }

    public static Memory date_create_immutable(Environment env, TraceInfo traceInfo, Memory... args) {
        DateTimeImmutable dateTime = new DateTimeImmutable(env);
        if (args == null || args.length == 0) {
            return dateTime.__construct(env, traceInfo, StringMemory.valueOf("now"), Memory.NULL);
        }
        return dateTime.__construct(env, traceInfo, args[0], args.length == 2 ? args[1] : Memory.NULL);
    }

    public static Memory date_date_set(Environment env, TraceInfo traceInfo, Memory object, int year, int month, int day) {
        return object.toObject(DateTime.class).setDate(env, traceInfo, year, month, day);
    }

    public static Memory date_add(Environment env, TraceInfo traceInfo,
                                  Memory dateTime, Memory interval) {
        return dateTime.toObject(DateTime.class).add(env, traceInfo, interval.toObject(DateInterval.class));
    }

    public static Memory date_sub(Environment env, TraceInfo traceInfo,
                                  Memory dateTime, Memory interval) {
        return dateTime.toObject(DateTime.class).sub(env, traceInfo, interval.toObject(DateInterval.class));
    }

    public static Memory date_diff(Environment env, TraceInfo traceInfo, Memory dateTime1,
                                   Memory dateTime2, boolean absolute) {
        return dateTime1.toObject(DateTimeInterface.class).diff(env, traceInfo, dateTime2);
    }

    public static Memory date_diff(Environment env, TraceInfo traceInfo, Memory dateTime1,
                                   Memory dateTime2) {
        return date_diff(env, traceInfo, dateTime1, dateTime2, false);
    }

    public static Memory date_timestamp_set(Environment env, TraceInfo traceInfo, DateTime dateTime, long timestamp) {
        return dateTime.setTimestamp(env, traceInfo, timestamp);
    }

    public static Memory date_timestamp_get(Environment env, TraceInfo traceInfo, DateTime dateTime) {
        return dateTime.getTimestamp(env, traceInfo);
    }

    public static Memory date_isodate_set(Environment env, TraceInfo traceInfo, DateTime dateTime, int year, int week, int day) {
        return dateTime.setISODate(env, traceInfo, year, week, day);
    }

    public static Memory date_isodate_set(Environment env, TraceInfo traceInfo, DateTime dateTime, int year, int week) {
        return dateTime.setISODate(env, traceInfo, year, week);
    }

    public static Memory date_modify(Environment env, TraceInfo traceInfo, DateTime dateTime, String modify) {
        return dateTime.modify(env, traceInfo, modify);
    }

    public static Memory date_offset_get(Environment env, TraceInfo traceInfo, DateTime dateTime) {
        return dateTime.getOffset(env, traceInfo);
    }

    public static Memory date_time_set(Environment env, TraceInfo traceInfo, DateTime dateTime,
                                       int hour, int minute, int second, int micosecond) {
        return dateTime.setTime(env, traceInfo, hour, minute, second, micosecond);
    }

    public static Memory date_time_set(Environment env, TraceInfo traceInfo, DateTime dateTime,
                                       int hour, int minute, int second) {
        return dateTime.setTime(env, traceInfo, hour, minute, second);
    }

    public static Memory date_time_set(Environment env, TraceInfo traceInfo, DateTime dateTime,
                                       int hour, int minute) {
        return dateTime.setTime(env, traceInfo, hour, minute);
    }

    public static Memory date_parse(Environment env, TraceInfo traceInfo, Memory date) {
        try {
            DateTimeParseResult result = DateTime.parse(env, traceInfo, date, Memory.NULL, ZonedDateTime.now());

            return result.toArrayMemory();
        } catch (DateTimeParserException e) {
            return Memory.FALSE;
        }
    }

    public static Memory date_parse_from_format(Environment env, TraceInfo traceInfo, Memory format, Memory date) {
        try {
            ZoneId zoneId = zoneId(date_default_timezone_get(env, traceInfo));
            DateTimeParseResult result = DateFormat.createParseResultFromFormat(format.toString(), date.toString(),
                ZonedDateTime.now(zoneId));

            return result.toArrayMemory();
        } catch (DateTimeException | NoSuchElementException | IllegalArgumentException e) {
            return new DateTimeParseResult(null, Collections.emptySet(), null, null)
                .toArrayMemory();
        }
    }

    public static Memory date_interval_format(Environment env, TraceInfo traceInfo, Memory object, String format) {
        return object.toObject(DateInterval.class).format(env, traceInfo, format);
    }

    public static Memory date_interval_create_from_date_string(Environment env, TraceInfo traceInfo,
                                                               Memory time) {
        return DateInterval.createFromDateString(env, traceInfo, time);
    }

    // Timezone
    public static Memory date_timezone_get(Environment env, TraceInfo traceInfo, Memory object) {
        return object.toObject(DateTimeInterface.class).getTimezone(env, traceInfo);
    }

    public static Memory date_timezone_set(Environment env, TraceInfo traceInfo, Memory object, DateTimeZone timeZone) {
        return object.toObject(DateTime.class).setTimezone(env, traceInfo, timeZone);
    }

    public static Memory timezone_offset_get(Environment env, TraceInfo traceInfo, Memory object, Memory dateTime) {
        return object.toObject(DateTimeZone.class).getOffset(env, traceInfo, dateTime);
    }

    public static Memory timezone_name_from_abbr(Environment env, TraceInfo traceInfo, String abbr, int gmtOffset, int isDst) {
        String timezone = ZoneIdFactory.abbrToRegion(abbr, gmtOffset, isDst);
        if (timezone == null) {
            return Memory.FALSE;
        }

        return StringMemory.valueOf(timezone);
    }

    public static Memory timezone_name_from_abbr(Environment env, TraceInfo traceInfo, String abbr, int gmtOffset) {
        return timezone_name_from_abbr(env, traceInfo, abbr, gmtOffset, -1);
    }

    public static Memory timezone_name_from_abbr(Environment env, TraceInfo traceInfo, String abbr) {
        return timezone_name_from_abbr(env, traceInfo, abbr, -1);
    }

    public static Memory timezone_abbreviations_list(Environment env, TraceInfo traceInfo) {
        return DateTimeZone.listAbbreviations(env, traceInfo);
    }

    public static Memory timezone_open(Environment env, TraceInfo traceInfo, Memory... args) {
        StringMemory arg = args[0].toValue(StringMemory.class);
        if (arg.toString().indexOf('\0') != -1) {
            env.warning(traceInfo, Messages.ERR_TIMEZONE_NULL_BYTE, "timezone_open()");
            return Memory.FALSE;
        }

        DateTimeZone dateTimeZone = new DateTimeZone(env);
        return dateTimeZone.__construct(env, traceInfo, arg);
    }

    public static Memory timezone_identifiers_list(Environment env, TraceInfo traceInfo, int what, String country) {
        return DateTimeZone.listIdentifiers(env, traceInfo, what, country);
    }

    public static Memory timezone_identifiers_list(Environment env, TraceInfo traceInfo, int what) {
        return DateTimeZone.listIdentifiers(env, traceInfo, what);
    }

    public static Memory timezone_identifiers_list(Environment env, TraceInfo traceInfo) {
        return DateTimeZone.listIdentifiers(env, traceInfo);
    }

    public static Memory time() {
        return LongMemory.valueOf(epochSeconds());
    }

    public static Memory idate(Environment env, TraceInfo traceInfo, String format) {
        return idate(env, traceInfo, format, epochSeconds());
    }

    public static Memory idate(Environment env, TraceInfo traceInfo, String format, long time) {
        if (format.length() != 1) {
            env.warning(traceInfo, "idate(): idate format is one char");
            return Memory.FALSE;
        }

        char c = format.charAt(0);

        ZonedDateTime dateTime = zonedDateTime(env, traceInfo, time);
        long ret;

        switch (c) {
            case 'B':
                // https://stackoverflow.com/questions/22693794/how-do-i-get-the-current-time-in-swatch-internet-time-in-java
                ZonedDateTime dt = dateTime.withZoneSameInstant(ZoneId.of("UTC+01:00"));
                ret = (int) ((dt.get(ChronoField.SECOND_OF_MINUTE) +
                    (dt.get(ChronoField.MINUTE_OF_HOUR) * 60) +
                    (dt.get(ChronoField.HOUR_OF_DAY) * 3600)) / 86.4);
                break;
            case 'd':
                ret = dateTime.getDayOfMonth();
                break;
            case 'h':
                ret = dateTime.getHour() % 12;
                break;
            case 'H':
                ret = dateTime.getHour();
                break;
            case 'i':
                ret = dateTime.getMinute();
                break;
            case 'I':
                boolean dst = dateTime.getZone().getRules().isDaylightSavings(dateTime.toInstant());
                ret = dst ? 1 : 0;
                break;
            case 'L':
                ret = Year.isLeap(dateTime.getYear()) ? 1 : 0;
                break;
            case 'm':
                ret = dateTime.getMonthValue();
                break;
            case 's':
                ret = dateTime.getSecond();
                break;
            case 't':
                ret = dateTime.getMonth().length(Year.isLeap(dateTime.getYear()));
                break;
            case 'U':
                ret = time;
                break;
            case 'w': // 0 - Sunday
                ret = dateTime.getDayOfWeek().getValue();
                break;
            case 'W':
                ret = dateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                break;
            case 'y':
                ret = dateTime.getYear() % 100;
                break;
            case 'Y':
                ret = dateTime.getYear();
                break;
            case 'z':
                ret = dateTime.getDayOfYear() - 1;
                break;
            case 'Z':
                ret = dateTime.toOffsetDateTime().getOffset().getTotalSeconds();
                break;
            default:
                env.warning(traceInfo, "idate(): Unrecognized date format token");
                return Memory.FALSE;
        }

        return LongMemory.valueOf(ret);
    }

    public static Memory strftime(Environment env, TraceInfo traceInfo, String format) {
        return strftime(env, traceInfo, format, epochSeconds());
    }

    public static Memory strftime(Environment env, TraceInfo traceInfo, String format, long time) {
        if (format.isEmpty())
            return Memory.FALSE;

        StringBuilder buff = strftimeImpl(zonedDateTime(env, traceInfo, time), env.getLocale(), format, new StringBuilder());

        return StringMemory.valueOf(buff.toString());
    }

    private static StringBuilder strftimeImpl(ZonedDateTime date, Locale l, String format, StringBuilder buff) {
        for (int i = 0, n = format.length(); i < n; i++) {
            char c = format.charAt(i);

            if (c == '%' && ++i < n) {
                c = format.charAt(i);

                switch (c) {
                    // Days
                    case 'a': {
                        buff.append(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, l));
                        break;
                    }
                    case 'A': {
                        buff.append(date.getDayOfWeek().getDisplayName(TextStyle.FULL, l));
                        break;
                    }
                    case 'd': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.getDayOfMonth()));
                        break;
                    }
                    case 'e': {
                        int dayOfMonth = date.getDayOfMonth();
                        if (dayOfMonth < 10)
                            buff.append(' ');

                        buff.append(dayOfMonth);
                        break;
                    }
                    case 'j': {
                        buff.append(String.format(l, "%03d", date.getDayOfYear()));
                        break;
                    }
                    case 'u':
                    case 'w': {
                        buff.append(date.getDayOfWeek().getValue());
                        break;
                    }
                    // Week
                    case 'W':
                    case 'U': {
                        int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                        buff.append(--week);
                        break;
                    }
                    case 'V': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)));
                        break;
                    }
                    // Month
                    case 'h':
                    case 'b': {
                        buff.append(date.getMonth().getDisplayName(TextStyle.SHORT, l));
                        break;
                    }
                    case 'B': {
                        buff.append(date.getMonth().getDisplayName(TextStyle.FULL, l));
                        break;
                    }
                    case 'm': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.getMonth().getValue()));
                        break;
                    }
                    // Year
                    case 'C': {
                        buff.append(date.getYear() / 100);
                        break;
                    }
                    case 'y':
                    case 'g': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.getYear() % 100));
                        break;
                    }
                    case 'G':
                    case 'Y': {
                        buff.append(String.format(l, "%04d", date.getYear()));
                        break;
                    }
                    // Time
                    case 'H': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.getHour()));
                        break;
                    }
                    case 'k': {
                        buff.append(String.format(l, "% 4d", date.getHour()));
                        break;
                    }
                    case 'I': {
                        int hRem = date.getHour() % 12;
                        buff.append(String.format(l, TWO_DIGIT_INT, hRem > 0 ? hRem : 12));
                        break;
                    }
                    case 'L': {
                        int hRem = date.getHour() % 12;
                        buff.append(String.format(l, "% 2d", hRem > 0 ? hRem : 12));
                        break;
                    }
                    case 'M': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.getMinute()));
                        break;
                    }
                    case 'p': {
                        buff.append(date.getHour() >= 12 ? "PM" : "AM");
                        break;
                    }
                    case 'P': {
                        buff.append(date.getHour() >= 12 ? "pm" : "am");
                        break;
                    }
                    case 'r': {
                        strftimeImpl(date, l, "%I:%M:%S %p", buff);
                        break;
                    }
                    case 'R': {
                        strftimeImpl(date, l, "%H:%M", buff);
                        break;
                    }
                    case 'S': {
                        buff.append(String.format(l, TWO_DIGIT_INT, date.getSecond()));
                        break;
                    }
                    case 'X':
                    case 'T': {
                        strftimeImpl(date, l, "%H:%M:%S", buff);
                        break;
                    }
                    case 'z': {
                        long hours = Duration.ofSeconds(date.getOffset().getTotalSeconds()).toHours();
                        String offset = ((hours < 0) ? "-" : "+") + String.format(l, "%02d00", Math.abs(hours));
                        buff.append(offset);
                        break;
                    }
                    case 'Z': {
                        ZoneId zone = date.getZone();
                        if (zone instanceof ZoneOffset) {
                            buff.append("GMT");
                            strftimeImpl(date, l, "%z", buff);
                        } else {
                            String str = ZoneIdFactory.aliasFor(date);
                            if (str == null) {
                                long hours = Duration.ofSeconds(date.getOffset().getTotalSeconds()).toHours();
                                buff.append(hours < 0 ? '-' : '+')
                                    .append(String.format(l, TWO_DIGIT_INT, Math.abs(hours)));
                            } else {
                                buff.append(str);
                            }
                        }
                        break;
                    }
                    // Timestamps
                    case 'c': {
                        strftimeImpl(date, l, "%a %b %e %H:%M:%S %Y", buff);
                        break;
                    }
                    case 'x':
                    case 'D': {
                        strftimeImpl(date, l, "%m/%d/%y", buff);
                        break;
                    }
                    case 'F': {
                        strftimeImpl(date, l, "%Y-%m-%d", buff);
                        break;
                    }
                    case 's': {
                        buff.append(date.toEpochSecond());
                        break;
                    }
                    case 'n': {
                        buff.append('\n');
                        break;
                    }
                    case 't': {
                        buff.append('\t');
                        break;
                    }
                    case '%': {
                        buff.append(c);
                        break;
                    }
                    default: {
                        buff.append('%').append(c);
                        break;
                    }
                }
            } else {
                buff.append(c);
            }
        }

        return buff;
    }

    private static ZonedDateTime zonedDateTime(Environment env, TraceInfo traceInfo, long time) {
        return Instant.ofEpochSecond(time).atZone(zoneId(env, traceInfo));
    }

    private static long epochSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    private static class LocaltimeStructureHolder {
        private static final Memory[] VALUE = {
            StringMemory.valueOf("tm_sec"),
            StringMemory.valueOf("tm_min"),
            StringMemory.valueOf("tm_hour"),
            StringMemory.valueOf("tm_mday"),
            StringMemory.valueOf("tm_mon"),
            StringMemory.valueOf("tm_year"),
            StringMemory.valueOf("tm_wday"),
            StringMemory.valueOf("tm_yday"),
            StringMemory.valueOf("tm_isdst")
        };
    }
}
