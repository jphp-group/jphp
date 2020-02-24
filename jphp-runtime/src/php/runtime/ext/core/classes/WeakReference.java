package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.BaseObject;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Final
public class WeakReference extends BaseObject {
    private java.lang.ref.WeakReference<IObject> ref;

    public WeakReference(Environment env, java.lang.ref.WeakReference<IObject> ref) {
        super(env);
        this.ref = ref;
    }

    public WeakReference(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature()
    public Memory __construct(Environment env, Memory... args) {
        env.error(ErrorType.E_ERROR, "Direct instantiation of 'WeakReference' is not allowed, use WeakReference::create instead");
        return null;
    }

    @Reflection.Signature({
            @Reflection.Arg(value = "object", type = HintType.OBJECT)
    })
    public static Memory create(Environment env, Memory... args) {
        return new ObjectMemory(
                new WeakReference(env, new java.lang.ref.WeakReference<>(args[0].toObject(IObject.class)))
        );
    }

    @Reflection.Signature
    public Memory get(Environment env, Memory... args) {
        return ObjectMemory.valueOf(ref.get());
    }
}
