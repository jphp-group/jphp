package ru.regenix.jphp.compiler.common.compile;


import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;

import java.lang.annotation.Annotation;

public class CompileFunction {
    public final String name;

    public Method[] methods;

    private Method methodVarArgs;
    private int methodVarArgsCount;

    public final boolean isImmutable;
    private int minArgs = Integer.MAX_VALUE;
    private int maxArgs = 0;

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

    public void addMethod(java.lang.reflect.Method method){
        if (method.isVarArgs()){
            if (methodVarArgs != null)
                throw new IllegalArgumentException("Cannot add two var-args methods");
            methodVarArgs = new Method(method);
            methodVarArgsCount = method.getParameterTypes().length;
            int count = 0;
            Class<?>[] types = method.getParameterTypes();
            for(int i = 0; i < types.length; i++){
                Class<?> type = types[i];
                if (type == Environment.class || type == TraceInfo.class)
                    continue;
                if (type == Memory[].class)
                    continue;

                count++;
            }
            if (count < minArgs)
                minArgs = count;

            maxArgs = Integer.MAX_VALUE;
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
        if (count < minArgs)
            minArgs = count;
        if (count > maxArgs)
            maxArgs = count;

        if (count >= methods.length){
            Method[] newMethods = new Method[methods.length * 2];
            System.arraycopy(methods, 0, newMethods, 0, methods.length);
            methods = newMethods;
        }

        if (methods[count] != null)
            throw new IllegalArgumentException("Method " + name + " with " + count + " args already exists");

        methods[count] = new Method(method);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Method find(int paramCount) {
        if (paramCount < minArgs)
            return null;

        Method method = null;
        if (paramCount < methods.length && paramCount >= 0)
            method = methods[paramCount];

        if (method == null && methodVarArgsCount <= paramCount)
            method = methodVarArgs;

        if (method == null && paramCount > maxArgs) {
            for(int i = methods.length - 1; i >= 0; i--){
                method = methods[i];
                if (method != null)
                    return method;
            }
        }

        return method;
    }

    public static class Method {
        public final java.lang.reflect.Method method;
        public final MemoryUtils.Converter<?>[] converters;
        public final Annotation[][] parameterAnnotations;
        public final Class<?>[] parameterTypes;
        public final Class<?> resultType;

        public final boolean[] references;

        public Method(java.lang.reflect.Method method) {
            this.method = method;
            converters = MemoryUtils.getConverters(parameterTypes = method.getParameterTypes());
            parameterAnnotations = method.getParameterAnnotations();
            resultType = method.getReturnType();

            references = new boolean[parameterTypes.length];
            int i = 0;

            for (Class<?> type : parameterTypes){
                for(Annotation annotation : parameterAnnotations[i]){
                    if (annotation instanceof Reflection.Reference)
                        references[i] = true;
                }
                i++;
            }
        }
    }
}
