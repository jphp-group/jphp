package ru.regenix.jphp.runtime.lang.spl;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("Countable")
public interface Countable {
    
    @Reflection.Signature
    public Memory count(Environment env, Memory... args);
}
