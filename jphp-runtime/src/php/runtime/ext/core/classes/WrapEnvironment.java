package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.*;
import php.runtime.env.Package;
import php.runtime.ext.support.Extension;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;

import java.util.Set;

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

    @Signature(@Arg(value = "sourceMap", nativeType = WrapSourceMap.class))
    public Memory registerSourceMap(Environment env, Memory... args) {
        this.environment.registerSourceMap(args[0].toObject(WrapSourceMap.class).getWrappedObject());
        return Memory.NULL;
    }

    @Signature(@Arg(value = "sourceMap", nativeType = WrapSourceMap.class))
    public Memory unregisterSourceMap(Environment env, Memory... args) {
        this.environment.unregisterSourceMap(args[0].toObject(WrapSourceMap.class).getWrappedObject());
        return Memory.NULL;
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
    public Memory onOutput(Environment env, Memory... args) {
        if (args[0].isNull()) {
            Invoker invoker = Invoker.valueOf(this.environment, null, args[0]);

            if (invoker == null) {
                env.exception("Argument 1 must be callable in environment");
                return Memory.NULL;
            }

            invoker.setTrace(env.trace());
            this.environment.getDefaultBuffer().setCallback(args[0], invoker);
        } else {
            this.environment.getDefaultBuffer().setCallback(null);
        }

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

    @Signature(@Arg("path"))
    public Memory findModule(Environment env,  Memory... args) throws Throwable {
        ModuleManager moduleManager = this.environment.getModuleManager();

        boolean hasModule = moduleManager.hasModule(args[0].toString());

        if (hasModule) {
            return ObjectMemory.valueOf(new WrapModule(env, moduleManager.fetchModule(args[0].toString())));
        }

        return Memory.NULL;
    }

    @Signature({
        @Arg("name"), @Arg(value = "package", nativeType = WrapPackage.class)
    })
    public Memory setPackage(Environment env, Memory... args) {
        PackageManager packageManager = this.environment.getPackageManager();

        packageManager.set(args[0].toString(), args[1].toObject(WrapPackage.class).getPackage());

        return Memory.NULL;
    }

    @Signature
    public Memory getPackages(Environment env, Memory... args) {
        PackageManager packageManager = this.environment.getPackageManager();

        ArrayMemory result = new ArrayMemory();

        for (String name : packageManager.names()) {
            result.add(new WrapPackage(env, packageManager.fetch(name)));
        }

        return result.toConstant();
    }

    @Signature(@Arg("name"))
    public Memory hasPackage(Environment env, Memory... args) {
        return TrueMemory.valueOf(this.environment.getPackageManager().has(args[0].toString()));
    }

    @Signature(@Arg("name"))
    public Memory getPackage(Environment env, Memory... args) {
        PackageManager packageManager = this.environment.getPackageManager();

        if (packageManager.has(args[0].toString())) {
            Package aPackage = packageManager.tryFind(args[0].toString(), env.trace());

            if (aPackage == null) {
                return Memory.NULL;
            }

            return ObjectMemory.valueOf(new WrapPackage(env, aPackage));
        } else {
            return Memory.NULL;
        }
    }

    @Signature
    public Memory __destruct(Environment env, Memory... args) throws Throwable {
        environment.doFinal();
        return Memory.NULL;
    }

    @Signature(@Arg("extensionClass"))
    public Memory registerExtension(Environment env, Memory... args) {
        Extension extension = this.environment.scope.getExtension(args[0].toString());

        if (extension != null) {
            env.exception("Extension '%s' already registered", extension.getName());
        }

        this.environment.scope.registerExtension(args[0].toString());
        return Memory.NULL;
    }

    @Signature({
            @Arg("name"), @Arg(value = "value", optional = @Optional("null"))
    })
    public Memory addSuperGlobal(Environment env, Memory... args) {
        if (hasSuperGlobal(env, args[0]).toBoolean()) {
            environment.exception("Super-global variable $%s already exists", args[0]);
        }

        environment.getScope().superGlobals.add(args[0].toString());
        environment.getGlobals().putAsKeyString(args[0].toString(), args[1].toImmutable());
        return Memory.UNDEFINED;
    }

    @Signature(@Arg("name"))
    public Memory hasSuperGlobal(Environment env, Memory... args) {
        return environment.getScope().superGlobals.contains(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Set<String> getSuperGlobals() {
        return environment.getScope().superGlobals;
    }

    @Signature(@Arg("name"))
    public Memory getGlobal(Environment env, Memory... args) {
        return environment.getGlobals().valueOfIndex(args[0]).toImmutable();
    }

    @Signature(@Arg("name"))
    public Memory hasGlobal(Environment env, Memory... args) {
        return environment.getGlobals().containsKey(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("name"), @Arg("value")})
    public Memory setGlobal(Environment env, Memory... args) {
        return environment.getOrCreateGlobal(args[0].toString()).assign(args[1]);
    }

    @Signature
    public Memory getGlobals(Environment env, Memory... args) {
        return environment.getGlobals().toImmutable();
    }

    @Signature
    public static Memory current(Environment env, Memory... args){
        return new ObjectMemory(new WrapEnvironment(env, env));
    }
}
