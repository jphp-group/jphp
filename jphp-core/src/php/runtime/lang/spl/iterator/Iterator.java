package php.runtime.lang.spl.iterator;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.spl.Traversable;
import php.runtime.Memory;

@Reflection.Name("Iterator")
public interface Iterator extends Traversable {
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
