package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("IteratorIterator")
public class IteratorIterator extends BaseObject implements Iterator, OuterIterator, Traversable {
    protected Memory iterator;
    protected ForeachIterator foreachIterator;
    protected boolean valid = true;

    public IteratorIterator(Environment env) {
        super(env);
    }

    public IteratorIterator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg(value = "iterator", type = HintType.TRAVERSABLE))
    public Memory __construct(Environment env, Memory... args) {
        iterator = args[0].toImmutable();
        foreachIterator = iterator.getNewIterator(env);
        return iterator;
    }

    @Override
    @Signature
    public Memory getInnerIterator(Environment env, Memory... args) {
        return iterator;
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        return foreachIterator.getValue();
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return foreachIterator.getMemoryKey();
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        valid = foreachIterator.next();
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        foreachIterator.reset();
        valid = foreachIterator.next();
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return valid ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }
}
