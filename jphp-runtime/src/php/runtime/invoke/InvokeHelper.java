package php.runtime.invoke;

import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.FatalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.cache.FunctionCallCache;
import php.runtime.invoke.cache.MethodCallCache;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.helper.VariadicMemory;
import php.runtime.reflection.*;

import java.lang.reflect.Field;

final public class InvokeHelper {

    private InvokeHelper() { }

    public static void checkAccess(Environment env, TraceInfo trace, MethodEntity method) {
        int access = method.canAccess(env);
        if (access == 0)
            return;

        ClassEntity contextCls = env.getLastClassOnStack();
        String context = contextCls == null ? "" : contextCls.getName();

        switch (access){
            case 1: throw new FatalException(
                    Messages.ERR_CALL_TO_PROTECTED_METHOD.fetch(
                            method.getClazz().getName() + "::" + method.getName(), context
                    ),
                    trace
            );
            case 2: throw new FatalException(
                    Messages.ERR_CALL_TO_PRIVATE_METHOD.fetch(
                            method.getClazz().getName() + "::" + method.getName(), context
                    ),
                    trace
            );
        }
    }

    public static void checkAccess(Environment env, TraceInfo trace, PropertyEntity property) {
        switch (property.canAccess(env)){
            case 1: throw new FatalException(
                    Messages.ERR_ACCESS_TO_PROTECTED_PROPERTY.fetch(
                            property.getClazz().getName(), property.getName()
                    ),
                    trace
            );
            case 2: throw new FatalException(
                    Messages.ERR_ACCESS_TO_PRIVATE_PROPERTY.fetch(
                            property.getClazz().getName(), property.getName()
                    ),
                    trace
            );
        }
    }

    public static void invalidTypeHinting(Environment env,
                                          TraceInfo trace, ParameterEntity param, int index, Memory passed,
                                          String originClassName, String originMethodName){
        String given;
        if (passed == null){
            given = "none";
        } else if (passed.isObject()) {
            given = "instance of " + passed.toValue(ObjectMemory.class).getReflection().getName();
        } else {
            given = passed.getRealType().toString();
        }

        String method = originMethodName == null ? originClassName : originClassName + "::" + originMethodName;

        if (param.getTypeClass() == null) {
            if (param.getTypeEnum() != null && param.getType() == HintType.ANY) {
                Field[] fields = param.getTypeEnum().getFields();
                String[] names = new String[fields.length];

                for (int i = 0; i < names.length; i++) {
                    names[i] = fields[i].getName();
                }

                env.error(
                        param.getTrace(),
                        ErrorType.E_RECOVERABLE_ERROR,
                        "Argument %s passed to %s() must be a string belonging to the range [" + StringUtils.join(names, ", ") + "] as string, called in %s on line %d, position %d and defined",
                        index,
                        method,

                        trace.getFileName(),
                        trace.getStartLine() + 1,
                        trace.getStartPosition() + 1
                );
            } else if (param.getTypeHintingChecker() != null) {
                env.error(
                        param.getTrace(),
                        ErrorType.E_RECOVERABLE_ERROR,
                        "Argument %s passed to %s() must be %s, called in %s on line %d, position %d and defined",
                        index,
                        method,
                        param.getTypeHintingChecker().getNeeded(env, passed),

                        trace.getFileName(),
                        trace.getStartLine() + 1,
                        trace.getStartPosition() + 1
                );
            } else {
                env.error(
                        param.getTrace(),
                        ErrorType.E_RECOVERABLE_ERROR,
                        "Argument %s passed to %s() must be of the type %s, %s given, called in %s on line %d, position %d and defined",
                        index,
                        method,
                        param.getType().toString(), given,

                        trace.getFileName(),
                        trace.getStartLine() + 1,
                        trace.getStartPosition() + 1
                );
            }
        } else {
            ClassEntity need = env.fetchClass(param.getTypeClass(), false);
            String what = "";
            if (need == null || need.isClass()){
                what = "be an instance of";
            } else if (need.isInterface()){
                what = "implement interface";
            }

            what = what + " " + param.getTypeClass();
            env.error(
                    param.getTrace(),
                    ErrorType.E_RECOVERABLE_ERROR,
                    "Argument %s passed to %s() must %s, %s given, called in %s on line %d, position %d and defined",
                    index,
                    method,

                    what, given,
                    trace.getFileName(),
                    trace.getStartLine() + 1,
                    trace.getStartPosition() + 1
            );
        }
    }

