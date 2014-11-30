package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.InvokeHelper;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.Closure;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.CompileFunctionEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.support.AbstractFunctionEntity;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionFunction")
@Signature(
        @Arg(value = "name", type = HintType.STRING, readOnly = true)
)
public class ReflectionFunction extends ReflectionFunctionAbstract {
    public final static int IS_DEPRECATED = 262144;

    protected Closure closure;
    protected ClosureEntity closureEntity;
    protected FunctionEntity functionEntity;
    protected ArrayMemory cachedParameters;

    public ReflectionFunction(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setFunctionEntity(FunctionEntity functionEntity) {
        this.functionEntity = functionEntity;
        getProperties().put("name", new StringMemory(functionEntity.getName()));
    }

    @Signature(@Arg(value = "name"))
    public Memory __construct(Environment env, Memory... args){
        Memory name = args[0].toValue();
        if (name.isClosure()){
            closure = (Closure)name.toValue(ObjectMemory.class).value;
            closureEntity = (ClosureEntity)closure.getReflection();
        } else {
            functionEntity = env.fetchFunction(name.toString());
            if (functionEntity == null)
                exception(env, "Function %s does not exist", name.toString());

            setFunctionEntity(functionEntity);
        }

        return Memory.NULL;
    }

    @Override
    protected AbstractFunctionEntity getEntity() {
        return functionEntity;
    }

    @Override
    protected ClosureEntity getClosureEntity() {
        return closureEntity;
    }

    @Override
    protected IObject getInstance() {
        return closure;
    }

    @Override
    @Signature
    public Memory getNumberOfParameters(Environment env, Memory... args) {
        if (functionEntity instanceof CompileFunctionEntity)
            return LongMemory.valueOf(((CompileFunctionEntity) functionEntity).getCompileFunction().getMaxArgs());
        else
            return super.getNumberOfParameters(env, args);
    }

    @Override
    @Signature
    public Memory getNumberOfRequiredParameters(Environment env, Memory... args) {
        if (functionEntity instanceof CompileFunctionEntity)
            return LongMemory.valueOf(((CompileFunctionEntity) functionEntity).getCompileFunction().getMinArgs());
        else
            return super.getNumberOfRequiredParameters(env, args);
    }

    @Signature
    public Memory isDisabled(Environment env, Memory... args){
        if (closure != null)
            return Memory.FALSE;

        return Memory.FALSE;
    }

    @Signature
    public Memory getClosure(final Environment env, Memory... args) throws Throwable {
        if (closure != null)
            return new ObjectMemory(closure);

        return new ObjectMemory(functionEntity.getClosure(env));
    }

    @Signature
    public Memory invoke(Environment env, Memory... args) throws Throwable {
        if (closure != null)
            return ObjectInvokeHelper.invokeMethod(
                    closure, closureEntity.methodMagicInvoke, env, env.peekCall(0).trace, args, true
            );
        else {
            return InvokeHelper.call(env, env.peekCall(0).trace, functionEntity, args);
        }
    }

    @Signature(@Arg(value = "args", type = HintType.ARRAY))
    public Memory invokeArgs(Environment env, Memory... args) throws Throwable {
        ArrayMemory value = args[0].toValue(ArrayMemory.class);

        Memory[] passed = value.values();
        return invoke(env, passed);
    }

    @Override
    @Signature
    public Memory getParameters(Environment env, Memory... args) {
        if (cachedParameters != null)
            return cachedParameters;

        if (functionEntity instanceof CompileFunctionEntity)
            exception(env, "Cannot get parameters for internal function %s()", functionEntity.getName());

        ParameterEntity[] parameters = closureEntity == null ? functionEntity.getParameters() : closureEntity.parameters;

        ClassEntity entity = env.fetchClass("ReflectionParameter");
        ArrayMemory result = new ArrayMemory();

        int i = 0;
        for(ParameterEntity param : parameters){
            ReflectionParameter e = new ReflectionParameter(env, entity);
            e.setEntity(param);
            e.setFunctionEntity(functionEntity);
            e.setPosition(i);
            i++;

            result.add(new ObjectMemory(e));
        }

        return cachedParameters = result;
    }

    @Signature({
            @Arg(value = "reflector", type = HintType.OBJECT),
            @Arg(value = "return", type = HintType.BOOLEAN, optional = @Optional(value = "", type = HintType.BOOLEAN))
    })
    public static Memory export(Environment env, Memory... args){
        ReflectionFunction e = new ReflectionFunction(env, env.fetchClass("ReflectionFunction"));
        if (args[1].toBoolean())
            return e.__toString(env);
        else
            env.echo(e.__toString(env));
        return Memory.NULL;
    }
}
