package php.runtime.lang.support;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

import static php.runtime.annotation.Reflection.Arg;


@Reflection.Name("Object")
public class MagicSignatureClass {

    @Reflection.Signature({@Arg("property")})
    public Memory __get(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Arg("property"), @Arg("value")})
    public Memory __set(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Arg("name"), @Arg("arguments")})
    public Memory __call(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Reflection.Signature({@Arg("name"), @Arg("arguments")})
    public static Memory __callStatic(Environment env, Memory... args){
        return Memory.NULL;
    }
}
