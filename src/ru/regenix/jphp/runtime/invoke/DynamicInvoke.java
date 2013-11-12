package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.env.message.NoticeMessage;
import ru.regenix.jphp.runtime.env.message.WarningMessage;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ParameterEntity;

import java.lang.reflect.InvocationTargetException;

public class DynamicInvoke {

    public static Memory callStaticDynamic(Environment env, String _static, TraceInfo trace,
                                           String originClassName, String className,
                                           String originMethodName, String methodName,
                                           Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        return callStatic(
                env, _static, trace,
                (className + "#" + methodName).hashCode(),
                originClassName, originMethodName,
                args
        );
    }

    public static Memory callStatic(Environment env, String _static, TraceInfo trace,
                                    int hash, String originClassName, String originMethodName,
                                    Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        MethodEntity method = env.scope.fastMethodMap.get(hash);
        if (method == null){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(originClassName + "::" + originMethodName),
                    trace
            ));
        }

        int i = 0;
        Memory[] passed = args;
        assert method != null;
        
        if (args.length < method.parameters.length){
            passed = new Memory[method.parameters.length];
            if (args.length > 0){
                System.arraycopy(args, 0, passed, 0, args.length);
            }
        }

        for(ParameterEntity param : method.parameters){
            if (passed[i] == null){
                passed[i] = param.getDefaultValue();
                if (passed[i] == null){
                    env.triggerMessage(new WarningMessage(
                            trace,
                            Messages.ERR_WARNING_MISSING_ARGUMENT, (i + 1) + " ($" + param.getName() + ")",
                            originClassName + "::" + originMethodName
                    ));
                    passed[i] = Memory.NULL;
                }

            } else if (!param.isReference())
                passed[i] = passed[i].toImmutable();

            switch (param.getType()){
                case ARRAY: {
                    if (!passed[i].isArray())
                        env.triggerError(new CompileException("Argument " + i + " must be array", trace));
                }
            }
            i++;
        }
        return method.invokeStatic(_static, env, passed);
    }

    public static void checkReturnReference(Memory memory, Environment env, TraceInfo trace){
        if (memory.isImmutable()){
            env.triggerMessage(new NoticeMessage(
                    trace,
                    Messages.ERR_NOTICE_RETURN_NOT_REFERENCE
            ));
        }
    }
}
