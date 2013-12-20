package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.exceptions.RecursiveException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.spl.Countable;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ArrayFunctions extends FunctionsContainer {

    private static int recursive_count(Environment env, TraceInfo trace, ArrayMemory array, Set<Integer> used){
        ForeachIterator iterator = array.foreachIterator(false, false);
        int size = array.size();
        while (iterator.next()){
            Memory el = iterator.getCurrentValue();
            if (el.isArray()){
                if (used == null)
                    used = new HashSet<Integer>();

                int pointer = el.getPointer();

                if (!used.add(pointer)){
                    env.warning(trace, "recursion detected");
                } else {
                    size += recursive_count(env, trace, array, used);
                }
                used.remove(pointer);
            }
        }
        return size;
    }

    @Runtime.Immutable
    public static Memory count(Environment env, TraceInfo trace, Memory var, int mode){
        switch (var.type){
            case ARRAY:
                if (mode == 1){
                    return LongMemory.valueOf(recursive_count(env, trace, var.toValue(ArrayMemory.class), null));
                } else
                    return LongMemory.valueOf(var.toValue(ArrayMemory.class).size());
            case NULL: return Memory.CONST_INT_0;
            case OBJECT:
                ObjectMemory objectMemory = var.toValue(ObjectMemory.class);
                if (objectMemory.value instanceof Countable){
                    long size = ((Countable) objectMemory.value).count(env, "", var).toLong();
                    return LongMemory.valueOf(size);
                } else {
                    return Memory.CONST_INT_1;
                }
            default:
                return Memory.CONST_INT_1;
        }
    }

    @Runtime.Immutable
    public static Memory count(Environment env, TraceInfo trace, Memory var){
        return count(env, trace, var, 0);
    }

    @Runtime.Immutable
    public static Memory sizeof(Environment env, TraceInfo trace, Memory var){
        return count(env, trace, var, 0);
    }

    @Runtime.Immutable
    public static Memory sizeof(Environment env, TraceInfo trace, Memory var, int mode){
        return count(env, trace, var, mode);
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
                    return iterator.getCurrentValue().toImmutable();
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
                    return iterator.getCurrentValue().toImmutable();
                else
                    return Memory.FALSE;
            }
        }
        return Memory.FALSE;
    }

    public static Memory current(Environment env, TraceInfo trace, @Runtime.Reference Memory array){
        if (expectingReference(env, trace, array)){
            if (expecting(env, trace, 1, array, Memory.Type.ARRAY)){
                Memory value = array.toValue(ArrayMemory.class).getCurrentIterator().getCurrentValue();
                return value == null ? Memory.FALSE : value.toImmutable();
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
                    return iterator.getCurrentValue().toImmutable();
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
                    Memory value = iterator.getCurrentValue().toImmutable();
                    Memory key   = iterator.getCurrentMemoryKey();

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
            throws InvocationTargetException, IllegalAccessException {
        Invoker invoker = expectingCallback(env, trace, 1, callback);
        if (invoker == null) {
            return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory();
        ForeachIterator[] iterators;
        if (!expecting(env, trace, 2, _array, Memory.Type.ARRAY))
            return Memory.NULL;

        if (arrays == null) {
            iterators = new ForeachIterator[] { _array.getNewIterator(false, false) };
        } else {
            iterators = new ForeachIterator[1 + arrays.length];
            iterators[0] = _array.getNewIterator(false, false);
            for(int i = 0; i < arrays.length; i++){
                if (!expecting(env, trace, i + 3, arrays[i], Memory.Type.ARRAY))
                    return Memory.NULL;
                iterators[i + 1] = arrays[i].getNewIterator(false, false);
            }
        }

        Memory[] args = new Memory[iterators.length];

        while (true){
            int i = 0;
            boolean done = true;
            for(ForeachIterator iterator : iterators){
                if (iterator.next()) {
                    args[i] = iterator.getCurrentValue();
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
            throws InvocationTargetException, IllegalAccessException {
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY)){
            return Memory.NULL;
        }

        Invoker invoker = null;
        if (callback != null && callback != Memory.NULL) {
            invoker = expectingCallback(env, trace, 2, callback);
            if (invoker == null)
                return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(true, false);
        while (iterator.next()){
            Object key = iterator.getCurrentKey();
            Memory value = iterator.getCurrentValue();
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
            throws InvocationTargetException, IllegalAccessException {
        return array_filter(env, trace, input, null);
    }

    public static Memory array_reduce(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback,
                                      Memory initial)
            throws InvocationTargetException, IllegalAccessException {
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        if (array.size() == 0)
            return Memory.NULL;

        Memory result = initial;
        ForeachIterator iterator = array.getNewIterator(true, false);
        while (iterator.next()){
            Memory el = iterator.getCurrentValue();
            result = invoker.call(result, el);
        }

        return result.toValue();
    }

    public static Memory array_reduce(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback)
            throws InvocationTargetException, IllegalAccessException {
        return array_reduce(env, trace, input, callback, Memory.NULL);
    }

    public static boolean array_walk(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback,
                                     Memory userData) throws InvocationTargetException, IllegalAccessException {
        if (!expectingReference(env, trace, input))
            return false;

        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return false;

        Invoker invoker = expectingCallback(env, trace, 2, callback);
        if (invoker == null)
            return false;

        ForeachIterator iterator = input.getNewIterator(true, false);
        while (iterator.next()){
            Memory item = iterator.getCurrentValue();
            Memory key  = iterator.getCurrentMemoryKey();
            invoker.call(item, key, userData);
        }
        return true;
    }

    public static boolean array_walk(Environment env, TraceInfo trace, @Runtime.Reference Memory input, Memory callback)
            throws InvocationTargetException, IllegalAccessException {
        return array_walk(env, trace, input, callback, Memory.NULL);
    }

    public static boolean _array_walk_recursive(Environment env, TraceInfo trace,
                                                Memory input,
                                                Invoker invoker, Memory userData, Set<Integer> used)
            throws InvocationTargetException, IllegalAccessException {
        if (used == null)
            used = new HashSet<Integer>();

        ForeachIterator iterator = input.getNewIterator(true, false);
        while (iterator.next()){
            Memory item = iterator.getCurrentValue();
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
                Memory key  = iterator.getCurrentMemoryKey();
                invoker.call(item, key, userData);
            }
        }
        return true;
    }

    public static boolean array_walk_recursive(Environment env, TraceInfo trace, @Runtime.Reference Memory input,
                                                Memory callback, Memory userData)
            throws InvocationTargetException, IllegalAccessException {
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
            throws InvocationTargetException, IllegalAccessException {
        return array_walk_recursive(env, trace, input, callback, Memory.NULL);
    }

    public static Memory array_flip(Environment env, TraceInfo trace, @Runtime.Reference Memory input){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(false, false);
        while (iterator.next())
            result.put(ArrayMemory.toKey(iterator.getCurrentValue()), iterator.getCurrentMemoryKey());

        return result.toConstant();
    }

    public static Memory array_reverse(Environment env, TraceInfo trace, @Runtime.Reference Memory input, boolean saveKeys){
        if (!expecting(env, trace, 1, input, Memory.Type.ARRAY))
            return Memory.NULL;

        ArrayMemory array = input.toValue(ArrayMemory.class);
        ArrayMemory result = new ArrayMemory();
        ForeachIterator iterator = input.getNewIterator(false, false);

        Memory[] values = new Memory[array.size()];
        Object[] keys = saveKeys ? new Object[values.length] : null;

        int i = 0;
        while (iterator.next()) {
            if (saveKeys)
                keys[i] = iterator.getCurrentKey();

            values[i] = iterator.getCurrentValue().toImmutable();
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
            ForeachIterator iterator = input.getNewIterator(false, false);
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
                result.add(iterator.getCurrentMemoryKey());
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
        ForeachIterator iterator = input.getNewIterator(false, false);
        while (iterator.next())
            result.add(iterator.getCurrentMemoryKey());

        return result.toConstant();
    }
}
