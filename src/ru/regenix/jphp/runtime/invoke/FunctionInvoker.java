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

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, null, args, entity.getName(), null);
    }

    @Override
    public Memory call(Memory... args) throws InvocationTargetException, IllegalAccessException {
        return InvokeHelper.call(env, null, entity, args);
    }

    public static FunctionInvoker valueOf(Environment env, TraceInfo trace, String name){
        FunctionEntity functionEntity = env.scope.functionMap.get(name.toLowerCase());
        if (functionEntity == null){
            if (trace == null)
                return null;
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(name),
                    env.peekCall(0).trace
            ));
        }

        return new FunctionInvoker(env, null, functionEntity);
    }
}
