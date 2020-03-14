package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.ReflectionUtils;

public class IObjectMemoryOperation extends MemoryOperation {
    private Class<? extends IObject> type;

    public IObjectMemoryOperation(Class<? extends IObject> type) {
        this.type = type;
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[]{ type };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return arg.toObject((Class<? extends IObject>) type);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf((IObject) arg);
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(ReflectionUtils.getClassName(type));
    }
}
