package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lib\\item")
final public class ItemUtils extends BaseObject {
    public ItemUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    @Signature({
            @Arg(value = "traversable", type = HintType.TRAVERSABLE),
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public Memory each(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, args[1]);

        return Memory.NULL;
    }
}
