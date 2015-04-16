package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.time.WrapTimeZone;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.TimeZone;

public class TimeZoneMemoryOperation extends MemoryOperation<TimeZone> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { TimeZone.class };
    }

    @Override
    public TimeZone convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return arg.toObject(WrapTimeZone.class).getTimeZone();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, TimeZone arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf(new WrapTimeZone(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeNativeClass(WrapTimeZone.class);
    }
}
