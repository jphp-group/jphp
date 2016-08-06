package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("Reflection")
abstract public class Reflection extends BaseObject {

    public Reflection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    protected Reflection(Environment env) {
        super(env);
    }

    protected final void exception(Environment env, String message, Object... args){
        ReflectionException e = new ReflectionException(env);
        e.__construct(env, new StringMemory(String.format(message, args)));
        env.__throwException(e, false);
    }

    @Signature(@Arg(value = "modifiers", type = HintType.INT))
    public static Memory getModifierNames(Environment env, Memory... args){
        return new ArrayMemory();
    }

    @Signature({
            @Arg(value = "reflector", type = HintType.OBJECT),
            @Arg(value = "return", type = HintType.BOOLEAN, optional = @Optional(value = "", type = HintType.BOOLEAN))
    })
    public static Memory export(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Signature
    final public Memory __clone(Environment env, Memory... args){
        env.error(ErrorType.E_ERROR, "Trying to clone an uncloneable object of class %s", getReflection().getName());
        return null;
    }

    @Signature
    public Memory __toString(Environment env, Memory... args){
        return new StringMemory("TODO");
    }
}
