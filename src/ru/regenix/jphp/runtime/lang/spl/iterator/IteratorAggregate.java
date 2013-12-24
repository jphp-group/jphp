package ru.regenix.jphp.runtime.lang.spl.iterator;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("IteratorAggregate")
public interface IteratorAggregate {

    @Reflection.Signature
    public Memory getIterator(Environment env, Memory... args);
}
