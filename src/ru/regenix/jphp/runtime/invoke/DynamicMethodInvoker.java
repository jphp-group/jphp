package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.lang.reflect.InvocationTargetException;

public class DynamicMethodInvoker extends Invoker {
    protected final PHPObject object;
    protected final MethodEntity method;

    public DynamicMethodInvoker(Environment env, TraceInfo trace, PHPObject object, MethodEntity method) {
        super(env, trace);
        this.object = object;
        this.method = method;
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, object, args, method.getName(), object.__class__.getName());
    }

    @Override
    public Memory call(Memory... args) throws InvocationTargetException, IllegalAccessException {
        return ObjectInvokeHelper.invokeMethod(object, method, env, null, args);
    }


    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, PHPObject object, String methodName){
        MethodEntity methodEntity = object.__class__.findMethod(methodName.toLowerCase());
        if (methodEntity == null){
            if (trace == null) {
                if (object.__class__.methodMagicCall != null) {
                    return new MagicDynamicMethodInvoker(
                            env, trace, object, object.__class__.methodMagicCall, methodName
                    );
                }

                return null;
            }
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(object.__class__.getName() +"::"+ methodName),
                    trace
            ));
        }

        return new DynamicMethodInvoker(env, trace, object, methodEntity);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, Memory object, String methodName){
        return valueOf(env, trace, ((ObjectMemory)object).value, methodName);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, PHPObject object){
        MethodEntity methodEntity = object.__class__.methodMagicInvoke;
        if (methodEntity == null){
            if (trace == null)
                return null;
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(object.__class__.getName() +"::__invoke"),
                    trace
            ));
        }

        return new DynamicMethodInvoker(env, null, object, methodEntity);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, Memory object){
        return valueOf(env, trace, ((ObjectMemory)object).value);
    }
}
