package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("EmptyIterator")
public class EmptyIterator extends BaseObject implements Iterator {
    public EmptyIterator(Environment env) {
        super(env);
    }

    public EmptyIterator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return Memory.FALSE;
    }
}
