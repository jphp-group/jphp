package ru.regenix.jphp.compiler.jvm.ext.core;

import ru.regenix.jphp.compiler.common.compile.*;
import ru.regenix.jphp.compiler.jvm.runtime.memory.DoubleMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.LongMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;

public class MathFunctions extends FunctionsContainer {

    public static Memory abs(Memory value){
        switch (value.type){
            case DOUBLE: return new DoubleMemory(Math.abs(value.toDouble()));
            case STRING: return abs(value.toNumeric());
            default: return LongMemory.valueOf(Math.abs(value.toLong()));
        }
    }

    public static double acos(Memory value){
        switch (value.type){
            case STRING: return acos(value.toNumeric());
            default: return Math.acos(value.toDouble());
        }
    }

    public static double ceil(Memory value){
        return Math.ceil(value.toDouble());
    }
}
