package php.runtime.lang.support;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.Memory;


@Reflection.Name("Object")
public class MagicSignatureClass {

    @Reflection.Signature({@Reflection.Arg("property")})
    public Memory __get(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Reflection.Arg("property"), @Reflection.Arg("value")})
    public Memory __set(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Reflection.Arg("name"), @Reflection.Arg("arguments")})
    public Memory __call(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Reflection.Arg("name"), @Reflection.Arg("arguments")})
    public static Memory __callStatic(Environment env, Memory... args){
        return Memory.NULL;
    }
}
