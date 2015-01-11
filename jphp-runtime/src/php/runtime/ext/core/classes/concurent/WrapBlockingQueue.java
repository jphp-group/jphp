package php.runtime.ext.core.classes.concurent;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.ext.core.classes.util.WrapCollection;
import php.runtime.ext.core.classes.util.WrapQueue;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Name(CoreExtension.NAMESPACE + "concurrent\\BlockingQueue")
public class WrapBlockingQueue extends WrapQueue {
    interface WrappedInterface {
        void put(Object el);
        Object take();
    }

    public WrapBlockingQueue(Environment env, BlockingQueue wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapBlockingQueue(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg("capacity"),
            @Arg(value = "fair", optional = @Optional("false"))
    })
    public Memory __construct(Environment env, Memory... args) {
        __wrappedObject = new ArrayBlockingQueue(args[0].toInteger(), args[1].toBoolean());
        return Memory.NULL;
    }

    @Override
    public BlockingQueue getWrappedObject() {
        return (BlockingQueue) super.getWrappedObject();
    }

    @Signature
    public Object offer(Object el, long timeout) throws InterruptedException {
        return getWrappedObject().offer(el, timeout, TimeUnit.MILLISECONDS);
    }

    @Signature
    public Object poll(long timeout) throws InterruptedException {
        return getWrappedObject().poll(timeout, TimeUnit.MILLISECONDS);
    }

    @Signature
    public int drainTo(WrapCollection collection) {
        return getWrappedObject().drainTo(collection.getWrappedObject());
    }

    @Signature
    public int drainTo(WrapCollection collection, int maxElements) {
        return getWrappedObject().drainTo(collection.getWrappedObject(), maxElements);
    }

    @Getter
    protected int getRemainingCapacity() {
        return getWrappedObject().remainingCapacity();
    }
}
