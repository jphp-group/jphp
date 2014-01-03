package ru.regenix.jphp.runtime.lang.support;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;


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
