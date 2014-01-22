package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import static php.runtime.annotation.Reflection.*;


@Name("ReflectionClass")
@Signature(
        @Arg(value = "name", type = HintType.STRING, readOnly = true)
)
public class ReflectionClass extends Reflection {
    protected ClassEntity entity;

    public ReflectionClass(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg("argument"))
    public Memory __construct(Environment env, Memory... args){
        Memory argument = args[0];
        if (argument.isObject()){
            entity = argument.toValue(ObjectMemory.class).getReflection();
        } else {
            entity = env.fetchClass(argument.toString(), true);
            if (entity == null)
                exception(env, Messages.ERR_CLASS_NOT_FOUND.fetch(argument.toString()));
        }

        getProperties().put("name", new StringMemory(entity.getName()));
        return Memory.NULL;
    }
}
