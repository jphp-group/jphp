package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.PromiseMemory;
import php.runtime.reflection.ClassEntity;

@Name("php\\util\\Promise")
public class PromiseUtils extends BaseObject {
    public PromiseUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void  __construct() { }

    @Signature
    public static Memory create(final Memory value, final Invoker callback) {
        return new PromiseMemory(value, new PromiseMemory.Handler() {
            @Override
            public void assign(Memory memory) {
                callback.callNoThrow(memory);
            }
        });
    }

    @Signature
    public static Memory create(final Invoker callback) {
        return create(Memory.NULL, callback);
    }
}
