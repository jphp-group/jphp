package ru.regenix.jphp.runtime.lang.spl;

import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;

@Reflection.Name("Iterator")
public interface PHPIterator extends Traversable {
    @Reflection.Signature
    public Memory current(Environment env, Memory... args);

    @Reflection.Signature
    public Memory key(Environment env, Memory... args);

    @Reflection.Signature
    public Memory next(Environment env, Memory... args);

    @Reflection.Signature
    public Memory rewind(Environment env, Memory... args);

    @Reflection.Signature
    public Memory valid(Environment env, Memory... args);
}
