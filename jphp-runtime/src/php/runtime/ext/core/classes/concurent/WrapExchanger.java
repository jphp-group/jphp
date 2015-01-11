package php.runtime.ext.core.classes.concurent;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Name(CoreExtension.NAMESPACE + "concurrent\\Exchanger")
public class WrapExchanger extends BaseWrapper<Exchanger> {
    public WrapExchanger(Environment env, Exchanger wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapExchanger(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new Exchanger();
    }

    @Signature
    public void exchange(Environment env, Memory value) throws InterruptedException {
        getWrappedObject().exchange(Memory.unwrap(env, value));
    }

    @Signature
    public void exchange(Environment env, Memory value, long timeout) throws TimeoutException, InterruptedException {
        getWrappedObject().exchange(Memory.unwrap(env, value), timeout, TimeUnit.MILLISECONDS);
    }
}
