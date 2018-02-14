package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.ItemsUtils;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\util\\Flow")
public class WrapFlow extends BaseObject implements Iterator {
    protected ForeachIterator selfIterator;
    protected ForeachIterator iterator;
    protected Worker worker;

    protected boolean init;
    protected boolean valid = true;

    protected boolean withKeys = false;

    public WrapFlow(Environment env, Iterable iterable) {
        this(env, ForeachIterator.of(env, iterable));
    }

    public WrapFlow(Environment env, ForeachIterator iterator) {
        super(env);
        this.iterator = iterator;
        this.worker = new Worker() {
            @Override
            public boolean next(Environment env) {
                return WrapFlow.this.iterator.next();
            }
        };
        this.worker.setIterator(iterator);
    }

    public WrapFlow(Environment env, ForeachIterator iterator, Worker worker) {
        super(env);
        this.iterator = iterator;
        this.worker = worker;
        this.worker.setIterator(iterator);
    }

    public WrapFlow(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    protected ForeachIterator getSelfIterator(Environment env) {
        if (this.selfIterator == null)
            this.selfIterator = new ObjectMemory(this).getNewIterator(env);

        if (!this.valid)
            env.exception("Unable to iterate the flow repeatedly");

        return this.selfIterator;
    }

    protected static Memory call(ForeachIterator iterator, Invoker invoker) {
        if (invoker == null)
            return iterator.getValue();

        if (invoker.getArgumentCount() == 1)
            return invoker.callNoThrow(iterator.getValue());
        else
            return invoker.callNoThrow(iterator.getValue(), iterator.getMemoryKey());
    }

    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE)
    })
    public Memory __construct(Environment env, Memory... args) {
        ForeachIterator iterator = args[0].toImmutable().getNewIterator(env);

        this.iterator = iterator;
        this.worker = new Worker() {
            @Override
            public boolean next(Environment env) {
                return WrapFlow.this.iterator.next();
            }
        };
        this.worker.setIterator(iterator);

        return Memory.NULL;
    }


    @Signature
    public Memory withKeys(Environment env, Memory... args) {
        withKeys = true;
        return new ObjectMemory(this);
    }

    @FastMethod
    @Signature
    public static Memory ofEmpty(Environment env, Memory... args) {
        return new ObjectMemory(new WrapFlow(env, new ArrayMemory().getNewIterator(env)));
    }

    @FastMethod
    @Signature({
            @Arg(value = "collection", type = HintType.TRAVERSABLE)
    })
    public static Memory of(Environment env, Memory... args) {
        return new ObjectMemory(new WrapFlow(env, args[0].toImmutable().getNewIterator(env)));
    }

    @FastMethod
    @Signature({
            @Arg("from"),
            @Arg("to"),
            @Arg(value = "step", optional = @Optional("1"))
    })
    public static Memory ofRange(Environment env, Memory... args) {
        final int from = args[0].toInteger();
        final int to = args[1].toInteger();
        final int step = args[2].toInteger() < 1 ? 1 : args[2].toInteger();

        return new ObjectMemory(new WrapFlow(env, new ForeachIterator(false, false, false) {
            protected int i;

            @Override
            public void reset() {
                this.i = from;
                currentKeyMemory = LongMemory.valueOf(-1);
                currentKey = currentKeyMemory;
            }

            @Override
            protected boolean init() {
                reset();
                return true;
            }

            @Override
            protected boolean nextValue() {
                if (i <= to) {
                    currentValue = LongMemory.valueOf(i);
                    if (currentKey == null)
                        currentKey = LongMemory.valueOf(0);

                    currentKey = ((LongMemory) currentKey).inc();
                    currentKeyMemory = null;
                    i += step;
                    return true;
                }

                return false;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }
        }));
    }

    @FastMethod
    @Signature({
            @Arg(value = "stream", typeClass = Stream.CLASS_NAME),
            @Arg(value = "chunkSize", optional = @Optional("1"))
    })
    public static Memory ofStream(final Environment env, Memory... args) {
        final Stream stream = args[0].toObject(Stream.class);
        final int chunkSize = args[1].toInteger() < 1 ? 1 : args[1].toInteger();

        return new ObjectMemory(new WrapFlow(env, new ForeachIterator(false, false, false) {

            protected boolean eof() {
                env.pushCall(stream, "eof");
                try {
                    return stream.eof(env).toBoolean();
                } finally {
                    env.popCall();
                }
            }

            @Override
            protected boolean init() {
                currentKey = Memory.CONST_INT_M1;
                return !eof();
            }

            @Override
            protected boolean nextValue() {
                if (eof())
                    return false;

                env.pushCall(stream, "read", LongMemory.valueOf(chunkSize));
                try {
                    currentValue = stream.read(env, LongMemory.valueOf(chunkSize));
                    currentKey = ((LongMemory) currentKey).inc();
                } catch (IOException e) {
                    env.catchUncaught(e);
                } finally {
                    env.popCall();
                }
                return true;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }

            @Override
            public void reset() {
                currentKey = Memory.CONST_INT_M1;
                env.pushCall(stream, "seek", Memory.CONST_INT_0);
                try {
                    stream.seek(env, Memory.CONST_INT_0);
                } catch (IOException e) {
                    env.catchUncaught(e);
                } finally {
                    env.popCall();
                }
            }
        }));
    }

    @FastMethod
    @Signature({
            @Arg("string"),
            @Arg(value = "chunkSize", optional = @Optional("1"))
    })
    public static Memory ofString(Environment env, Memory... args) {
        final String string = args[0].toString();
        final int chunkSize = args[1].toInteger() < 1 ? 1 : args[1].toInteger();

        return new ObjectMemory(new WrapFlow(env, new ForeachIterator(false, false, false) {
            protected int i = 0;
            protected int length = string.length();

            @Override
            public void reset() {
                this.i = 0;
            }

            @Override
            protected boolean init() {
                this.reset();
                return length > 0;
            }

            @Override
            protected boolean nextValue() {
                if (i < length) {
                    int endIndex = i + chunkSize;
                    if (endIndex > length)
                        endIndex = length;

                    currentValue = chunkSize == 1
                            ? StringMemory.valueOf(string.charAt(i))
                            : StringMemory.valueOf(string.substring(i, endIndex));

                    if (currentKeyMemory == null)
                        currentKeyMemory = LongMemory.valueOf(0);

                    currentKeyMemory = currentKeyMemory.inc();
                    currentKey = currentKeyMemory;
                    i += chunkSize;
                    return true;
                }

                return false;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }
        }));
    }

    @Signature(@Arg(value = "collection", type = HintType.TRAVERSABLE))
    public Memory append(Environment env, Memory... args) {
        final ForeachIterator appendIterator = args[0].toImmutable().getNewIterator(env);
        final ForeachIterator iterator = getSelfIterator(env);

        return new ObjectMemory(new WrapFlow(env, new ForeachIterator(false, false, false) {
            protected boolean applyAppended = false;

            @Override
            public void reset() {
                iterator.reset();
                appendIterator.reset();
                applyAppended = false;
            }

            @Override
            protected boolean init() {
                return true;
            }

            @Override
            protected boolean nextValue() {
                ForeachIterator it = appendIterator;
                boolean r = false;

                if (!applyAppended) {
                    it = iterator;
                    r = it.next();
                    if (!r) {
                        applyAppended = true;
                        it = appendIterator;
                    }
                } else
                    applyAppended = true;

                if (applyAppended)
                    r = it.next();
                return r;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }

            @Override
            public Object getKey() {
                return applyAppended ? appendIterator.getKey() : iterator.getKey();
            }

            @Override
            public Memory getMemoryKey() {
                return applyAppended ? appendIterator.getMemoryKey() : iterator.getMemoryKey();
            }

            @Override
            public Memory getValue() {
                return applyAppended ? appendIterator.getValue() : iterator.getValue();
            }
        }));
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        if (!valid)
            return Memory.NULL;
        if (!init)
            rewind(env, args);

        return worker.current(env);
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return worker.key(env);
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        if (!valid)
            return Memory.NULL;

        if (!worker.next(env))
            valid = false;
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        if (init) {
            worker.reset();
            return Memory.NULL;
            //throw new IllegalStateException("Cannot rewind() collection object");
        }

        init = true;
        if (!worker.next(env)) {
            valid = false;
            return Memory.NULL;
        }
        valid = true;

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return valid ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory count(Environment env, Memory... args) {
        int cnt = 0;
        ForeachIterator iterator = getSelfIterator(env);

        Set<Object> keys = new HashSet<>();

        while (iterator.next()) {
            if (withKeys) {
                if (keys.add(iterator.getKey())) {
                    cnt++;
                }
            } else {
                cnt++;
            }
        }

        return LongMemory.valueOf(cnt);
    }

    @Signature(@Arg(value = "comparator", type = HintType.CALLABLE, optional = @Optional("null")))
    public Memory sort(Environment env, Memory... args) {
        return ItemsUtils.sort(env, new ObjectMemory(this), args[0], this.withKeys ? Memory.TRUE : Memory.FALSE);
    }

    @Signature(@Arg(value = "comparator", type = HintType.CALLABLE, optional = @Optional("null")))
    public Memory sortByKeys(Environment env, Memory... args) {
        return ItemsUtils.sortByKeys(env, new ObjectMemory(this), args[0], this.withKeys ? Memory.TRUE : Memory.FALSE);
    }

    @Signature(@Arg(value = "withKeys", optional = @Optional("null")))
    public Memory toMap(Environment env, Memory... args) {
        return toArray(env, Memory.TRUE);
    }

    @Signature(@Arg(value = "withKeys", optional = @Optional("null")))
    public Memory toArray(Environment env, Memory... args) {
        boolean withKeys = args[0].isNull() ? this.withKeys : args[0].toBoolean();

        ForeachIterator iterator = getSelfIterator(env);
        ArrayMemory r = new ArrayMemory();
        while (iterator.next()) {
            if (withKeys) {
                r.put(iterator.getKey(), iterator.getValue());
            } else {
                r.add(iterator.getValue());
            }
        }

        return r.toConstant();
    }

    @Signature(@Arg(value = "separator"))
    public Memory toString(Environment env, Memory... args) {
        String sep = args[0].toString();

        ForeachIterator iterator = getSelfIterator(env);
        StringBuilderMemory sb = new StringBuilderMemory();
        int i = 0;
        while (iterator.next()) {
            if (i != 0)
                sb.append(sep);

            sb.append(iterator.getValue());
            i++;
        }

        return sb;
    }

    @Signature(@Arg(value = "comparator", type = HintType.CALLABLE, nullable = true, optional = @Optional("null")))
    public Memory max(Environment env, Memory... args) {
        Invoker comparator = args[0].isNull() ? null : Invoker.create(env, args[0]);

        return getSelfIterator(env)
                .stream()
                .reduce((one, two) -> {
                    if (comparator == null) {
                        return two.greater(one) ? two : one;
                    } else {
                        return comparator.callNoThrow(one, two).toLong() > 0 ? one : two;
                    }
                }).orElse(Memory.NULL);
    }

    @Signature(@Arg(value = "comparator", type = HintType.CALLABLE, nullable = true, optional = @Optional("null")))
    public Memory min(Environment env, Memory... args) {
        Invoker comparator = args[0].isNull() ? null : Invoker.create(env, args[0]);

        return getSelfIterator(env)
                .stream()
                .reduce((one, two) -> {
                    if (comparator == null) {
                        return two.smaller(one) ? two : one;
                    } else {
                        return comparator.callNoThrow(one, two).toLong() > 0 ? two : one;
                    }
                }).orElse(Memory.NULL);
    }

    @Signature
    public Memory sum(Environment env, Memory... args) {
        return getSelfIterator(env)
                .stream()
                .reduce(Memory::plus).orElse(Memory.NULL);
    }

    @Signature
    public Memory avg(Environment env, Memory... args) {
        Memory result = Memory.CONST_INT_0;

        int cnt = 0;
        for (Memory memory : getSelfIterator(env)) {
            result = result.plus(memory);
            cnt++;
        }

        return result.div(LongMemory.valueOf(cnt));
    }

    @Signature(@Arg(value = "comparator", type = HintType.CALLABLE, optional = @Optional("null")))
    public Memory numMedian(Environment env, Memory... args) {
        List<Memory> list = sort(env, args[0])
                .toValue(ArrayMemory.class)
                .stream().collect(Collectors.toList());

        if (list.isEmpty()) return Memory.NULL;

        if (list.size() % 2 == 0) {
            return list.get(list.size() / 2).plus(list.get(list.size() / 2 + 1)).div(Memory.CONST_INT_2).toNumeric();
        } else {
            return list.get(list.size() / 2).toNumeric();
        }
    }

    @Signature(@Arg(value = "comparator", type = HintType.CALLABLE, optional = @Optional("null")))
    public Memory median(Environment env, Memory... args) {
        List<Memory> list = sort(env, args[0])
                .toValue(ArrayMemory.class)
                .stream().collect(Collectors.toList());

        if (list.isEmpty()) return Memory.NULL;

        if (list.size() % 2 == 0) {
            return list.get(list.size() / 2);
        } else {
            return list.get(list.size() / 2);
        }
    }

    @Signature
    public Memory concat(Environment env, Memory... args) {
        StringBuilder sb = new StringBuilder();

        for (Memory memory : getSelfIterator(env)) {
            sb.append(memory.toString());
        }

        return StringMemory.valueOf(sb.toString());
    }

    @Signature(@Arg(value = "callback", type = HintType.CALLABLE))
    public Memory reduce(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[0]);

        ForeachIterator iterator = getSelfIterator(env);
        Memory r = Memory.NULL;
        int argCount = invoker.getArgumentCount();

        while (iterator.next()) {
            if (argCount < 3)
                r = invoker.callNoThrow(r, iterator.getValue());
            else
                r = invoker.callNoThrow(r, iterator.getValue(), iterator.getMemoryKey());
        }

        return r;
    }

    @Signature(@Arg(value = "callback", type = HintType.CALLABLE))
    public Memory each(Environment env, Memory... args) {
        ForeachIterator iterator = getSelfIterator(env);

        Invoker invoker = Invoker.valueOf(env, null, args[0]);
        int cnt = 0;
        while (iterator.next()) {
            cnt++;
            if (call(iterator, invoker).toValue() == Memory.FALSE)
                break;
        }
        return LongMemory.valueOf(cnt);
    }


    @Signature({
            @Arg(value = "sliceSize"),
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public Memory eachSlice(Environment env, Memory... args) {
        Invoker invoker = Invoker.valueOf(env, null, args[1]);

        ArrayMemory tmp = new ArrayMemory();
        int cnt = 0, r = 0, n = args[0].toInteger();
        while (iterator.next()) {
            cnt++;
            if (withKeys)
                tmp.refOfIndex(iterator.getMemoryKey()).assign(iterator.getValue());
            else
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

    @Signature({
            @Arg(value = "callback", type = HintType.CALLABLE)
    })
    public Memory group(Environment env, Memory... args) {
        final Invoker invoker = Invoker.valueOf(env, null, args[0]);
        final ForeachIterator iterator = getSelfIterator(env);

        return new ObjectMemory(new WrapFlow(env, new ForeachIterator(false, false, false) {
            protected Memory key;

            @Override
            protected boolean init() {
                key = Memory.CONST_INT_M1;
                return true;
            }

            @Override
            protected boolean nextValue() {
                Memory r = new ArrayMemory();

                boolean done = false;
                while (iterator.next()) {
                    done = true;
                    if (withKeys)
                        r.refOfIndex(iterator.getMemoryKey()).assign(iterator.getValue());
                    else
                        r.refOfPush().assign(iterator.getValue());

                    if (call(iterator, invoker).toBoolean())
                        break;
                }

                if (done) {
                    currentKeyMemory = key.inc();
                    currentKey = currentKeyMemory;
                    currentValue = r;
                }
                return done;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }

            @Override
            public void reset() {
                iterator.reset();
                key = Memory.CONST_INT_M1;
            }
        }));
    }

    @Signature
    public IObject onlyKeys(Environment env, ForeachIterator iterator) {
        return onlyKeys(env, iterator, false);
    }

    @Signature
    public IObject onlyKeys(Environment env, ForeachIterator iterator, final boolean ignoreCase) {
        final Set<String> keys = new HashSet<String>();

        for (Memory el : iterator) {
            if (ignoreCase) {
                keys.add(el.toString());
            } else {
                keys.add(el.toString().toLowerCase());
            }
        }

        return new WrapFlow(env, getSelfIterator(env), new Worker() {
            @Override
            public boolean next(Environment env) {
                while (iterator.next()) {
                    String key = iterator.getKey().toString();

                    if (ignoreCase) {
                        key = key.toLowerCase();
                    }

                    if (keys.contains(key)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    @Signature(@Arg(value = "filter", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public Memory find(Environment env, Memory... args) {
        final Invoker invoker = Invoker.valueOf(env, null, args[0]);

        return new ObjectMemory(new WrapFlow(env, getSelfIterator(env), new Worker() {
            @Override
            public boolean next(final Environment env) {
                while (iterator.next()) {
                    if (call(iterator, invoker).toBoolean())
                        return true;
                }

                return false;
            }
        }));
    }

    @Signature(@Arg(value = "filter", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public Memory findOne(Environment env, Memory... args) {
        final Invoker invoker = Invoker.valueOf(env, null, args[0]);

        ForeachIterator iterator = getSelfIterator(env);

        while (iterator.next()) {
            if (call(iterator, invoker).toBoolean())
                return iterator.getValue();
        }

        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "value"),
            @Arg(value = "strict", optional = @Optional("false"))
    })
    public Memory findValue(Environment env, Memory... args) {
        ForeachIterator iterator = getSelfIterator(env);

        boolean strict = args[1].toBoolean();

        while (iterator.next()) {
            if (strict && iterator.getValue().identical(args[0])) {
                return iterator.getMemoryKey();
            } else if (iterator.getValue().equal(args[0])) {
                return iterator.getMemoryKey();
            }
        }

        return Memory.NULL;
    }

    @Signature
    public Memory keys(Environment env, Memory... args) {
        return new ObjectMemory(new WrapFlow(env, getSelfIterator(env), new Worker() {
            Memory current;
            Memory key;

            @Override
            public boolean next(final Environment env) {
                if (iterator.next()) {
                    current = iterator.getMemoryKey();
                    key = key == null ? Memory.CONST_INT_0 : key.inc();
                    return true;
                }

                return false;
            }

            @Override
            public Memory current(Environment env) {
                return current == null ? Memory.NULL : current;
            }

            @Override
            public Memory key(Environment env) {
                return key == null ? Memory.NULL : key;
            }

            @Override
            public void reset() {
                key = null;
                current = null;
            }
        }));
    }

    @Signature(@Arg(value = "callback", type = HintType.CALLABLE))
    public Memory map(Environment env, Memory... args) {
        final Invoker invoker = Invoker.valueOf(env, null, args[0]);

        return new ObjectMemory(new WrapFlow(env, getSelfIterator(env), new Worker() {
            Memory current;

            @Override
            public boolean next(final Environment env) {
                if (iterator.next()) {
                    current = call(iterator, invoker);
                    return true;
                }

                return false;
            }

            @Override
            public Memory current(Environment env) {
                return current == null ? Memory.NULL : current;
            }
        }));
    }

    @Signature(@Arg(value = "n"))
    public Memory skip(Environment env, Memory... args) {
        final int skip = args[0].toInteger();
        if (skip <= 0)
            return new ObjectMemory(this);

        return new ObjectMemory(new WrapFlow(env, getSelfIterator(env), new Worker() {
            protected int i = 0;

            @Override
            public boolean next(final Environment env) {
                while (iterator.next()) {
                    i++;
                    if (i > skip)
                        return true;
                }

                return false;
            }
        }));
    }

    @Signature(@Arg("max"))
    public Memory limit(Environment env, Memory... args) {
        final int limit = args[0].toInteger();
        return new ObjectMemory(new WrapFlow(env, getSelfIterator(env), new Worker() {
            protected int i = 0;

            @Override
            public boolean next(final Environment env) {
                if (i >= limit)
                    return false;
                if (iterator.next()) {
                    i++;
                    return true;
                }
                return false;
            }
        }));
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }

    abstract static protected class Worker {
        protected ForeachIterator iterator;

        public void setIterator(ForeachIterator iterator) {
            this.iterator = iterator;
        }

        abstract public boolean next(Environment env);

        public Memory current(Environment env) {
            return iterator.getValue();
        }

        public Memory key(Environment env) {
            return iterator.getMemoryKey();
        }

        public void reset() {
            iterator.reset();
        }
    }
}
