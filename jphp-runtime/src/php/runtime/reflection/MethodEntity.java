package php.runtime.reflection;

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
import php.runtime.invoke.InvokeHelper;
import php.runtime.lang.Closure;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.support.AbstractFunctionEntity;
import php.runtime.reflection.support.TypeChecker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Reflection.Signature
public class MethodEntity extends AbstractFunctionEntity {
    protected ClassEntity clazz;
    protected ClassEntity trait;
    protected Extension extension;
    protected MethodEntity prototype;

    protected Method nativeMethod;

    protected boolean isAbstract;
    protected boolean isFinal;
    protected boolean isStatic;
    protected Modifier modifier;
    protected boolean dynamicSignature = false;

    protected String signature;
    protected Closure cachedClosure;

    protected boolean contextDepends = false;

    public MethodEntity(Context context) {
        super(context);
    }

    public MethodEntity(FunctionEntity entity){
        super(entity.getContext());
        setParameters(entity.getParameters());
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

        if (signature == null) {
            signature = getClass().getAnnotation(Reflection.Signature.class);
        }

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
        isFinal = method.isAnnotationPresent(Reflection.Final.class);

        isStatic = java.lang.reflect.Modifier.isStatic(modifiers);
        isAbstract = java.lang.reflect.Modifier.isAbstract(modifiers);

        modifier = Modifier.PUBLIC;
        if (java.lang.reflect.Modifier.isProtected(modifiers))
            modifier = Modifier.PROTECTED;
        else if (java.lang.reflect.Modifier.isPrivate(modifiers))
            modifier = Modifier.PRIVATE;

        nativeMethod = method;
    }

    /*
            ClassNode classNode = clazz.getClassNode();
            for(Object m : classNode.methods) {
                MethodNode method = (MethodNode) m;
                if (method.name.equals(getInternalName()) ){
                    return cachedMethodNode = method;
                }
            }

        throw new CriticalException("Cannot find MethodNode for method - " + name + "(" + getSignatureString(true) + ")");
    */

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

        Closure tmp = new Closure(env, closureEntity1, new ObjectMemory(env.getLateObject()), clazz.getName(), new Memory[0]){
            @Override
            public Memory __invoke(Environment e, Memory... args) {
                try {
                    if (object == null)
                        return bind.invokeStatic(e, args);
                    else
                        return bind.invokeDynamic(object, e, trace, args);
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

        if (nativeMethod != null) {
            nativeMethod.setAccessible(true);
        }
    }

    @Override
    public Memory getImmutableResultTyped(Environment env, TraceInfo trace) {
        Memory result = getImmutableResult();

        if (result != null && !resultTypeChecked) {
            result = InvokeHelper.checkReturnType(env, trace, result, this);

            if (result != null) {
                this.result = result;
                this.resultTypeChecked = true;
            }
        }

        return result;
    }

    public Memory invokeDynamic(IObject _this, Environment env, TraceInfo trace, Memory... arguments) throws Throwable {
        try {
            if (isAbstract){
                env.error(ErrorType.E_ERROR, "Cannot call abstract method %s", getSignatureString(false));
                return Memory.NULL;
            }

            if (_this == null && !isStatic){
                _this = clazz.newMock(env);
                if (_this == null)
                    env.error(ErrorType.E_ERROR, Messages.ERR_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(
                            getClazz().getName() + "::" + getName())
                    );
            }

            if (isEmpty) {
                return Memory.UNDEFINED;
            }

            Memory result = (Memory) nativeMethod.invoke(_this, env, arguments);

            return InvokeHelper.checkReturnType(env, trace, result, this);
        } catch (InvocationTargetException e){
            return env.__throwException(e);
        } catch (Throwable e) {
            throw e;
        } finally {
            unsetArguments(arguments);
        }
    }

    public Memory invokeStatic(Environment env, TraceInfo trace, Memory... arguments) throws Throwable {
        return invokeDynamic(null, env, trace, arguments);
    }

    final public Memory invokeStatic(Environment environment, Memory... arguments) throws Throwable {
        return invokeStatic(environment, TraceInfo.UNKNOWN, arguments);
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
        this.contextDepends = prototype != null && (prototype.contextDepends || prototype.isPrivate());
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

    public boolean isContextDepends() {
        return contextDepends;
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

        TypeChecker returnTypeChecker = getReturnTypeChecker();

        if (returnTypeChecker != null) {
            sb.append(": ").append(returnTypeChecker.getSignature());
        }

        return sb.toString();
    }

    public String getSignature() {
        if (signature != null)
            return signature;

        StringBuilder sb = new StringBuilder();
        int i = 0;

        if (parameters != null) {
            for (ParameterEntity param : parameters) {
                if (param.getDefaultValue() == null) {
                    sb.append(
                            param.getType() == null ? HintType.ANY : param.getType()
                    ).append("|").append(param.isReference ? "&" : "");
                }
            }
        }

        TypeChecker typeChecker = getReturnTypeChecker();

        if (typeChecker != null) {
            String signature = typeChecker.getSignature();

            sb.append(":");
            sb.append(signature);
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
                ClassEntity originClass;
                ClassEntity clazz = originClass = context == null ? env.getLateStaticClass() : context;
                if (clazz == null)
                    return 1;

                long id = this.clazz.getId();
                do {
                    if (clazz.getId() == id)
                        return 0;
                    clazz = clazz.parent;
                } while (clazz != null);

                if (this.clazz.isInstanceOf(originClass)) {
                    return 0;
                }

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

        methodEntity.setReturnTypeNullable(isReturnTypeNullable());
        methodEntity.setReturnTypeChecker(getReturnTypeChecker());

        if (trace == null || trace == TraceInfo.UNKNOWN)
            methodEntity.setTrace(getClazz().getTrace());
        else
            methodEntity.setTrace(trace);

        return methodEntity;
    }
}
