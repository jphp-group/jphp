package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;

import java.lang.reflect.InvocationTargetException;

public class FunctionInvoker extends Invoker {
    private final FunctionEntity entity;
    protected final Environment env;

    public FunctionInvoker(Environment env, TraceInfo trace, FunctionEntity function) {
        super(env, trace);
        entity = function;
        this.env = env;
    }

    public FunctionEntity getFunction() {
        return entity;
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, null, args, entity.getName(), null);
    }

    @Override
    public Memory call(Memory... args) throws Throwable {
        return InvokeHelper.call(env, trace, entity, args);
    }

    @Override
    public int canAccess(Environment env, boolean external) throws InvocationTargetException, IllegalAccessException {
        return 0;
    }

    public static FunctionInvoker valueOf(Environment env, TraceInfo trace, String name){
        FunctionEntity functionEntity = env.functionMap.get(name.toLowerCase());
        if (functionEntity == null){
            if (trace == null)
                return null;
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(name),
                    trace
            ));
        }

        return new FunctionInvoker(env, trace, functionEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionInvoker)) return false;

        FunctionInvoker that = (FunctionInvoker) o;

        if (!entity.equals(that.entity)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }
}