    private static Memory _makeArgumentValue(ParameterEntity param, Memory arg, Environment env, TraceInfo trace) {
        if (param.isReference()) {
            if (!arg.isReference() && !arg.isObject()) {
                env.error(trace, ErrorType.E_ERROR, "Only variables can be passed by reference");
                arg = new ReferenceMemory(arg);
            }
        } else {
            arg = param.isMutable() ? arg.toImmutable() : arg.toValue();
        }

        return arg;
    }

    private static void _makeVariadic(ForeachIterator iterator, ArrayMemory variadicArray, ParameterEntity param,
                                      Environment env, TraceInfo trace, int index, String originClassName,
                                      String originMethodName) {
        while (iterator.next()) {
            Memory arg = iterator.getValue();

            if (!param.checkTypeHinting(env, arg)) {
                invalidTypeHinting(env, trace, param, index + 1, arg, originClassName, originMethodName);
            }

            variadicArray.add(_makeArgumentValue(param, iterator.getValue(), env, trace));
        }
    }

    public static Memory[] makeArguments(Environment env, Memory[] args,
                                       ParameterEntity[] parameters,
                                       String originClassName, String originMethodName,
                                       TraceInfo trace){
        if (parameters == null)
            return args;

        Memory[] passed = args;
        if ((args == null && parameters.length > 0) || (args != null && args.length < parameters.length)){
            passed = new Memory[parameters.length];
            if (args != null && args.length > 0){
                System.arraycopy(args, 0, passed, 0, args.length);
            }
        }

        int i = 0;

        if (passed != null) {
            for (ParameterEntity param : parameters) {
                if (!param.isUsed() && param.getType() == HintType.ANY) {
                    i++;
                    continue;
                }

                Memory arg = passed[i];

                if (param.isVariadic()) {
                    ArrayMemory variadicArgs = new ArrayMemory();

                    int _i = i;
                    boolean variadicMemoryExists = false;

                    while (arg != null) {
                        if (arg instanceof VariadicMemory) {
                            variadicMemoryExists = true;

                            ForeachIterator iterator = arg.getNewIterator(env, param.isReference(), false);

                            if (iterator == null) {
                                env.warning(trace, "Only arrays and Traversables can be unpacked");
                            }

                            _makeVariadic(iterator, variadicArgs, param, env, trace, _i, originClassName, originMethodName);
                        } else {
                            if (variadicMemoryExists) {
                                env.error(trace, "Cannot use positional argument after argument unpacking");
                            }

                            if (!param.checkTypeHinting(env, arg)) {
                                invalidTypeHinting(env, trace, param, _i + 1, arg, originClassName, originMethodName);
                            }

                            variadicArgs.add(_makeArgumentValue(param, arg, env, trace));
                        }

                        i++;
                        if (i < passed.length) {
                            arg = passed[i];
                        } else {
                            break;
                        }
                    }

                    passed[_i] = variadicArgs;

                    break;
                }

                if (arg == null) {
                    Memory def = param.getDefaultValue();

                    if (def != null) {
                        if (!param.isReference()) {
                            passed[i] = param.isMutable() ? def.toImmutable(env, trace) : def;
                        } else {
                            passed[i] = new ReferenceMemory(param.isMutable() ? def.toImmutable(env, trace) : def);
                        }
                    } else {
                        if (param.getTypeClass() != null) {
                            invalidTypeHinting(env, trace, param, i + 1, null, originClassName, originMethodName);
                        }

                        env.error(trace, ErrorType.E_ERROR,
                                Messages.ERR_MISSING_ARGUMENT, (i + 1) + " ($" + param.getName() + ")",
                                originMethodName == null ? originClassName : originClassName + "::" + originMethodName
                        );
                        passed[i] = param.isReference() ? new ReferenceMemory() : Memory.NULL;
                    }
                } else {
                    if (param.isReference()) {
                        if (!arg.isReference() && !arg.isObject()) {
                            env.error(trace, ErrorType.E_ERROR, "Only variables can be passed by reference");
                            passed[i] = new ReferenceMemory(arg);
                        }
                    } else {
                        passed[i] = param.isMutable() ? arg.toImmutable() : arg.toValue();
                    }
                }

                if (!param.checkTypeHinting(env, passed[i])) {
                    invalidTypeHinting(env, trace, param, i + 1, passed[i], originClassName, originMethodName);
                }
                i++;
            }
        }
        return passed;
    }

