package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static php.runtime.annotation.Reflection.*;

@Name("php\\concurrent\\Future")
public class WrapFuture extends BaseObject {
    protected Future<Memory> future;

    public WrapFuture(Environment env, Future<Memory> future) {
        super(env);
        this.future = future;
    }

    public WrapFuture(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Signature
    public Memory isCancelled(Environment env, Memory... args){
        return future.isCancelled() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isDone(Environment env, Memory... args){
        return future.isDone() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("mayInterruptIfRunning"))
    public Memory cancel(Environment env, Memory... args){
        return future.cancel(args[0].toBoolean()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "timeout", optional = @Optional("NULL")))
    public Memory get(Environment env, Memory... args) throws ExecutionException, InterruptedException,
            TimeoutException {
        if (args[0].isNull())
            return future.get();
        else
            return future.get(args[0].toLong(), TimeUnit.MILLISECONDS);
    }
}
