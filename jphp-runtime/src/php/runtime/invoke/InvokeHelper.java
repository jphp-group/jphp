package php.runtime.invoke;

import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.common.Callback;
import php.runtime.common.Function;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.FatalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.cache.FunctionCallCache;
import php.runtime.invoke.cache.MethodCallCache;
import php.runtime.lang.IObject;
import php.runtime.lang.exception.BaseTypeError;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.*;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.support.TypeChecker;

final public class InvokeHelper {

    private InvokeHelper() {
    }

    public static void checkAccess(Environment env, TraceInfo trace, MethodEntity method) {
        int access = method.canAccess(env);
        if (access == 0)
            return;

        ClassEntity contextCls = env.getLastClassOnStack();
        String context = contextCls == null ? "" : contextCls.getName();

        switch (access) {
            case 1:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_CALL_TO_PROTECTED_METHOD,
                        method.getClazz().getName() + "::" + method.getName(), context
                );
            case 2:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_CALL_TO_PRIVATE_METHOD,
                        method.getClazz().getName() + "::" + method.getName(), context
                );
        }
    }

    public static void checkAccess(Environment env, TraceInfo trace, PropertyEntity property) {
        switch (property.canAccess(env)) {
            case 1:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_ACCESS_TO_PROTECTED_PROPERTY,
                        property.getClazz().getName(), property.getName()
                );
            case 2:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_ACCESS_TO_PRIVATE_PROPERTY,
                        property.getClazz().getName(), property.getName()
                );
        }
    }

    public static void checkAccess(Environment env, TraceInfo trace, ConstantEntity constant, boolean lateStaticCall) {
        switch (constant.canAccess(env, null, lateStaticCall)) {
            case 1:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_ACCESS_TO_PROTECTED_CONSTANT,
                        constant.getClazz().getName(), constant.getName()
                );
            case 2:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_ACCESS_TO_PRIVATE_CONSTANT,
                        constant.getClazz().getName(), constant.getName()
                );
        }
    }

    /*public static Memory[] makeArguments(Environment env, Memory[] args,
                                         ParameterEntity[] parameters,
                                         String originClassName, String originMethodName,
                                         TraceInfo trace) {
        return InvokeArgumentHelper.makeArguments(env, args, parameters, originClassName, originMethodName, trace);
    }*/

    /**
     * Method is invoked via bytecode
     *
     * @throws Throwable
     */
    public static Memory callAny(Memory method, Memory[] args, Environment env, TraceInfo trace)
            throws Throwable {
        method = method.toValue();
        if (method.isObject()) {
            return ObjectInvokeHelper.invokeMethod(method, null, null, env, trace, args);
        } else if (method.isArray()) {
            Memory one = null, two = null;
            for (Memory el : (ArrayMemory) method) {
                if (one == null)
                    one = el;
                else if (two == null)
                    two = el;
                else
                    break;
            }

            if (one == null || two == null) {
                env.error(trace, Messages.ERR_CALL_TO_UNDEFINED_FUNCTION.fetch(method.toString()));
                return Memory.NULL;
            }

            String methodName = two.toString();
            if (one.isObject())
                return ObjectInvokeHelper.invokeMethod(one, methodName, methodName.toLowerCase(), env, trace, args);
            else {
                String className = one.toString();
                ClassEntity magic = env.fetchMagicClass(className);
                if (magic != null)
                    className = magic.getName();

                return InvokeHelper.callStaticDynamic(
                        env,
                        trace,
                        className, className.toLowerCase(),
                        methodName, methodName.toLowerCase(),
                        args,
                        null, 0
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
                        args,
                        null, 0
                );
            } else {
                return InvokeHelper.call(env, trace, methodName.toLowerCase(), methodName, args, null, 0);
            }
        }
    }

    public static Memory checkReturnType(Environment env, TraceInfo trace, Memory result, final MethodEntity method) {
        return checkReturnType(
                env, trace, result,
                new Function<String>() {
                    @Override
                    public String call() {
                        ClassEntity clazz = method.getClazz();

                        switch (clazz.getType()) {
                            case CLOSURE:
                                return "{closure}";
                            default:
                                return clazz.getName() + "::" + method.getName();
                        }
                    }
                },
                method.getReturnTypeChecker(), method.isReturnTypeNullable()
        );
    }

    public static Memory checkReturnType(Environment env, TraceInfo trace, Memory result, final FunctionEntity function) {
        return checkReturnType(
                env, trace, result,
                new Function<String>() {
                    @Override
                    public String call() {
                        return function.getName();
                    }
                },
                function.getReturnTypeChecker(), function.isReturnTypeNullable()
        );
    }

    public static Memory checkReturnType(Environment env, TraceInfo trace, Memory result, Function<String> callName,
                                         TypeChecker typeChecker, boolean nullable) {
        if (typeChecker == null) {
            return result;
        }

        if (!typeChecker.check(env, result, nullable, null)) {
            ModuleEntity module = env.getModuleManager().findModule(trace);

            Memory newReturn = typeChecker.apply(
                    env, result, nullable, module != null && module.isStrictTypes()
            );

            if (newReturn == null) {
                String given = result.isObject()
                        ? result.toValue(ObjectMemory.class).getReflection().getName()
                        : result.getRealType().toString();

                env.exception(
                        trace,
                        BaseTypeError.class,
                        Messages.ERR_RETURN_TYPE_INVALID.fetch(callName.call(), typeChecker.getHumanString(), given)
                );

                return null;
            } else {
                return newReturn;
            }
        }


        return result;
    }

    public static Memory call(Environment env, TraceInfo trace, FunctionEntity function, Memory[] args)
            throws Throwable {
        Memory result = function.getImmutableResultTyped(env, trace);

        Memory[] passed = null;

        if (result != null && !function.hasParameters() && args == null) {
            return result;
        }

        passed = InvokeArgumentHelper.makeArguments(
                env, args, function.getParameters(), function.getName(), null, null, trace
        );

        if (result != null) {
            return result;
        }

        if (trace != null && function.isUsesStackTrace())
            env.pushCall(trace, null, args, function.getName(), null, null);

        try {
            result = function.invoke(env, trace, passed);
        } finally {
            if (trace != null && function.isUsesStackTrace())
                env.popCall();
        }
        return result;
    }

    public static Memory call(Environment env, TraceInfo trace, String sign, String originName,
                              Memory[] args, FunctionCallCache callCache, int cacheIndex) throws Throwable {
        FunctionEntity function = null;

        if (callCache != null)
            function = callCache.get(env, cacheIndex);

        if (function == null) {
            function = env.fetchFunction(originName, sign);
            if (function != null && callCache != null) {
                callCache.put(env, cacheIndex, function);
            }
        }

        if (function == null) {
            if (!sign.isEmpty() && sign.charAt(0) != Information.NAMESPACE_SEP_CHAR) { // for global style invoke
                int p = sign.lastIndexOf(Information.NAMESPACE_SEP_CHAR);
                if (p > -1)
                    function = env.fetchFunction(originName.substring(p + 1), sign.substring(p + 1));
            }

            if (function == null) {
                env.error(trace, Messages.ERR_CALL_TO_UNDEFINED_FUNCTION.fetch(originName));
                return Memory.NULL;
            }

            if (callCache != null) {
                callCache.put(env, cacheIndex, function);
            }
        }

        return call(env, trace, function, args);
    }

    public static Memory callStaticDynamic(Environment env, TraceInfo trace,
                                           String originClassName, String className,
                                           String originMethodName, String methodName,
                                           Memory[] args, MethodCallCache callCache, int cacheIndex) throws Throwable {
        return callStatic(
                env, trace,
                className, methodName,
                originClassName, originMethodName,
                args,
                callCache, cacheIndex
        );
    }

    public static Memory callStatic(Environment env, TraceInfo trace,
                                    String className, String methodName, String originClassName, String originMethodName,
                                    Memory[] args, MethodCallCache callCache, int cacheIndex)
            throws Throwable {
        if (callCache != null) {
            MethodEntity entity = callCache.get(env, cacheIndex);
            if (entity != null) {
                return callStatic(env, trace, entity, originClassName, args, false);
            }
        }

        ClassEntity classEntity = env.fetchClass(originClassName, className, true);

        MethodEntity method = classEntity == null ? null : classEntity.findMethod(methodName);
        Memory[] passed = null;
        boolean isMagic = false;

        if (method == null) {
            IObject maybeObject = env.getLateObject();
            if (maybeObject != null && maybeObject.getReflection().isInstanceOf(classEntity))
                return ObjectInvokeHelper.invokeMethod(
                        new ObjectMemory(maybeObject), originMethodName, methodName, env, trace, args
                );

            if (classEntity != null && classEntity.methodMagicCallStatic != null) {
                method = classEntity.methodMagicCallStatic;
                isMagic = true;
                passed = new Memory[]{
                        new StringMemory(originMethodName),
                        ArrayMemory.of(args)
                };
            } else {
                if (classEntity == null) {
                    env.error(trace, Messages.ERR_CLASS_NOT_FOUND.fetch(originClassName));
                    return Memory.NULL;
                }
            }
        }

        if (method == null) {
            env.error(trace, Messages.ERR_CALL_TO_UNDEFINED_METHOD.fetch(originClassName + "::" + originMethodName));
            return Memory.NULL;
        }

        if (!method.isStatic()) {
            IObject maybeObject = env.getLateObject();
            if (maybeObject != null
                    && maybeObject.getReflection().isInstanceOf(classEntity))
                return ObjectInvokeHelper.invokeMethod(maybeObject, method, env, trace, args, true);

            env.error(trace,
                    ErrorType.E_STRICT,
                    Messages.ERR_NON_STATIC_METHOD_CALLED_DYNAMICALLY,
                    originClassName, originMethodName
            );
        }

        if (callCache != null && !isMagic) {
            callCache.put(env, cacheIndex, method);
        }

        checkAccess(env, trace, method);

        if (passed == null) {
            passed = InvokeArgumentHelper.makeArguments(
                    env, args, method.getParameters(), originClassName, originMethodName, originClassName, trace
            );
        }

        Memory result = method.getImmutableResultTyped(env, trace);

        if (result != null) {
            return result;
        }

        try {
            if (trace != null)
                env.pushCall(trace, null, args, originMethodName, method.getClazz().getName(), originClassName);

            return method.invokeStatic(env, trace, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
    }


    public static Memory callStatic(Environment env, TraceInfo trace,
                                    MethodEntity method,
                                    Memory[] args)
            throws Throwable {
        return callStatic(env, trace, method, null, args, true);
    }

    public static Memory callStatic(Environment env, TraceInfo trace,
                                    MethodEntity method, @Nullable String staticClass,
                                    Memory[] args, boolean checkAccess)
            throws Throwable {
        if (checkAccess)
            checkAccess(env, trace, method);

        Memory result = method.getImmutableResultTyped(env, trace);

        if (result != null) {
            return result;
        }

        String originClassName = method.getClazz().getName();
        String originMethodName = method.getName();

        String staticClazz = staticClass == null ? originClassName : staticClass;

        Memory[] passed = InvokeArgumentHelper.makeArguments(
                env, args, method.getParameters(), originClassName, originMethodName, staticClass, trace
        );

        try {
            if (trace != null && method.isUsesStackTrace()) {
                env.pushCall(trace, null, passed, originMethodName, originClassName, staticClazz);
            }

            return method.invokeStatic(env, passed);
        } finally {
            if (trace != null && method.isUsesStackTrace())
                env.popCall();
        }
    }

    public static void checkReturnReference(Memory memory, Environment env, TraceInfo trace) {
        if (memory.isImmutable()) {
            env.warning(trace, Messages.ERR_RETURN_NOT_REFERENCE.fetch());
        }
    }

    public static void checkYieldReference(Memory memory, Environment env, TraceInfo trace) {
        if (memory.isImmutable()) {
            env.error(trace, ErrorType.E_NOTICE, Messages.ERR_YIELD_NOT_REFERENCE.fetch());
        }
    }
}
