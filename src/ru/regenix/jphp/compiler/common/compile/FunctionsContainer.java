package ru.regenix.jphp.compiler.common.compile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

abstract public class FunctionsContainer {

    protected Map<String, Method> getNativeFunctions() {
        return new HashMap<String, Method>();
    }

    protected Method getNative(Class clazz, String name, Class<?>... argumentTypes) {
        try {
            return clazz.getDeclaredMethod(name, argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<CompileFunction> getFunctions() {
        Map<String, CompileFunction> result = new HashMap<String, CompileFunction>();

        for(Method method : getClass().getDeclaredMethods()){
            if (Modifier.isStatic(method.getModifiers())){
                CompileFunction function = result.get(method.getName());
                if (function == null)
                    result.put(method.getName(), function = new CompileFunction(method.getName()));

                function.methods.add(method);
            }
        }

        for(Map.Entry<String, Method> item : getNativeFunctions().entrySet()){
            Method method = item.getValue();
            CompileFunction function = result.get(item.getKey());
            if (function == null){
                result.put(item.getKey(), function = new CompileFunction(item.getKey()));
            }
            function.methods.add(method);
        }

        return result.values();
    }
}
