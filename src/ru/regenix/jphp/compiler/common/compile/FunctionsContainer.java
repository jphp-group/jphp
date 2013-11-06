package ru.regenix.jphp.compiler.common.compile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

abstract public class FunctionsContainer {
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
        return result.values();
    }
}
