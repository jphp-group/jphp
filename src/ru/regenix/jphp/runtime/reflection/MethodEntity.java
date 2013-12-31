package ru.regenix.jphp.runtime.reflection;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.support.Memory;
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
    protected boolean dynamicSignature = false;
    private String key;

    protected String signature;

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
        if (types.length != 2 || types[0] != Environment.class || types[1] != Memory[].class){
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

    public boolean isDynamicSignature() {
        return dynamicSignature;
    }

    public void setDynamicSignature(boolean dynamicSignature) {
        this.dynamicSignature = dynamicSignature;
    }

    public Extension getExtension() {
        return extension;
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(Method nativeMethod) {
        this.nativeMethod = nativeMethod;
        nativeMethod.setAccessible(true);
    }

    public Memory invokeDynamic(IObject _this, Environment environment, Memory... arguments) throws Throwable {
        Memory result = null;
        try {
            if (_this == null)
                _this = clazz.newMock(environment);

            result = isEmpty ? Memory.NULL : (Memory)nativeMethod.invoke(_this, environment, arguments);
        } catch (InvocationTargetException e){
            throw e.getTargetException();
        } finally {
            if (arguments != null){
                int x = 0;
                for(ParameterEntity argument : this.parameters){
                    if (!argument.isReference) {
                        arguments[x].unset();
                    }
                    x++;
                }
            }
        }
        if (!isReturnReference())
            return result.toImmutable();
        else
            return result;
    }

    public Memory invokeStatic(Environment environment, Memory... arguments) throws Throwable {
        return invokeDynamic(null, environment, arguments);
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

    /*@Override
    public int hashCode() {
        return hashCode(clazz.getLowerName(), lowerName);
    }*/

    public static int hashCode(String classLowerName, String methodLowerName){
        return classLowerName.hashCode() + methodLowerName.hashCode();
    }

    public String getKey() {
        return key;
    }

    public String getSignatureString(boolean withArgs){
        StringBuilder sb = new StringBuilder(getClazz().getName() + "::" + getName() + "(");

        int i = 0;
        if (parameters != null && withArgs)
        for(ParameterEntity param : parameters){
            sb.append(param.getSignatureString());
            if (i < parameters.length - 1)
                sb.append(", ");
        }

        sb.append(")");
        return sb.toString();
    }

    public String getSignature() {
        if (signature != null)
            return signature;

        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (parameters != null)
        for(ParameterEntity param : parameters){
            sb.append(param.type).append("|").append(param.isReference ? "&" : "");
        }
        return signature = sb.toString();
    }

    public boolean equalsBySignature(MethodEntity method){
        return getSignature().equals(method.getSignature());
    }

    /**
     * 0 - success
     * 1 - invalid protected
     * 2 - invalid private
     * @param env
     * @return
     */
    public int canAccess(Environment env, boolean external) throws InvocationTargetException, IllegalAccessException {
        switch (modifier){
            case PUBLIC: return 0;
            case PRIVATE:
                ClassEntity cl = env.getLastClassOnStack();
                return cl != null && cl.getId() == this.clazz.getId() ? 0 : 2;
            case PROTECTED:
                ClassEntity clazz = env.getLastClassOnStack();
                if (clazz == null)
                    return 1;

                long id = this.clazz.getId();
                do {
                    if (clazz.getId() == id)
                        return 0;
                    clazz = clazz.parent;
                } while (clazz != null);
        }
        return 2;
    }
}
