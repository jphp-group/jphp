package org.develnext.jphp.zend.ext.standard;

import java.util.TimeZone;
import java.util.Date;

import php.runtime.Memory;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

public class DateFunctions extends FunctionsContainer {
    public static final int MSEC_IN_MIN = 60 * 1000;

    public static Memory microtime(boolean getAsFloat){
        double now = System.currentTimeMillis() / 1000.0;
        int s = (int)now;

        return getAsFloat
                ? new DoubleMemory(now)
                : new StringMemory((Math.round((now - s) * 1000) / 1000) + " " + s);
    }

    public static Memory microtime(){
        return microtime(false);
    }

    public static Memory gettimeofday(boolean getAsFloat){
        long msec_time = System.currentTimeMillis();

	if (getAsFloat) {
	    double now = msec_time / 1000.0;

	    return new DoubleMemory(now);
	} else {
	    ArrayMemory result = new ArrayMemory();

	    TimeZone timeZone = TimeZone.getDefault();
	    long sec  = msec_time / 1000;
	    long usec = (msec_time % 1000) * 1000;
	    long minuteswest = - timeZone.getOffset(msec_time) / MSEC_IN_MIN;
	    boolean is_dst = timeZone.inDaylightTime(new Date(msec_time));

	    result.refOfIndex("sec").assign(sec);
	    result.refOfIndex("usec").assign(usec);
	    result.refOfIndex("minuteswest").assign(minuteswest);
	    result.refOfIndex("dsttime").assign(is_dst ? 1 : 0);

	    return result.toConstant();
	}
    }

    public static Memory gettimeofday(){
        return gettimeofday(false);
    }
}
