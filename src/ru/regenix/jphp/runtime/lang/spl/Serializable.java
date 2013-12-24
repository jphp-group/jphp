package ru.regenix.jphp.runtime.lang.spl;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("Serializable")
public interface Serializable extends Traversable {

    @Reflection.Signature
    public Memory serialize(Environment env, Memory... args);

    @Reflection.Signature({@Reflection.Arg("serialized")})
    public Memory unserialize(Environment env, Memory... args);
}
