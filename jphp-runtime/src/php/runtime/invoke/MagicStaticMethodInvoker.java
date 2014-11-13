package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.MethodEntity;

class MagicStaticMethodInvoker extends StaticMethodInvoker {
    protected Memory methodName;

    public MagicStaticMethodInvoker(Environment env, TraceInfo trace, String calledClass, MethodEntity method,
                                    String methodName) {
        super(env, trace, calledClass, method);
        this.methodName = new StringMemory(methodName);
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, null, args, methodName.toString(), method.getClazz().getName(), calledClass);
        env.pushCall(
                trace, null,
                new Memory[]{methodName, new ArrayMemory(true, args)},
                method.getName(), method.getClazz().getName(), calledClass
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
