package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ext.support.NaturalOrderComparator;
import php.runtime.Memory;
import php.runtime.annotation.Runtime;
import php.runtime.annotation.Runtime.Reference;
import php.runtime.common.Callback;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.RecursiveException;
import php.runtime.ext.core.MathFunctions;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.Invoker;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.KeyValueMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ReferenceMemory;

import java.util.*;

import static org.develnext.jphp.zend.ext.standard.ArrayConstants.*;
import static php.runtime.Memory.Type.ARRAY;

public class ArrayFunctions extends FunctionsContainer {

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean in_array(Environment env, TraceInfo trace, Memory needle, @Reference Memory array,
                                   boolean strict) {
        if (expecting(env, trace, 2, array, ARRAY)) {
            ForeachIterator iterator = array.getNewIterator(env, false, false);
            while (iterator.next()) {
                if (strict) {
                    if (needle.identical(iterator.getValue()))
                        return true;
                } else {
                    if (needle.equal(iterator.getValue()))
                        return true;
                }
            }
            return false;
        } else
            return false;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean in_array(Environment env, TraceInfo trace, Memory needle, @Reference Memory array) {
        return in_array(env, trace, needle, array, false);
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean array_key_exists(Environment env, TraceInfo trace, Memory key, @Reference Memory array) {
        if (expecting(env, trace, 2, array, ARRAY)) {
            ArrayMemory tmp = array.toValue(ArrayMemory.class);
            return tmp.get(key) != null;
        } else
            return false;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean key_exists(Environment env, TraceInfo trace, Memory key, @Reference Memory array) {
        return array_key_exists(env, trace, key, array);
    }

    public static Memory reset(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "reset")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                ArrayMemory memory = array.toValue(ArrayMemory.class);
                return memory.resetCurrentIterator().toImmutable();
            }
        }
        return Memory.FALSE;
    }

