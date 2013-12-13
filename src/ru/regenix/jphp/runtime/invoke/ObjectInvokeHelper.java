package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.lang.reflect.InvocationTargetException;

final public class ObjectInvokeHelper {

    private ObjectInvokeHelper(){ }

    public static Memory invokeMethod(Memory object, String methodName, String methodLowerName,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        object = object.toImmutable();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_CALL_OF_NON_OBJECT.fetch(methodName),
                    trace
            ));
        }
        PHPObject phpObject = ((ObjectMemory)object).value;
        MethodEntity method;
        if (methodName == null)
            method = phpObject.__class__.methodMagicInvoke;
        else {
            method = phpObject.__class__.methods.get(methodLowerName);
            /*if (method.isStatic()) { IT's not needed!!!
                env.triggerError(new FatalException(
                        Messages.ERR_FATAL_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(
                                phpObject.__class__.getName() + "::" + methodName
                        ),
                        trace
                ));
            }*/
        }

        String className = phpObject.__class__.getName();

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

        assert method != null;
        Memory[] passed = InvokeHelper.makeArguments(
                env, args, method.parameters, className, methodName, trace
        );
        Memory result;
        if (trace != null)
            env.pushCall(trace, phpObject, args, methodName, className);
        try {
            result = method.invokeDynamic(phpObject, className, env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
        return result;
    }

    public static Memory invokeMethod(PHPObject phpObject, MethodEntity method,
                                      Environment env, TraceInfo trace, Memory[] args)
            throws InvocationTargetException, IllegalAccessException {
        if (method == null)
            method = phpObject.__class__.methodMagicInvoke;

        String className = phpObject.__class__.getName();

        if (method == null){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_METHOD.fetch(
                            className + "::__invoke"
                    ),
                    trace
            ));
        }

        assert method != null;
        Memory[] passed = InvokeHelper.makeArguments(
                env, args, method.parameters, className, method.getName(), trace
        );
        Memory result;
        if (trace != null)
            env.pushCall(trace, phpObject, args, method.getName(), className);

        try {
            result = method.invokeDynamic(phpObject, className, env, passed);
        } finally {
            if (trace != null)
                env.popCall();
        }
        return result;
    }

    public static void unsetProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        object = object.toImmutable();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        PHPObject phpObject = ((ObjectMemory)object).value;
        phpObject.__class__.unsetProperty(env, trace, phpObject, property);
    }

    public static Memory getProperty(Memory object, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        object = object.toImmutable();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        PHPObject phpObject = ((ObjectMemory)object).value;
        return phpObject.__class__.getProperty(env, trace, phpObject, property);
    }

    private static PHPObject fetchObject(Memory object, String property, Environment env, TraceInfo trace){
        object = object.toImmutable();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_SET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        return  ((ObjectMemory)object).value;
    }

    public static Memory assignProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.setProperty(env, trace, phpObject, property, value, null);
    }

    public static Memory plusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.plusProperty(env, trace, phpObject, property, value);
    }

    public static Memory minusProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.minusProperty(env, trace, phpObject, property, value);
    }

    public static Memory mulProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.mulProperty(env, trace, phpObject, property, value);
    }

    public static Memory divProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.divProperty(env, trace, phpObject, property, value);
    }

    public static Memory modProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.modProperty(env, trace, phpObject, property, value);
    }

    public static Memory concatProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.concatProperty(env, trace, phpObject, property, value);
    }

    public static Memory bitAndProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.bitAndProperty(env, trace, phpObject, property, value);
    }

    public static Memory bitOrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.bitOrProperty(env, trace, phpObject, property, value);
    }

    public static Memory bitXorProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.bitXorProperty(env, trace, phpObject, property, value);
    }

    public static Memory bitShrProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.bitShrProperty(env, trace, phpObject, property, value);
    }

    public static Memory bitShlProperty(Memory object, Memory value, String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        PHPObject phpObject = fetchObject(object, property, env, trace);
        return phpObject.__class__.bitShlProperty(env, trace, phpObject, property, value);
    }
}
