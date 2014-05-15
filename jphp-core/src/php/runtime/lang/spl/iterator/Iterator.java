package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.spl.Traversable;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("Iterator")
public interface Iterator extends Traversable {
    @Signature
    public Memory current(Environment env, Memory... args);

    @Signature
    public Memory key(Environment env, Memory... args);

    @Signature
    public Memory next(Environment env, Memory... args);

    @Signature
    public Memory rewind(Environment env, Memory... args);

    @Signature
    public Memory valid(Environment env, Memory... args);
}
