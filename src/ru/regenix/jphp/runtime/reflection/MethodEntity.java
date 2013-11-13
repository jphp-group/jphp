package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.exceptions.ClassNotLoadedException;
import ru.regenix.jphp.runtime.memory.Memory;

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
    private String key;

    public MethodEntity(Context context) {
        super(context);
    }

    public Memory invokeDynamicNoThrow(Object _this, String _static, Environment environment, Memory... arguments){
        try {
            return invokeDynamic(_this, _static, environment, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ErrorException)
                throw (ErrorException) e.getCause();
            throw new RuntimeException(e.getCause());
        }
    }

    public Memory invokeDynamic(Object _this, String _static, Environment environment, Memory... arguments)
            throws IllegalAccessException, InvocationTargetException {
        if (_static == null){
            _static = _this.getClass().getName().replace('.', '\\').toLowerCase();
        }
        if (nativeMethod == null){
            if (clazz.nativeClazz == null)
                throw new ClassNotLoadedException(clazz.getName());

            try {
                synchronized (this){
                    nativeMethod = clazz.nativeClazz.getDeclaredMethod(
                            getName(), Environment.class, String.class, Memory[].class
                    );
                    nativeMethod.setAccessible(true);
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        Memory result = (Memory)nativeMethod.invoke(_this, environment, _static, arguments);
        if (!isReturnReference())
            return result.toImmutable();
        else
            return result;
    }

    public Memory invokeStaticNoThrow(String _static, Environment environment, Memory... arguments){
        try {
            return invokeStatic(_static, environment, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ErrorException)
                throw (ErrorException) e.getCause();
            throw new RuntimeException(e.getCause());
        }
    }

    public Memory invokeStatic(String _static, Environment environment, Memory... arguments)
            throws IllegalAccessException, InvocationTargetException {
        if (_static == null)
            _static = clazz.getLowerName();

        return invokeDynamic(null, _static, environment, arguments);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        key = clazz.getLowerName() + "#" + lowerName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodEntity)) return false;
        if (!super.equals(o)) return false;

        MethodEntity that = (MethodEntity) o;
        return hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return hashCode(clazz.lowerName, lowerName);
    }

    public static int hashCode(String classLowerName, String methodLowerName){
        return classLowerName.hashCode() + methodLowerName.hashCode();
    }

    public String getKey() {
        return key;
    }
}
