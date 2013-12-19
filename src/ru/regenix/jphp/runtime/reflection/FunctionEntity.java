package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.DieException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.support.AbstractFunctionEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionEntity extends AbstractFunctionEntity {
    protected boolean isInternal = false;

    private byte[] data;
    protected ModuleEntity module;

    private Class<?> nativeClazz;
    private Method nativeMethod;

    public FunctionEntity(Context context) {
        super(context);
    }

    public boolean isInternal() {
        return isInternal;
    }

    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(Method nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public Memory invoke(Environment env, TraceInfo trace, Memory[] arguments) throws IllegalAccessException, InvocationTargetException {
        Memory result = (Memory)nativeMethod.invoke(null, env, arguments);
        if (arguments != null){
            int x = 0;
            for(ParameterEntity argument : this.parameters){
                if (!argument.isReference) {
                    arguments[x].unset();
                }
                x++;
            }
        }

        if (!isReturnReference())
            return result.toImmutable();
        else
            return result;
    }

    public Memory invokeNoThrow(Environment env, TraceInfo trace, Memory[] arguments){
        try {
            return invoke(env, trace, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = getCause(e);
            if (cause instanceof ErrorException)
                throw (ErrorException) cause;
            if (cause instanceof DieException)
                throw (DieException) cause;

            throw new RuntimeException(cause);
        }
    }
}
