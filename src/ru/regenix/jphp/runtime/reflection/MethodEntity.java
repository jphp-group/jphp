package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.exceptions.ClassNotLoadedException;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.MemoryUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEntity extends AbstractFunctionEntity {
    protected ClassEntity clazz;
    protected MethodEntity prototype;

    protected Method nativeMethod;

    protected boolean isAbstract;
    protected boolean isFinal;
    protected boolean isStatic;
    protected Modifier modifier;

    public MethodEntity(Context context) {
        super(context);
    }

    public Memory invoke(Object _this, Environment environment, Memory... arguments){
        if (nativeMethod == null){
            if (clazz.nativeClazz == null)
                throw new ClassNotLoadedException(clazz.getName());

            try {
                synchronized (this){
                    nativeMethod = clazz.nativeClazz.getDeclaredMethod(getName(), Environment.class, Memory[].class);
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return (Memory)nativeMethod.invoke(_this, environment, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ErrorException)
                throw (ErrorException) e.getCause();

            throw new RuntimeException(e);
        }
    }

    public Memory invoke(Environment environment, Memory... arguments){
        return invoke(null, environment, arguments);
    }

    public Memory invoke(Object _this, Environment environment, Object... objects){
        for(int i = 0; i < objects.length; i++){
            objects[i] = MemoryUtils.valueOf(objects[i]);
        }
        return invoke(_this, environment, (Memory[])objects);
    }

    public Memory invoke(Environment environment, Object... objects){
        return invoke(null, environment, objects);
    }

    public MethodEntity getPrototype() {
        return prototype;
    }

    public void setPrototype(MethodEntity prototype) {
        this.prototype = prototype;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    public void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    @Override
    public boolean isNamespace(){
        return false;
    }

    public boolean isDeprecated(){
        return false; // TODO
    }
}
