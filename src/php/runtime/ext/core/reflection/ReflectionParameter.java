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
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.helper.ClosureEntity;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionParameter")
@Signature(
    @Arg(value = "name", type = HintType.STRING, readOnly = true)
)
public class ReflectionParameter extends Reflection implements Reflector {
    private ParameterEntity entity;
    private FunctionEntity functionEntity;
    private ObjectMemory cachedFunction;
    private int position;

    public ReflectionParameter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({@Arg("function"), @Arg("parameter")})
    public Memory __construct(Environment env, Memory... args){
        ParameterEntity[] parameters = null;
        if (args[0].isClosure()){
            ClosureEntity tmp = (ClosureEntity)args[0].toValue(ObjectMemory.class).getReflection();
            parameters = tmp.parameters;
        } else if (args[0].isArray()){
            Invoker invoker = Invoker.valueOf(env, null, args[0]);
            if (invoker == null){
                exception(env, "%s does not exists", args[0].toString());
                return Memory.NULL;
            }
            parameters = invoker.getParameters();
        } else {
            FunctionEntity tmp = functionEntity = env.functionMap.get(args[0].toString());
            if (tmp == null){
                exception(env, "Function %s does not exist", args[0].toString());
                return Memory.NULL;
            }
            parameters = tmp.parameters;
        }
        entity = null;

        String name = args[1].toString();
        if (parameters != null){
            if (args[1].isNumber()){
                int index = args[1].toInteger();
                if (index >= 0 && index < parameters.length){
                    entity = parameters[index];
                    position = index;
                }
            } else {
                int i = 0;
                for(ParameterEntity e : parameters){
                    if (e.getName().equals(name)){
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

        getProperties().put("name", new StringMemory(entity.getName()));
        return Memory.NULL;
    }

    public void setEntity(ParameterEntity entity) {
        this.entity = entity;
    }

    public void setFunctionEntity(FunctionEntity functionEntity) {
        this.functionEntity = functionEntity;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Signature
    public Memory allowsNull(Environment env, Memory... args){
        if (entity.getType() == HintType.OBJECT && entity.getDefaultValue() == null)
            return Memory.FALSE;

        return Memory.TRUE;
    }

    @Signature
    public Memory canBePassedByValue(Environment env, Memory... args){
        return entity.canBePassedByValue() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getDefaultValue(Environment env, Memory... args){
        if (entity.getDefaultValue() == null) {
            exception(env, "Parameter has no default value");
            return Memory.NULL;
        }

        return entity.getDefaultValue().toImmutable(env, env.trace());
    }

    @Signature
    public Memory getDefaultValueConstantName(Environment env, Memory... args){
        Memory value = entity.getDefaultValue();
        if (value instanceof ConstantMemory)
            return new StringMemory(((ConstantMemory) value).getName());
        else if (value instanceof ClassConstantMemory){
            return new StringMemory(
                    ((ClassConstantMemory) value).getClassName()
                            + "::" +
                    ((ClassConstantMemory) value).getName()
            );
        } else
            return Memory.NULL;
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(entity.getName());
    }

    @Signature
    public Memory getPosition(Environment env, Memory... args){
        return LongMemory.valueOf(position);
    }

    @Signature
    public Memory isArray(Environment env, Memory... args){
        return entity.getType() == HintType.ARRAY ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isCallable(Environment env, Memory... args){
        return entity.getType() == HintType.CALLABLE ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isScalar(Environment env, Memory... args){
        return entity.getType() == HintType.SCALAR ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isString(Environment env, Memory... args){
        return entity.getType() == HintType.STRING ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isBoolean(Environment env, Memory... args){
        return entity.getType() == HintType.BOOLEAN ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isNumber(Environment env, Memory... args){
        return entity.getType() == HintType.NUMBER ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isInteger(Environment env, Memory... args){
        return entity.getType() == HintType.INT ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDouble(Environment env, Memory... args){
        return entity.getType() == HintType.DOUBLE ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isObject(Environment env, Memory... args){
        return entity.getType() == HintType.OBJECT ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getHintType(Environment env, Memory... args){
        return LongMemory.valueOf(entity.getType().ordinal());
    }

    @Signature
    public Memory isDefaultValueAvailable(Environment env, Memory... args){
        return entity.getDefaultValue() != null ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDefaultValueConstant(Environment env, Memory... args){
        return entity.getDefaultValue() instanceof ConstantMemory
                || entity.getDefaultValue() instanceof ClassConstantMemory ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isOptional(Environment env, Memory... args){
        return entity.isOptional() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPassedByReference(Environment env, Memory... args){
        return entity.isPassedByReference() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getDeclaringFunction(Environment env, Memory... args) throws Throwable {
        if (cachedFunction != null)
            return cachedFunction;

        if (functionEntity == null)
            return Memory.NULL;

        return cachedFunction = new ObjectMemory(
                env.fetchClass("ReflectionFunction").newObject(
                        env, env.trace(), new StringMemory(functionEntity.getName())
                )
        );
    }
}
