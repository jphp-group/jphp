package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.lang.reflect.InvocationTargetException;

public class StaticMethodInvoker extends Invoker {
    protected final MethodEntity method;
    protected final String calledClass;

    public StaticMethodInvoker(Environment env, TraceInfo trace, String calledClass, MethodEntity method) {
        super(env, trace);
        this.method = method;
        this.calledClass = calledClass;
    }

    @Override
    public Memory call(Memory... args) throws InvocationTargetException, IllegalAccessException {
        return InvokeHelper.callStatic(env, calledClass, null, method, args);
    }

    public static StaticMethodInvoker valueOf(Environment env, String className, String methodName){
        MethodEntity methodEntity = env.scope.methodMap.get(
                className.toLowerCase() + "#" + methodName.toLowerCase()
        );
        if (methodEntity == null){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(className +"::"+ methodName),
                    env.peekCall(0).trace
            ));
        }

        return new StaticMethodInvoker(env, null, "", methodEntity);
    }
}
