package ru.regenix.jphp.compiler.common.compile;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CompileFunction {
    public final String name;
    public final List<Method> methods;
    public final boolean isImmutable;

    public CompileFunction(String name, boolean isImmutable) {
        this.name = name;
        this.methods = new ArrayList<Method>();
        this.isImmutable = isImmutable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompileFunction)) return false;

        CompileFunction that = (CompileFunction) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Method find(int paramCount) {
        for(Method method : methods){
            if (method.getParameterTypes().length == paramCount)
                return method;
        }
        return null;
    }
}
