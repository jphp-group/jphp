package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.helper.ClassConstantMemory;
import php.runtime.memory.helper.ConstantMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.support.AbstractFunctionEntity;
import php.runtime.reflection.support.TypeChecker;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionParameter")
@Signature(
        @Arg(value = "name", type = HintType.STRING, readOnly = true)
)
public class ReflectionParameter extends Reflection implements Reflector {
    private ParameterEntity entity;
    private AbstractFunctionEntity functionEntity;
    private ObjectMemory cachedFunction;
    private int position;

    public ReflectionParameter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({@Arg("function"), @Arg("parameter")})
    public Memory __construct(Environment env, Memory... args) {
        ParameterEntity[] parameters = null;
        if (args[0].isClosure()) {
            ClosureEntity tmp = (ClosureEntity) args[0].toValue(ObjectMemory.class).getReflection();
            parameters = tmp.parameters;
        } else if (args[0].isArray()) {
            Invoker invoker = Invoker.valueOf(env, null, args[0]);
            if (invoker == null) {
                exception(env, "%s does not exists", args[0].toString());
                return Memory.NULL;
            }
            parameters = invoker.getParameters();
        } else {
            String name = args[0].toString();
            if (name.contains("::")) {
                Invoker invoker = Invoker.valueOf(env, null, args[0]);
                if (invoker == null) {
                    exception(env, "%s does not exists", args[0].toString());
                    return Memory.NULL;
                }
                parameters = invoker.getParameters();
            } else {
                FunctionEntity tmp = env.fetchFunction(name);
                functionEntity = tmp;
                if (tmp == null) {
                    exception(env, "Function %s does not exist", args[0].toString());
                    return Memory.NULL;
                }
                if (tmp.isInternal()) {
                    exception(env, "%s(): ReflectionParameter does not support internal functions", tmp.getName());
                    return Memory.NULL;
                }
                parameters = tmp.getParameters();
            }
        }
        entity = null;

        String name = args[1].toString();
        if (parameters != null) {
            if (args[1].isNumber()) {
                int index = args[1].toInteger();
                if (index >= 0 && index < parameters.length) {
                    entity = parameters[index];
                    position = index;
                }
            } else {
                int i = 0;
                for (ParameterEntity e : parameters) {
                    if (e.getName().equals(name)) {
                        entity = e;
                        position = i;
                        break;
                    }
                    i++;
                }
            }
        }
        if (entity == null)
            exception(env, "Parameter %s does not exist", name);

        setEntity(entity);
        return Memory.NULL;
    }

    public void setEntity(ParameterEntity entity) {
        this.entity = entity;
        getProperties().put("name", new StringMemory(entity.getName()));
    }

    public void setFunctionEntity(AbstractFunctionEntity functionEntity) {
        this.functionEntity = functionEntity;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Signature
    public Memory allowsNull(Environment env, Memory... args) {
        return entity.isNullableOrDefaultNull() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory hasType(Environment env, Memory... args) {
        TypeChecker typeChecker = entity.getTypeChecker();
        return typeChecker != null ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getType(Environment env, Memory... args) {
        TypeChecker typeChecker = entity.getTypeChecker();

        if (typeChecker == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf(new ReflectionType(
                env, typeChecker.getSignature(), entity.isNullableOrDefaultNull(), typeChecker.isBuiltin()
        ));
    }

    @Signature
    public Memory canBePassedByValue(Environment env, Memory... args) {
        return entity.canBePassedByValue() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getDefaultValue(Environment env, Memory... args) {
        if (entity.getDefaultValue() == null) {
            exception(env, "Parameter has no default value");
            return Memory.NULL;
        }

        return entity.getDefaultValue().toImmutable(env, env.trace());
    }

    @Signature
    public Memory getDefaultValueConstantName(Environment env, Memory... args) {
        if (entity.getDefaultValueConstName() == null)
            return Memory.FALSE;
        else
            return new StringMemory(entity.getDefaultValueConstName());
    }

    @Signature
    public Memory getName(Environment env, Memory... args) {
        return new StringMemory(entity.getName());
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args) {
        return LongMemory.valueOf(position);
    }

    @Signature
    public Memory isArray(Environment env, Memory... args) {
        return entity.getType() == HintType.ARRAY ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isCallable(Environment env, Memory... args) {
        return entity.getType() == HintType.CALLABLE ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isString(Environment env, Memory... args) {
        return entity.getType() == HintType.STRING ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isBoolean(Environment env, Memory... args) {
        return entity.getType() == HintType.BOOLEAN ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isNumber(Environment env, Memory... args) {
        return entity.getType() == HintType.NUMBER ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isInteger(Environment env, Memory... args) {
        return entity.getType() == HintType.INT ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDouble(Environment env, Memory... args) {
        return entity.getType() == HintType.DOUBLE ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isObject(Environment env, Memory... args) {
        return entity.getType() == HintType.OBJECT ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getHintType(Environment env, Memory... args) {
        return LongMemory.valueOf(entity.getType().ordinal());
    }

    @Signature
    public Memory isDefaultValueAvailable(Environment env, Memory... args) {
        return entity.getDefaultValue() != null ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDefaultValueConstant(Environment env, Memory... args) {
        return entity.getDefaultValue() instanceof ConstantMemory
                || entity.getDefaultValue() instanceof ClassConstantMemory ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isOptional(Environment env, Memory... args) {
        return entity.isOptional() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPassedByReference(Environment env, Memory... args) {
        return entity.isPassedByReference() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    @Name("getClass")
    public Memory _getClass(Environment env, Memory... args) {
        if (entity.getTypeClass() == null)
            return Memory.NULL;

        ClassEntity entity = env.fetchClass(this.entity.getTypeClass(), this.entity.getTypeClassLower(), true);
        if (entity == null)
            return Memory.NULL;

        ClassEntity classEntity = env.fetchClass("ReflectionClass");
        ReflectionClass r = new ReflectionClass(env, classEntity);
        r.setEntity(entity);

        return new ObjectMemory(r);
    }

    @Signature
    public Memory getDeclaringClass(Environment env, Memory... args) {
        if (functionEntity == null)
            return Memory.NULL;

        if (functionEntity instanceof FunctionEntity)
            return Memory.NULL;

        MethodEntity method = (MethodEntity) functionEntity;

        ClassEntity classEntity = env.fetchClass("ReflectionClass");
        ReflectionClass r = new ReflectionClass(env, classEntity);
        r.setEntity(method.getClazz());

        return new ObjectMemory(r);
    }

    @Signature
    public Memory getDeclaringFunction(Environment env, Memory... args) throws Throwable {
        if (cachedFunction != null)
            return cachedFunction;

        if (functionEntity == null)
            return Memory.NULL;

        if (functionEntity instanceof FunctionEntity) {
            ClassEntity classEntity = env.fetchClass("ReflectionFunction");
            ReflectionFunction e = new ReflectionFunction(env, classEntity);
            e.setFunctionEntity((FunctionEntity) functionEntity);
            return cachedFunction = new ObjectMemory(e);
        } else {
            ClassEntity classEntity = env.fetchClass("ReflectionMethod");
            ReflectionMethod e = new ReflectionMethod(env, classEntity);
            e.setEntity((MethodEntity) functionEntity);
            return cachedFunction = new ObjectMemory(e);
        }
    }
}
