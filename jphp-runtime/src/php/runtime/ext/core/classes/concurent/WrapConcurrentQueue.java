package php.runtime.ext.core.classes.concurent;

import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.ext.core.classes.util.WrapQueue;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.ConcurrentLinkedQueue;

@Name(CoreExtension.NAMESPACE + "concurrent\\ConcurrentQueue")
public class WrapConcurrentQueue extends WrapQueue {
    public WrapConcurrentQueue(Environment env, ConcurrentLinkedQueue wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapConcurrentQueue(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ConcurrentLinkedQueue();
    }
}
