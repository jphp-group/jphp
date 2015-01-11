package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.annotation.Runtime;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.RecursiveException;
import php.runtime.ext.core.MathFunctions;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.Invoker;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;

import java.util.HashSet;
import java.util.Set;

public class ArrayFunctions extends FunctionsContainer {

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean in_array(Environment env, TraceInfo trace, Memory needle, @Runtime.Reference Memory array,
                                   boolean strict){
        if (expecting(env, trace, 2, array, Memory.Type.ARRAY)){
            ForeachIterator iterator = array.getNewIterator(env, false, false);
            while (iterator.next()){
                if (strict){
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
    public static boolean in_array(Environment env, TraceInfo trace, Memory needle, @Runtime.Reference Memory array){
        return in_array(env, trace, needle, array, false);
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean array_key_exists(Environment env, TraceInfo trace, Memory key, @Runtime.Reference Memory array){
        if (expecting(env, trace, 2, array, Memory.Type.ARRAY)){
            ArrayMemory tmp = array.toValue(ArrayMemory.class);
            return tmp.get(key) != null;
        } else
            return false;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static boolean key_exists(Environment env, TraceInfo trace, Memory key, @Runtime.Reference Memory array){
        return array_key_exists(env, trace, key, array);
    }

    public static Memory reset(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)){
                ArrayMemory memory = array.toValue(ArrayMemory.class);
                return memory.resetCurrentIterator().toImmutable();
            }
        }
        return Memory.FALSE;
    }

    public static Memory next(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)){
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

    public static Memory prev(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)){
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

    public static Memory current(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)){
                Memory value = array.toValue(ArrayMemory.class).getCurrentIterator().getValue();
                return value == null ? Memory.FALSE : value.toImmutable();
            }
        }
        return Memory.FALSE;
    }

