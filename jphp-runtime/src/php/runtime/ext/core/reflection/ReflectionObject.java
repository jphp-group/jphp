package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("ReflectionObject")
public class ReflectionObject extends ReflectionClass {

    public ReflectionObject(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature(@Arg(value = "object", type = HintType.OBJECT))
    public Memory __construct(Environment env, Memory... args) {
        return super.__construct(env, args);
    }
}
