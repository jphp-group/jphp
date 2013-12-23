package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.lang.reflect.InvocationTargetException;

abstract public class Invoker {
    protected final Environment env;
    protected TraceInfo trace;

    protected Invoker(Environment env, TraceInfo trace) {
        this.env = env;
        this.trace = trace;
    }

    abstract public void pushCall(TraceInfo trace, Memory[] args);

    abstract public Memory call(Memory... args) throws InvocationTargetException, IllegalAccessException;

    public void popCall(){
        env.popCall();
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    public static Invoker valueOf(Environment env, Memory method){
        return valueOf(env, env.peekCall(0).trace, method);
    }

    abstract public int canAccess(Environment env, boolean external) throws InvocationTargetException, IllegalAccessException;

    public static Invoker valueOf(Environment env, TraceInfo trace, Memory method){
        method = method.toImmutable();
        if (method.isObject()){
            return DynamicMethodInvoker.valueOf(env, trace, method);
        } else if (method.isArray()){
            Memory one = null, two = null;
            for(Memory el : method.toValue(ArrayMemory.class).values()){
                if (one == null)
                    one = el;
                else if (two == null)
                    two = el;
                else
                    break;
            }

            if (one == null || two == null) {
                if (trace == null)
                    return null;
                env.triggerError(new FatalException(
                        Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(method.toString()),
                        trace
                ));
            }

            assert one != null;
            assert two != null;
            String methodName = two.toString();
            if (one.isObject()) {
                return DynamicMethodInvoker.valueOf(env, trace, one.toValue(), methodName);
            } else {
                String className = one.toString();
                return StaticMethodInvoker.valueOf(env, trace, className, methodName);
            }
        } else {
            String methodName = method.toString();
            int p;
            if ((p = methodName.indexOf("::")) > -1) {
                String className = methodName.substring(0, p);
                methodName = methodName.substring(p + 2, methodName.length());
                return StaticMethodInvoker.valueOf(env, trace, className, methodName);
            } else {
                return FunctionInvoker.valueOf(env, trace, methodName);
            }
        }
    }
}
