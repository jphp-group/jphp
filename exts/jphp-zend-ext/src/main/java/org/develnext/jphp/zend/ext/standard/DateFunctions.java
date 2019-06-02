package org.develnext.jphp.zend.ext.standard;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.StringMemory;

public class DateFunctions extends FunctionsContainer {
    private static final Memory TZ_UTC = StringMemory.valueOf("UTC");
    public static final int MSEC_IN_MIN = 60 * 1000;

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
            ZoneId.of(tz);

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

    /*public static Memory date(Environment env, String format) {
    	return date(env, format, LangFunctions.time().toLong());
	}

    public static Memory date(Environment env, String format, long time) {
		WrapTime wrapTime = new WrapTime(env, new Date(time * 1000));
		return wrapTime.toString(env, StringMemory.valueOf(format), Memory.NULL);
	}*/
}
