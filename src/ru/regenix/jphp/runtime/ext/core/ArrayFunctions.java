package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.exceptions.RecursiveException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.util.HashSet;
import java.util.Set;

import static ru.regenix.jphp.runtime.annotation.Reflection.Reference;

public class ArrayFunctions extends FunctionsContainer {

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
                used.add(el.getPointer(true));

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
        if (value.isReference() && value.isArray()){
            ArrayMemory array = value.toValue(ArrayMemory.class);
            array.shuffle(MathFunctions.RANDOM);
            return true;
        } else {
            return false;
        }
    }
}
