package org.develnext.jphp.ext.javafx.bind;

import javafx.util.Duration;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.support.MemoryOperation;

public class DurationMemoryOperation extends MemoryOperation<Duration> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] {Duration.class};
    }

    @Override
    public Duration convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        long millis = arg.toLong();
        return millis == -1 ? Duration.INDEFINITE : new Duration(millis);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Duration arg) throws Throwable {
        return arg == null ? Memory.NULL : LongMemory.valueOf(arg.isIndefinite() ? -1L : (long) arg.toMillis());
    }
}
