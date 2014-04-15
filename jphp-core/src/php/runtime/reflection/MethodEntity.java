package php.runtime.reflection;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.common.Modifier;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.Extension;
import php.runtime.lang.Closure;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.support.AbstractFunctionEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEntity extends AbstractFunctionEntity {
    protected ClassEntity clazz;
    protected ClassEntity trait;
    protected MethodNode cachedMethodNode;
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
    protected Closure cachedClosure;

    public MethodEntity(Context context) {
        super(context);
    }

    public MethodEntity(FunctionEntity entity){
        super(entity.getContext());
        setParameters(entity.parameters);
        setReturnReference(entity.isReturnReference());
        setDeprecated(entity.isDeprecated());
        setAbstract(false);
        setAbstractable(false);
        setModifier(Modifier.PUBLIC);
    }

    public MethodEntity(MethodEntity entity){
        super(entity.getContext());
        setParameters(entity.parameters);
        setReturnReference(entity.isReturnReference());
        setDeprecated(entity.isDeprecated());
        setAbstract(false);
        setAbstractable(false);
        setModifier(Modifier.PUBLIC);
    }

    public MethodEntity(Extension extension, Method method){
        this((Context)null);
        this.extension = extension;
        this.usesStackTrace = true;

        Reflection.Signature signature = method.getAnnotation(Reflection.Signature.class);
        do {
            if (signature != null)
                break;
            try {
                Class<?> cls = method.getDeclaringClass().getSuperclass();
                if (cls == null)
                    break;
                signature = cls.getMethod(method.getName(), method.getParameterTypes()).getAnnotation(Reflection.Signature.class);
            } catch (Exception e) {
                throw new CriticalException(e);
            }
        } while (true);

        if (signature == null)
            throw new IllegalArgumentException("Method is not annotated with @Reflection.Signature");

        Class<?>[] types = method.getParameterTypes();
        if (types.length != 2 || types[0] != Environment.class || types[1] != Memory[].class){
            throw new IllegalArgumentException(
                    "Invalid method signature - " + method.toGenericString()
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
            param.setType(arg.type());
            if (!arg.typeClass().isEmpty())
                param.setTypeClass(arg.typeClass());

            param.setReference(arg.reference());
            param.setName(arg.value().isEmpty() ? "arg" + i : arg.value());
            i++;
        }

        nativeMethod = method;
    }

    public MethodNode getMethodNode() {
        if (cachedMethodNode != null)
            return cachedMethodNode;

        synchronized (this) {
            if (cachedMethodNode != null)
                return cachedMethodNode;

            ClassNode classNode = clazz.getClassNode();
            for(Object m : classNode.methods) {
                MethodNode method = (MethodNode) m;
                if (method.name.equals(getInternalName()) ){
                    return cachedMethodNode = method;
                }
            }
        }
        throw new CriticalException("Cannot find MethodNode for method - " + name + "(" + getSignature() + ")");
    }

    public Closure getClosure(Environment env, final IObject object) {
        if (cachedClosure != null)
            return cachedClosure;

        final MethodEntity bind = this;
        final ClosureEntity closureEntity1 = new ClosureEntity(this.getContext());
        closureEntity1.setParent(env.scope.fetchUserClass(Closure.class));
        closureEntity1.parameters = this.parameters;
        closureEntity1.setReturnReference(this.isReturnReference());

        MethodEntity m = new MethodEntity(this);
        m.setClazz(closureEntity1);
        m.setName("__invoke");
        closureEntity1.addMethod(m, null);
        closureEntity1.doneDeclare();

        Closure tmp = new Closure(closureEntity1, new ObjectMemory(env.getLateObject()), new Memory[0]){
            @Override
            public Memory __invoke(Environment e, Memory... args) {
                try {
                    if (object == null)
                        return bind.invokeStatic(e, args);
                    else
                        return bind.invokeDynamic(object, e, args);
                } catch (RuntimeException e1){
                    throw e1;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }

            @Override
            public Memory getOrCreateStatic(String name) {
                return Memory.NULL;
            }

            @Override
            public ClassEntity getReflection() {
                return closureEntity1;
            }
        };

        try {
            m.setNativeMethod(tmp.getClass().getDeclaredMethod("__invoke", Environment.class, Memory[].class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return cachedClosure = tmp;
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

    public Memory invokeDynamic(IObject _this,
                                Environment environment, Memory... arguments) throws Throwable {
        try {
            if (isAbstract){
                environment.error(ErrorType.E_ERROR, "Cannot call abstract method %s", getSignatureString(false));
                return Memory.NULL;
            }

            if (_this == null && !isStatic){
                _this = clazz.newMock(environment);
                if (_this == null)
                    environment.error(ErrorType.E_ERROR, Messages.ERR_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(
                            getClazz().getName() + "::" + getName())
                    );
            }

            return isEmpty ? Memory.NULL : (Memory)nativeMethod.invoke(_this, environment, arguments);
        } catch (InvocationTargetException e){
            return environment.__throwException(e);
        } finally {
            unsetArguments(arguments);
        }
    }

    public Memory invokeStatic(Environment environment, Memory... arguments) throws Throwable {
        return invokeDynamic(null, environment, arguments);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
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

    public String getClazzName(){
        return clazz.getName();
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

    public boolean isPublic(){
        return modifier == Modifier.PUBLIC;
    }

    public boolean isProtected(){
        return modifier == Modifier.PROTECTED;
    }

    public boolean isPrivate(){
        return modifier == Modifier.PRIVATE;
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
        String ownerName = getClazz().getName();
        if (getTrait() != null)
            ownerName = getTrait().getName();

        StringBuilder sb = new StringBuilder(ownerName + "::" + getName() + "(");

        int i = 0;
        if (parameters != null && withArgs)
        for(ParameterEntity param : parameters){
            sb.append(param.getSignatureString());
            if (i != parameters.length - 1)
                sb.append(", ");

            i++;
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
            if (param.getDefaultValue() == null)
                sb.append(
                        param.type == null ? HintType.ANY : param.type
                ).append("|").append(param.isReference ? "&" : "");
        }
        return signature = sb.toString();
    }

    public boolean equalsBySignature(MethodEntity method, boolean strong){
        if (strong)
            return getSignature().equals(method.getSignature());
        else {
            int cnt1 = parameters != null ? parameters.length : 0;
            int cnt2 = method.parameters != null ? method.parameters.length : 0;

            return cnt1 == cnt2;
        }
    }

    public boolean equalsBySignature(MethodEntity method){
        return equalsBySignature(method, true);
    }


    public boolean equalsByHintTypingSignature(MethodEntity method){
        if (parameters == null || method.parameters == null)
            return true;

        int i = 0;
        for(ParameterEntity param : parameters){
            if (i >= method.parameters.length)
                break;

            ParameterEntity other = method.parameters[i];
            if (param.getTypeClass() != null){
                if (other.getTypeClass() == null || !other.getTypeClassLower().equals(param.getTypeClassLower()))
                    return false;
            } else if (param.getType() != HintType.ANY){
                if (other.getType() != param.getType())
                    return false;
            }
            i++;
        }
        return true;
    }

    public boolean isOwned(ClassEntity entity){
        return clazz.getId() == entity.getId();
    }

    public int canAccess(Environment env) {
        return canAccess(env, null);
    }

    /**
     * 0 - success
     * 1 - invalid protected
     * 2 - invalid private
     * @param env
     * @return
     */
    public int canAccess(Environment env, ClassEntity context) {
        switch (modifier){
            case PUBLIC: return 0;
            case PRIVATE:
                ClassEntity cl = context == null ? env.getLastClassOnStack() : context;
                if (cl == null)
                    return 2;

                if (cl.getId() == this.clazz.getId())
                    return 0;

                MethodEntity tmp = prototype;
                while (tmp != null){
                    if (cl.getId() == tmp.clazz.getId())
                        return 0;
                    tmp = tmp.prototype;
                }
                return 2;
            case PROTECTED:
                ClassEntity clazz = context == null ? env.getLateStaticClass() : context;
                if (clazz == null)
                    return 1;

                long id = this.clazz.getId();
                do {
                    if (clazz.getId() == id)
                        return 0;
                    clazz = clazz.parent;
                } while (clazz != null);
                return 1;
        }
        return 2;
    }

    public ClassEntity getTrait() {
        return trait;
    }

    public void setTrait(ClassEntity trait) {
        this.trait = trait;
    }

    public int getRequiredParamCount() {
        int cnt = 0;
        if (parameters != null)
        for(ParameterEntity e : parameters) {
            if (e.getDefaultValue() != null)
                break;
        }
        return cnt;
    }

    public MethodEntity duplicateForInject() {
        MethodEntity methodEntity = new MethodEntity(this.context);
        methodEntity.setExtension(getExtension());
        methodEntity.setAbstract(isAbstract);
        methodEntity.setFinal(isFinal);
        methodEntity.setDynamicSignature(isDynamicSignature());
        methodEntity.setModifier(modifier);
        methodEntity.setName(name);
        methodEntity.setStatic(isStatic);
        methodEntity.setAbstractable(isAbstractable());
        methodEntity.setDocComment(getDocComment());
        methodEntity.setParameters(parameters);
        methodEntity.setReturnReference(isReturnReference());
        methodEntity.setEmpty(isEmpty);
        methodEntity.setImmutable(isImmutable);
        methodEntity.setResult(result);
        methodEntity.setUsesStackTrace(isUsesStackTrace());
        methodEntity.setDeprecated(isDeprecated());
        methodEntity.setInternalName(getInternalName());
        if (trace == null || trace == TraceInfo.UNKNOWN)
            methodEntity.setTrace(getClazz().getTrace());
        else
            methodEntity.setTrace(trace);

        return methodEntity;
    }
}
