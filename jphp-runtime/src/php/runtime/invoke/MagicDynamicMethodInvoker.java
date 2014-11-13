package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.MethodEntity;

class MagicDynamicMethodInvoker extends DynamicMethodInvoker {
    protected final Memory methodName;

    public MagicDynamicMethodInvoker(Environment env, TraceInfo trace,
                                     IObject object, MethodEntity method, String methodName) {
        super(env, trace, object, method);
        this.methodName = new StringMemory(methodName);
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, object, args, methodName.toString(), method.getClazz().getName(), object.getReflection().getName());
        env.pushCall(
                trace, object,
                new Memory[]{methodName, new ArrayMemory(true, args)},
                method.getName(), method.getClazz().getName(), object.getReflection().getName()
        );
    }

    @Override
    public void popCall() {
        env.popCall();
        env.popCall();
    }

    @Override
    protected Memory invoke(Memory... args) throws Throwable {
        return super.invoke(methodName, new ArrayMemory(false, args));
    }
}
