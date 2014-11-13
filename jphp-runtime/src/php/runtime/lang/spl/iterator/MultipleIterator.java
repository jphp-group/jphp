package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.exception.InvalidArgumentException;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("MultipleIterator")
public class MultipleIterator extends BaseObject implements Iterator {
    public final static int MIT_NEED_ANY = 0;
    public final static int MIT_NEED_ALL = 1;
    public final static int MIT_KEYS_NUMERIC = 0;
    public final static int MIT_KEYS_ASSOC = 2;

    protected ArrayMemory iterators = new ArrayMemory();
    protected int flags;

    public MultipleIterator(Environment env) {
        super(env);
    }

    public MultipleIterator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg(value = "flags", optional = @Optional("0")))
    public Memory __construct(Environment env, Memory... args) {
        flags = args[0].toInteger();
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "iterator", typeClass = "Iterator"),
            @Arg(value = "info", optional = @Optional("null"))
    })
    public Memory attachIterator(Environment env, Memory... args) {
        Iterator iterator = args[0].toObject(Iterator.class);

        if (!iterator.valid(env, args).toBoolean()) {
            env.exception(InvalidArgumentException.class, "Iterator is not valid");
        }

        if (args[1].isNull()) {
            iterators.add(args[0]);
        } else {
            if (iterators.get(args[1]) != null) {
                env.exception(InvalidArgumentException.class, "Iterator '" + args[1] + "' already exists");
            }

            iterators.refOfIndex(args[1]).assign(args[0]);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory countIterators(Environment env, Memory... args) {
        return LongMemory.valueOf(iterators.size());
    }

    @Signature
    public Memory getFlags(Environment env, Memory... args) {
        return LongMemory.valueOf(flags);
    }

    @Signature(@Arg("flags"))
    public Memory setFlags(Environment env, Memory... args) {
        flags = args[0].toInteger();
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        if (iterators.size() == 0) {
            return Memory.FALSE;
        }

        ArrayMemory result = new ArrayMemory();

        ForeachIterator iterator = iterators.getNewIterator(env);

        while (iterator.next()) {
            Iterator el = iterator.getValue().toObject(Iterator.class);

            if (env.invokeMethodNoThrow(el, "valid").toBoolean()) {
                Memory current = env.invokeMethodNoThrow(el, "current");

                if ((flags & MIT_KEYS_ASSOC) == MIT_KEYS_ASSOC) {
                    result.put(iterator.getKey(), current);
                } else {
                    result.add(current);
                }
            } else {
                if ((flags & MIT_NEED_ALL) == MIT_NEED_ALL) {
                    env.exception(InvalidArgumentException.class, "One of iterators is not valid");
                }
            }
        }

        return result.toConstant();
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return current(env, args);
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        for (Memory el : iterators) {
            env.invokeMethodNoThrow(el.toObject(Iterator.class), "next");
        }

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        for (Memory el : iterators) {
            env.invokeMethodNoThrow(el.toObject(Iterator.class), "rewind");
        }

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        for (Memory el : iterators) {
            if (env.invokeMethodNoThrow(el.toObject(Iterator.class), "valid").toBoolean()) {
                if ((flags & MIT_NEED_ALL) != flags) {
                    return Memory.TRUE;
                }
            } else {
                if ((flags & MIT_NEED_ALL) == flags) {
                    return Memory.FALSE;
                }
            }
        }

        return Memory.TRUE;
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
