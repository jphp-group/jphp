package php.runtime.ext.support.compile;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;
import php.runtime.Memory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract public class FunctionsContainer {

    protected static boolean expectingReference(Environment env, TraceInfo trace, Memory memory){
        if (!memory.isReference()){
            env.warning(trace, "Only variables can be passed by reference");
            return false;
        }
        return true;
    }

    protected static boolean expecting(Environment env, TraceInfo trace, int index, Memory memory, Memory.Type type){
        if (memory.getRealType() != type) {
            env.warning(trace, "expects parameter " + index + " to be " + type.toString() +
                    ", " + memory.getRealType().toString() + " given");
            return false;
        }
        return true;
    }

    protected static boolean expectingImplement(Environment env, TraceInfo trace,
                                                int index, Memory memory, Class<?> clazz){
        if (!memory.isObject() || !memory.toValue(ObjectMemory.class).getClass().isAssignableFrom(clazz)) {
            String given = memory.getRealType().toString();
            if (memory.isObject())
                given = memory.toValue(ObjectMemory.class).getReflection().getName();

            env.warning(trace, "expects parameter " + index + " must implement "
                    + (clazz.isInterface() ? "interface " : "") + clazz.getSimpleName() +
                    ", " + given + " given");
            return false;
        }
        return true;
    }

    protected static Invoker expectingCallback(Environment env, TraceInfo trace, int index, Memory memory){
        Invoker invoker = Invoker.valueOf(env, null, memory);
        if (invoker == null) {
            env.warning(trace, "expects parameter " + index + " to be valid callback");
            return null;
        }
        if (invoker.canAccess(env) != 0) {
            env.warning(trace, "expects parameter " + index + " to be valid callback, cannot access");
            return null;
        }
        return invoker;
    }

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
            if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())){
                CompileFunction function = result.get(method.getName());
                if (function == null)
                    result.put(method.getName(), function = new CompileFunction(method.getName()));

                function.addMethod(method);
            }
        }

        for(Map.Entry<String, Method> item : getNativeFunctions().entrySet()){
            Method method = item.getValue();
            CompileFunction function = result.get(item.getKey());
            if (function == null){
                result.put(item.getKey(),
                        function = new CompileFunction(
                                item.getKey()
                        )
                );
            }
            function.addMethod(method, true);
        }

        return result.values();
    }
}
