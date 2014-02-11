package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.*;

import static php.runtime.annotation.Reflection.*;

@Name("php\\concurrent\\ExecutorService")
public class WrapExecutorService extends BaseObject {
    protected ExecutorService service;

    public WrapExecutorService(Environment env, ExecutorService service) {
        super(env);
        this.service = service;
    }

    public WrapExecutorService(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public ExecutorService getService() {
        return service;
    }

    @Signature
    private Memory __construct(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Signature({
            @Arg("runnable"),
            @Arg(value = "env", typeClass = "php\\lang\\Environment", optional = @Optional("NULL"))
    })
    public Memory execute(Environment env, Memory... args){
        Environment _env = env;
        if (!args[1].isNull())
            _env = args[1].toObject(WrapEnvironment.class).getEnvironment();

        final Invoker invoker = Invoker.valueOf(_env, null, args[0]);
        if (invoker == null){
            env.exception("Argument 1 must be callable for passed environment");
            return Memory.NULL;
        }

        service.execute(new Runnable() {
            @Override
            public void run() {
                invoker.callNoThrow();
            }
        });
        return Memory.NULL;
    }

    private ScheduledExecutorService getScheduledExecutorService(Environment env){
        if (!(service instanceof ScheduledExecutorService)){
            env.exception("Unsupported operation for non-scheduled executor service");
            return null;
        }
        return (ScheduledExecutorService) service;
    }

    @Signature
    public Memory isScheduled(Environment env, Memory... args){
        return service instanceof ScheduledExecutorService ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({
            @Arg("runnable"),
            @Arg(value = "env", typeClass = "php\\lang\\Environment", optional = @Optional("NULL"))
    })
    public Memory submit(Environment env, Memory... args){
        final Environment _env = args[1].isNull()
                ? env
                : args[1].toObject(WrapEnvironment.class).getEnvironment();
        final Invoker invoker = Invoker.valueOf(_env, null, args[0]);
        if (invoker == null) {
            env.exception("Argument 1 must be callable in passed environment");
            return Memory.NULL;
        }

        Future<Memory> future = service.submit(new Callable<Memory>() {
            @Override
            public Memory call() throws Exception {
                return invoker.callNoThrow();
            }
        });
        return new ObjectMemory(new WrapFuture(env, future));
    }

    @Signature({
            @Arg("runnable"),
            @Arg("delay"),
            @Arg(value = "env", typeClass = "php\\lang\\Environment", optional = @Optional("NULL"))
    })
    public Memory schedule(Environment env, Memory... args){
        final Environment _env = args[2].isNull()
                ? env
                : args[2].toObject(WrapEnvironment.class).getEnvironment();
        final Invoker invoker = Invoker.valueOf(_env, null, args[0]);
        if (invoker == null) {
            env.exception("Argument 1 must be callable in passed environment");
            return Memory.NULL;
        }

        ScheduledFuture<Memory> future = getScheduledExecutorService(env).schedule(new Callable<Memory>() {
            @Override
            public Memory call() throws Exception {
                return invoker.callNoThrow();
            }
        }, args[1].toLong(), TimeUnit.MILLISECONDS);

        return new ObjectMemory(new WrapFuture(env, future));
    }

    @Signature
    public Memory shutdown(Environment env, Memory... args){
        service.shutdown();
        return Memory.NULL;
    }

    @Signature
    public Memory shutdownNow(Environment env, Memory... args){
        service.shutdownNow();
        return Memory.NULL;
    }

    @Signature
    public Memory isShutdown(Environment env, Memory... args){
        return service.isShutdown() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isTerminated(Environment env, Memory... args){
        return service.isTerminated() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("timeout"))
    public Memory awaitTermination(Environment env, Memory... args) throws InterruptedException {
        return service.awaitTermination(args[0].toLong(), TimeUnit.MILLISECONDS) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("max"))
    public static Memory newFixedThreadPool(Environment env, Memory... args){
        return new ObjectMemory(new WrapExecutorService(env,
                Executors.newFixedThreadPool(args[0].toInteger())
        ));
    }

    @Signature
    public static Memory newCachedThreadPool(Environment env, Memory... args){
        return new ObjectMemory(new WrapExecutorService(env,
                Executors.newCachedThreadPool()
        ));
    }

    @Signature
    public static Memory newSingleThreadExecutor(Environment env, Memory... args){
        return new ObjectMemory(new WrapExecutorService(env,
                Executors.newSingleThreadExecutor()
        ));
    }

    @Signature(@Arg("corePoolSize"))
    public static Memory newScheduledThreadPool(Environment env, Memory... args){
        return new ObjectMemory(new WrapExecutorService(env,
                Executors.newScheduledThreadPool(args[0].toInteger())
        ));
    }
}
