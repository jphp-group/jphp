package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.env.message.WarningMessage;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.*;

import java.lang.reflect.InvocationTargetException;

final public class InvokeHelper {

    private InvokeHelper() { }

    public static void checkAccess(Environment env, TraceInfo trace, MethodEntity method)
            throws InvocationTargetException, IllegalAccessException {
        int access = method.canAccess(env);
        if (access == 0)
            return;

        ClassEntity contextCls = env.getLastClassOnStack();
        String context = contextCls == null ? "" : contextCls.getName();

        switch (access){
            case 1: throw new FatalException(
                    Messages.ERR_FATAL_CALL_TO_PROTECTED_METHOD.fetch(
                            method.getClazz().getName() + "::" + method.getName(), context
                    ),
                    trace
            );
            case 2: throw new FatalException(
                    Messages.ERR_FATAL_CALL_TO_PRIVATE_METHOD.fetch(
                            method.getClazz().getName() + "::" + method.getName(), context
                    ),
                    trace
            );
        }
    }

    public static void checkAccess(Environment env, TraceInfo trace, PropertyEntity property)
            throws InvocationTargetException, IllegalAccessException {
        switch (property.canAccess(env)){
            case 1: throw new FatalException(
                    Messages.ERR_FATAL_ACCESS_TO_PROTECTED_PROPERTY.fetch(
                            property.getClazz().getName(), property.getName()
                    ),
                    trace
            );
            case 2: throw new FatalException(
                    Messages.ERR_FATAL_ACCESS_TO_PRIVATE_PROPERTY.fetch(
                            property.getClazz().getName(), property.getName()
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
                            new CallStackItem(trace),
                            Messages.ERR_WARNING_MISSING_ARGUMENT, (i + 1) + " ($" + param.getName() + ")",
                            originMethodName == null ? originClassName : originClassName + "::" + originMethodName
                    ));
                    passed[i] = param.isReference() ? new ReferenceMemory() : Memory.NULL;
                }
            } else {
                if (param.isReference()) {
                    if (!arg.isReference() && !arg.isObject()){
                        env.warning(trace, "Only variables can be passed by reference");
                        passed[i] = new ReferenceMemory(arg);
                    }
                } else
                    passed[i] = arg.toImmutable();
            }

            switch (param.getType()){
                case ARRAY: {
                    if (!passed[i].isArray())
                        env.error(trace, ErrorType.E_RECOVERABLE_ERROR, "Argument %s must be array", i);
                } break;
                case INT:
                    if (!passed[i].isNumber())
                        env.error(trace, ErrorType.E_RECOVERABLE_ERROR, "Argument %s must be int or double", i);
                    break;
                case STRING:
                    if (!passed[i].isString())
                        env.error(trace, ErrorType.E_RECOVERABLE_ERROR, "Argument %s must be string", i);
            }
            i++;
        }
        return passed;
    }

    public static Memory callAny(Memory method, Memory[] args, Environment env, TraceInfo trace)
            throws Throwable {
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
                env.error(trace, Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(method.toString()));
                return Memory.NULL;
            }

            assert one != null;
            assert two != null;
            String methodName = two.toString();
            if (one.isObject())
                return ObjectInvokeHelper.invokeMethod(one, methodName, methodName.toLowerCase(), env, trace, args);
            else {
                String className = one.toString();
                if ("self".equals(className)){
                    ClassEntity e = env.getContextClass();
                    if (e == null)
                        e = env.getLateStaticClass();
                    if (e != null)
                        className = e.getName();
                } else if ("static".equals(className))
                    className = env.getLateStatic();

                return InvokeHelper.callStaticDynamic(
                        env,
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
                        env, trace,
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
            throws Throwable {
        Memory[] passed = function.parameters == null
                ? args
                : makeArguments(env, args, function.parameters, function.getName(), null, trace);

        if (function.isImmutable()){
            function.unsetArguments(passed);
            return function.getResult().toImmutable();
        }

        Memory result;
        if (trace != null)
            env.pushCall(trace, null, args, function.getName(), null, null);

        try {
            result = function.invoke(env, trace, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
        return result;
    }

    public static Memory call(Environment env, TraceInfo trace, String sign, String originName,
                              Memory[] args) throws Throwable {
        FunctionEntity function = env.functionMap.get(sign);
        if (function == null) {
            env.error(trace, Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch(originName));
            return Memory.NULL;
        }
        return call(env, trace, function, args);
    }

    public static Memory callStaticDynamic(Environment env, TraceInfo trace,
                                           String originClassName, String className,
                                           String originMethodName, String methodName,
                                           Memory[] args) throws Throwable {
        return callStatic(
                env, trace,
                className, methodName,
                originClassName, originMethodName,
                args
        );
    }

    public static Memory callStatic(Environment env, TraceInfo trace,
                                    String className, String methodName, String originClassName, String originMethodName,
                                    Memory[] args)
            throws Throwable {
        ClassEntity classEntity = env.classMap.get(className);
        if (classEntity == null){
            // try autoload
            classEntity = env.fetchClass(originClassName, className, true);
        }

        MethodEntity method = classEntity == null ? null : classEntity.findMethod(methodName);
        Memory[] passed = null;

        IObject maybeObject = env.getLateObject();
        if (method == null){
            if (maybeObject.getReflection().isInstanceOf(classEntity))
                return ObjectInvokeHelper.invokeMethod(
                        new ObjectMemory(maybeObject), originMethodName, methodName, env, trace, args
                );

            if (classEntity != null && classEntity.methodMagicCallStatic != null){
                method = classEntity.methodMagicCallStatic;
                passed = new Memory[]{
                        new StringMemory(originMethodName),
                        new ArrayMemory(true, args)
                };
            } else {
                if (classEntity == null) {
                    env.error(trace, Messages.ERR_FATAL_CLASS_NOT_FOUND.fetch(originClassName));
                    return Memory.NULL;
                }
            }
        }

        if (method == null){
            env.error(trace, Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(originClassName + "::" + originMethodName));
            return Memory.NULL;
        }

        assert method != null;
        if (!method.isStatic()) {
            if (maybeObject != null && maybeObject.getReflection().isInstanceOf(classEntity))
                return ObjectInvokeHelper.invokeMethod(maybeObject, method, env, trace, args);

            env.error(trace,
                    ErrorType.E_STRICT,
                    Messages.ERR_FATAL_NON_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(originClassName + "::" + originMethodName)
            );
            //return Memory.NULL;
        }

        checkAccess(env, trace, method);
        if (passed == null)
            passed = makeArguments(env, args, method.parameters, originClassName, originMethodName, trace);

        Memory result;

        if (method.isImmutable()){
            method.unsetArguments(passed);
            return method.getResult().toImmutable();
        }

        try {
            if (trace != null)
                env.pushCall(trace, null, args, originMethodName, method.getClazz().getName(), originClassName);

            result = method.invokeStatic(env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }

        return result;
    }

    public static Memory callStatic(Environment env, TraceInfo trace,
                                    MethodEntity method,
                                    Memory[] args)
            throws Throwable {
        String originClassName = method.getClazz().getName();
        String originMethodName = method.getName();

        Memory[] passed = makeArguments(env, args, method.parameters, originClassName, originMethodName, trace);
        Memory result;

        checkAccess(env, trace, method);
        try {
            if (trace != null)
                env.pushCall(trace, null, args, originMethodName, method.getClazz().getName(), originClassName);

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
