package ru.regenix.jphp.runtime.lang.spl.iterator;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("RecursiveIterator")
public interface RecursiveIterator extends Iterator {

    @Reflection.Signature
    public Memory getChildren(Environment env, Memory... args);

    @Reflection.Signature
    public Memory hasChildren(Environment env, Memory... args);
}
