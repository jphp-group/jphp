package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.lang.reflect.InvocationTargetException;

abstract public class Invoker {
    protected final Environment env;
    protected TraceInfo trace;
    protected boolean pushCallTrace = true;

    protected Invoker(Environment env, TraceInfo trace) {
        this.env = env;
        this.trace = trace;
    }

    public void setPushCallTrace(boolean pushCallTrace) {
        this.pushCallTrace = pushCallTrace;
    }

    abstract public int getArgumentCount();

    abstract public void pushCall(TraceInfo trace, Memory[] args);

    abstract public Memory call(Memory... args) throws Throwable;

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
        method = method.toValue();
        if (method.isObject()){
            return DynamicMethodInvoker.valueOf(env, trace, method);
        } else if (method.isArray()){
            Memory one = null, two = null;
            ForeachIterator iterator = method.getNewIterator(env, false, false);
            while (iterator.next()){
                if (one == null)
                    one = iterator.getValue();
                else if (two == null)
                    two = iterator.getValue();
                else
                    break;
            }

            if (one == null || two == null) {
                if (trace == null)
                    return null;
                env.error(trace, ErrorType.E_ERROR, Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(method.toString()));
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
