package ru.regenix.jphp.compiler.common.compile;


import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;

import java.lang.reflect.Method;

public class CompileFunction {
    public final String name;

    private Method[] methods;
    private Method methodVarArgs;
    private int methodVarArgsCount;

    public final boolean isImmutable;


    public CompileFunction(String name, boolean isImmutable) {
        this.name = name;
        this.isImmutable = isImmutable;
        this.methods = new Method[5];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompileFunction)) return false;

        CompileFunction that = (CompileFunction) o;
        return name.equals(that.name);
    }

    public void addMethod(Method method){
        if (method.isVarArgs()){
            if (methodVarArgs != null)
                throw new IllegalArgumentException("Cannot add two var-args methods");
            methodVarArgs = method;
            methodVarArgsCount = method.getParameterTypes().length;
            if (methodVarArgsCount < methods.length && methods[methodVarArgsCount] != null)
                throw new IllegalArgumentException("Method with " + methodVarArgsCount + " args already exists");
        }

        Class<?>[] types = method.getParameterTypes();
        int count = 0;
        for(int i = 0; i < types.length; i++){
            Class<?> type = types[i];
            if (type == Environment.class || type == TraceInfo.class)
                continue;

            count++;
        }

        if (count >= methods.length){
            Method[] newMethods = new Method[methods.length * 2];
            System.arraycopy(methods, 0, newMethods, 0, methods.length);
            methods = newMethods;
        }

        if (methods[count] != null)
            throw new IllegalArgumentException("Method " + name + " with " + count + " args already exists");

        methods[count] = method;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Method find(int paramCount) {
        Method method = null;
        if (paramCount < methods.length && paramCount >= 0)
            method = methods[paramCount];

        if (method == null && methodVarArgsCount <= paramCount)
            return methodVarArgs;

        return method;
    }
}
