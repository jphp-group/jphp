package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Countable;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.HashSet;
import java.util.Set;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\items")
final public class ItemsUtils extends BaseObject {
    public ItemsUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    protected static Memory call(ForeachIterator iterator, Invoker invoker) {
        if (invoker == null)
            return iterator.getValue();

        if (invoker.getArgumentCount() == 1)
            return invoker.callNoThrow(iterator.getValue());
        else
            return invoker.callNoThrow(iterator.getValue(), iterator.getMemoryKey());
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public static Memory each(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[1]);
        ForeachIterator iterator = args[0].getNewIterator(env);

        int cnt = 0;
        while (iterator.next()) {
            Memory r = call(iterator, invoker);

            cnt++;
            if (r.toValue() == Memory.FALSE)
                break;
        }

        return LongMemory.valueOf(cnt);
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "size"),
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public static Memory eachSlice(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[2]);
        ForeachIterator iterator = args[0].getNewIterator(env);

        ArrayMemory tmp = new ArrayMemory();
        int cnt = 0, r = 0, n = args[1].toInteger();
        while (iterator.next()) {
            cnt++;
            tmp.add(iterator.getValue());
            if (cnt >= n) {
                r++;
                Memory ret = invoker.callNoThrow(tmp);
                tmp = new ArrayMemory();
                if (ret.toValue() == Memory.FALSE)
                    break;
                cnt = 0;
            }
        }

        if (tmp.size() > 0) {
            r++;
            invoker.callNoThrow(tmp);
        }

        return LongMemory.valueOf(r);
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public static Memory map(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[1]);
        ForeachIterator iterator = args[0].getNewIterator(env);

        ArrayMemory result = new ArrayMemory();
        while (iterator.next()) {
           result.add(call(iterator, invoker));
        }
        return result.toConstant();
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "callback", type = HintType.CALLABLE, optional = @Optional("NULL"))
    })
    public static Memory find(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[1]);
        ForeachIterator iterator = args[0].getNewIterator(env);

        while (iterator.next()) {
            if (call(iterator, invoker).toBoolean())
                return iterator.getValue();
        }
        return Memory.NULL;
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "callback", type = HintType.CALLABLE, optional = @Optional("NULL"))
    })
    public static Memory findAll(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[1]);
        ForeachIterator iterator = args[0].getNewIterator(env);

        ArrayMemory r = new ArrayMemory();
        while (iterator.next()) {
            if (call(iterator, invoker).toBoolean())
                r.put(iterator.getMemoryKey(), iterator.getValue());
        }

        return r.toConstant();
    }

    @FastMethod
    @Signature(@Arg(value = "collection", type = HintType.TRAVERSABLE))
    public static Memory count(Environment env, Memory... args) {
        if (args[0].isArray())
            return LongMemory.valueOf(args[0].toValue(ArrayMemory.class).size());
        else if (args[0].isObject()) {
            ObjectMemory objectMemory = args[0].toValue(ObjectMemory.class);
            if (objectMemory.value instanceof Countable) {
                env.pushCall(objectMemory.value, "count");
                try {
                    long size = ((Countable) objectMemory.value).count(env).toLong();
                    return LongMemory.valueOf(size);
                } finally {
                    env.popCall();
                }
            } else {
                ForeachIterator iterator = args[0].getNewIterator(env);
                int r = 0;
                while (iterator.next()) r++;
                return LongMemory.valueOf(r);
            }
        } else
            return Memory.CONST_INT_0;
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "count", optional = @Optional(value = "1", type = HintType.INT))
    })
    public static Memory first(Environment env, Memory... args) {
        ForeachIterator iterator = args[0].getNewIterator(env);
        int i = 0, count = args[1].toInteger();

        ArrayMemory r = null;
        while (iterator.next()) {
            i++;
            if (count <= 1)
                return iterator.getValue();

            if (i <= count) {
                if (r == null)
                    r = new ArrayMemory();

                r.add(iterator.getValue());
            }
        }

        return r == null ? Memory.NULL : r.toConstant();
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public static Memory reduce(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[1]);
        ForeachIterator iterator = args[0].getNewIterator(env);

        Memory r = Memory.NULL;
        while (iterator.next()) {
            r = invoker.callNoThrow(r, iterator.getValue());
        }

        return r;
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "withKeys", optional = @Optional("false"))
    })
    public static Memory toArray(Environment env, Memory... args) {
        boolean withKeys = args[1].toBoolean();
        if (withKeys && args[0].isArray())
            return args[0].toImmutable();

        ForeachIterator iterator = args[0].getNewIterator(env);
        ArrayMemory r = new ArrayMemory();
        while (iterator.next()) {
            if (withKeys)
                r.put(iterator.getMemoryKey(), iterator.getValue());
            else
                r.add(iterator.getValue());
        }

        return r.toConstant();
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE)
    })
    public static Memory keys(Environment env, Memory... args) {
        ForeachIterator iterator = args[0].getNewIterator(env);
        ArrayMemory r = new ArrayMemory();
        while (iterator.next())
            r.add(iterator.getMemoryKey());

        return r.toConstant();
    }

    protected static void flatten(Environment env, ForeachIterator iterator, Set<Integer> used, ArrayMemory array,
                                  int level, int maxLevel) {
        while (iterator.next()) {
            Memory el = iterator.getValue();
            ForeachIterator innerIterator = el.getNewIterator(env);
            if (innerIterator == null || (level >= maxLevel && maxLevel > -1)) {
                array.add(el);
            } else {
                if (used.add(el.getPointer())) {
                    flatten(env, innerIterator, used, array, level + 1, maxLevel);
                    used.remove(el.getPointer());
                }
            }
        }
    }

    @FastMethod
    @Signature({
        @Arg(value = "collection", type = HintType.TRAVERSABLE),
        @Arg(value = "level", optional = @Optional("-1"))
    })
    public static Memory flatten(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        int level = args[1].toInteger();
        ForeachIterator iterator = args[0].getNewIterator(env);

        Set<Integer> used = new HashSet<Integer>();
        used.add(args[0].getPointer());

        flatten(env, iterator, used, r, 0, level);
        return r.toConstant();
    }
}
