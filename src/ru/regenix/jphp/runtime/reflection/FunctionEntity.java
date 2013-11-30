package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.support.AbstractFunctionEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionEntity extends AbstractFunctionEntity {

    private byte[] data;
    protected ModuleEntity module;

    private Class<?> nativeClazz;
    private Method nativeMethod;

    public FunctionEntity(Context context) {
        super(context);
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

    public Memory invoke(Environment env, Memory[] arguments) throws IllegalAccessException, InvocationTargetException {
        Memory result = (Memory)nativeMethod.invoke(null, env, "", arguments);
        if (arguments != null)
        for(Memory e : arguments)
            e.unset();

        if (!isReturnReference())
            return result.toImmutable();
        else
            return result;
    }

    public Memory invokeNoThrow(Environment env, Memory[] arguments){
        try {
            return invoke(env, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ErrorException)
                throw (ErrorException) e.getCause();
            throw new RuntimeException(e.getCause());
        }
    }
}
