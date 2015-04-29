package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.CompileScope;
import php.runtime.env.ConcurrentEnvironment;
import php.runtime.env.Environment;
import php.runtime.env.SplClassLoader;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\Environment")
public class WrapEnvironment extends BaseObject {
    public final static int CONCURRENT = 1;
    public final static int HOT_RELOAD = 2;

    protected Environment environment;
    protected Invoker onMessage;

    public WrapEnvironment(Environment env, Environment wrapEnv) {
        super(env);
        setEnvironment(wrapEnv);
    }

    public WrapEnvironment(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Environment getWrapEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Signature({
            @Arg(value = "parent", nativeType = WrapEnvironment.class, optional = @Optional("NULL")),
            @Arg(value = "flags", optional = @Optional(value = "0", type = HintType.INT))
    })
    public Memory __construct(Environment env, Memory... args){
        CompileScope scope = env.scope;
        int flags = args[1].toInteger();

        boolean hotReload  = (flags & HOT_RELOAD) == HOT_RELOAD;
        boolean concurrent = (flags & CONCURRENT) == CONCURRENT;

        if (hotReload) {
            scope = new CompileScope(scope);
        }

        if (args[0].isNull()) {
            if (concurrent)
                setEnvironment(new ConcurrentEnvironment(scope, env.getDefaultBuffer().getOutput()));
            else
                setEnvironment(new Environment(scope, env.getDefaultBuffer().getOutput()));
        } else {
            if (hotReload)
                env.exception("Environment cannot be hot-reloadable with parent");

            if (concurrent)
                setEnvironment(new ConcurrentEnvironment(args[0].toObject(WrapEnvironment.class).getWrapEnvironment()));
            else
                setEnvironment(new Environment(args[0].toObject(WrapEnvironment.class).getWrapEnvironment()));
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

        invoker.setTrace(env.trace());
        return invoker.call();
    }

    @Signature(@Arg("className"))
    public Memory exportClass(final Environment env, Memory... args) throws Throwable {
        ClassEntity classEntity = environment.fetchClass(args[0].toString());
        if (classEntity == null) {
            env.exception(Messages.ERR_CLASS_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        env.registerClass(classEntity);
        return Memory.NULL;
    }

    @Signature
    public Memory importAutoLoaders(Environment env, Memory... args) {
        for (SplClassLoader loader : env.getClassLoaders()) {
            environment.registerAutoloader(loader.forEnvironment(environment), false);
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

        environment.registerClass(classEntity);
        return Memory.NULL;
    }

    @Signature(@Arg("functionName"))
    public Memory importFunction(Environment env, Memory... args){
        FunctionEntity functionEntity = env.fetchFunction(args[0].toString());
        if (functionEntity == null) {
            env.exception(Messages.ERR_FUNCTION_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        environment.registerFunction(functionEntity);
        return Memory.NULL;
    }

    @Signature(@Arg("functionName"))
    public Memory exportFunction(Environment env, Memory... args){
        FunctionEntity functionEntity = environment.fetchFunction(args[0].toString());
        if (functionEntity == null) {
            env.exception(Messages.ERR_FUNCTION_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        }

        env.registerFunction(functionEntity);
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
