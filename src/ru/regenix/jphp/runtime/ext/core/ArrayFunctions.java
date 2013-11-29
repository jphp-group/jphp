package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import static ru.regenix.jphp.runtime.annotation.Reflection.Reference;

public class ArrayFunctions extends FunctionsContainer {

    public static Memory array_merge(Environment env, TraceInfo trace, Memory array, Memory[] arrays){
        if (!array.isArray()){
            env.warning("Argument %s is not an array", 1);
            return Memory.NULL;
        }

        if (arrays == null || arrays.length == 0)
            return array;

        ArrayMemory result = (ArrayMemory)array.toImmutable();

        int i = 2;
        for(Memory el : arrays){
            if (!el.isArray()){
                env.warning("Argument %s is not an array", i);
                return Memory.NULL;
            }
            result.merge((ArrayMemory)el);
            i++;
        }

        return result.toConstant();
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
