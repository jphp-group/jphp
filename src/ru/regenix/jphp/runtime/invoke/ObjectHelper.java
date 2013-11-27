package ru.regenix.jphp.runtime.invoke;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.lang.reflect.InvocationTargetException;

final public class ObjectHelper {

    private ObjectHelper(){ }

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
        else
            method = phpObject.__class__.methods.get(methodLowerName);

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
        Memory[] passed = DynamicInvoke.makeArguments(
                env, args, method.parameters, className, methodName, trace
        );
        Memory result;
        env.pushCall(trace, object, args, methodName, className);
        try {
            result = method.invokeDynamic(phpObject, className, env, passed);
        } finally {
            env.popCall();
        }
        return result;
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

    public static Memory setProperty(Memory object, Memory value,
                                     String property, Environment env, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException {
        object = object.toImmutable();
        if (!object.isObject()){
            env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_SET_PROPERTY_OF_NON_OBJECT.fetch(property),
                    trace
            ));
        }

        PHPObject phpObject = ((ObjectMemory)object).value;
        return phpObject.__class__.setProperty(env, trace, phpObject, property, value);
    }
}
