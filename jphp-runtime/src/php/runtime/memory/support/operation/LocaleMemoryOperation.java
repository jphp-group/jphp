package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.util.WrapLocale;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.util.Locale;

public class LocaleMemoryOperation extends MemoryOperation<Locale> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Locale.class };
    }

    @Override
    public Locale convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return arg.toObject(WrapLocale.class).getLocale();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Locale arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf(new WrapLocale(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeClass(ReflectionUtils.getClassName(WrapLocale.class));
    }
}
