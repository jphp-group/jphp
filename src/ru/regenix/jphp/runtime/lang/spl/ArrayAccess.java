package ru.regenix.jphp.runtime.lang.spl;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

import static ru.regenix.jphp.runtime.annotation.Reflection.*;

@Name("ArrayAccess")
public interface ArrayAccess {

    @Signature(value = @Arg("offset"), result = @Arg(type = HintType.BOOLEAN))
    public Memory offsetExists(Environment env, Memory... args);

    @Signature(@Arg("offset"))
    public Memory offsetGet(Environment env, Memory... args);

    @Signature({@Arg("offset"), @Arg("value")})
    public Memory offsetSet(Environment env, Memory... args);

    @Signature(@Arg("offset"))
    public Memory offsetUnset(Environment env, Memory... args);

}
