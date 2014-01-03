package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

final public class ObjectInvokeHelper {

    private ObjectInvokeHelper(){ }

    public static Memory invokeParentMethod(Memory object, String methodName, String methodLowerName,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        Memory[] passed = null;
        boolean doublePop = false;

        IObject iObject = ((ObjectMemory)object).value;
        ClassEntity childClazz = iObject.getReflection();
        ClassEntity clazz = childClazz.getParent();
        MethodEntity method;

        if (methodName == null) {
            method = childClazz.methodMagicInvoke != null ? childClazz.methodMagicInvoke : clazz.methodMagicInvoke;
        } else {
            method = clazz.findMethod(methodLowerName);
            if (method == null
                    && ((
                    method = childClazz.methodMagicCall != null
                        ? childClazz.methodMagicCall
                        : clazz.methodMagicCall)
                    != null)){
                passed = new Memory[]{new StringMemory(methodName), new ArrayMemory(true, args)};
                doublePop = true;
            }
        }

        String className = clazz.getName();

        if (method == null){
            if (methodName == null)
                methodName = "__invoke";

            env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(
                            className + "::" + methodName
                    )
            );
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
                env.pushCall(trace, iObject, args, methodName, method.getClazz().getName(), className);
                if (doublePop)
                    env.pushCall(trace, iObject, passed, method.getName(), method.getClazz().getName(), className);
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

        if (object.type != Memory.Type.OBJECT)
            env.error(trace, ErrorType.E_ERROR, Messages.ERR_FATAL_CANNOT_CALL_OF_NON_OBJECT.fetch(methodName));

        IObject iObject = ((ObjectMemory)object).value;
        ClassEntity clazz = iObject.getReflection();
        MethodEntity method;

        if (methodName == null) {
            method = clazz.methodMagicInvoke;
        } else {
            ClassEntity context = env.getLastClassOnStack();
            method = context == null ? null : clazz.isInstanceOf(context) ? context.findMethod(methodLowerName) : null;
            if (method == null || method.getModifier() != Modifier.PRIVATE){
                method = clazz.findMethod(methodLowerName);
            }

            if (method == null && ((method = clazz.methodMagicCall) != null)){
                passed = new Memory[]{new StringMemory(methodName), new ArrayMemory(true, args)};
                doublePop = true;
            }
        }

        String className = clazz.getName();

        if (method == null){
            if (methodName == null)
                methodName = "__invoke";

            env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(className + "::" + methodName)
            );
            return Memory.NULL;
        }

        if (passed == null)
            passed = InvokeHelper.makeArguments(
                    env, args, method.parameters, className, methodName, trace
            );

        Memory result;
        InvokeHelper.checkAccess(env, trace, method);
        if (method.isImmutable()){
            method.unsetArguments(passed);
            return method.getResult().toImmutable();
        }

        try {
            if (trace != null) {
                env.pushCall(trace, iObject, args, methodName, method.getClazz().getName(), className);
                if (doublePop)
                    env.pushCall(trace, iObject, passed, method.getName(), method.getClazz().getName(), className);
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
                                      Environment env, TraceInfo trace, Memory[] args) throws Throwable {
        ClassEntity clazz = iObject.getReflection();
        if (method == null)
            method = clazz.methodMagicInvoke;

        String className = clazz.getName();

        if (method == null){
            env.error(trace, Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(className + "::__invoke"));
            return Memory.NULL;
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
            env.pushCall(trace, iObject, args, method.getName(), method.getClazz().getName(), className);

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
            env.error(trace, Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property));
        }

        IObject iObject = ((ObjectMemory)object).value;
        return iObject.getReflection().emptyProperty(env, trace, iObject, property);
    }

    public static Memory issetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.error(trace, Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property));
        }

        IObject iObject = ((ObjectMemory)object).value;
        return iObject.getReflection().issetProperty(env, trace, iObject, property);
    }

    public static void unsetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.error(trace, Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property));
        }

        IObject iObject = ((ObjectMemory)object).value;
        iObject.getReflection().unsetProperty(env, trace, iObject, property);
    }

    public static Memory getConstant(String className, String lowerClassName, String constant,
                                     Environment env, TraceInfo trace){
        ClassEntity entity = env.fetchClass(className, lowerClassName, true);
        /*if (entity == null)
            entity = env.fetchMagicClass(className, lowerClassName);*/

        if (entity == null) {
            env.error(trace, Messages.ERR_FATAL_CLASS_NOT_FOUND.fetch(className));
            return Memory.NULL;
        }

        ConstantEntity constantEntity = entity.findConstant(constant);
        if (constantEntity == null){
            env.error(trace, Messages.ERR_FATAL_UNDEFINED_CLASS_CONSTANT.fetch(constant));
            return Memory.NULL;
        }

        return constantEntity.getValue();
    }

    public static Memory getProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()){
            env.error(trace,
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property)
            );
            return Memory.NULL;
        }

        IObject iObject = ((ObjectMemory)object).value;
        return iObject.getReflection().getProperty(env, trace, iObject, property);
    }

    public static Memory getStaticProperty(String className, String lowerClassName, String property, Environment env,
                                           TraceInfo trace) throws Throwable {
        ClassEntity entity = env.fetchClass(className, lowerClassName, true);
        if (entity == null) {
            env.error(trace, Messages.ERR_FATAL_CLASS_NOT_FOUND.fetch(className));
            return Memory.NULL;
        }

        return entity.getStaticProperty(env, trace, property);
    }

    private static IObject fetchObject(Memory object, String property, Environment env, TraceInfo trace){
        object = object.toValue();
        if (!object.isObject()){
            env.error(trace, Messages.ERR_FATAL_CANNOT_SET_PROPERTY_OF_NON_OBJECT.fetch(property));
            return null;
        }

        return ((ObjectMemory)object).value;
    }

    public static Memory incAndGetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        return assignPlusProperty(object, Memory.CONST_INT_1, property, env, trace);
    }

    public static Memory GetAndIncProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;

        ReferenceMemory ref = new ReferenceMemory();
        iObject.getReflection().plusProperty(env, trace, iObject, property, Memory.CONST_INT_1, ref);
        return ref.value;
    }

    public static Memory decAndGetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        return assignMinusProperty(object, Memory.CONST_INT_1, property, env, trace);
    }

    public static Memory GetAndDecProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;

        ReferenceMemory ref = new ReferenceMemory();
        iObject.getReflection().minusProperty(env, trace, iObject, property, Memory.CONST_INT_1, ref);
        return ref.value;
    }

    public static Memory assignProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().setProperty(env, trace, iObject, property, value, null);
    }

    public static Memory assignPlusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().plusProperty(env, trace, iObject, property, value, null);
    }

    public static Memory assignMinusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().minusProperty(env, trace, iObject, property, value, null);
    }

    public static Memory assignMulProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().mulProperty(env, trace, iObject, property, value);
    }

    public static Memory assignDivProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().divProperty(env, trace, iObject, property, value);
    }

    public static Memory assignModProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().modProperty(env, trace, iObject, property, value);
    }

    public static Memory assignConcatProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().concatProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitAndProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitAndProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitOrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitOrProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitXorProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitXorProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitShrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitShrProperty(env, trace, iObject, property, value);
    }

    public static Memory assignBitShlProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitShlProperty(env, trace, iObject, property, value);
    }
}
