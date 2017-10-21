package php.runtime.ext.support.compile;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

abstract public class FunctionsContainer {

    protected static boolean expectingReference(Environment env, TraceInfo trace, Memory memory, String funcName) {
        if (!memory.isReference()) {
            env.error(trace, (funcName == null ? "" : funcName + "() - ") + "Only variables can be passed by reference");
            return false;
        }
        return true;
    }

    /**
     * Use expectingReference(Environment env, TraceInfo trace, Memory memory, String funcName)
     */
    @Deprecated
    protected static boolean expectingReference(Environment env, TraceInfo trace, Memory memory) {
        return expectingReference(env, trace, memory, null);
    }

    protected static boolean expecting(Environment env, TraceInfo trace, int index, Memory memory, Memory.Type type) {
        if (memory.getRealType() != type) {
            env.warning(trace, "expects parameter " + index + " to be " + type.toString() +
                    ", " + memory.getRealType().toString() + " given");
            return false;
        }
        return true;
    }

    protected static boolean expectingImplement(Environment env, TraceInfo trace,
                                                int index, Memory memory, Class<?> clazz) {
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

    protected static Invoker expectingCallback(Environment env, TraceInfo trace, int index, Memory memory) {
        Invoker invoker = Invoker.create(env, memory);

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
        return new HashMap<>();
    }

    protected Method getNative(Class clazz, String name, Class<?>... argumentTypes) {
        try {
            return clazz.getDeclaredMethod(name, argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<CompileFunctionSpec> getFunctionSpecs() {
        Map<String, CompileFunctionSpec> result = new HashMap<>();

        for (Method method : getClass().getDeclaredMethods()) {
            int mod = method.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
                String name = method.getName();
                Name altName = method.getAnnotation(Name.class);

                if (altName != null) {
                    name = altName.value();
                }

                CompileFunctionSpec function = result.get(name);

                if (function == null) {
                    result.put(name, function = new CompileFunctionSpec(name));
                }

                function.addMethod(method);
            }
        }

        for (Map.Entry<String, Method> item : getNativeFunctions().entrySet()) {
            Method method = item.getValue();
            CompileFunctionSpec function = new CompileFunctionSpec(item.getKey(), true);

            result.put(item.getKey(), function);

            function.addMethod(method);
        }

        return result.values();
    }

    public Collection<CompileFunction> getFunctions() {
        Map<String, CompileFunction> result = new HashMap<String, CompileFunction>();

        for (Method method : getClass().getDeclaredMethods()) {
            int mod = method.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
                String name = method.getName();
                Name altName = method.getAnnotation(Name.class);
                if (altName != null)
                    name = altName.value();

                CompileFunction function = result.get(name);
                if (function == null)
                    result.put(name, function = new CompileFunction(name));

                function.addMethod(method);
            }
        }

        for (Map.Entry<String, Method> item : getNativeFunctions().entrySet()) {
            Method method = item.getValue();
            CompileFunction function = result.get(item.getKey());
            if (function == null) {
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
