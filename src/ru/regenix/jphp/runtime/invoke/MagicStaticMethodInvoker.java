package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

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
    public Memory call(Memory... args) throws Throwable {
        return super.call(methodName, new ArrayMemory(false, args));
    }
}
