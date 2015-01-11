package php.runtime.ext.core.classes.concurent;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Name(CoreExtension.NAMESPACE + "concurrent\\Lock")
public class WrapLock extends BaseWrapper<Lock> {
    interface WrappedInterface {
        void lock();
        void lockInterruptibly();

        boolean tryLock();

        void unlock();
    }

    public WrapLock(Environment env, Lock wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapLock(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "fair", optional = @Reflection.Optional("false"))
    })
    public Memory __construct(Environment env, Memory... args) {
        __wrappedObject = new ReentrantLock(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    public boolean tryLock(long timeout) throws InterruptedException {
        return getWrappedObject().tryLock(timeout, TimeUnit.MILLISECONDS);
    }
}
