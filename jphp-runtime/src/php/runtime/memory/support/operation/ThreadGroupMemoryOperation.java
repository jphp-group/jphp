package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.WrapThreadGroup;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class ThreadGroupMemoryOperation extends MemoryOperation<ThreadGroup> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { ThreadGroup.class };
    }

    @Override
    public ThreadGroup convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return arg.toObject(WrapThreadGroup.class).getGroup();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ThreadGroup arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf(new WrapThreadGroup(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeNativeClass(WrapThreadGroup.class);
    }
}
