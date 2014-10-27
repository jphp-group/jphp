package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.WrapEnvironment;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.memory.support.MemoryOperation;

public class EnvironmentMemoryOperation extends MemoryOperation<Environment> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Environment.class };
    }

    @Override
    public Environment convert(Environment env, TraceInfo trace, Memory arg) {
        return arg.toObject(WrapEnvironment.class).getEnvironment();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Environment arg) {
        return ObjectMemory.valueOf(new WrapEnvironment(env, arg));
    }
}
