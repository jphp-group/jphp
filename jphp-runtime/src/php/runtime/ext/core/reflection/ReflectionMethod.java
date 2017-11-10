package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.invoke.DynamicMethodInvoker;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.StaticMethodInvoker;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.AbstractFunctionEntity;

import java.util.Arrays;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionMethod")
@Signature({
        @Arg(value = "name", type = HintType.STRING, readOnly = true),
        @Arg(value = "class", type = HintType.STRING, readOnly = true)
})
public class ReflectionMethod extends ReflectionFunctionAbstract {
    public final static int IS_STATIC = 1 ;
    public final static int IS_PUBLIC = 256 ;
    public final static int IS_PROTECTED = 512 ;
    public final static int IS_PRIVATE = 1024 ;
    public final static int IS_ABSTRACT = 2 ;
    public final static int IS_FINAL = 4 ;

    protected MethodEntity methodEntity;
    protected ArrayMemory cachedParameters;
    protected boolean hackAccess = false;

    public ReflectionMethod(Environment env, MethodEntity methodEntity) {
        super(env);
        setEntity(methodEntity);
    }

    public ReflectionMethod(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEntity(MethodEntity entity) {
        this.methodEntity = entity;
        getProperties().put("name", new StringMemory(entity.getName()));
        getProperties().put("class", new StringMemory(entity.getClazz().getName()));
    }

    @Signature({@Arg("class"), @Arg("name")})
    public Memory __construct(Environment env, Memory... args){
        ClassEntity classEntity;
        Memory _class = args[0];
        if (_class.isObject())
            classEntity = _class.toValue(ObjectMemory.class).getReflection();
        else
            classEntity = env.fetchClass(_class.toString(), true);

        if (classEntity == null){
            exception(env, Messages.ERR_CLASS_NOT_FOUND.fetch(_class));
            return Memory.NULL;
        }

        MethodEntity entity = classEntity.findMethod(args[1].toString().toLowerCase());
        if (entity == null){
            exception(env, Messages.ERR_METHOD_NOT_FOUND.fetch(_class, args[1]));
            return Memory.NULL;
        }

        setEntity(entity);
        return Memory.NULL;
    }

    @Override
    protected AbstractFunctionEntity getEntity() {
        return methodEntity;
    }

    @Override
    @Signature
    public Memory getParameters(Environment env, Memory... args) {
        if (cachedParameters != null)
            return cachedParameters;

        ParameterEntity[] parameters = methodEntity.getParameters(Integer.MAX_VALUE);

        ClassEntity entity = env.fetchClass("ReflectionParameter");
        ArrayMemory result = new ArrayMemory();

        int i = 0;
        for(ParameterEntity param : parameters){
            ReflectionParameter e = new ReflectionParameter(env, entity);
            e.setEntity(param);
            e.setFunctionEntity(methodEntity);
            e.setPosition(i);
            i++;

            result.add(new ObjectMemory(e));
        }

        return cachedParameters = result;
    }

    @Signature
    public Memory getDeclaringClass(Environment env, Memory... args){
        ClassEntity entity = env.fetchClass("ReflectionClass");
        ReflectionClass r = new ReflectionClass(env, entity);
        r.setEntity(methodEntity.getClazz());

        return new ObjectMemory(r);
    }

    @Signature
    public Memory getModifiers(Environment env, Memory... args){
        int mod = 0;
        if (methodEntity.isAbstract())
            mod |= IS_ABSTRACT;

        if (methodEntity.isFinal())
            mod |= IS_FINAL;

        if (methodEntity.isPrivate())
            mod |= IS_PRIVATE;
        else if (methodEntity.isProtected())
            mod |= IS_PROTECTED;
        else if (methodEntity.isPublic())
            mod |= IS_PUBLIC;

        if (methodEntity.isStatic())
            mod |= IS_STATIC;

        return LongMemory.valueOf(mod);
    }

    @Signature
    public Memory getPrototype(Environment env, Memory... args){
        if (methodEntity.getPrototype() == null)
            return Memory.NULL;

        ClassEntity classEntity = env.fetchClass("ReflectionMethod");
        ReflectionMethod r = new ReflectionMethod(env, classEntity);
        r.setEntity(methodEntity.getPrototype());

        return new ObjectMemory(r);
    }

    @Signature
    public Memory isConstructor(Environment env, Memory... args) {
        return methodEntity.getClazz().methodConstruct == methodEntity ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDestruct(Environment env, Memory... args) {
        return methodEntity.getClazz().methodDestruct == methodEntity ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAbstract(Environment env, Memory... args){
        return methodEntity.isAbstract() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isFinal(Environment env, Memory... args){
        return methodEntity.isFinal() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isStatic(Environment env, Memory... args){
        return methodEntity.isStatic() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPublic(Environment env, Memory... args){
        return methodEntity.isPublic() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isProtected(Environment env, Memory... args){
        return methodEntity.isProtected() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPrivate(Environment env, Memory... args){
        return methodEntity.isPrivate() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("accessible"))
    public Memory setAccessible(Environment env, Memory... args){
        hackAccess = args[0].toBoolean();
        return Memory.NULL;
    }

    @Signature({@Arg(value = "object", optional = @Optional, type = HintType.OBJECT)})
    public Memory invoke(Environment env, Memory... args) throws Throwable {
        Memory self = args[0];

        Memory[] arguments = args.length == 1 ? new Memory[0] : Arrays.copyOfRange(args, 1, args.length - 1);
        Invoker invoker;

        if (self.isNull()) {
            invoker = new StaticMethodInvoker(env, env.trace(), methodEntity.getClazzName(), methodEntity);
        } else {
            invoker = new DynamicMethodInvoker(env, env.trace(), self.toObject(IObject.class), methodEntity);
        }

        return invoker.call(arguments);
    }

    @Signature({
            @Arg(value = "object", optional = @Optional, type = HintType.OBJECT),
            @Arg(value = "args", type = HintType.ARRAY)
    })
    public Memory invokeArgs(Environment env, Memory... args) throws Throwable {
        Memory self = args[0];

        Memory[] arguments = args[1].toValue(ArrayMemory.class).values();
        Invoker invoker;

        if (self.isNull()) {
            invoker = new StaticMethodInvoker(env, env.trace(), methodEntity.getClazzName(), methodEntity);
        } else {
            invoker = new DynamicMethodInvoker(env, env.trace(), self.toObject(IObject.class), methodEntity);
        }

        return invoker.call(arguments);
    }

    @Signature(@Arg("object"))
    public Memory getClosure(final Environment env, Memory... args) throws Throwable {
        IObject object;
        if (args[0].isNull()){
            object = null;
        } else if (args[0].isObject()) {
            object = args[0].toValue(ObjectMemory.class).value;
        } else {
            exception(env, "Argument 1 must be NULL or object, %s given", args[0].getRealType().toString());
            return Memory.NULL;
        }

        if (object == null && !methodEntity.isStatic()){
            exception(env, "Cannot use method as static");
            return Memory.NULL;
        } else if (object != null && methodEntity.isStatic()){
            exception(env, "Cannot use method as non static");
            return Memory.NULL;
        }

        return new ObjectMemory(methodEntity.getClosure(env, object));
    }
}
