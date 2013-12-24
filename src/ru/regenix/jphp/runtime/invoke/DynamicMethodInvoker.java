package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.lang.reflect.InvocationTargetException;

public class DynamicMethodInvoker extends Invoker {
    protected final IObject object;
    protected final MethodEntity method;

    public DynamicMethodInvoker(Environment env, TraceInfo trace, IObject object, MethodEntity method) {
        super(env, trace);
        this.object = object;
        this.method = method;
    }

    public IObject getObject() {
        return object;
    }

    public MethodEntity getMethod() {
        return method;
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, object, args, method.getName(), object.getReflection().getName());
    }

    @Override
    public Memory call(Memory... args) throws InvocationTargetException, IllegalAccessException {
        return ObjectInvokeHelper.invokeMethod(object, method, env, null, args);
    }

    @Override
    public int canAccess(Environment env, boolean external) throws InvocationTargetException, IllegalAccessException {
        return method.canAccess(env, external);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, IObject object, String methodName){
        MethodEntity methodEntity = object.getReflection().findMethod(methodName.toLowerCase());
        if (methodEntity == null){
            if (trace == null) {
                if (object.getReflection().methodMagicCall != null) {
                    return new MagicDynamicMethodInvoker(
                            env, trace, object, object.getReflection().methodMagicCall, methodName
                    );
                }

                return null;
            }
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(object.getReflection().getName() +"::"+ methodName),
                    trace
            ));
        }

        return new DynamicMethodInvoker(env, trace, object, methodEntity);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, Memory object, String methodName){
        return valueOf(env, trace, ((ObjectMemory)object).value, methodName);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, IObject object){
        MethodEntity methodEntity = object.getReflection().methodMagicInvoke;
        if (methodEntity == null){
            if (trace == null)
                return null;
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(object.getReflection().getName() +"::__invoke"),
                    trace
            ));
        }

        return new DynamicMethodInvoker(env, null, object, methodEntity);
    }

    public static DynamicMethodInvoker valueOf(Environment env, TraceInfo trace, Memory object){
        return valueOf(env, trace, ((ObjectMemory)object).value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynamicMethodInvoker)) return false;

        DynamicMethodInvoker that = (DynamicMethodInvoker) o;

        if (!method.equals(that.method)) return false;
        if (object.getPointer() != that.object.getPointer()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = object.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
