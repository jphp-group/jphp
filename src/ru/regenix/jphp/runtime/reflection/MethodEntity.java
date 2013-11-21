package ru.regenix.jphp.runtime.reflection;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.support.AbstractFunctionEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEntity extends AbstractFunctionEntity {
    protected ClassEntity clazz;
    protected Extension extension;
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

    public MethodEntity(Extension extension, Method method){
        this(null);
        this.extension = extension;

        Reflection.Signature signature = method.getAnnotation(Reflection.Signature.class);
        if (signature == null)
            throw new IllegalArgumentException("Method is not annotated with @Reflection.Signature");

        Class<?>[] types = method.getParameterTypes();
        if (types.length != 3 || types[0] != Environment.class || types[1] != String.class || types[2] != Memory[].class){
            throw new IllegalArgumentException(
                    "Invalid method signature: " + StringUtils.join(method.getTypeParameters(), ", ")
            );
        }

        int modifiers = method.getModifiers();
        isFinal = java.lang.reflect.Modifier.isFinal(modifiers);
        isStatic = java.lang.reflect.Modifier.isStatic(modifiers);
        isAbstract = java.lang.reflect.Modifier.isAbstract(modifiers);

        modifier = Modifier.PUBLIC;
        if (java.lang.reflect.Modifier.isProtected(modifiers))
            modifier = Modifier.PROTECTED;
        else if (java.lang.reflect.Modifier.isPrivate(modifiers))
            modifier = Modifier.PRIVATE;

        int i = 1;
        for(Reflection.Arg arg : signature.value()){
            ParameterEntity param = new ParameterEntity(context);
            param.setMethod(this);
            param.setType(arg.type());
            param.setReference(arg.reference());
            param.setName(arg.value().isEmpty() ? "arg_" + i : arg.value());
            i++;
        }

        nativeMethod = method;
    }

    public Extension getExtension() {
        return extension;
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(Method nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    public Memory invokeDynamicNoThrow(PHPObject _this, String _static, Environment environment, Memory... arguments){
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

    public Memory invokeDynamic(PHPObject _this, String _static, Environment environment, Memory... arguments)
            throws IllegalAccessException, InvocationTargetException {
        if (_static == null){
            _static = _this.getClass().getName().replace('.', '\\').toLowerCase();
        }

        Memory result = (Memory)nativeMethod.invoke(_this, environment, _static, arguments);
        for(Memory e : arguments)
            e.unset();

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
        return hashCode(clazz.getLowerName(), lowerName);
    }

    public static int hashCode(String classLowerName, String methodLowerName){
        return classLowerName.hashCode() + methodLowerName.hashCode();
    }

    public String getKey() {
        return key;
    }
}
