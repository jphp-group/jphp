package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.runtime.env.Context;
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

    private boolean isStatic = false;

    public FunctionEntity(Context context) {
        super(context);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
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
        nativeMethod.setAccessible(true);
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

    public Memory invoke(Environment env, TraceInfo trace, Memory[] arguments) throws Throwable {
        Memory result = null;
        try {
            result = (Memory)nativeMethod.invoke(null, env, arguments);
            if (!isReturnReference())
                return result.toImmutable();
            else
                return result;
        } catch (InvocationTargetException e){
            throw e.getTargetException();
        } finally {
            unsetArguments(arguments);
        }
    }
}
