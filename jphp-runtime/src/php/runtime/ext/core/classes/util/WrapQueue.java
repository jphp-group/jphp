package php.runtime.ext.core.classes.util;

import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.reflection.ClassEntity;

import java.util.Queue;

@Abstract
@Name(CoreExtension.NAMESPACE + "util\\Queue")
public class WrapQueue extends WrapCollection {
    interface WrappedInterface {
        Object remove();
        Object poll();
        Object peek();
        boolean offer(Object el);
    }

    public WrapQueue(Environment env, Queue wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapQueue(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Queue getWrappedObject() {
        return (Queue) super.getWrappedObject();
    }
}
