package org.develnext.jphp.zend.ext.standard;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.compile.FunctionsContainer;
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

    public static Memory localtime(Environment env, TraceInfo traceInfo, long time, boolean isAssociative) {
        ZoneId zone = zoneId(date_default_timezone_get(env, traceInfo));

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
