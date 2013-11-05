package ru.regenix.jphp.compiler.common.compile;


import java.lang.reflect.Method;

public class CompileFunction {
    public final String name;
    public final Method method;

    public CompileFunction(String name, Method method) {
        this.name = name;
        this.method = method;
    }
}
