package php.runtime.ext.core.classes.format;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\format\\Processor")
abstract public class WrapProcessor extends BaseObject {
    public WrapProcessor(Environment env) {
        super(env);
    }

    public WrapProcessor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg("value"))
    abstract public Memory parse(Environment env, Memory... args);

    @Signature(@Arg("string"))
    abstract public Memory format(Environment env, Memory... args);
}
