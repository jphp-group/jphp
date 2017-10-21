package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.PropertyEntity;

import static php.runtime.annotation.Reflection.*;


@Name("ReflectionClass")
@Signature(
        @Arg(value = "name", type = HintType.STRING, readOnly = true)
)
public class ReflectionClass extends Reflection {
    public final static int IS_IMPLICIT_ABSTRACT = 16;
    public final static int IS_EXPLICIT_ABSTRACT = 32;
    public final static int IS_FINAL = 64;

    protected ClassEntity entity;

    protected ReflectionClass(Environment env) {
        super(env);
    }

    public ReflectionClass(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public ReflectionClass setEntity(ClassEntity entity) {
        this.entity = entity;
        getProperties().put("name", new StringMemory(entity.getName()));

        return this;
    }

    @Signature(@Arg("argument"))
    public Memory __construct(Environment env, Memory... args) {
        Memory argument = args[0];
        if (argument.isObject()) {
            entity = argument.toValue(ObjectMemory.class).getReflection();
        } else {
            entity = env.fetchClass(argument.toString(), true);
            if (entity == null)
                exception(env, Messages.ERR_CLASS_NOT_FOUND.fetch(argument.toString()));
        }

        setEntity(entity);
        return Memory.NULL;
    }

    @Signature
    public Memory getName(Environment env, Memory... args) {
        return new StringMemory(entity.getName());
    }

    @Signature(@Arg("name"))
    public Memory getConstant(Environment env, Memory... args) {
        ConstantEntity constantEntity = entity.findConstant(args[0].toString());
        if (constantEntity == null)
            return Memory.FALSE;

        return constantEntity.getValue(env);
    }

    @Signature
    public Memory getConstants(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        for (ConstantEntity e : entity.getConstants()) {
            result.put(e.getName(), e.getValue(env));
        }
        return result.toConstant();
    }

    @Signature
    public Memory getReflectionConstants(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        for (ConstantEntity e : entity.getConstants()) {
            result.putAsKeyString(e.getName(), ObjectMemory.valueOf(new ReflectionClassConstant(env, e)));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getDefaultProperties(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        for (PropertyEntity e : entity.getStaticProperties()) {
            result.put(e.getName(), e.getDefaultValue(env));
        }

        for (PropertyEntity e : entity.getProperties()) {
            result.put(e.getName(), e.getDefaultValue(env));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getDocComment(Environment env, Memory... args) {
        if (entity.getDocComment() == null)
            return Memory.NULL;

        return new StringMemory(entity.getDocComment().toString());
    }

    @Signature
    public Memory getEndLine(Environment env, Memory... args) {
        return LongMemory.valueOf(entity.getTrace().getEndLine());
    }

    @Signature
    public Memory getExtension(Environment env, Memory... args) {
        if (entity.getExtension() == null)
            return Memory.NULL;

        ReflectionExtension extension = new ReflectionExtension(env);
        extension.setExtension(entity.getExtension());

        return new ObjectMemory(extension);
    }

    @Signature
    public Memory getExtensionName(Environment env, Memory... args) {
        if (entity.getExtension() == null)
            return Memory.FALSE;

        return new StringMemory(entity.getExtension().getName());
    }

    @Signature
    public Memory getFileName(Environment env, Memory... args) {
        if (entity.isInternal())
            return Memory.FALSE;

        return new StringMemory(entity.getTrace().getFileName());
    }

    @Signature
    public Memory getInterfaceNames(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        for (ClassEntity e : entity.getInterfaces().values()) {
            result.add(new StringMemory(e.getName()));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getInterfaces(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        ClassEntity classEntity = env.fetchClass("ReflectionClass");

        for (ClassEntity e : entity.getInterfaces().values()) {
            ReflectionClass cls = new ReflectionClass(env, classEntity);
            cls.setEntity(e);
            result.add(new ObjectMemory(cls));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getConstructor(Environment env, Memory... args) {
        return entity.methodConstruct == null
                ? Memory.NULL
                : new ObjectMemory(new ReflectionMethod(env, entity.methodConstruct));
    }

    @Signature(@Arg("name"))
    public Memory getMethod(Environment env, Memory... args) {
        MethodEntity m = entity.findMethod(args[0].toString().toLowerCase());
        if (m == null) {
            exception(env, Messages.ERR_METHOD_NOT_FOUND.fetch(entity.getName(), args[0]));
            return Memory.NULL;
        }

        ReflectionMethod r = new ReflectionMethod(env, m);

        return new ObjectMemory(r);
    }

    private boolean checkModifiers(MethodEntity e, int mod) {
        if (mod == -1)
            return true;

        if (e.isStatic() && (mod & ReflectionMethod.IS_STATIC) == ReflectionMethod.IS_STATIC)
            return true;

        if (e.isFinal() && (mod & ReflectionMethod.IS_FINAL) == ReflectionMethod.IS_FINAL)
            return true;

        if (e.isAbstract() && (mod & ReflectionMethod.IS_ABSTRACT) == ReflectionMethod.IS_ABSTRACT)
            return true;

        switch (e.getModifier()) {
            case PRIVATE:
                return (mod & ReflectionMethod.IS_PRIVATE) == ReflectionMethod.IS_PRIVATE;
            case PROTECTED:
                return (mod & ReflectionMethod.IS_PROTECTED) == ReflectionMethod.IS_PROTECTED;
            case PUBLIC:
                return (mod & ReflectionMethod.IS_PUBLIC) == ReflectionMethod.IS_PUBLIC;
        }

        return false;
    }

    @Signature(@Arg(value = "filter", optional = @Optional("NULL")))
    public Memory getMethods(Environment env, Memory... args) {
        int mod = args[0].isNull() ? -1 : args[0].toInteger();

        ArrayMemory result = new ArrayMemory();
        ClassEntity classEntity = env.fetchClass("ReflectionMethod");

        for (MethodEntity e : entity.getMethods().values()) {
            if (checkModifiers(e, mod)) {
                ReflectionMethod method = new ReflectionMethod(env, classEntity);
                method.setEntity(e);
                result.add(new ObjectMemory(method));
            }
        }

        return result.toConstant();
    }

    @Signature
    public Memory getModifiers(Environment env, Memory... args) {
        int mod = 0;
        if (entity.isFinal())
            mod |= IS_FINAL;
        if (entity.isAbstract())
            mod |= IS_EXPLICIT_ABSTRACT;

        return LongMemory.valueOf(mod);
    }

    @Signature
    public Memory getNamespaceName(Environment env, Memory... args) {
        if (!entity.isNamespace())
            return Memory.FALSE;
        return new StringMemory(entity.getNamespaceName());
    }

    @Signature
    public Memory getShortName(Environment env, Memory... args) {
        return new StringMemory(entity.getShortName());
    }

    @Signature
    public Memory isNamespace(Environment env, Memory... args) {
        return entity.isNamespace() ? Memory.TRUE : Memory.FALSE;
    }

    private boolean checkModifiers(PropertyEntity prop, int mod) {
        boolean add = mod == -1;
        if (!add) {
            switch (prop.getModifier()) {
                case PRIVATE:
                    add = (mod & ReflectionProperty.IS_PRIVATE) == ReflectionProperty.IS_PRIVATE;
                    break;
                case PROTECTED:
                    add = (mod & ReflectionProperty.IS_PROTECTED) == ReflectionProperty.IS_PROTECTED;
                    break;
                case PUBLIC:
                    add = (mod & ReflectionProperty.IS_PUBLIC) == ReflectionProperty.IS_PUBLIC;
                    break;
            }
        }
        return add;
    }

    @Signature(@Arg("name"))
    public Memory getProperty(Environment env, Memory... args) {
        String name = args[0].toString();
        PropertyEntity e = entity.findProperty(name);
        if (e == null)
            e = entity.findStaticProperty(name);

        if (e == null)
            return Memory.NULL;

        ClassEntity classEntity = env.fetchClass("ReflectionProperty");
        ReflectionProperty prop = new ReflectionProperty(env, classEntity);
        prop.setEntity(e);
        return new ObjectMemory(prop);
    }

    @Signature
    public Memory getStaticProperties(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        ClassEntity classEntity = env.fetchClass("ReflectionProperty");
        for (PropertyEntity e : entity.getStaticProperties()) {
            ReflectionProperty prop = new ReflectionProperty(env, classEntity);
            prop.setEntity(e);
            result.add(new ObjectMemory(prop));
        }

        return result.toConstant();
    }

    @Signature(@Arg("name"))
    public Memory getStaticPropertyValue(Environment env, Memory... args) {
        String name = args[0].toString();
        PropertyEntity e = entity.findStaticProperty(name);
        if (e == null) {
            exception(env, Messages.ERR_UNDEFINED_PROPERTY.fetch(entity.getName(), name));
            return Memory.NULL;
        }

        if (!e.isDefault())
            return Memory.FALSE;

        return e.getDefaultValue(env).toImmutable();
    }

    @Signature(@Arg(value = "filter", optional = @Optional("NULL")))
    public Memory getProperties(Environment env, Memory... args) {
        int mod = args[0].isNull() ? -1 : args[0].toInteger();

        ArrayMemory result = new ArrayMemory();
        ClassEntity classEntity = env.fetchClass("ReflectionProperty");

        if (mod == -1 || (mod & ReflectionProperty.IS_STATIC) == ReflectionProperty.IS_STATIC) {
            for (PropertyEntity e : entity.getStaticProperties()) {
                ReflectionProperty prop = new ReflectionProperty(env, classEntity);
                prop.setEntity(e);
                result.add(new ObjectMemory(prop));
            }
        }

        for (PropertyEntity e : entity.getProperties()) {
            if (checkModifiers(e, mod)) {
                ReflectionProperty prop = new ReflectionProperty(env, classEntity);
                prop.setEntity(e);
                result.add(new ObjectMemory(prop));
            }
        }

        return result.toConstant();
    }

    @Signature
    public Memory getParentClass(Environment env, Memory... args) {
        if (entity.getParent() == null)
            return Memory.NULL;

        ClassEntity classEntity = env.fetchClass("ReflectionClass");
        ReflectionClass result = new ReflectionClass(env, classEntity);
        result.setEntity(entity.getParent());
        return new ObjectMemory(result);
    }

    @Signature
    public Memory getTraits(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();

        for (ClassEntity el : entity.getTraits().values()) {
            result.add(new ReflectionClass(env).setEntity(el));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getTraitNames(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();

        for (ClassEntity el : entity.getTraits().values()) {
            result.add(el.getName());
        }

        return result.toConstant();
    }

    @Signature
    public Memory getStartLine(Environment env, Memory... args) {
        if (entity.isInternal() || entity.getTrace().isUnknown())
            return Memory.FALSE;

        return LongMemory.valueOf(entity.getTrace().getStartLine() + 1);
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args) {
        if (entity.isInternal() || entity.getTrace().isUnknown())
            return Memory.FALSE;

        return LongMemory.valueOf(entity.getTrace().getStartPosition() + 1);
    }

    @Signature(@Arg("name"))
    public Memory hasConstant(Environment env, Memory... args) {
        return entity.findConstant(args[0].toString()) != null ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("name"))
    public Memory hasMethod(Environment env, Memory... args) {
        MethodEntity method = entity.findMethod(args[0].toString().toLowerCase());
        return method == null
                || (method.isPrivate() && !method.getClazz().equals(entity)) ? Memory.FALSE : Memory.TRUE;
    }

    @Signature(@Arg("name"))
    public Memory hasProperty(Environment env, Memory... args) {
        PropertyEntity prop = entity.findProperty(args[0].toString());
        return prop == null
                || (prop.isPrivate() && !prop.getClazz().equals(entity)) ? Memory.FALSE : Memory.TRUE;
    }

    @Signature(@Arg("interface"))
    public Memory implementsInterface(Environment env, Memory... args) {
        String name = args[0].toString();
        ClassEntity e = env.fetchClass(name, true);
        if (!e.isInterface())
            exception(env, "Interface %s is a Class", name);

        if (!entity.isInstanceOf(e))
            return Memory.FALSE;

        return env.fetchClass(name).isInterface() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAbstract(Environment env, Memory... args) {
        return entity.isAbstract() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isCloneable(Environment env, Memory... args) {
        if (!entity.isClass())
            return Memory.FALSE;

        if (entity.methodMagicClone == null || entity.methodMagicClone.isPublic())
            return Memory.TRUE;
        else
            return Memory.FALSE;
    }

    @Signature
    public Memory isFinal(Environment env, Memory... args) {
        return entity.isFinal() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("object"))
    public Memory isInstance(Environment env, Memory... args) {
        if (args[0].isObject()) {
            return args[0].toValue(ObjectMemory.class).getReflection().isInstanceOf(entity)
                    ? Memory.TRUE : Memory.FALSE;
        } else
            return Memory.FALSE;
    }

    @Signature
    public Memory isInstantiable(Environment env, Memory... args) {
        if (entity.isClass() && !entity.isAbstract()) {
            return entity.methodConstruct == null || !entity.methodConstruct.isPrivate() ? Memory.TRUE : Memory.FALSE;
        } else
            return Memory.FALSE;
    }

    @Signature
    public Memory isInterface(Environment env, Memory... args) {
        return entity.isInterface() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isTrait(Environment env, Memory... args) {
        return entity.isTrait() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isInternal(Environment env, Memory... args) {
        return entity.isInternal() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isIterable(Environment env, Memory... args) {
        return entity.isInstanceOfLower("iterator") ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("class"))
    public Memory isSubclassOf(Environment env, Memory... args) {
        String name = args[0].toString().toLowerCase();
        return entity.isInstanceOf(name) && !entity.getLowerName().equals(name) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isUserDefined(Environment env, Memory... args) {
        return entity.isInternal() ? Memory.FALSE : Memory.TRUE;
    }

    @Signature
    public Memory newInstance(Environment env, Memory... args) throws Throwable {
        return new ObjectMemory(entity.newObject(env, env.trace(), true, args));
    }

    @Signature(@Arg(value = "args", type = HintType.ARRAY, optional = @Optional(type = HintType.ARRAY)))
    public Memory newInstanceArgs(Environment env, Memory... args) throws Throwable {
        if (args[0].isArray()) {
            return newInstance(env, args[0].toValue(ArrayMemory.class).values(true));
        } else
            return Memory.NULL;
    }

    @Signature
    public Memory newInstanceWithoutConstructor(Environment env, Memory... args) throws Throwable {
        return new ObjectMemory(entity.newObject(env, env.trace(), false, args));
    }

    @Signature({
            @Arg(value = "reflector", type = HintType.OBJECT),
            @Arg(value = "return", type = HintType.BOOLEAN, optional = @Optional(value = "", type = HintType.BOOLEAN))
    })
    public static Memory export(Environment env, Memory... args) {
        ReflectionClass e = new ReflectionClass(env, env.fetchClass("ReflectionClass"));
        if (args[1].toBoolean())
            return e.__toString(env);
        else
            env.echo(e.__toString(env));
        return Memory.NULL;
    }
}
