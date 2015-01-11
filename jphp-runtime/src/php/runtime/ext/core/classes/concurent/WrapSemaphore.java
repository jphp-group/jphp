package php.runtime.ext.core.classes.concurent;

import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Name(CoreExtension.NAMESPACE + "concurrent\\Semaphore")
public class WrapSemaphore extends BaseWrapper<Semaphore> {
    interface WrappedInterface {
        @Property boolean fair();
        @Property int queueLength();

        void acquire();
        void acquire(int permits);
        void acquireUninterruptibly();
        void acquireUninterruptibly(int permits);

        boolean hasQueuedThreads();

        void release();
        void release(int permits);

        boolean tryAcquire();
        boolean tryAcquire(int permits);
    }

    public WrapSemaphore(Environment env, Semaphore wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapSemaphore(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(int permits) {
        __wrappedObject = new Semaphore(permits);
    }

    @Signature
    public void __construct(int permits, boolean fair) {
        __wrappedObject = new Semaphore(permits, fair);
    }

    @Getter
    protected int getAvailablePermits() {
        return getWrappedObject().availablePermits();
    }

    @Getter
    protected int getDrainPermits() {
        return getWrappedObject().drainPermits();
    }

    @Signature
    public boolean tryAcquire(int permits, long timeout) throws InterruptedException {
        return getWrappedObject().tryAcquire(permits, timeout, TimeUnit.MILLISECONDS);
    }
}
