package ru.regenix.jphp.runtime.lang.spl.iterator;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("SeekableIterator")
public interface SeekableIterator extends Iterator {

    @Reflection.Signature({@Reflection.Arg("position")})
    public Memory seek(Environment env, Memory... args);
}