    public static Memory key(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)){
                Memory value = array.toValue(ArrayMemory.class).getCurrentIterator().getMemoryKey();
                return value == null ? Memory.FALSE : value;
            }
        }
        return Memory.FALSE;
    }

    public static Memory pos(Environment env, TraceInfo trace, @Runtime.Reference Memory array) {
        return current(env, trace, array);
    }

    public static Memory end(Environment env, TraceInfo trace, @Runtime.Reference Memory array) {
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)) {
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

    public static Memory each(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)) {
                ForeachIterator iterator = array.toValue(ArrayMemory.class).getCurrentIterator();
                if (iterator.next()) {
                    Memory value = iterator.getValue().toImmutable();
                    Memory key   = iterator.getMemoryKey();

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

    private static Memory _array_merge(Environment env, TraceInfo trace, boolean recursive, Memory array, Memory... arrays){
        if (!array.isArray()){
            env.warning(trace, "Argument %s is not an array", 1);
            return Memory.NULL;
        }

        if (arrays == null || arrays.length == 0)
            return array;

        ArrayMemory result = (ArrayMemory)array.toImmutable();

        int i = 2;
        Set<Integer> used = recursive ? new HashSet<Integer>() : null;
        for(Memory el : arrays){
            if (!el.isArray()){
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

        return result.toConstant();
    }

    public static Memory array_merge(Environment env, TraceInfo trace, Memory array, Memory... arrays){
        return _array_merge(env, trace, false, array, arrays);
    }

    public static Memory array_merge_recursive(Environment env, TraceInfo trace, Memory array, Memory... arrays){
        try {
            return _array_merge(env, trace, true, array, arrays);
        } catch (RecursiveException e){
            env.warning(trace, "recursion detected");
            return Memory.NULL;
        }
    }

    public static boolean shuffle(Environment env, TraceInfo trace, @Runtime.Reference Memory value){
        if (expectingReference(env, trace, value) && expecting(env, trace, 1, value, Memory.Type.ARRAY)){
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
        if (!expecting(env, trace, 2, _array, Memory.Type.ARRAY))
            return Memory.NULL;

        if (arrays == null) {
            iterators = new ForeachIterator[] { _array.getNewIterator(env, false, false) };
        } else {
            iterators = new ForeachIterator[1 + arrays.length];
            iterators[0] = _array.getNewIterator(env, false, false);
            for(int i = 0; i < arrays.length; i++){
                if (!expecting(env, trace, i + 3, arrays[i], Memory.Type.ARRAY))
                    return Memory.NULL;
                iterators[i + 1] = arrays[i].getNewIterator(env, false, false);
            }
        }

        Memory[] args = new Memory[iterators.length];

        while (true){
            int i = 0;
            boolean done = true;
            for(ForeachIterator iterator : iterators){
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

    public static Memory array_filter(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback)
            throws Throwable {
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY)){
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
        while (iterator.next()){
            Object key = iterator.getKey();
            Memory value = iterator.getValue();
            if (invoker == null){
                if (!value.toBoolean())
                    continue;
            } else if (!invoker.call(value).toBoolean())
                continue;

            result.put(key, value.toImmutable());
        }

        return result.toConstant();
    }

    public static Memory array_filter(Environment env, TraceInfo trace, @Runtime.Reference Memory input)
            throws Throwable {
        return array_filter(env, trace, input, null);
    }

    public static Memory array_reduce(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback,
                                      Memory initial)
            throws Throwable {
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        if (array.size() == 0)
            return Memory.NULL;

        Memory result = initial;
        ForeachIterator iterator = array.getNewIterator(env, true, false);
        while (iterator.next()){
            Memory el = iterator.getValue();
            result = invoker.call(result, el);
        }

        return result.toValue();
    }

    public static Memory array_reduce(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback)
            throws Throwable {
        return array_reduce(env, trace, input, callback, Memory.NULL);
    }

    public static boolean array_walk(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback,
                                     Memory userData) throws Throwable {
        if (!expectingReference(env, trace, input))
            return false;

        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return false;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return false;

        ForeachIterator iterator = input.getNewIterator(env, true, false);
        while (iterator.next()){
            Memory item = iterator.getValue();
            Memory key  = iterator.getMemoryKey();
            invoker.call(item, key, userData);
        }
        return true;
    }

    public static boolean array_walk(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback)
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
        while (iterator.next()){
            Memory item = iterator.getValue();
            if (item.isArray()){
                if (used.add(item.getPointer())) {
                    boolean result = _array_walk_recursive(env, trace, item, invoker, userData, used);
                    used.remove(item.getPointer());
                    if (!result)
                        return false;
                } else {
                    env.warning(trace, "array_walk_recursive(): recursion detected");
                }
            } else {
                Memory key  = iterator.getMemoryKey();
                invoker.call(item, key, userData);
            }
        }
        return true;
    }

    public static boolean array_walk_recursive(Environment env, TraceInfo trace, @Runtime.Reference Memory input,
                                                Memory callback, Memory userData)
            throws Throwable {
        if (!expectingReference(env, trace, input))
            return false;

        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return false;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return false;

        Set<Integer> used = new HashSet<Integer>();
        used.add(input.getPointer());
        return _array_walk_recursive(env, trace, input, invoker, userData, used);
    }

    public static boolean array_walk_recursive(Environment env, TraceInfo trace, @Runtime.Reference Memory input,
                                               Memory callback)
            throws Throwable {
        return array_walk_recursive(env, trace, input, callback, Memory.NULL);
    }

    public static Memory array_flip(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(env, false, false);
        while (iterator.next())
            result.put(ArrayMemory.toKey(iterator.getValue()), iterator.getMemoryKey());

        return result.toConstant();
    }

    public static Memory array_reverse(Environment env, TraceInfo trace, @Runtime.Reference Memory input, boolean saveKeys){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
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

        for(i = values.length - 1; i >= 0; i--){
            if (saveKeys)
                result.put(keys[i], values[i]);
            else
                result.add(values[i]);
        }
        return result.toConstant();
    }

    public static Memory array_reverse(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        return array_reverse(env, trace, input, false);
    }

    public static Memory array_rand(Environment env, TraceInfo trace, @Runtime.Reference Memory input, int numReq){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);

        int size = array.size();
        if (size < numReq || numReq < 1){
            env.warning(trace, "array_rand(): Second argument has to be between 1 and the number of elements in the array");
            return Memory.NULL;
        }

        int i;
        if (numReq == 1){
            return array.getRandomElementKey(MathFunctions.RANDOM);
        } else {
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            Set<Integer> rands = new HashSet<Integer>();
            for(i = 0; i < numReq; i++){
                while (!rands.add( MathFunctions.rand(0, size - 1).toInteger() ));
            }

            ArrayMemory result = new ArrayMemory();
            i = -1;
            while (iterator.next()){
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

    public static Memory array_rand(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        return array_rand(env, trace, input, 1);
    }

    public static Memory array_pop(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (!expectingReference(env, trace, input))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        Memory value = input.toValue(ArrayMemory.class).pop();
        return value == null ? Memory.NULL : value.toImmutable();
    }

    public static Memory array_push(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory var,
                                    Memory... args){
        if (!expectingReference(env, trace, input))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        array.add(var);
        if (args != null)
            for (Memory arg : args)
                array.add(arg);

        return LongMemory.valueOf(array.size());
    }

    public static Memory array_shift(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (!expectingReference(env, trace, input))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        Memory value = input.toValue(ArrayMemory.class).shift();
        return value == null ? Memory.NULL : value.toImmutable();
    }

    public static Memory array_unshift(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory var,
                                    Memory... args){
        if (!expectingReference(env, trace, input))
            return Memory.NULL;
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        Memory[] tmp = args == null ? new Memory[] { var } : new Memory[args.length + 1];
        if (args != null){
            tmp[0] = var;
            System.arraycopy(args, 0, tmp, 1, args.length);
        }

        array.unshift(tmp);
        return LongMemory.valueOf(array.size());
    }

    public static Memory array_values(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        Memory[] values = input.toValue(ArrayMemory.class).values();
        return new ArrayMemory(true, values).toConstant();
    }

    public static Memory array_keys(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(env, false, false);
        while (iterator.next())
            result.add(iterator.getMemoryKey());

        return result.toConstant();
    }

    public static Memory array_pad(Environment env, TraceInfo trace, @Runtime.Reference Memory input,
                                   int padSize, Memory padValue){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)){
            ArrayMemory array = input.toValue(ArrayMemory.class);
            int size = array.size();
            int needSize = Math.abs(padSize);

            ArrayMemory result = input.toImmutable().toValue(ArrayMemory.class);
            if (size == needSize)
                return result;

            result.checkCopied();
            int count = needSize - size;
            if (padSize >= 0) {
                for(int i = 0; i < needSize - size; i++)
                    result.add(padValue);
            } else {
                result.unshift(padValue, count);
            }
            return result;
        } else
            return Memory.NULL;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_product(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)) {
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            Memory result = Memory.CONST_INT_1;
            while (iterator.next()){
                result = result.mul(iterator.getValue());
            }
            return result;
        } else
            return Memory.NULL;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_sum(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)) {
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            Memory result = Memory.CONST_INT_0;
            while (iterator.next()){
                result = result.plus(iterator.getValue());
            }
            return result;
        } else
            return Memory.NULL;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_change_key_case(Environment env, TraceInfo trace, @Runtime.Reference Memory input, int _case){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)){
            ArrayMemory result = new ArrayMemory();
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            while (iterator.next()){
                Object key = iterator.getKey();
                if (key instanceof String){
                    String str = (String)key;
                    str = _case == 0 ? str.toLowerCase() : str.toUpperCase();
                    result.put(str, iterator.getValue().toImmutable());
                } else
                    result.put(key, iterator.getValue().toImmutable());
            }
            return result.toConstant();
        }
        return Memory.NULL;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_change_key_case(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        return array_change_key_case(env, trace, input, 0);
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_chunk(Environment env, TraceInfo trace, @Runtime.Reference Memory input, int size,
                                     boolean saveKeys){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)){
            if (size < 1) {
                env.warning(trace, "array_chunk(): Size parameter expected to be greater than 0");
                return Memory.NULL;
            }

            ArrayMemory result = new ArrayMemory();
            ArrayMemory item = null;
            ForeachIterator iterator = input.getNewIterator(env, false, false);

            int i = 0;
            while (iterator.next()){
                if (i == 0){
                    item = new ArrayMemory();
                    result.add(item);
                }

                if (saveKeys){
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

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_chunk(Environment env, TraceInfo trace, @Runtime.Reference Memory input, int size){
        return array_chunk(env, trace, input, size, false);
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_column(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory columnKey,
                                      Memory indexKey){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)){
            if (columnKey.isNull() && indexKey.isNull())
                return array_values(env, trace, input);

            ArrayMemory result = new ArrayMemory();
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            while (iterator.next()){
                Memory value = iterator.getValue();
                if (indexKey.isNull()){
                    result.add(value.valueOfIndex(columnKey).toImmutable());
                } else {
                    if (columnKey.isNull())
                        result.refOfIndex( value.valueOfIndex(indexKey) ).assign( value.toImmutable() );
                    else
                        result.refOfIndex( value.valueOfIndex(indexKey) ).assign(value.valueOfIndex(columnKey));
                }
            }
            return result.toConstant();
        } else
            return Memory.NULL;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_column(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory columnKey){
        return array_column(env, trace, input, columnKey, Memory.NULL);
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_combine(Environment env, TraceInfo trace, @Runtime.Reference Memory keys,
                                       @Runtime.Reference Memory values){
        if (expecting(env, trace, 1, keys, Memory.Type.ARRAY) && expecting(env, trace, 2, values, Memory.Type.ARRAY)){
            ArrayMemory _keys = keys.toValue(ArrayMemory.class);
            ArrayMemory _values = keys.toValue(ArrayMemory.class);
            int size1 = _keys.size();
            int size2 = _values.size();

            if (size1 != size2){
                env.warning(trace, "array_combine(): Both parameters should have an equal number of elements");
                return Memory.FALSE;
            }

            ArrayMemory result = new ArrayMemory();
            if (size1 == 0)
                return result.toConstant();

            ForeachIterator iteratorKeys = _keys.getNewIterator(env, false, false);
            ForeachIterator iteratorValues = _values.getNewIterator(env, false, false);
            while (iteratorKeys.next()){
                iteratorValues.next();
                result.refOfIndex(iteratorKeys.getValue())
                        .assign(iteratorValues.getValue().toImmutable());
            }
            return result.toConstant();
        } else
            return Memory.FALSE;
    }

    @Runtime.Immutable(ignoreRefs = true)
    public static Memory array_count_values(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (expecting(env, trace, 1, input, Memory.Type.ARRAY)){
            ArrayMemory counts = new ArrayMemory();
            ForeachIterator iterator = input.getNewIterator(env, false, false);
            boolean warning = false;
            while (iterator.next()){
                Memory value = iterator.getValue();
                switch (value.getRealType()){
                    case INT:
                    case STRING:
                        Memory count = counts.getOrCreate(value);
                        count.assign(count.inc());
                        break;
                    default:
                        if (!warning){
                            env.warning(trace, "array_count_values(): Can only count STRING and INTEGER values!");
                            warning = true;
                        }
                }
            }
            return counts.toConstant();
        } else
            return Memory.NULL;
    }

    private static Memory _range_double(Environment env, TraceInfo trace, double low, double high, double step){
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

    private static Memory _range_long(Environment env, TraceInfo trace, long low, long high, long step){
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

    public static Memory range(Environment env, TraceInfo trace, Memory low, Memory high, Memory step){
    	if (low.getRealType() == Memory.Type.DOUBLE || high.getRealType() == Memory.Type.DOUBLE || step.getRealType() == Memory.Type.DOUBLE) {
	        return _range_double(env, trace, low.toDouble(), high.toDouble(), step.toDouble());
	    } else {
	        return _range_long(env, trace, low.toLong(), high.toLong(), step.toLong());
	    }
    }

    public static Memory range(Environment env, TraceInfo trace, Memory low, Memory high){
        return range(env, trace, low, high, Memory.CONST_INT_1);
    }
}
