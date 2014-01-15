package php.runtime.reflection;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.Memory;
import php.runtime.reflection.support.AbstractFunctionEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionEntity extends AbstractFunctionEntity {
    protected boolean isInternal = false;
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
        try {
            return (Memory)nativeMethod.invoke(null, env, arguments);
        } catch (InvocationTargetException e){
            throw e.getTargetException();
        } finally {
            unsetArguments(arguments);
        }
    }
}
