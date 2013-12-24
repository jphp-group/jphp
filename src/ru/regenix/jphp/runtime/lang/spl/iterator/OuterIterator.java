package ru.regenix.jphp.runtime.lang.spl.iterator;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("OuterIterator")
public interface OuterIterator extends Iterator {

    @Reflection.Signature
    public Memory getInnerIterator(Environment env, Memory... args);
}
