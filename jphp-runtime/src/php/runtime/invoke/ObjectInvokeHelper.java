package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.common.Modifier;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.cache.ConstantCallCache;
import php.runtime.invoke.cache.PropertyCallCache;
import php.runtime.lang.Closure;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;

final public class ObjectInvokeHelper {

    private ObjectInvokeHelper() {
    }

    public static Memory invokeParentMethod(Memory object, String methodName, String methodLowerName,
                                            Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        Memory[] passed = null;
        boolean doublePop = false;

        if (object.isNull()) {
            ClassEntity parent = env.__getParentClass(trace);
            return InvokeHelper.callStatic(
                    env, trace, parent.getLowerName(), methodLowerName, parent.getName(), methodName, args, null, 0
            );
        }

        IObject iObject = ((ObjectMemory) object).value;
        ClassEntity childClazz = iObject.getReflection();
        ClassEntity clazz = env.getLastClassOnStack().getParent();
        MethodEntity method;

        if (clazz == null) {
            env.error(trace, "Cannot access parent:: when current class scope has no parent");
            return Memory.NULL;
        }

        if (methodName == null) {
            method = childClazz.methodMagicInvoke != null ? childClazz.methodMagicInvoke : clazz.methodMagicInvoke;
        } else {
            method = clazz.findMethod(methodLowerName);
            if (method == null
                    && ((
                    method = childClazz.methodMagicCall != null
                            ? childClazz.methodMagicCall
                            : clazz.methodMagicCall)
                    != null)) {
                passed = new Memory[]{new StringMemory(methodName), ArrayMemory.of(args)};
                doublePop = true;
            }
        }

        String className = clazz.getName();

        if (method == null) {
            if (methodName == null)
                methodName = "__invoke";

            env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_CALL_TO_UNDEFINED_METHOD.fetch(
                            className + "::" + methodName
                    )
            );

            return Memory.NULL;
        }

        InvokeHelper.checkAccess(env, trace, method);

        if (passed == null) {
            passed = InvokeArgumentHelper.makeArguments(
                    env, args, method.getParameters(), className, methodName, className, trace
            );
        }

        Memory result = method.getImmutableResultTyped(env, trace);

        if (result != null) {
            return result;
        }