    public static Memory next(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "next")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                ArrayMemory memory = array.toValue(ArrayMemory.class);
                ForeachIterator iterator = memory.getCurrentIterator();
                if (iterator.next())
                    return iterator.getValue().toImmutable();
                else
                    return Memory.FALSE;
            }
        }
        return Memory.FALSE;
    }

    public static Memory prev(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "prev")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                ArrayMemory memory = array.toValue(ArrayMemory.class);
                ForeachIterator iterator = memory.getCurrentIterator();
                if (iterator.prev())
                    return iterator.getValue().toImmutable();
                else
                    return Memory.FALSE;
            }
        }
        return Memory.FALSE;
    }

    public static Memory current(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "current")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                Memory value = array.toValue(ArrayMemory.class).getCurrentIterator().getValue();
                return value == null ? Memory.FALSE : value.toImmutable();
            }
        }
        return Memory.FALSE;
    }

    public static Memory key(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "key")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                Memory value = array.toValue(ArrayMemory.class).getCurrentIterator().getMemoryKey();
                return value == null ? Memory.FALSE : value;
            }
        }
        return Memory.FALSE;
    }

    public static Memory pos(Environment env, TraceInfo trace, @Reference Memory array) {
        return current(env, trace, array);
    }

    public static Memory end(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "end")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                ForeachIterator iterator = array.toValue(ArrayMemory.class).getCurrentIterator();
                if (iterator.end()) {
                    return iterator.getValue().toImmutable();
                } else {
                    return Memory.FALSE;
                }
            }
        }
        return Memory.FALSE;
    }

    public static Memory each(Environment env, TraceInfo trace, @Reference Memory array) {
        if (expectingReference(env, trace, array, "each")) {
            if (expecting(env, trace, 1, array, ARRAY)) {
                ForeachIterator iterator = array.toValue(ArrayMemory.class).getCurrentIterator();
                if (iterator.next()) {
                    Memory value = iterator.getValue().toImmutable();
                    Memory key = iterator.getMemoryKey();

                    ArrayMemory result = new ArrayMemory();

                    result.refOfIndex(1).assign(value);
                    result.refOfIndex("value").assign(value);
                    result.refOfIndex(0).assign(key);
                    result.refOfIndex("key").assign(key);

                    return result.toConstant();
                } else {
                    return Memory.FALSE;
                }
            }
        }
        return Memory.FALSE;
    }

    private static Memory _array_merge(Environment env, TraceInfo trace, boolean recursive, Memory array, Memory... arrays) {
        if (!array.isArray()) {
            env.warning(trace, "Argument %s is not an array", 1);
            return Memory.NULL;
        }

        if (arrays == null || arrays.length == 0)
            return array;

        ArrayMemory result = (ArrayMemory) array.toImmutable();

        int i = 2;
        Set<Integer> used = recursive ? new HashSet<Integer>() : null;
        for (Memory el : arrays) {
            if (!el.isArray()) {
                env.warning(trace, "Argument %s is not an array", i);
                continue;
            }

            if (used != null)
                used.add(el.getPointer(true));

            result.merge((ArrayMemory) el, recursive, used);

            if (used != null)
                used.remove(el.getPointer(true));

            i++;
        }

        return result;
    }

    public static Memory array_merge(Environment env, TraceInfo trace, Memory array, Memory... arrays) {
        return _array_merge(env, trace, false, array, arrays);
    }

    public static Memory array_merge_recursive(Environment env, TraceInfo trace, Memory array, Memory... arrays) {
        try {
            return _array_merge(env, trace, true, array, arrays);
        } catch (RecursiveException e) {
            env.warning(trace, "recursion detected");
            return Memory.NULL;
        }
    }

    public static boolean shuffle(Environment env, TraceInfo trace, @Reference Memory value) {
        if (expectingReference(env, trace, value, "shuffle") && expecting(env, trace, 1, value, ARRAY)) {
            ArrayMemory array = value.toValue(ArrayMemory.class);
            array.shuffle(MathFunctions.RANDOM);
            return true;
        } else {
            return false;
        }
    }

    public static Memory array_map(Environment env, TraceInfo trace, Memory callback, Memory _array, Memory... arrays)
            throws Throwable {
        Invoker invoker = expectingCallback(env, trace, 1, callback);
        if (invoker == null) {
            return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory();
        ForeachIterator[] iterators;
        if (!expecting(env, trace, 2, _array, ARRAY))
            return Memory.NULL;

        if (arrays == null) {
            iterators = new ForeachIterator[]{_array.getNewIterator(env, false, false)};
        } else {
            iterators = new ForeachIterator[1 + arrays.length];
            iterators[0] = _array.getNewIterator(env, false, false);
            for (int i = 0; i < arrays.length; i++) {
                if (!expecting(env, trace, i + 3, arrays[i], ARRAY))
                    return Memory.NULL;
                iterators[i + 1] = arrays[i].getNewIterator(env, false, false);
            }
        }

        Memory[] args = new Memory[iterators.length];

        while (true) {
            int i = 0;
            boolean done = true;
            for (ForeachIterator iterator : iterators) {
                if (iterator.next()) {
                    args[i] = iterator.getValue();
                    done = false;
                } else
                    args[i] = Memory.NULL;
                i++;
            }
            if (done)
                break;

            result.add(invoker.call(args));
        }

        return result.toConstant();
    }

    public static Memory array_filter(Environment env, TraceInfo trace, Memory input, Memory callback)
            throws Throwable {
        if (!expecting(env, trace, 1, input, ARRAY)) {
            return Memory.NULL;
        }

        Invoker invoker = null;
        if (callback != null && callback.toBoolean()) {
            invoker = expectingCallback(env, trace, 2, callback);
            if (invoker == null)
                return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(env, true, false);
        while (iterator.next()) {
            Object key = iterator.getKey();
            Memory value = iterator.getValue();
            if (invoker == null) {
                if (!value.toBoolean())
                    continue;
            } else if (!invoker.call(value).toBoolean())
                continue;

            result.put(key, value.toImmutable());
        }

        return result.toConstant();
    }

    public static Memory array_filter(Environment env, TraceInfo trace, Memory input)
            throws Throwable {
        return array_filter(env, trace, input, null);
    }

    public static Memory array_reduce(Environment env, TraceInfo trace, Memory input, Memory callback,
                                      Memory initial)
            throws Throwable {
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        if (array.size() == 0)
            return Memory.NULL;

        Memory result = initial;
        ForeachIterator iterator = array.getNewIterator(env, true, false);
        while (iterator.next()) {
            Memory el = iterator.getValue();
            result = invoker.call(result, el);
        }

        return result.toValue();
    }

    public static Memory array_reduce(Environment env, TraceInfo trace, Memory input, Memory callback)
            throws Throwable {
        return array_reduce(env, trace, input, callback, Memory.NULL);
    }

    public static boolean array_walk(Environment env, TraceInfo trace, @Reference Memory input, Memory callback,
                                     Memory userData) throws Throwable {
        if (!expectingReference(env, trace, input, "array_walk"))
            return false;

        if (!expecting(env, trace, 1, input, ARRAY))
            return false;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return false;

        ForeachIterator iterator = input.getNewIterator(env, true, false);
        while (iterator.next()) {
            Memory item = iterator.getValue();
            Memory key = iterator.getMemoryKey();
            invoker.call(item, key, userData);
        }
        return true;
    }

    public static boolean array_walk(Environment env, TraceInfo trace, @Reference Memory input, Memory callback)
            throws Throwable {
        return array_walk(env, trace, input, callback, Memory.NULL);
    }

    public static boolean _array_walk_recursive(Environment env, TraceInfo trace,
                                                Memory input,
                                                Invoker invoker, Memory userData, Set<Integer> used)
            throws Throwable {
        if (used == null)
            used = new HashSet<Integer>();

        ForeachIterator iterator = input.getNewIterator(env, true, false);
        while (iterator.next()) {
            Memory item = iterator.getValue();
            if (item.isArray()) {
                if (used.add(item.getPointer())) {
                    boolean result = _array_walk_recursive(env, trace, item, invoker, userData, used);
                    used.remove(item.getPointer());
                    if (!result)
                        return false;
                } else {
                    env.warning(trace, "array_walk_recursive(): recursion detected");
                }
            } else {
                Memory key = iterator.getMemoryKey();
                invoker.call(item, key, userData);
            }
        }
        return true;
    }

    public static boolean array_walk_recursive(Environment env, TraceInfo trace, @Reference Memory input,
                                               Memory callback, Memory userData)
            throws Throwable {
        if (!expectingReference(env, trace, input, "array_walk_recursive"))
            return false;

        if (!expecting(env, trace, 1, input, ARRAY))
            return false;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return false;

        Set<Integer> used = new HashSet<Integer>();
        used.add(input.getPointer());
        return _array_walk_recursive(env, trace, input, invoker, userData, used);
    }

    public static boolean array_walk_recursive(Environment env, TraceInfo trace, Memory input,
                                               Memory callback)
            throws Throwable {
        return array_walk_recursive(env, trace, input, callback, Memory.NULL);
    }

    public static Memory array_flip(Environment env, TraceInfo trace, Memory input) {
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(env, false, false);
        while (iterator.next())
            result.put(ArrayMemory.toKey(iterator.getValue()), iterator.getMemoryKey());

        return result.toConstant();
    }

    public static Memory array_reverse(Environment env, TraceInfo trace, Memory input, boolean saveKeys) {
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(env, false, false);

        Memory[] values = new Memory[array.size()];
        Object[] keys = saveKeys ? new Object[values.length] : null;

        int i = 0;
        while (iterator.next()) {
            if (saveKeys)
                keys[i] = iterator.getKey();

            values[i] = iterator.getValue().toImmutable();
            i++;
        }

        for (i = values.length - 1; i >= 0; i--) {
            if (saveKeys)
                result.put(keys[i], values[i]);
            else
                result.add(values[i]);
        }
        return result.toConstant();
    }

    public static Memory array_reverse(Environment env, TraceInfo trace, Memory input) {
        return array_reverse(env, trace, input, false);
    }

    public static Memory array_rand(Environment env, TraceInfo trace, Memory input, int numReq) {
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);

        int size = array.size();
        if (size < numReq || numReq < 1) {
            env.warning(trace, "array_rand(): Second argument has to be between 1 and the number of elements in the array");
            return Memory.NULL;
        }

        int i;
        if (numReq == 1) {
            return array.getRandomElementKey(MathFunctions.RANDOM);
        } else {
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            Set<Integer> rands = new HashSet<Integer>();
            for (i = 0; i < numReq; i++) {
                while (!rands.add(MathFunctions.rand(0, size - 1).toInteger())) ;
            }

            ArrayMemory result = new ArrayMemory();
            i = -1;
            while (iterator.next()) {
                i++;
                if (!rands.contains(i))
                    continue;
                result.add(iterator.getMemoryKey());
                if (result.size() >= numReq)
                    break;
            }

            return result.toConstant();
        }
    }

    public static Memory array_rand(Environment env, TraceInfo trace, Memory input) {
        return array_rand(env, trace, input, 1);
    }

    public static Memory array_pop(Environment env, TraceInfo trace, @Reference Memory input) {
        if (!expectingReference(env, trace, input, "array_pop"))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        Memory value = input.toValue(ArrayMemory.class).pop();
        return value == null ? Memory.NULL : value.toImmutable();
    }

    public static Memory array_push(Environment env, TraceInfo trace, @Reference Memory input, Memory var,
                                    Memory... args) {
        if (!expectingReference(env, trace, input, "array_push"))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        array.add(var);
        if (args != null)
            for (Memory arg : args)
                array.add(arg);

        return LongMemory.valueOf(array.size());
    }

    public static Memory array_shift(Environment env, TraceInfo trace, @Reference Memory input) {
        if (!expectingReference(env, trace, input, "array_shift"))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        Memory value = input.toValue(ArrayMemory.class).shift();
        return value == null ? Memory.NULL : value.toImmutable();
    }

    public static Memory array_unshift(Environment env, TraceInfo trace, @Reference Memory input, Memory var,
                                       Memory... args) {
        if (!expectingReference(env, trace, input, "array_unshift"))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        Memory[] tmp = args == null ? new Memory[]{var} : new Memory[args.length + 1];
        if (args != null) {
            tmp[0] = var;
            System.arraycopy(args, 0, tmp, 1, args.length);
        }

        array.unshift(tmp);
        return LongMemory.valueOf(array.size());
    }

    public static Memory array_values(Environment env, TraceInfo trace, Memory input) {
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        Memory[] values = input.toValue(ArrayMemory.class).values();
        return ArrayMemory.of(values).toConstant();
    }

    public static Memory array_keys(Environment env, TraceInfo trace, Memory input) {
        return array_keys(env, trace, input, null);
    }

    public static Memory array_keys(Environment env, TraceInfo trace, Memory input, Memory search) {
        return array_keys(env, trace, input, search, false);
    }

    public static Memory array_keys(Environment env, TraceInfo trace, Memory input, Memory search, boolean strict) {
        if (!expecting(env, trace, 1, input, ARRAY))
            return Memory.NULL;

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(env, false, false);

        while (iterator.next()) {
            if (search == null) {
                result.add(iterator.getMemoryKey());
            } else {
                if (strict && iterator.getValue().identical(search)) {
                    result.add(iterator.getMemoryKey());
                } else if (iterator.getValue().equal(search)) {
                    result.add(iterator.getMemoryKey());
                }
            }
        }

        return result.toConstant();
    }

    public static Memory array_pad(Environment env, TraceInfo trace, Memory input,
                                   int padSize, Memory padValue) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            ArrayMemory array = input.toValue(ArrayMemory.class);
            int size = array.size();
            int needSize = Math.abs(padSize);

            ArrayMemory result = input.toImmutable().toValue(ArrayMemory.class);
            if (size == needSize)
                return result;

            result.checkCopied();
            int count = needSize - size;
            if (padSize >= 0) {
                for (int i = 0; i < needSize - size; i++)
                    result.add(padValue);
            } else {
                result.unshift(padValue, count);
            }
            return result;
        } else
            return Memory.NULL;
    }

    public static Memory array_product(Environment env, TraceInfo trace, Memory input) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            Memory result = Memory.CONST_INT_1;
            while (iterator.next()) {
                result = result.mul(iterator.getValue());
            }
            return result;
        } else
            return Memory.NULL;
    }

    public static Memory array_sum(Environment env, TraceInfo trace, Memory input) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            Memory result = Memory.CONST_INT_0;
            while (iterator.next()) {
                result = result.plus(iterator.getValue());
            }
            return result;
        } else
            return Memory.NULL;
    }

    public static Memory array_change_key_case(Environment env, TraceInfo trace, Memory input, int _case) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            ArrayMemory result = new ArrayMemory();
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            while (iterator.next()) {
                Object key = iterator.getKey();
                if (key instanceof String) {
                    String str = (String) key;
                    str = _case == 0 ? str.toLowerCase() : str.toUpperCase();
                    result.put(str, iterator.getValue().toImmutable());
                } else
                    result.put(key, iterator.getValue().toImmutable());
            }
            return result.toConstant();
        }
        return Memory.NULL;
    }

    public static Memory array_change_key_case(Environment env, TraceInfo trace, Memory input) {
        return array_change_key_case(env, trace, input, 0);
    }

    public static Memory array_chunk(Environment env, TraceInfo trace, Memory input, int size,
                                     boolean saveKeys) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            if (size < 1) {
                env.warning(trace, "array_chunk(): Size parameter expected to be greater than 0");
                return Memory.NULL;
            }

            ArrayMemory result = new ArrayMemory();
            ArrayMemory item = null;
            ForeachIterator iterator = input.getNewIterator(env, false, false);

            int i = 0;
            while (iterator.next()) {
                if (i == 0) {
                    item = new ArrayMemory();
                    result.add(item);
                }

                if (saveKeys) {
                    item.put(iterator.getKey(), iterator.getValue().toImmutable());
                } else
                    item.add(iterator.getValue().toImmutable());

                if (++i == size)
                    i = 0;
            }

            return result.toConstant();
        } else
            return Memory.NULL;
    }

    public static Memory array_chunk(Environment env, TraceInfo trace, Memory input, int size) {
        return array_chunk(env, trace, input, size, false);
    }

    public static Memory array_column(Environment env, TraceInfo trace, Memory input, Memory columnKey,
                                      Memory indexKey) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            if (columnKey.isNull() && indexKey.isNull())
                return array_values(env, trace, input);

            ArrayMemory result = new ArrayMemory();
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            while (iterator.next()) {
                Memory value = iterator.getValue();
                if (indexKey.isNull()) {
                    result.add(value.valueOfIndex(columnKey).toImmutable());
                } else {
                    if (columnKey.isNull())
                        result.refOfIndex(value.valueOfIndex(indexKey)).assign(value.toImmutable());
                    else
                        result.refOfIndex(value.valueOfIndex(indexKey)).assign(value.valueOfIndex(columnKey));
                }
            }
            return result.toConstant();
        } else
            return Memory.NULL;
    }

    public static Memory array_column(Environment env, TraceInfo trace, Memory input, Memory columnKey) {
        return array_column(env, trace, input, columnKey, Memory.NULL);
    }

    public static Memory array_combine(Environment env, TraceInfo trace, Memory keys, Memory values) {
        if (expecting(env, trace, 1, keys, ARRAY) && expecting(env, trace, 2, values, ARRAY)) {
            ArrayMemory _keys = keys.toValue(ArrayMemory.class);
            ArrayMemory _values = values.toValue(ArrayMemory.class);
            int size1 = _keys.size();
            int size2 = _values.size();

            if (size1 != size2) {
                env.warning(trace, "array_combine(): Both parameters should have an equal number of elements");
                return Memory.FALSE;
            }

            ArrayMemory result = new ArrayMemory();
            if (size1 == 0)
                return result.toConstant();

            ForeachIterator iteratorKeys = _keys.getNewIterator(env, false, false);
            ForeachIterator iteratorValues = _values.getNewIterator(env, false, false);
            while (iteratorKeys.next()) {
                iteratorValues.next();
                result.refOfIndex(iteratorKeys.getValue())
                        .assign(iteratorValues.getValue().toImmutable());
            }
            return result.toConstant();
        } else
            return Memory.FALSE;
    }

    public static Memory array_count_values(Environment env, TraceInfo trace, Memory input) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            ArrayMemory counts = new ArrayMemory();
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            boolean warning = false;
            while (iterator.next()) {
                Memory value = iterator.getValue();
                switch (value.getRealType()) {
                    case INT:
                    case STRING:
                        Memory count = counts.getOrCreate(value);
                        count.assign(count.inc());
                        break;
                    default:
                        if (!warning) {
                            env.warning(trace, "array_count_values(): Can only count STRING and INTEGER values!");
                            warning = true;
                        }
                }
            }
            return counts.toConstant();
        } else
            return Memory.NULL;
    }

    public static Memory array_search(Environment env, TraceInfo trace, Memory needle, Memory input) {
        return array_search(env, trace, needle, input, false);
    }

    public static Memory array_search(Environment env, TraceInfo trace, Memory needle, Memory input, boolean strict) {
        if (expecting(env, trace, 1, input, ARRAY)) {
            ForeachIterator iterator = input.getNewIterator(env, false, false);

            while (iterator.next()) {
                Memory value = iterator.getValue();

                if (strict && needle.identical(value)) {
                    return iterator.getMemoryKey();
                } else if (needle.equal(value)) {
                    return iterator.getMemoryKey();
                }
            }

            return Memory.FALSE;
        } else {
            return Memory.FALSE;
        }
    }

    private static Memory _range_double(Environment env, TraceInfo trace, double low, double high, double step) {
        ArrayMemory result = new ArrayMemory();
        double value;
        long i = 0;
        boolean error_occurred = false;

        if (step < 0.0) {
            step *= -1;
        }

        if (low > high) {    /* Negative steps */
            if (low - high < step || step <= 0) {
                error_occurred = true;
            } else {
                for (value = low; value >= high; value = low - (++i * step)) {
                    result.add(value);
                }
            }
        } else if (high > low) {        /* Positive steps */
            if (high - low < step || step <= 0) {
                error_occurred = true;
            } else {
                for (value = low; value <= high; value = low + (++i * step)) {
                    result.add(value);
                }
            }
        } else {
            result.add(low);
        }
        if (error_occurred) {
            env.warning(trace, "range(): step exceeds the specified range");
            return Memory.FALSE;
        }
        return result.toConstant();
    }

    private static Memory _range_long(Environment env, TraceInfo trace, long low, long high, long step) {
        ArrayMemory result = new ArrayMemory();
        boolean error_occurred = false;

        if (step < 0) {
            step *= -1;
        }

        if (low > high) {               /* Negative steps */
            if (low - high < step || step <= 0) {
                error_occurred = true;
            } else {
                for (; low >= high; low -= step) {
                    result.add(low);
                }
            }
        } else if (high > low) {        /* Positive steps */
            if (high - low < step || step <= 0) {
                error_occurred = true;
            } else {
                for (; low <= high; low += step) {
                    result.add(low);
                }
            }
        } else {
            result.add(low);
        }
        if (error_occurred) {
            env.warning(trace, "range(): step exceeds the specified range");
            return Memory.FALSE;
        }
        return result.toConstant();
    }

    public static Memory range(Environment env, TraceInfo trace, Memory low, Memory high, Memory step) {
        if (low.getRealType() == Memory.Type.DOUBLE || high.getRealType() == Memory.Type.DOUBLE || step.getRealType() == Memory.Type.DOUBLE) {
            return _range_double(env, trace, low.toDouble(), high.toDouble(), step.toDouble());
        } else {
            return _range_long(env, trace, low.toLong(), high.toLong(), step.toLong());
        }
    }

    public static Memory range(Environment env, TraceInfo trace, Memory low, Memory high) {
        return range(env, trace, low, high, Memory.CONST_INT_1);
    }

    public static Memory array_fill(int start, int num, Memory value) {
        ArrayMemory result = new ArrayMemory();

        if (start >= 0) {
            for (int i = start; i < start + num; i++) {
                if (start == 0) {
                    result.add(value);
                } else {
                    result.refOfIndex(i).assign(value);
                }
            }
        } else {
            result.refOfIndex(start).assign(value);

            for (int i = 0; i < num - 1; i++) {
                result.refOfIndex(i).assign(value);
            }
        }

        return result.toConstant();
    }

    public static Memory array_fill_keys(Environment env, TraceInfo trace, Memory keys, Memory value) {
        if (expecting(env, trace, 1, keys, ARRAY)) {
            ForeachIterator iterator = keys.getNewIterator(env);
            ArrayMemory result = new ArrayMemory();

            while (iterator.next()) {
                result.refOfIndex(iterator.getValue()).assign(value.toImmutable());
            }

            return result.toConstant();
        } else {
            return new ArrayMemory().toConstant();
        }
    }

    public static Memory array_unique(Environment env, TraceInfo trace, Memory array) {
        return array_unique(env, trace, array, 0);
    }

    public static Memory array_unique(Environment env, TraceInfo trace, Memory array, int flag) {
        if (expecting(env, trace, 1, array, ARRAY)) {
            Set<Object> keys = new TreeSet<>();
            ArrayMemory newArray = new ArrayMemory();

            ForeachIterator iterator = array.getNewIterator(env);

            while (iterator.next()) {
                Object key;

                switch (flag) {
                    case 1:
                        key = iterator.getValue().toLong();
                        break;
                    case 2:
                        key = iterator.getValue().toBinaryString();
                        break;
                    case 5:
                        key = iterator.getValue().toString();
                        break;
                    case 0:
                    default:
                        key = iterator.getValue().toString();
                        break;
                }

                if (keys.add(key)) {
                    newArray.put(iterator.getKey(), iterator.getValue().toImmutable());
                }
            }

            return newArray.toConstant();
        }

        return Memory.NULL;
    }

    public static Memory array_replace(Environment env, TraceInfo trace, Memory array, Memory replacement, Memory... replacements) {
        if (expecting(env, trace, 1, array, ARRAY) && expecting(env, trace, 2, replacement, ARRAY)) {
            ArrayMemory result = array.toValue(ArrayMemory.class).duplicate();

            for (int i = 0; i < (replacements == null ? 0 : replacements.length) + 1; i++) {
                if (i > 0) {
                    replacement = replacements[i - 1];

                    if (!expecting(env, trace, i + 2, replacement, ARRAY)) {
                        return Memory.NULL;
                    }
                }

                ForeachIterator iterator = replacement.getNewIterator(env);

                while (iterator.next()) {
                    result.put(iterator.getKey(), iterator.getValue().toImmutable());
                }
            }

            return result;
        }

        return Memory.NULL;
    }

    interface ArrayDiffCallback {
        boolean apply(Memory keyValue, Memory value, Memory keyComparable, Memory comparable) throws Throwable;
    }

    protected static Memory _array_diff_impl(Environment env, TraceInfo trace, Memory array1, Memory array, Memory[] arrays, ArrayDiffCallback callback) throws Throwable {
        if (expecting(env, trace, 1, array1, ARRAY) && expecting(env, trace, 2, array, ARRAY)) {
            ForeachIterator iterator = array1.getNewIterator(env);

            ArrayMemory result = new ArrayMemory();

            while (iterator.next()) {
                Memory value = iterator.getValue();

                boolean exists = false;

                for (int i = 0; i < (arrays == null ? 0 : arrays.length) +1; i++){
                    if (i > 0) {
                        array = arrays[i - 1];

                        if (!expecting(env, trace, i + 2, array, ARRAY)) {
                            return Memory.NULL;
                        }
                    }

                    ForeachIterator newIterator = array.getNewIterator(env);

                    while (newIterator.next()) {
                        if (callback.apply(iterator.getMemoryKey(), value, newIterator.getMemoryKey(), newIterator.getValue())) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) break;
                }

                if (!exists) {
                    result.put(iterator.getKey(), value.toImmutable());
                }
            }

            return result.toConstant();
        }

        return Memory.NULL;
    }

    public static Memory array_diff(Environment env, TraceInfo trace, Memory array1, Memory array, Memory... arrays) throws Throwable {
        return _array_diff_impl(env, trace, array1, array, arrays, new ArrayDiffCallback() {
            @Override
            public boolean apply(Memory keyValue, Memory value, Memory keyComparable, Memory comparable) {
                return value.toString().equals(comparable.toString());
            }
        });
    }

    public static Memory array_diff_key(Environment env, TraceInfo trace, Memory array1, Memory array, Memory... arrays) throws Throwable {
        return _array_diff_impl(env, trace, array1, array, arrays, new ArrayDiffCallback() {
            @Override
            public boolean apply(Memory keyValue, Memory value, Memory keyComparable, Memory comparable) {
                return keyValue.equal(keyComparable);
            }
        });
    }

    public static Memory array_diff_assoc(Environment env, TraceInfo trace, Memory array1, Memory array, Memory... arrays) throws Throwable {
        return _array_diff_impl(env, trace, array1, array, arrays, new ArrayDiffCallback() {
            @Override
            public boolean apply(Memory keyValue, Memory value, Memory keyComparable, Memory comparable) {
                return keyValue.equal(keyComparable) && value.toString().equals(comparable.toString());
            }
        });
    }

    protected static Memory _array_udiff_impl(Environment env, TraceInfo trace, Memory array1, Memory array, final boolean assoc, Memory... arrays) throws Throwable {
        if (arrays == null) {
            expectingCallback(env, trace, 3, Memory.NULL);
            return Memory.NULL;
        }

        Memory callback = arrays[arrays.length - 1];
        final Invoker expectingCallback = expectingCallback(env, trace, arrays.length + 2, callback);

        if (expectingCallback != null) {
            return _array_diff_impl(env, trace, array1, array, Arrays.copyOf(arrays, arrays.length - 1), new ArrayDiffCallback() {
                @Override
                public boolean apply(Memory keyValue, Memory value, Memory keyComparable, Memory comparable) throws Throwable {
                    if (assoc && keyValue.notEqual(keyComparable)) {
                        return false;
                    }

                    Memory memory = expectingCallback.call(value, comparable);
                    return memory.toInteger() == 0;
                }
            });
        } else {
            return Memory.NULL;
        }
    }

    public static Memory array_udiff(Environment env, TraceInfo trace, Memory array1, Memory array, Memory... arrays) throws Throwable {
        return _array_udiff_impl(env, trace, array1, array, false, arrays);
    }

    public static Memory array_udiff_assoc(Environment env, TraceInfo trace, Memory array1, Memory array, Memory... arrays) throws Throwable {
        return _array_udiff_impl(env, trace, array1, array, true, arrays);
    }

    public static Memory array_slice(Environment env, TraceInfo trace, Memory array, int offset) {
        return array_slice(env, trace, array, offset, Memory.NULL);
    }

    public static Memory array_slice(Environment env, TraceInfo trace, Memory array, int offset, Memory length) {
        return array_slice(env, trace, array, offset, length, false);
    }

    public static Memory array_slice(Environment env, TraceInfo trace, Memory array, int offset, Memory length, boolean preserveKeys) {
        if (expecting(env, trace, 1, array, ARRAY)) {
            ArrayMemory arrayMemory = array.toValue(ArrayMemory.class);

            try {
                return (length.isNull()
                        ? arrayMemory.slice(offset, preserveKeys)
                        : arrayMemory.slice(offset, length.toInteger(), preserveKeys)).toConstant();
            } catch (IndexOutOfBoundsException e) {
                return new ArrayMemory().toConstant();
            }
        } else {
            return Memory.NULL;
        }
    }

    protected static Comparator<Memory> makeComparatorForUSort(final Environment env, final Invoker invoker) {
        return new Comparator<Memory>() {
            @Override
            public int compare(Memory o1, Memory o2) {
                if (invoker == null) {
                    return 0;
                }

                try {
                    return invoker.call(o1, o2).toInteger();
                } catch (Throwable throwable) {
                    env.forwardThrow(throwable);
                    return 0;
                }
            }
        };
    }

    protected static Comparator makeComparatorForSort(int flags, final boolean revert) {
        switch (flags) {
            case ArrayConstants.SORT_NUMERIC:
                return new Comparator<Memory>() {
                    @Override
                    public int compare(Memory o1, Memory o2) {
                        o1 = o1.toNumeric();
                        o2 = o2.toNumeric();

                        return o1.equal(o2) ? 0 : (o1.greater(o2) ? 1 : -1) * (revert ? -1 : 1);
                    }
                };

            case SORT_STRING | SORT_FLAG_CASE:
            case SORT_LOCALE_STRING | SORT_FLAG_CASE:
                return new Comparator<Memory>() {
                    @Override
                    public int compare(Memory o1, Memory o2) {
                        String s1 = o1.toString();
                        String s2 = o2.toString();

                        int cmp = s1.compareToIgnoreCase(s2);
                        return cmp == 0 ? 0 : (cmp < 0 ? -1 : 1) * (revert ? -1 : 1);
                    }
                };

            case SORT_STRING:
            case SORT_LOCALE_STRING:
                return new Comparator<Memory>() {
                    @Override
                    public int compare(Memory o1, Memory o2) {
                        String s1 = o1.toString();
                        String s2 = o2.toString();

                        int cmp = s1.compareTo(s2);
                        return cmp == 0 ? 0 : (cmp < 0 ? -1 : 1) * (revert ? -1 : 1);
                    }
                };

            case SORT_NATURAL | SORT_FLAG_CASE:
                return new NaturalOrderComparator(true, revert);

            case SORT_NATURAL:
                return new NaturalOrderComparator(false, revert);

            case ArrayConstants.SORT_REGULAR:
            default:
                return new Comparator<Memory>() {
                    @Override
                    public int compare(Memory o1, Memory o2) {
                        return o1.compareTo(o2)  * (revert ? -1 : 1);
                    }
                };
        }
    }

    protected static boolean _sort_impl(Environment env, TraceInfo trace, @Reference Memory array, int flags, boolean revert) {
        return _sort_impl(env, trace, array, makeComparatorForSort(flags, revert));
    }

    protected static boolean _sort_impl(Environment env, TraceInfo trace, @Reference Memory array, Comparator comparator) {
        if (expecting(env, trace, 1, array, ARRAY)) {
            ArrayMemory arrayMemory = array.toValue(ArrayMemory.class);

            Memory[] values = arrayMemory.values();
            arrayMemory.clear();

            try {
                Arrays.sort(values, comparator);
            } catch (IllegalArgumentException e) {
                return false;
            }

            for (Memory value : values) {
                arrayMemory.add(value);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean sort(Environment env, TraceInfo trace, @Reference Memory array) {
        return sort(env, trace, array, 0);
    }

    public static boolean sort(Environment env, TraceInfo trace, @Reference Memory array, int flags) {
        return _sort_impl(env, trace, array, flags, false);
    }

    public static boolean rsort(Environment env, TraceInfo trace, @Reference Memory array) {
        return rsort(env, trace, array, 0);
    }

    public static boolean rsort(Environment env, TraceInfo trace, @Reference Memory array, int flags) {
        return _sort_impl(env, trace, array, flags, true);
    }

    protected static boolean _asort_impl(Environment env, TraceInfo trace, @Reference Memory array, int flags, boolean revert) {
        return _asort_impl(env, trace, array, makeComparatorForSort(flags, revert));
    }

    protected static boolean _asort_impl(Environment env, TraceInfo trace, @Reference Memory array, Comparator comparator) {
        if (expecting(env, trace, 1, array, ARRAY)) {
            ArrayMemory arrayMemory = array.toValue(ArrayMemory.class);

            Memory[] values = new Memory[arrayMemory.size()];
            ForeachIterator iterator = arrayMemory.getNewIterator(env);

            int i = 0;
            while (iterator.next()) {
                values[i++] = new KeyValueMemory(iterator.getMemoryKey(), iterator.getValue());
            }

            arrayMemory.clear();

            try {
                Arrays.sort(values, comparator);
            } catch (IllegalArgumentException e) {
                return false;
            }

            for (Memory value : values) {
                arrayMemory.add(value);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean asort(Environment env, TraceInfo trace, @Reference Memory array) {
        return asort(env, trace, array, 0);
    }

    public static boolean asort(Environment env, TraceInfo trace, @Reference Memory array, int flags) {
        return _asort_impl(env, trace, array, flags, false);
    }

    public static boolean arsort(Environment env, TraceInfo trace, @Reference Memory array) {
        return arsort(env, trace, array, 0);
    }

    public static boolean arsort(Environment env, TraceInfo trace, @Reference Memory array, int flags) {
        return _asort_impl(env, trace, array, flags, true);
    }

    public static boolean natsort(Environment env, TraceInfo trace, @Reference Memory array) {
        return _asort_impl(env, trace, array, SORT_NATURAL, false);
    }

    public static boolean natcasesort(Environment env, TraceInfo trace, @Reference Memory array) {
        return _asort_impl(env, trace, array, SORT_NATURAL | SORT_FLAG_CASE, false);
    }

    protected static boolean _ksort_impl(Environment env, TraceInfo trace, @Reference Memory array, int flags, boolean revert) {
        return _ksort_impl(env, trace, array, makeComparatorForSort(flags, revert));
    }

    protected static boolean _ksort_impl(Environment env, TraceInfo trace, @Reference Memory array, Comparator comparator) {
        if (expecting(env, trace, 1, array, ARRAY)) {
            ArrayMemory arrayMemory = array.toValue(ArrayMemory.class);

            Memory[] values = new Memory[arrayMemory.size()];
            ForeachIterator iterator = arrayMemory.getNewIterator(env);

            int i = 0;
            while (iterator.next()) {
                values[i++] = iterator.getMemoryKey();
            }

            //arrayMemory.clear();

            try {
                Arrays.sort(values, comparator);
            } catch (IllegalArgumentException e) {
                return false;
            }

            ArrayMemory newArray = new ArrayMemory();

            for (Memory value : values) {
                newArray.refOfIndex(value).assign(arrayMemory.valueOfIndex(value).toImmutable());
                arrayMemory.remove(value);
            }

            array.assign(newArray);
            return true;
        } else {
            return false;
        }
    }

    public static boolean ksort(Environment env, TraceInfo trace, @Reference Memory array) {
        return ksort(env, trace, array, 0);
    }

    public static boolean ksort(Environment env, TraceInfo trace, @Reference Memory array, int flags) {
        return _ksort_impl(env, trace, array, flags, false);
    }

    public static boolean krsort(Environment env, TraceInfo trace, @Reference Memory array) {
        return krsort(env, trace, array, 0);
    }

    public static boolean krsort(Environment env, TraceInfo trace, @Reference Memory array, int flags) {
        return _ksort_impl(env, trace, array, flags, true);
    }

    protected static boolean _usort_impl(Environment env, TraceInfo trace, @Reference Memory array, Memory callback) {
        Invoker invoker = expectingCallback(env, trace, 2, callback);

        if (invoker != null) {
            return _sort_impl(env, trace, array, makeComparatorForUSort(env, invoker));
        } else {
            return false;
        }
    }

    public static boolean usort(Environment env, TraceInfo trace, @Reference Memory array, Memory callback) {
        return _usort_impl(env, trace, array, callback);
    }

    protected static boolean _uasort_impl(Environment env, TraceInfo trace, @Reference Memory array, Memory callback) {
        Invoker invoker = expectingCallback(env, trace, 2, callback);

        if (invoker != null) {
            return _asort_impl(env, trace, array, makeComparatorForUSort(env, invoker));
        } else {
            return false;
        }
    }

    public static boolean uasort(Environment env, TraceInfo trace, @Reference Memory array, Memory callback) {
        return _uasort_impl(env, trace, array, callback);
    }

    protected static boolean _uksort_impl(Environment env, TraceInfo trace, @Reference Memory array, Memory callback) {
        Invoker invoker = expectingCallback(env, trace, 2, callback);

        if (invoker != null) {
            return _ksort_impl(env, trace, array, makeComparatorForUSort(env, invoker));
        } else {
            return false;
        }
    }

    public static boolean uksort(Environment env, TraceInfo trace, @Reference Memory array, Memory callback) {
        return _uksort_impl(env, trace, array, callback);
    }
}
