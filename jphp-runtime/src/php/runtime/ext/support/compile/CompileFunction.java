package php.runtime.ext.support.compile;


import php.runtime.Memory;
import php.runtime.annotation.Runtime;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CompileFunction {
    public String name;

    public Method[] methods;

    private Method methodVarArgs;
    private int methodVarArgsCount;

    private int minArgs = Integer.MAX_VALUE;
    private int maxArgs = 0;

    public CompileFunction(String name) {
        this.name = name;
        this.methods = new Method[5];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompileFunction)) return false;

        CompileFunction that = (CompileFunction) o;
        return name.equals(that.name);
    }

    public int getMinArgs() {
        return minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public Method addMethod(java.lang.reflect.Method method){
        return addMethod(method, false);
    }

    public void mergeFunction(CompileFunction function) {
        if (methods.length < function.methods.length) {
            methods = Arrays.copyOf(methods, function.methods.length);
        }

        for (int i = 0; i < function.methods.length; i++) {
            if (function.methods[i] != null) {
                methods[i] = function.methods[i];
            }
        }

        if (function.methodVarArgs != null) {
            methodVarArgs = function.methodVarArgs;
        }

        this.minArgs = Math.min(this.minArgs, function.minArgs);
        this.maxArgs = Math.max(this.maxArgs, function.maxArgs);
    }

    public Method addMethod(java.lang.reflect.Method method, boolean asImmutable){
        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        if (method.isVarArgs()){
            if (methodVarArgs != null)
                throw new IllegalArgumentException("Cannot add two var-args methods");
            methodVarArgs = new Method(method, 0, asImmutable);
            int count = 0;
            Class<?>[] types = method.getParameterTypes();
            for(int i = 0; i < types.length; i++){
                Class<?> type = types[i];
                if (type == Environment.class || type == TraceInfo.class)
                    continue;
                if (type == Memory[].class)
                    continue;

                boolean ignore = false;
                for (Annotation el : paramAnnotations[i]){
                    if (el.annotationType().equals(Runtime.GetLocals.class)) {
                        if (type != ArrayMemory.class)
                            throw new RuntimeException("@Runtime.GetLocals: param type must be ArrayMemory");
                        ignore = true;
                        break;
                    }
                }

                if (!ignore) count++;
            }
            if (count < minArgs)
                minArgs = count;

            methodVarArgsCount = count;

            maxArgs = Integer.MAX_VALUE;
            if (methodVarArgsCount < methods.length && methods[methodVarArgsCount] != null)
                throw new IllegalArgumentException("Method '"+ name +"' with " + methodVarArgsCount + " args already exists");
        }

        Class<?>[] types = method.getParameterTypes();
        int count = 0;

        for(int i = 0; i < types.length; i++){
            Class<?> type = types[i];
            if (type == Environment.class || type == TraceInfo.class)
                continue;

            boolean ignore = false;
            for (Annotation el : paramAnnotations[i]){
                if (el.annotationType().equals(Runtime.GetLocals.class)) {
                    if (type != ArrayMemory.class)
                        throw new RuntimeException("@Runtime.GetLocals: param type must be ArrayMemory");
                    ignore = true;
                    break;
                }
            }

            if (!ignore) count++;
        }

        if (count < minArgs)
            minArgs = count;
        if (count > maxArgs)
            maxArgs = count;

        if (count >= methods.length){
            Method[] newMethods = new Method[Math.max(methods.length * 2, count + 1)];
            System.arraycopy(methods, 0, newMethods, 0, methods.length);
            methods = newMethods;
        }

        if (methods[count] != null)
            throw new IllegalArgumentException("Method " + name + " with " + count + " args already exists");

        return methods[count] = createMethod(method, count, asImmutable);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean delete(int paramCount) {
        if (paramCount < minArgs)
            return false;

        Method method = null;
        if (paramCount < methods.length && paramCount >= 0) {
            methods[paramCount] = null;
            return true;
        }

        if (methodVarArgsCount <= paramCount) {
            methodVarArgs = null;
            return true;
        }

        if (paramCount > maxArgs) {
            for(int i = methods.length - 1; i >= 0; i--){
                method = methods[i];
                if (method != null) {
                    methods[i] = null;
                    return true;
                }
            }
        }

        return false;
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

    protected Method createMethod(java.lang.reflect.Method method, int count, boolean asImmutable) {
        return new Method(method, count, asImmutable);
    }

    public static class Method {
        public boolean isImmutable;
        public final boolean isImmutableIgnoreRefs;
        public final java.lang.reflect.Method method;
        public final MemoryUtils.Converter<?>[] converters;
        public final Annotation[][] parameterAnnotations;
        public final Class<?>[] parameterTypes;
        public final Class<?> resultType;

        public final boolean[] references;
        public final boolean[] mutableValues;

        public final int argsCount;

        public Method(java.lang.reflect.Method method, int argsCount, boolean _asImmutable) {
            this.argsCount = argsCount;
            this.method = method;

            parameterTypes = method.getParameterTypes();
            converters     = new MemoryUtils.Converter[parameterTypes.length];

            int i = 0;
            for (Class<?> type : parameterTypes) {
                converters[i++] = getConverterForArgument(type);
            }

            if (method.isVarArgs())
                converters[converters.length - 1] = null;

            parameterAnnotations = method.getParameterAnnotations();
            resultType = method.getReturnType();
            isImmutable = method.isAnnotationPresent(Runtime.Immutable.class) || _asImmutable;
            if (isImmutable){
                Runtime.Immutable annotation = method.getAnnotation(Runtime.Immutable.class);
                isImmutableIgnoreRefs = annotation != null && annotation.ignoreRefs();
            } else
                isImmutableIgnoreRefs = false;

            references    = new boolean[parameterTypes.length];
            mutableValues = new boolean[parameterTypes.length];
            i = 0;

            for (Class<?> type : parameterTypes){
                for(Annotation annotation : parameterAnnotations[i]){
                    if (annotation.annotationType() == Runtime.Reference.class) {
                        references[i] = true;
                        if (!isImmutableIgnoreRefs)
                            isImmutable = false;
                    } else if (annotation.annotationType() == Runtime.MutableValue.class) {
                        mutableValues[i] = true;
                    }
                }
                i++;
            }

            if (resultType == void.class)
                isImmutable = false;
        }

        protected MemoryUtils.Converter<?> getConverterForArgument(Class<?> type) {
            return MemoryUtils.getConverter(type);
        }

        public boolean isPresentAnnotationOfParam(int index, Class<? extends Annotation> clazz){
            assert index >= 0 && index < parameterAnnotations.length;

            for (Annotation el : parameterAnnotations[index])
                if (el.annotationType().equals(clazz))
                    return true;

            return false;
        }

        public boolean isVarArg(){
            return method.isVarArgs();
        }

        public Memory call(Environment env, Memory... arguments) {
            Class<?>[] types = parameterTypes;
            Object[] passed = new Object[ types.length ];

            int i = 0;
            int j = 0;
            for(Class<?> clazz : types) {
                boolean isRef = references[i];
                boolean mutableValue = mutableValues[i];

                MemoryUtils.Converter<?> converter = converters[i];
                if (clazz == Memory.class) {
                    passed[i] = isRef ? arguments[j] : (mutableValue ? arguments[j].toImmutable() : arguments[j].toValue());
                    j++;
                } else if (converter != null) {
                    passed[i] = converter.run(arguments[j]);
                    j++;
                } else if (clazz == Environment.class) {
                    passed[i] = env;
                } else if (clazz == TraceInfo.class) {
                    passed[i] = env.trace();
                } else if (i == types.length - 1 && types[i] == Memory[].class){
                    Memory[] arg = new Memory[arguments.length - i + 1];
                    if (!isRef){
                        for(int k = 0; k < arg.length; k++)
                            arg[i] = arguments[i].toImmutable();
                    } else {
                        System.arraycopy(arguments, j, arg, 0, arg.length);
                    }
                    passed[i] = arg;
                    break;
                } else {
                    env.error(env.trace(), ErrorType.E_CORE_ERROR, "Cannot call this method dynamically");
                    passed[i] = Memory.NULL;
                }
                i++;
            }

            try {
                if (resultType == void.class){
                    method.invoke(null, passed);
                    return Memory.NULL;
                } else
                    return MemoryUtils.valueOf(method.invoke(null, passed));
            } catch (InvocationTargetException e){
                return env.__throwException(e);
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            }
        }
    }
}