    public static void checkTypeHinting(Environment env, TraceInfo trace, MethodEntity methodEntity, Memory... args) {
        if (args == null) {
            return;
        }

        ParameterEntity[] parameters = methodEntity.getParameters(args.length);

        int i = 0;

        for (Memory arg : args) {
            if (i > parameters.length - 1) {
                break;
            }

            if (!parameters[i].checkTypeHinting(env, arg)) {
                invalidTypeHinting(
                        env, trace, parameters[i], i + 1, arg, methodEntity.getClazzName(), methodEntity.getName()
                );
            }

            i++;
        }
    }

    /**
     * Method is invoked via bytecode
     * @throws Throwable
     */
    public static Memory callAny(Memory method, Memory[] args, Environment env, TraceInfo trace)
            throws Throwable {
        method = method.toValue();
        if (method.isObject()) {
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

    public static Memory call(Environment env, TraceInfo trace, FunctionEntity function, Memory[] args)
            throws Throwable {
        Memory[] passed = makeArguments(env, args, function.getParameters(), function.getName(), null, trace);

        Memory result = function.getImmutableResult();
        if (result != null) return result;

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
                return callStatic(env, trace, entity, args, false);
            }
        }

        ClassEntity classEntity = env.fetchClass(originClassName, className, true);

        MethodEntity method = classEntity == null ? null : classEntity.findMethod(methodName);
        Memory[] passed = null;

        if (method == null){
            IObject maybeObject = env.getLateObject();
            if (maybeObject != null && maybeObject.getReflection().isInstanceOf(classEntity))
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
                    env.error(trace, Messages.ERR_CLASS_NOT_FOUND.fetch(originClassName));
                    return Memory.NULL;
                }
            }
        }

        if (method == null){
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

        if (callCache != null) {
            callCache.put(env, cacheIndex, method);
        }

        checkAccess(env, trace, method);

        if (passed == null)
            passed = makeArguments(env, args, method.getParameters(), originClassName, originMethodName, trace);

        Memory result = method.getImmutableResult();
        if (result != null) return result;

        try {
            if (trace != null)
                env.pushCall(trace, null, args, originMethodName, method.getClazz().getName(), originClassName);

            return method.invokeStatic(env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
    }


    public static Memory callStatic(Environment env, TraceInfo trace,
                                    MethodEntity method,
                                    Memory[] args)
            throws Throwable {
        return callStatic(env, trace, method, args, true);
    }

    public static Memory callStatic(Environment env, TraceInfo trace,
                                    MethodEntity method,
                                    Memory[] args, boolean checkAccess)
            throws Throwable {
        if (checkAccess)
            checkAccess(env, trace, method);

        Memory result = method.getImmutableResult();
        if (result != null)
            return result;

        String originClassName = method.getClazz().getName();
        String originMethodName = method.getName();

        Memory[] passed = makeArguments(env, args, method.getParameters(), originClassName, originMethodName, trace);

        try {
            if (trace != null && method.isUsesStackTrace())
                env.pushCall(trace, null, passed, originMethodName, originClassName, originClassName);

            return method.invokeStatic(env, passed);
        } finally {
            if (trace != null && method.isUsesStackTrace())
                env.popCall();
        }
    }

    public static void checkReturnReference(Memory memory, Environment env, TraceInfo trace){
        if (memory.isImmutable()){
            env.warning(trace, Messages.ERR_RETURN_NOT_REFERENCE.fetch());
        }
    }

    public static void checkYieldReference(Memory memory, Environment env, TraceInfo trace){
        if (memory.isImmutable()){
            env.error(trace, ErrorType.E_NOTICE, Messages.ERR_YIELD_NOT_REFERENCE.fetch());
        }
    }
}
