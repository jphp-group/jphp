package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
}
