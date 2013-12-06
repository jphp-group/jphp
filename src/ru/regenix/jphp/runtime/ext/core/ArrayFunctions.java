package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.exceptions.RecursiveException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.spl.Countable;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.util.HashSet;
import java.util.Set;

import static ru.regenix.jphp.runtime.annotation.Reflection.Reference;

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

    private static Memory _array_merge(Environment env, TraceInfo trace, boolean recursive, Memory array, Memory[] arrays){
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

    public static Memory array_merge(Environment env, TraceInfo trace, Memory array, Memory[] arrays){
        return _array_merge(env, trace, false, array, arrays);
    }

    public static Memory array_merge_recursive(Environment env, TraceInfo trace, Memory array, Memory[] arrays){
        try {
            return _array_merge(env, trace, true, array, arrays);
        } catch (RecursiveException e){
            env.warning(trace, "recursion detected");
            return Memory.NULL;
        }
    }

    public static boolean shuffle(Environment env, TraceInfo trace, @Reference Memory value){
        if (expectingReference(env, trace, value) && expecting(env, trace, 1, value, Memory.Type.ARRAY)){
            ArrayMemory array = value.toValue(ArrayMemory.class);
            array.shuffle(MathFunctions.RANDOM);
            return true;
        } else {
            return false;
        }
    }
}
