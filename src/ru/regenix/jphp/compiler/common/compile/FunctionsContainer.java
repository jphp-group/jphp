package ru.regenix.jphp.compiler.common.compile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

abstract public class FunctionsContainer {
    public List<CompileFunction> getFunctions() {
        List<CompileFunction> result = new ArrayList<CompileFunction>();
        for(Method method : getClass().getDeclaredMethods()){
            if (Modifier.isStatic(method.getModifiers())){
                result.add(new CompileFunction(method.getName(), method));
            }
        }
        return result;
    }
}
