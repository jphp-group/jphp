package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.CustomErrorException;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.*;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\ThreadPool")
public class WrapThreadPool extends BaseObject {
    protected ExecutorService service;

    public WrapThreadPool(Environment env, ExecutorService service) {
        super(env);
        this.service = service;
    }

    public WrapThreadPool(Environment env, ClassEntity clazz) {
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
            @Arg(value = "runnable", type = HintType.CALLABLE),
            @Arg(value = "env", nativeType = WrapEnvironment.class, optional = @Optional("NULL"))
    })
    public Memory execute(final Environment env, Memory... args){
        Environment _env = env;

        if (!args[1].isNull()) {
            _env = args[1].toObject(WrapEnvironment.class).getWrapEnvironment();
        }

        final Invoker invoker = Invoker.valueOf(_env, null, args[0]);

        invoker.setTrace(env.trace());
        final Environment final_env = _env;

        service.execute(() -> {
            Environment.addThreadSupport(final_env);
            invoker.callNoThrow();
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

    private Callable<Memory> makeCallable(Environment env, Invoker invoker) {
        return () -> {
            try {
                Environment.addThreadSupport(env);

                return invoker.call();
            } catch (Exception throwable) {
                if (throwable instanceof CriticalException) {
                    throwable.printStackTrace();
                }

                if (throwable instanceof CustomErrorException) {
                    CustomErrorException error = (CustomErrorException) throwable;
                    env.error(error.getType(), error.getMessage());
                } else {
                    env.catchUncaught(throwable);
                }

                return Memory.NULL;
            } catch (Throwable throwable) {
                env.wrapThrow(throwable);
                return Memory.NULL;
            }
        };
    }

    @Signature({
            @Arg(value = "runnable", type = HintType.CALLABLE),
            @Arg(value = "env", typeClass = "php\\lang\\Environment", optional = @Optional("NULL"))
    })
    public Memory submit(Environment env, Memory... args){
        final Environment _env = args[1].isNull()
                ? env
                : args[1].toObject(WrapEnvironment.class).getWrapEnvironment();

        final Invoker invoker = Invoker.valueOf(_env, null, args[0]);

        Future<Memory> future = service.submit(makeCallable(env, invoker));

        return new ObjectMemory(new WrapFuture(env, future));
    }

    @Signature({
            @Arg(value = "runnable", type = HintType.CALLABLE),
            @Arg("delay"),
            @Arg(value = "env", typeClass = "php\\lang\\Environment", optional = @Optional("NULL"))
    })
    public Memory schedule(final Environment env, Memory... args){
        final Environment _env = args[2].isNull()
                ? env
                : args[2].toObject(WrapEnvironment.class).getWrapEnvironment();

        final Invoker invoker = Invoker.valueOf(_env, null, args[0]);

        ScheduledFuture<Memory> future = getScheduledExecutorService(env)
                .schedule(makeCallable(env, invoker), args[1].toLong(), TimeUnit.MILLISECONDS);

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
    public static Memory createFixed(Environment env, Memory... args){
        return new ObjectMemory(new WrapThreadPool(env,
                Executors.newFixedThreadPool(args[0].toInteger())
        ));
    }

    @Signature({
            @Arg("corePoolSize"),
            @Arg("maxPoolSize"),
            @Arg(value = "keepAliveTime", optional = @Optional("0"))
    })
    public static Memory create(Environment env, Memory... args){
        int nThreads = args[0].toInteger();
        int nMaxThreads = args[1].toInteger();
        long keepAliveTime = args[2].toLong();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                nThreads, nMaxThreads, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()
        );

        return new ObjectMemory(new WrapThreadPool(env, executor));
    }

    @Signature
    public static Memory createCached(Environment env, Memory... args){
        return new ObjectMemory(new WrapThreadPool(env,
                Executors.newCachedThreadPool()
        ));
    }

    @Signature
    public static Memory createSingle(Environment env, Memory... args){
        return new ObjectMemory(new WrapThreadPool(env,
                Executors.newSingleThreadExecutor()
        ));
    }

    @Signature(@Arg("corePoolSize"))
    public static Memory createScheduled(Environment env, Memory... args){
        return new ObjectMemory(new WrapThreadPool(env,
                Executors.newScheduledThreadPool(args[0].toInteger())
        ));
    }
}
