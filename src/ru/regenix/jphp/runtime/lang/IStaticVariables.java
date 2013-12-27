package ru.regenix.jphp.runtime.lang;


import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Ignore
public interface IStaticVariables {
    Memory getStatic(String name);
    Memory getOrCreateStatic(String name);
}
