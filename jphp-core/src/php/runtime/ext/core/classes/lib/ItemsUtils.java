package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Countable;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.KeyValueMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;

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

    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "comparator", type = HintType.CALLABLE, optional = @Optional("null")),
            @Arg(value = "saveKeys", optional = @Optional("false"))
    })
    public static Memory sortByKeys(Environment env, Memory... args) {
        boolean saveKeys = args[2].toBoolean();
        List<KeyValueMemory> tmp = new ArrayList<KeyValueMemory>();

        ForeachIterator iterator = args[0].toImmutable().getNewIterator(env);
        while (iterator.next()) {
            tmp.add(new KeyValueMemory(iterator.getMemoryKey(), iterator.getValue().toImmutable()));
        }

        final Invoker invoker = args[0].isNull() ? null : Invoker.valueOf(env, null, args[1]);
        Collections.sort(tmp, new Comparator<KeyValueMemory>() {
            @Override
            public int compare(KeyValueMemory o1, KeyValueMemory o2) {
                if (invoker == null)
                    return o1.key.compareTo(o2.key);
                else
                    return invoker.callNoThrow(o1.key, o2.key).toInteger();
            }
        });

        ArrayMemory r = new ArrayMemory();
        Iterator<KeyValueMemory> iterator1 = tmp.iterator();
        while (iterator1.hasNext()) {
            if (saveKeys)
                r.add(iterator1.next());
            else
                r.add(iterator1.next().value);

            iterator1.remove();
        }

        return r.toConstant();
    }

    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE),
            @Arg(value = "comparator", type = HintType.CALLABLE, optional = @Optional("null")),
            @Arg(value = "saveKeys", optional = @Optional("false"))
    })
    public static Memory sort(Environment env, Memory... args) {
        boolean saveKeys = args[2].toBoolean();
        Memory[] sortTmp;

        if (!saveKeys && args[0].isArray()) {
            Memory[] original = args[0].toValue(ArrayMemory.class).values(true);
            sortTmp = Arrays.copyOf(original, original.length);
        } else {
            ForeachIterator iterator = args[0].toImmutable().getNewIterator(env);
            List<Memory> tmp = new ArrayList<Memory>();
            while (iterator.next()) {
                if (saveKeys)
                    tmp.add(new KeyValueMemory(iterator.getMemoryKey(), iterator.getValue().toImmutable()));
                else
                    tmp.add(iterator.getValue().toImmutable());
            }

            sortTmp = tmp.toArray(new Memory[tmp.size()]);
            tmp.clear();
        }

        if (args[1].isNull()) {
            Arrays.sort(sortTmp);
        } else {
            final Invoker invoker = Invoker.valueOf(env, null, args[1]);
            Arrays.sort(sortTmp, new Comparator<Memory>() {
                @Override
                public int compare(Memory o1, Memory o2) {
                    return invoker.callNoThrow(o1, o2).toInteger();
                }
            });
        }

        ArrayMemory r = new ArrayMemory();
        for(Memory el : sortTmp) {
            r.add(el);
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
                r.put(iterator.getMemoryKey(), iterator.getValue().toImmutable());
            else
                r.add(iterator.getValue().toImmutable());
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
                array.add(el.toImmutable());
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
