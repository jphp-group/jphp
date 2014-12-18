package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Runtime.FastMethod;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Arg;
import static php.runtime.annotation.Reflection.Name;

@Name("php\\lib\\mirror")
final public class MirrorUtils extends BaseObject {
    public MirrorUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {}

    @FastMethod
    @Signature({
            @Arg("object"),
            @Arg(value = "toLower", optional = @Optional("false"))
    })
    public static Memory typeOf(Environment env, Memory... args) {
        if (args[0].isObject()) {
            ClassEntity entity = args[0].toValue(ObjectMemory.class).getReflection();
            return StringMemory.valueOf(args[1].toBoolean() ? entity.getLowerName() : entity.getName());
        } else {
            return Memory.FALSE;
        }
    }

    @Signature({
            @Arg("className"),
            @Arg(value = "args", type = HintType.ARRAY, optional = @Optional("null")),
            @Arg(value = "withConstruct", optional = @Optional("true"))
    })
    public static Memory newInstance(Environment env, Memory... args) throws Throwable {
        ClassEntity entity = env.fetchClass(args[0].toString(), true);

        if (entity == null) {
            env.exception(env.trace(), Messages.ERR_CLASS_NOT_FOUND.fetch(args[0]));
            return Memory.NULL;
        } else {
            return new ObjectMemory(entity.newObject(
                    env,
                    env.trace(),
                    args[2].toBoolean(),
                    args[1].isNull() ? null : args[1].toValue(ArrayMemory.class).values(true)
            ));
        }
    }
}