        try {
            if (trace != null) {
                env.pushCall(trace, iObject, args, methodName, method.getClazz().getName(), className);
                if (doublePop)
                    env.pushCall(trace, iObject, passed, method.getName(), method.getClazz().getName(), className);
            }

            result = method.invokeDynamic(iObject, env, trace, passed);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CriticalException("Unable to call parent:: method " + className + "::" + methodName + "(), error = " + e.getMessage());
        } finally {
            if (trace != null) {
                env.popCall();
                if (doublePop)
                    env.popCall();
            }
        }
        return result;
    }

    public static Memory invokeMethod(Memory object, String methodName,
                                      Environment env, TraceInfo trace, Memory... args)
            throws Throwable {
        return invokeMethod(object, methodName, methodName.toLowerCase(), env, trace, args);
    }

    public static Memory invokeMethod(Memory object, String methodName, Environment env, Memory... args)
            throws Throwable {
        return invokeMethod(object, methodName, methodName.toLowerCase(), env, env.trace(), args);
    }

    public static Memory invokeMethod(Memory object, String methodName, String methodLowerName,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        object = object.toValue();
        Memory[] passed = null;
        boolean doublePop = false;

        if (object.type != Memory.Type.OBJECT) {
            env.error(trace, ErrorType.E_RECOVERABLE_ERROR, Messages.ERR_CANNOT_CALL_OF_NON_OBJECT.fetch(methodName));
            return Memory.NULL;
        }

        IObject iObject = ((ObjectMemory) object).value;
        ClassEntity clazz = iObject.getReflection();
        MethodEntity method;

        if (methodName == null) {
            method = clazz.methodMagicInvoke;
        } else {
            method = clazz.findMethod(methodLowerName);

            if (method != null && method.isContextDepends()) {
                ClassEntity context = env.getLastClassOnStack();

                if (context != null) {
                    MethodEntity contextMethod = context.findMethod(methodLowerName);

                    if (contextMethod != null) {
                        method = contextMethod;
                    }
                }
            }

            if (method == null && ((method = clazz.methodMagicCall) != null)) {
                clazz.methodMagicCall.setModifier(Modifier.PUBLIC);
                passed = new Memory[]{new StringMemory(methodName), ArrayMemory.of(args)};
                doublePop = true;
            }
        }

        String className = clazz.getName();

        if (method == null) {
            if (methodName == null)
                methodName = "__invoke";

            env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_CALL_TO_UNDEFINED_METHOD.fetch(className + "::" + methodName)
            );
            return Memory.NULL;
        }

        InvokeHelper.checkAccess(env, trace, method);

        Memory result = method.getImmutableResultTyped(env, trace);

        if (passed == null) {
            ParameterEntity[] parameters = method.getParameters(args == null ? 0 : args.length);

            if (result != null && args == null && (parameters == null || parameters.length == 0)) {
                return result;
            }

            passed = InvokeArgumentHelper.makeArguments(
                    env, args, parameters, className, methodName, className, trace
            );
        }

        if (result != null) {
            return result;
        }

        try {
            if (trace != null) {
                String staticClass = className;

                if (iObject instanceof Closure) {
                    staticClass = ((Closure) iObject).getScope();
                }

                String stackClass = clazz.isHiddenInCallStack() ? staticClass : method.getClazz().getName();

                env.pushCall(trace, iObject, args, methodName, stackClass, staticClass);

                if (doublePop) {
                    env.pushCall(trace, iObject, passed, method.getName(), stackClass, staticClass);
                }
            }

            return method.invokeDynamic(iObject, env, trace, passed);
        } catch (NoClassDefFoundError e) {
            throw new CriticalException("Unable to call method " + className + "::" + methodName + "(), " + e.getMessage());
        } finally {
            if (trace != null) {
                env.popCall();

                if (doublePop) {
                    env.popCall();
                }
            }
        }
    }

    public static Memory invokeMethod(IObject iObject, MethodEntity method,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        return invokeMethod(iObject, method, env, trace, args, true);
    }

    public static Memory invokeMethod(IObject iObject, MethodEntity method,
                                      Environment env, TraceInfo trace, Memory[] args, boolean checkAccess)
            throws Throwable {
        ClassEntity clazz = iObject.getReflection();
        if (method == null)
            method = clazz.methodMagicInvoke;

        String className = clazz.getName();

        if (method == null) {
            env.error(trace, Messages.ERR_CALL_TO_UNDEFINED_METHOD.fetch(className + "::__invoke"));
            return Memory.NULL;
        }

        if (checkAccess)
            InvokeHelper.checkAccess(env, trace, method);

        Memory[] passed = InvokeArgumentHelper.makeArguments(
                env, args, method.getParameters(args == null ? 0 : args.length), className, method.getName(), className, trace
        );

        Memory result = method.getImmutableResultTyped(env, trace);

        if (result != null) {
            return result;
        }

        if (trace != null) {
            String staticClass = className;

            if (iObject instanceof Closure) {
                staticClass = ((Closure) iObject).getScope();
            }

            String stackClass = clazz.isHiddenInCallStack() ? staticClass : method.getClazz().getName();

            env.pushCall(trace, iObject, args, method.getName(), stackClass, staticClass);
        }

        try {
            result = method.invokeDynamic(iObject, env, trace, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
        return result;
    }

    public static Memory emptyProperty(Memory object, String property, Environment env, TraceInfo trace,
                                       PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()) {
            return Memory.NULL;
            //env.error(trace, Messages.ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property));
        }

        IObject iObject = ((ObjectMemory) object).value;
        return iObject.getReflection().emptyProperty(env, trace, iObject, property);
    }

    public static Memory issetProperty(Memory object, String property, Environment env, TraceInfo trace,
                                       PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        object = object.toValue();

        if (!object.isObject()) {
            return Memory.NULL;
            //env.error(trace, Messages.ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property));
        }

        IObject iObject = ((ObjectMemory) object).value;
        return iObject.getReflection().issetProperty(env, trace, iObject, property, callCache, cacheIndex);
    }

    public static void unsetProperty(Memory object, String property, Environment env, TraceInfo trace,
                                     PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()) {
            env.error(trace, Messages.ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property));
        }

        IObject iObject = ((ObjectMemory) object).value;
        iObject.getReflection().unsetProperty(env, trace, iObject, property, callCache, cacheIndex);
    }

    public static Memory getConstant(String className, String lowerClassName, String constant,
                                     Environment env, TraceInfo trace, ConstantCallCache callCache, int cacheIndex,
                                     boolean lateStaticCall) {
        ConstantEntity constantEntity = null;

        if (callCache != null) {
            constantEntity = callCache.get(env, cacheIndex);
        }

        boolean alreadyCached = constantEntity != null;

        if (!alreadyCached) {
            ClassEntity entity = env.fetchClass(className, lowerClassName, true);

            if (entity == null) {
                env.error(trace, Messages.ERR_CLASS_NOT_FOUND.fetch(className));
                return Memory.NULL;
            }

            constantEntity = entity.findConstant(constant);
            if (constantEntity == null) {
                env.error(trace, Messages.ERR_UNDEFINED_CLASS_CONSTANT.fetch(constant));
                return Memory.NULL;
            }

            if (callCache != null) {
                callCache.put(env, cacheIndex, constantEntity);
            }
        }

        Memory value = constantEntity.getValue(env);

        if (!alreadyCached) {
            InvokeHelper.checkAccess(env, trace, constantEntity, lateStaticCall);
        }

        if (value == null) {
            return Memory.NULL;
        }

        return value;
    }

    public static Memory getProperty(Memory object, String property, Environment env, TraceInfo trace,
                                     PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        object = object.toValue();

        if (!object.isObject()) {
            env.error(trace,
                    Messages.ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property)
            );
            return Memory.NULL;
        }

        IObject iObject = ((ObjectMemory) object).value;

        return iObject.getReflection().getProperty(env, trace, iObject, property, callCache, cacheIndex);
    }

    public static Memory getRefProperty(Memory object, String property, Environment env, TraceInfo trace,
                                        PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        object = object.toValue();
        if (!object.isObject()) {
            env.error(trace,
                    Messages.ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property)
            );
            return Memory.NULL;
        }

        IObject iObject = ((ObjectMemory) object).value;
        return iObject.getReflection().getRefProperty(env, trace, iObject, property, callCache, cacheIndex);
    }

    public static Memory getStaticProperty(String className, String lowerClassName, String property, Environment env,
                                           TraceInfo trace, PropertyCallCache callCache, int cacheIndex, boolean lateStaticCall) throws Throwable {
        ClassEntity entity = env.fetchClass(className, lowerClassName, true);
        if (entity == null) {
            env.error(trace, Messages.ERR_CLASS_NOT_FOUND.fetch(className));
            return Memory.NULL;
        }

        return entity.getStaticProperty(
                env, trace, property, true, true, entity, callCache, cacheIndex, lateStaticCall
        );
    }

    public static Memory issetStaticProperty(String className, String lowerClassName, String property, Environment env,
                                             TraceInfo trace, PropertyCallCache callCache, int cacheIndex, boolean lateStaticCall) throws Throwable {
        ClassEntity entity = env.fetchClass(className, lowerClassName, true);
        if (entity == null) {
            env.error(trace, Messages.ERR_CLASS_NOT_FOUND.fetch(className));
            return Memory.NULL;
        }

        return entity.getStaticProperty(env, trace, property, false, true, entity, callCache, cacheIndex, lateStaticCall);
    }

    public static Memory unsetStaticProperty(String className, String lowerClassName, String property, Environment env,
                                             TraceInfo trace, PropertyCallCache callCache, int cacheIndex, boolean lateStaticCall) throws Throwable {
        Memory get = getStaticProperty(className, lowerClassName, property, env, trace, callCache, cacheIndex, lateStaticCall);
        get.manualUnset(env);
        return Memory.NULL;
    }

    private static IObject fetchObject(Memory object, String property, Environment env, TraceInfo trace) {
        object = object.toValue();
        if (!object.isObject()) {
            env.error(trace, Messages.ERR_CANNOT_SET_PROPERTY_OF_NON_OBJECT.fetch(property));
            return null;
        }

        return ((ObjectMemory) object).value;
    }

    public static Memory incAndGetProperty(Memory object, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return assignPlusProperty(object, Memory.CONST_INT_1, property, env, trace, callCache, cacheIndex);
    }

    public static Memory GetAndIncProperty(Memory object, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;

        ReferenceMemory ref = new ReferenceMemory();
        iObject.getReflection().plusProperty(env, trace, iObject, property, Memory.CONST_INT_1, ref);
        return ref.getValue();
    }

    public static Memory decAndGetProperty(Memory object, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return assignMinusProperty(object, Memory.CONST_INT_1, property, env, trace, callCache, cacheIndex);
    }

    public static Memory GetAndDecProperty(Memory object, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;

        ReferenceMemory ref = new ReferenceMemory();
        iObject.getReflection().minusProperty(env, trace, iObject, property, Memory.CONST_INT_1, ref);
        return ref.getValue();
    }

    public static Memory assignProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                        PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().setProperty(env, trace, iObject, property, value, null, callCache, cacheIndex);
    }

    public static Memory assignPropertyRight(Memory value, String property, Environment env, TraceInfo trace, Memory object,
                                             PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return assignProperty(object, value, property, env, trace, callCache, cacheIndex);
    }

    public static Memory assignPlusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                            PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().plusProperty(env, trace, iObject, property, value, null);
    }

    public static Memory assignMinusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                             PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().minusProperty(env, trace, iObject, property, value, null);
    }

    public static Memory assignMulProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().mulProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignDivProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().divProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignModProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                           PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().modProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignConcatProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().concatProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignBitAndProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitAndProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignBitOrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                             PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitOrProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignBitXorProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitXorProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignBitShrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitShrProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }

    public static Memory assignBitShlProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace,
                                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        IObject iObject = fetchObject(object, property, env, trace);
        if (iObject == null) return Memory.NULL;
        return iObject.getReflection().bitShlProperty(env, trace, iObject, property, value, callCache, cacheIndex);
    }


    /**
     * 0 - success
     * 1 - invalid protected
     * 2 - invalid private
     * @param env
     * @return
     */
    public static int canAccess(Environment env, Modifier modifier, ClassEntity classEntity, ClassEntity context, boolean lateStaticCall) {
        switch (modifier){
            case PUBLIC: return 0;
            case PRIVATE:
                ClassEntity cl = context == null
                        ? (lateStaticCall ? env.getLateStaticClass() : env.getLastClassOnStack())
                        : context;

                return cl != null && cl.getId() == classEntity.getId() ? 0 : 2;

            case PROTECTED:
                ClassEntity clazz = context == null
                        ? (lateStaticCall ? env.getLateStaticClass() : env.getLastClassOnStack())
                        : context;

                if (clazz == null) {
                    return 1;
                }

                long id = classEntity.getId();
                do {
                    if (clazz.getId() == id) {
                        return 0;
                    }

                    clazz = clazz.getParent();
                } while (clazz != null);

                return 1;
        }

        return 2;
    }
}
