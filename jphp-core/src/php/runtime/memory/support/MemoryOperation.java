package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.operation.*;
import php.runtime.memory.support.operation.array.IntegerArrayMemoryOperation;
import php.runtime.memory.support.operation.list.MemoryListMemoryOperation;
import php.runtime.memory.support.operation.list.StringListMemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.ReflectionUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

abstract public class MemoryOperation<T> {
    protected final static Map<Class<?>, MemoryOperation> operations = new HashMap<Class<?>, MemoryOperation>();
    protected final static Map<ParametrizedClass, MemoryOperation> genericOperations = new HashMap<ParametrizedClass, MemoryOperation>();

    abstract public Class<?>[] getOperationClasses();

    abstract public T convert(Environment env, TraceInfo trace, Memory arg);
    abstract public Memory unconvert(Environment env, TraceInfo trace, T arg);

    public void releaseConverted(Environment env, TraceInfo info, T arg) {
        // nop
    }

    public Type[] getGenericTypes() {
        return null;
    }

    public void applyTypeHinting(ParameterEntity parameter) {
        // nop
    }

    public static MemoryOperation get(final Class<?> type, Type genericTypes) {
        if (genericTypes instanceof ParameterizedTypeImpl) {
            return genericOperations.get(new ParametrizedClass(type, ((ParameterizedTypeImpl) genericTypes).getActualTypeArguments()));
        } else {
            MemoryOperation operation = operations.get(type);

            if (operation == null) {
                if (IObject.class.isAssignableFrom(type)) {
                    return new MemoryOperation() {
                        @Override
                        public Class<?>[] getOperationClasses() {
                            return new Class<?>[]{IObject.class};
                        }

                        @Override
                        public Object convert(Environment env, TraceInfo trace, Memory arg) {
                            return arg.toObject((Class<? extends IObject>) type);
                        }

                        @Override
                        public Memory unconvert(Environment env, TraceInfo trace, Object arg) {
                            return ObjectMemory.valueOf((IObject) arg);
                        }

                        @Override
                        public void applyTypeHinting(ParameterEntity parameter) {
                            parameter.setType(ReflectionUtils.getClassName(type));
                        }
                    };
                }
            }

            return operation;
        }
    }

    @SuppressWarnings("unchecked")
    public static void register(MemoryOperation operation) {
        if (operation.getGenericTypes() != null) {
            for (Class<?> type : operation.getOperationClasses()) {
                genericOperations.put(new ParametrizedClass(type, operation.getGenericTypes()), operation);
            }
        } else {
            for (Class<?> type : operation.getOperationClasses()) {
                operations.put(type, operation);
            }
        }
    }

    public static class ParametrizedClass<T> {
        protected Class<T> clazz;
        protected Type[] genericTypes;

        public ParametrizedClass(Class<T> clazz, Type[] genericTypes) {
            this.clazz = clazz;
            this.genericTypes = genericTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ParametrizedClass)) return false;

            ParametrizedClass that = (ParametrizedClass) o;

            if (!clazz.equals(that.clazz)) return false;
            if (!Arrays.equals(genericTypes, that.genericTypes)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + Arrays.hashCode(genericTypes);
            return result;
        }
    }

    static {
        register(new MemoryMemoryOperation());

        register(new BooleanMemoryOperation());

        register(new LongMemoryOperation());
        register(new IntegerMemoryOperation());
        register(new ShortMemoryOperation());
        register(new ByteMemoryOperation());

        register(new DoubleMemoryOperation());
        register(new FloatMemoryOperation());

        register(new StringMemoryOperation());

        register(new EnvironmentMemoryOperation());
        register(new InvokerMemoryOperation());

        register(new InputStreamMemoryOperation());
        register(new OutputStreamMemoryOperation());
        register(new FileMemoryOperation());
        register(new ByteArrayInputStreamMemoryOperation());

        register(new PatternMemoryOperation());

        register(new StringListMemoryOperation());
        register(new MemoryListMemoryOperation());

        register(new IntegerArrayMemoryOperation());
        register(new StringListMemoryOperation());
    }
}
