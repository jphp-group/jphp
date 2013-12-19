package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.env.message.WarningMessage;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.ParameterEntity;

import java.lang.reflect.InvocationTargetException;

final public class InvokeHelper {

    private InvokeHelper() { }

    public static void checkAccess(Environment env, TraceInfo trace, MethodEntity method){
        switch (method.canAccess(env)){
            case 1: throw new FatalException(
                    Messages.ERR_FATAL_CALL_TO_PROTECTED_METHOD.fetch(
                            method.getClazz().getName() + "::" + method.getName(), env.getContext()
                    ),
                    trace
            );
            case 2: throw new FatalException(
                    Messages.ERR_FATAL_CALL_TO_PRIVATE_METHOD.fetch(
                            method.getClazz().getName() + "::" + method.getName(), env.getContext()
                    ),
                    trace
            );
        }
    }

    public static Memory[] makeArguments(Environment env, Memory[] args,
                                       ParameterEntity[] parameters,
                                       String originClassName, String originMethodName,
                                       TraceInfo trace){
        Memory[] passed = args;
        if ((args == null && parameters.length > 0) || (args != null && args.length < parameters.length)){
            passed = new Memory[parameters.length];
            if (args != null && args.length > 0){
                System.arraycopy(args, 0, passed, 0, args.length);
            }
        }

        int i = 0;
        //assert passed != null;
        if (passed != null)
        for(ParameterEntity param : parameters){
            Memory arg = passed[i];
            if (arg == null){
                Memory def = param.getDefaultValue();
                if (def != null){
                    if (!param.isReference())
                        passed[i] = def.toImmutable();
                    else
                        passed[i] = new ReferenceMemory(def.toImmutable());
                } else {
                    env.triggerMessage(new WarningMessage(
                            env,
                            Messages.ERR_WARNING_MISSING_ARGUMENT, (i + 1) + " ($" + param.getName() + ")",
                            originMethodName == null ? originClassName : originClassName + "::" + originMethodName
                    ));
                    passed[i] = param.isReference() ? new ReferenceMemory() : Memory.NULL;
                }
            } else {
                if (param.isReference()) {
                    if (!arg.isReference()){
                        env.warning(trace, "Only variables can be passed by reference");
                        passed[i] = new ReferenceMemory(arg);
                    }
                } else
                    passed[i] = arg.toImmutable();
            }

            switch (param.getType()){
                case ARRAY: {
                    if (!passed[i].isArray())
                        env.triggerError(new CompileException("Argument " + i + " must be array", trace));
                } break;
                case NUMERIC:
                    if (!passed[i].isNumber())
                        env.triggerError(new CompileException("Argument " + i + " must be int or double", trace));
                    break;
                case STRING:
                    if (!passed[i].isString())
                        env.triggerError(new CompileException("Argument " + i + " must be string", trace));
            }
            i++;
        }
        return passed;
    }

    public static Memory callAny(Environment env, String calledClass, TraceInfo trace, Memory method, Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        method = method.toImmutable();
        if (method.isObject()){
            return ObjectInvokeHelper.invokeMethod(method, null, null, env, trace, args);
        } else if (method.isArray()){
            Memory one = null, two = null;
            for(Memory el : (ArrayMemory)method){
                if (one == null)
                    one = el;
                else if (two == null)
                    two = el;
                else
                    break;
            }

            if (one == null || two == null) {
                env.triggerError(new FatalException(
                        Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(method.toString()),
                        trace
                ));
            }

            assert one != null;
            assert two != null;
            String methodName = two.toString();
            if (one.isObject())
                return ObjectInvokeHelper.invokeMethod(one, methodName, methodName.toLowerCase(), env, trace, args);
            else {
                String className = one.toString();
                return InvokeHelper.callStaticDynamic(
                        env,
                        calledClass,
                        trace,
                        className, className.toLowerCase(),
                        methodName, methodName.toLowerCase(),
                        args
                );
            }
        } else {
            String methodName = method.toString();
            int p;
            if ((p = methodName.indexOf("::")) > -1) {
                String className = methodName.substring(0, p);
                methodName = methodName.substring(p + 2, methodName.length());
                return InvokeHelper.callStaticDynamic(
                        env, calledClass, trace,
                        className, className.toLowerCase(),
                        methodName, methodName.toLowerCase(),
                        args
                );
            } else {
                return InvokeHelper.call(env, trace, methodName.toLowerCase(), methodName, args);
            }
        }
    }

    public static Memory call(Environment env, TraceInfo trace, FunctionEntity function, Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        Memory[] passed = function.parameters == null
                ? args
                : makeArguments(env, args, function.parameters, function.getName(), null, trace);

        Memory result;
        if (trace != null)
            env.pushCall(trace, null, args, function.getName(), null);

        try {
            result = function.invoke(env, trace, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
        return result;
    }

    public static Memory call(Environment env, TraceInfo trace, String sign, String originName,
                              Memory[] args) throws InvocationTargetException, IllegalAccessException {
        FunctionEntity function = env.scope.functionMap.get(sign);
        if (function == null) {
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(originName),
                    trace
            ));
        }
        assert function != null;
        return call(env, trace, function, args);
    }

    public static Memory callStaticDynamic(Environment env, String _static, TraceInfo trace,
                                           String originClassName, String className,
                                           String originMethodName, String methodName,
                                           Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        return callStatic(
                env, _static, trace,
                className + "#" + methodName,
                originClassName, originMethodName,
                args
        );
    }

    public static Memory callStatic(Environment env, String _static, TraceInfo trace,
                                    String sign, String originClassName, String originMethodName,
                                    Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        MethodEntity method = env.scope.methodMap.get(sign);
        Memory[] passed = null;

        if (method == null){
            ClassEntity __class__ = env.scope.classMap.get(originClassName.toLowerCase());
            if (__class__ != null && __class__.methodMagicCallStatic != null){
                method = __class__.methodMagicCallStatic;
                passed = new Memory[]{
                        new StringMemory(originMethodName),
                        new ArrayMemory(true, args)
                };
            } else {
                // TODO: class auto loading...
            }
        }
        if (method == null){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(originClassName + "::" + originMethodName),
                    trace
            ));
        }
        assert method != null;
        if (!method.isStatic()) {
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_NON_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(originClassName + "::" + originMethodName),
                    trace
            ));
        }

        if (passed == null)
            passed = makeArguments(env, args, method.parameters, originClassName, originMethodName, trace);

        Memory result;

        if (trace != null)
            env.pushCall(trace, null, args, originMethodName, originClassName);

        try {
            checkAccess(env, trace, method);
            result = method.invokeStatic(env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }

        return result;
    }

    public static Memory callStatic(Environment env, String contex, TraceInfo trace,
                                    MethodEntity method,
                                    Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        String originClassName = method.getClazz().getName();
        String originMethodName = method.getName();

        Memory[] passed = makeArguments(env, args, method.parameters, originClassName, originMethodName, trace);
        Memory result;

        if (trace != null)
            env.pushCall(trace, null, args, originMethodName, originClassName);

        try {
            checkAccess(env, trace, method);
            result = method.invokeStatic(env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }

        return result;
    }

    public static void checkReturnReference(Memory memory, Environment env, TraceInfo trace){
        if (memory.isImmutable()){
            env.warning(trace, Messages.ERR_NOTICE_RETURN_NOT_REFERENCE.fetch());
        }
    }
}
