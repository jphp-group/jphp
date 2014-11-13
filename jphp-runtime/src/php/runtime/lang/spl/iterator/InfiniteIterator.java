package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("InfiniteIterator")
public class InfiniteIterator extends IteratorIterator {
    public InfiniteIterator(Environment env) {
        super(env);
    }

    public InfiniteIterator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Memory next(Environment env, Memory... args) {
        Memory r = super.next(env, args);
        if (!valid) {
            rewind(env);
        }

        return r;
    }
}
