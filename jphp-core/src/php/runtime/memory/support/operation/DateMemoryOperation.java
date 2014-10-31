package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.time.WrapTime;
import php.runtime.ext.core.classes.util.WrapLocale;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.Date;
import java.util.Locale;

public class DateMemoryOperation extends MemoryOperation<Date> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Date.class };
    }

    @Override
    public Date convert(Environment env, TraceInfo trace, Memory arg) {
        if (arg.isNull()) {
            return null;
        }

        return arg.toObject(WrapTime.class).getDate();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Date arg) {
        if (arg == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf(new WrapTime(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeNativeClass(WrapTime.class);
    }
}
