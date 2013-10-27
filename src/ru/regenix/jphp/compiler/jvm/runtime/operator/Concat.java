package ru.regenix.jphp.compiler.jvm.runtime.operator;

import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;

final public class Concat {

    private Concat() {
    }

    public static String call(String o1, Memory o2){
        return o1 + o2.toString();
    }

    public static String call(String o1, String o2){
        return o1 + o2;
    }

    public static String call(String o1, long o2){
        return o1 + String.valueOf(o2);
    }

    public static String call(String o1, double o2){
        return o1 + String.valueOf(o2);
    }
}
