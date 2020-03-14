package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class EnumMemoryOperation extends MemoryOperation {
    private Class<? extends Enum> enumClass;

    public EnumMemoryOperation(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { enumClass };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : Enum.valueOf(enumClass, arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
        return arg == null ? Memory.NULL : StringMemory.valueOf(((Enum) arg).name());
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeEnum(enumClass);
    }
}
