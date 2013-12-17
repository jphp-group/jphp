package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.lang.reflect.InvocationTargetException;

class MagicDynamicMethodInvoker extends DynamicMethodInvoker {
    protected final Memory methodName;

    public MagicDynamicMethodInvoker(Environment env, TraceInfo trace,
                                     PHPObject object, MethodEntity method, String methodName) {
        super(env, trace, object, method);
        this.methodName = new StringMemory(methodName);
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, object, args, methodName.toString(), object.__class__.getName());
        env.pushCall(
                trace, object,
                new Memory[]{methodName, new ArrayMemory(true, args)},
                method.getName(), object.__class__.getName()
        );
    }

    @Override
    public void popCall() {
        env.popCall();
        env.popCall();
    }

    @Override
    public Memory call(Memory... args) throws InvocationTargetException, IllegalAccessException {
        return super.call(methodName, new ArrayMemory(false, args));
    }
}
