package php.runtime.lang.support;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.env.Environment;

import static php.runtime.annotation.Reflection.Arg;

@Reflection.Name("Object")
public class MagicSignatureClass {
    @Reflection.Signature({@Arg(value = "property", type = HintType.STRING)})
    public Memory __get(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Arg(value = "property", type = HintType.STRING), @Arg("value")})
    public Memory __set(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Arg(value = "name", type = HintType.STRING), @Arg(value = "arguments", type = HintType.ARRAY)})
    public Memory __call(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Arg(value = "name", type = HintType.STRING), @Arg(value = "arguments", type = HintType.ARRAY)})
    public static Memory __callStatic(Environment env, Memory... args){
        return Memory.NULL;
    }
}
