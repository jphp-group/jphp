package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.PropertyEntity;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionProperty")
@Signature({
    @Arg(value = "name", type = HintType.STRING, readOnly = true),
    @Arg(value = "class", type = HintType.STRING, readOnly = true)
})
public class ReflectionProperty extends Reflection implements Reflector {
    public final static int IS_STATIC = 1;
    public final static int IS_PUBLIC = 256;
    public final static int IS_PROTECTED = 512;
    public final static int IS_PRIVATE = 1024;

    protected PropertyEntity entity;
    protected boolean hackAccess = false;

    public ReflectionProperty(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setEntity(PropertyEntity entity) {
        this.entity = entity;
        getProperties().put("name", new StringMemory(entity.getName()));
        getProperties().put("class", new StringMemory(entity.getClazz().getName()));
    }

    @Signature({@Arg("class"), @Arg("name")})
    public Memory __construct(Environment env, Memory... args){
        ClassEntity classEntity;
        Memory arg = args[0];

        if (arg.isObject()){
            classEntity = arg.toValue(ObjectMemory.class).getReflection();
        } else
            classEntity = env.fetchClass(arg.toString());

        if (classEntity == null){
            exception(env, Messages.ERR_CLASS_NOT_FOUND.fetch(arg));
            return Memory.NULL;
        }

        String prop = args[1].toString();
        PropertyEntity entity = classEntity.findProperty(prop);
        if (entity == null)
            entity = classEntity.findStaticProperty(prop);

        if (entity == null){
            exception(env, Messages.ERR_UNDEFINED_PROPERTY.fetch(classEntity.getName(), prop));
            return Memory.NULL;
        }

        setEntity(entity);
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory __toString(Environment env, Memory... args) {
        return new StringMemory("TODO");
    }

    @Signature
    public Memory getDeclaringClass(Environment env, Memory... args){
        ReflectionClass clazz = new ReflectionClass(env, env.fetchClass("ReflectionClass"));
        clazz.setEntity(entity.getClazz());
        return new ObjectMemory(clazz);
    }

    @Signature
    public Memory getDocComment(Environment env, Memory... args){
        if (entity.getDocComment() == null)
            return Memory.FALSE;

        return new StringMemory(entity.getDocComment().toString());
    }

    @Signature
    public Memory getModifiers(Environment env, Memory... args){
        int mod = 0;
        if (entity.isStatic())
            mod += IS_STATIC;

        if (entity.isPrivate())
            mod += IS_PRIVATE;

        if (entity.isProtected())
            mod += IS_PROTECTED;

        if (entity.isPublic())
            mod += IS_PUBLIC;

        return LongMemory.valueOf(mod);
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(entity.getName());
    }

    @Signature(@Arg(value = "object", optional = @Optional(value = "NULL")))
    public Memory getValue(Environment env, Memory... args) throws Throwable {
        if (!hackAccess){
            int access = entity.canAccess(env);
            if (access != 0) {
                exception(env, "Cannot access non-public member %s::$%s",
                        entity.getClazz().getName(), entity.getName());
                return Memory.NULL;
            }
        }

        Memory arg = args[0];
        if (arg.isNull()){
            if (!entity.isStatic())
                exception(env, "Dynamic property %s::$%s cannot get a property value statically",
                        entity.getClazz().getName(), entity.getName());

            return entity.getClazz().getStaticProperty(
                    env, env.trace(),
                    entity.getName(), true, false, null, null, 0, false);
        } else {
            if (arg.isObject()){
                if (entity.isStatic())
                    exception(env, "Static property %s::$%s cannot get a property value dynamically",
                            entity.getClazz().getName(), entity.getName());

                return arg.toValue(ObjectMemory.class).getProperties().valueOfIndex(entity.getSpecificName());
            } else
                exception(env, "Argument 1 must be object, given %s", arg.getRealType().toString());
        }
        return Memory.NULL;
    }

    @Signature
    public Memory isDefault(Environment env, Memory... args){
        return entity.isDefault() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPublic(Environment env, Memory... args){
        return entity.isPublic() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPrivate(Environment env, Memory... args){
        return entity.isPrivate() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isProtected(Environment env, Memory... args){
        return entity.isProtected() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isStatic(Environment env, Memory... args){
        return entity.isStatic() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("accessible"))
    public Memory setAccessible(Environment env, Memory... args){
        hackAccess = args[0].toBoolean();
        return Memory.NULL;
    }

    @Signature({@Arg("object"), @Arg("value")})
    public Memory setValue(Environment env, Memory... args) throws Throwable {
        if (!hackAccess){
            int access = entity.canAccess(env);
            if (access != 0) {
                exception(env, "Cannot access non-public member %s::$%s",
                        entity.getClazz().getName(), entity.getName());
            }
        }

        Memory arg = args[0];
        if (entity.isStatic()){
            if (!arg.isNull())
                exception(env, "Static property %s::$%s cannot get a property value dynamically",
                        entity.getClazz().getName(), entity.getName());

            entity.getClazz().getStaticProperty(env, env.trace(), entity.getName(), true, false, null, null, 0, false).assign(args[1]);
        } else {
            if (arg.isObject()){
                arg.toValue(ObjectMemory.class).getProperties().refOfIndex(entity.getSpecificName()).assign(args[1]);
            } else
                exception(env, "Argument 1 must be object, given %s", arg.getRealType().toString());
        }
        return Memory.NULL;
    }
}
