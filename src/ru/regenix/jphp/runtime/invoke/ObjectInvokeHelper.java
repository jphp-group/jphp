package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

final public class ObjectInvokeHelper {

    private ObjectInvokeHelper(){ }

    public static Memory invokeParentMethod(Memory object, String methodName, String methodLowerName,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        Memory[] passed = null;
        boolean doublePop = false;

        IObject iObject = ((ObjectMemory)object).value;
        ClassEntity clazz = iObject.getReflection().getParent();
        MethodEntity method;

        if (methodName == null) {
            method = clazz.methodMagicInvoke;
        } else {
            method = clazz.methods.get(methodLowerName);
            if (method == null && ((method = clazz.methodMagicCall) != null)){
                passed = new Memory[]{new StringMemory(methodName), new ArrayMemory(true, args)};
                doublePop = true;
            }
        }

        String className = clazz.getName();

        if (method == null){
            if (methodName == null)
                methodName = "__invoke";

            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(
                            className + "::" + methodName
                    ),
                    trace
            ));
        }

        if (passed == null) {
            passed = InvokeHelper.makeArguments(
                    env, args, method.parameters, className, methodName, trace
            );
        }

        Memory result;
        InvokeHelper.checkAccess(env, trace, method);
        if (method.isImmutable()){
            method.unsetArguments(passed);
            return method.getResult().toImmutable();
        }
        try {
            if (trace != null) {
                env.pushCall(trace, iObject, args, methodName, className);
                if (doublePop)
                    env.pushCall(trace, iObject, passed, method.getName(), className);
            }
            result = method.invokeDynamic(iObject, env, passed);
        } finally {
            if (trace != null){
                env.popCall();
                if (doublePop)
                    env.popCall();
            }
        }
        return result;
    }

    public static Memory invokeMethod(Memory object, String methodName, String methodLowerName,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        object = object.toValue();
        Memory[] passed = null;
        boolean doublePop = false;

        if (object.type != Memory.Type.OBJECT){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_CALL_OF_NON_OBJECT.fetch(methodName),
                    trace
            ));
        }
        IObject iObject = ((ObjectMemory)object).value;
        ClassEntity clazz = iObject.getReflection();
        MethodEntity method;

        if (methodName == null) {
            method = clazz.methodMagicInvoke;
        } else {
            method = clazz.methods.get(methodLowerName);
            if (method == null && ((method = clazz.methodMagicCall) != null)){
                passed = new Memory[]{new StringMemory(methodName), new ArrayMemory(true, args)};
                doublePop = true;
            }
            /*if (method.isStatic()) { IT's not needed!!!
                env.triggerError(new FatalException(
                        Messages.ERR_FATAL_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(
                                iObject.__class__.getName() + "::" + methodName
                        ),
                        trace
                ));
            }*/
        }

        String className = clazz.getName();

        if (method == null){
            if (methodName == null)
                methodName = "__invoke";

            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(
                            className + "::" + methodName
                    ),
                    trace
            ));
        }

        if (passed == null) {
            passed = InvokeHelper.makeArguments(
                    env, args, method.parameters, className, methodName, trace
            );
        }

        Memory result;
        InvokeHelper.checkAccess(env, trace, method);
        if (method.isImmutable()){
            method.unsetArguments(passed);
            return method.getResult().toImmutable();
        }

        try {
            if (trace != null) {
                env.pushCall(trace, iObject, args, methodName, className);
                if (doublePop)
                    env.pushCall(trace, iObject, passed, method.getName(), className);
            }
            result = method.invokeDynamic(iObject, env, passed);
        } finally {
            if (trace != null){
                env.popCall();
                if (doublePop)
                    env.popCall();
            }
        }
        return result;
    }

    public static Memory invokeMethod(IObject iObject, MethodEntity method,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        ClassEntity clazz = iObject.getReflection();
        if (method == null)
            method = clazz.methodMagicInvoke;

        String className = clazz.getName();

        if (method == null){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(
                            className + "::__invoke"
                    ),
                    trace
            ));
        }

        Memory[] passed = InvokeHelper.makeArguments(
                env, args, method.parameters, className, method.getName(), trace
        );
        Memory result;
        InvokeHelper.checkAccess(env, trace, method);
        if (method.isImmutable()){
            method.unsetArguments(passed);
            return method.getResult().toImmutable();
        }

        if (trace != null)
            env.pushCall(trace, iObject, args, method.getName(), className);

        try {
            result = method.invokeDynamic(iObject, env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
        return result;
    }

    public static Memory emptyProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        IObject iObject = ((ObjectMemory)object).value;
        return iObject.getReflection().emptyProperty(env, trace, iObject, property);
    }

    public static Memory issetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        IObject iObject = ((ObjectMemory)object).value;
        return iObject.getReflection().issetProperty(env, trace, iObject, property);
    }

    public static void unsetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        IObject iObject = ((ObjectMemory)object).value;
        iObject.getReflection().unsetProperty(env, trace, iObject, property);
    }

    public static Memory getProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        IObject iObject = ((ObjectMemory)object).value;
        return iObject.getReflection().getProperty(env, trace, iObject, property);
    }

    private static IObject fetchObject(Memory object, String property, Environment env, TraceInfo trace){
        object = object.toValue();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_SET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        return ((ObjectMemory)object).value;
    }

    public static Memory assignProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().setProperty(env, trace, iObject, property, value, null);
    }

    public static Memory assignPlusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().plusProperty(env, trace, iObject, property, value);
    }

    public static Memory assignMinusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().minusProperty(env, trace, iObject, property, value);
    }

    public static Memory assignMulProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().mulProperty(env, trace, iObject, property, value);
    }

    public static Memory assignDivProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().divProperty(env, trace, iObject, property, value);
    }

    public static Memory assignModProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().modProperty(env, trace, iObject, property, value);
    }

    public static Memory assignConcatProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().concatProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitAndProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().bitAndProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitOrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().bitOrProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitXorProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().bitXorProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitShrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().bitShrProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitShlProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        return iObject.getReflection().bitShlProperty(env, trace, iObject, property, value);
    }
}
