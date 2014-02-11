package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.ConcurrentEnvironment;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\Environment")
public class WrapEnvironment extends BaseObject {
    protected Environment environment;
    protected Invoker onMessage;

    public WrapEnvironment(Environment env, Environment wrapEnv) {
        super(env);
        setEnvironment(wrapEnv);
    }

    public WrapEnvironment(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Signature({
            @Arg(value = "parent", typeClass = "php\\lang\\Environment", optional = @Optional("NULL")),
            @Arg(value = "concurrent", optional = @Optional(value = "", type = HintType.BOOLEAN))
    })
    public Memory __construct(Environment env, Memory... args){
        if (args[0].isNull()) {
            if (args[1].toBoolean())
                setEnvironment(new ConcurrentEnvironment(env.scope, env.getDefaultBuffer().getOutput()));
            else
                setEnvironment(new Environment(env.scope, env.getDefaultBuffer().getOutput()));
        } else {
            if (args[1].toBoolean())
                setEnvironment(new ConcurrentEnvironment(args[0].toObject(WrapEnvironment.class).getEnvironment()));
            else
                setEnvironment(new Environment(args[0].toObject(WrapEnvironment.class).getEnvironment()));
        }

        return Memory.NULL;
    }

    @Signature(@Arg(value = "runnable"))
    public Memory execute(Environment env, Memory... args) throws Throwable {
        Invoker invoker = Invoker.valueOf(this.environment, null, args[0]);
        if (invoker == null) {
            env.exception("Argument 1 must be callable in environment");
            return Memory.NULL;
        }

        return invoker.call();
    }

    @Signature(@Arg("className"))
    public Memory exportClass(final Environment env, Memory... args) throws Throwable {
        ClassEntity classEntity = environment.fetchClass(args[0].toString());
        if (classEntity == null) {
            env.exception(Messages.ERR_CLASS_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        synchronized (env.classMap){
            if (env.classMap.containsKey(classEntity.getLowerName()))
                env.exception("Class '%s' already registered", classEntity.getName());

            env.classMap.put(classEntity.getLowerName(), classEntity);
            classEntity.initEnvironment(env);
        }
        return Memory.NULL;
    }

    @Signature(@Arg("className"))
    public Memory importClass(Environment env, Memory... args) throws Throwable {
        ClassEntity classEntity = env.fetchClass(args[0].toString());
        if (classEntity == null) {
            env.exception(Messages.ERR_CLASS_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        synchronized (environment.classMap){
            if (environment.classMap.containsKey(classEntity.getLowerName()))
                env.exception("Class '%s' already registered", classEntity.getName());

            environment.classMap.put(classEntity.getLowerName(), classEntity);
            classEntity.initEnvironment(environment);
        }
        return Memory.NULL;
    }

    @Signature(@Arg("functionName"))
    public Memory importFunction(Environment env, Memory... args){
        FunctionEntity functionEntity = env.functionMap.get(args[0].toString().toLowerCase());
        if (functionEntity == null) {
            env.exception(Messages.ERR_FUNCTION_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        synchronized (environment.functionMap){
            if (environment.functionMap.containsKey(functionEntity.getLowerName()))
                env.exception("Function '%s' already registered", functionEntity.getName());

            environment.functionMap.put(functionEntity.getLowerName(), functionEntity);
        }

        return Memory.NULL;
    }

    @Signature(@Arg("functionName"))
    public Memory exportFunction(Environment env, Memory... args){
        FunctionEntity functionEntity = environment.functionMap.get(args[0].toString().toLowerCase());
        if (functionEntity == null) {
            env.exception(Messages.ERR_FUNCTION_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        synchronized (env.functionMap){
            if (env.functionMap.containsKey(functionEntity.getLowerName()))
                env.exception("Function '%s' already registered", functionEntity.getName());

            env.functionMap.put(functionEntity.getLowerName(), functionEntity);
        }

        return Memory.NULL;
    }

    @Signature({
            @Arg("name"), @Arg("value"),
            @Arg(value = "caseSensitive", optional = @Optional(value = "true", type = HintType.BOOLEAN))
    })
    public Memory defineConstant(Environment env, Memory... args){
        Memory val = args[1].toValue();
        if (val.isArray() || val.isObject())
            env.exception("Argument 2 must be a scalar value");

        if (!environment.defineConstant(args[0].toString(), val, args[2].toBoolean()))
            env.exception("Constant '%s' already registered", args[0]);

        return Memory.NULL;
    }

    @Signature(@Arg("callback"))
    public Memory onMessage(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(this.environment, null, args[0]);
        if (invoker == null) {
            env.exception("Argument 1 must be callable in environment");
            return Memory.NULL;
        }
        onMessage = invoker;
        return Memory.NULL;
    }

    @Signature(@Arg("message"))
    public Memory sendMessage(Environment env, Memory... args) throws Throwable {
        if (onMessage == null) {
            env.exception("Environment cannot receive messages, onMessage callback is NULL");
            return Memory.NULL;
        }

        return onMessage.call(args);
    }

    @Signature
    public Memory __destruct(Environment env, Memory... args) throws Throwable {
        environment.doFinal();
        return Memory.NULL;
    }

    @Signature
    public static Memory current(Environment env, Memory... args){
        return new ObjectMemory(new WrapEnvironment(env, env));
    }
}
