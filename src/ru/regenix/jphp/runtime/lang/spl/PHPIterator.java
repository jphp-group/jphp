package ru.regenix.jphp.runtime.lang.spl;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("Iterator")
public interface PHPIterator extends Traversable {
    @Reflection.Signature
    public Memory current(Environment env, String calledClass, Memory... args);

    @Reflection.Signature
    public Memory key(Environment env, String calledClass, Memory... args);

    @Reflection.Signature
    public Memory next(Environment env, String calledClass, Memory... args);

    @Reflection.Signature
    public Memory rewind(Environment env, String calledClass, Memory... args);

    @Reflection.Signature
    public Memory valid(Environment env, String calledClass, Memory... args);
}
