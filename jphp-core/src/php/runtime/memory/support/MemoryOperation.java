package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.operation.*;
import php.runtime.memory.support.operation.array.IntegerArrayMemoryOperation;
import php.runtime.memory.support.operation.collection.HashSetMemoryOperation;
import php.runtime.memory.support.operation.collection.SetMemoryOperation;
import php.runtime.memory.support.operation.iterator.IterableMemoryOperation;
import php.runtime.memory.support.operation.collection.ListMemoryOperation;
import php.runtime.memory.support.operation.map.HashMapMemoryOperation;
import php.runtime.memory.support.operation.map.MapMemoryOperation;
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

    protected MemoryOperation<T> instance(Type... genericTypes) {
        return this;
    }

    @SuppressWarnings("unchecked")
    public static MemoryOperation get(final Class<?> type, Type genericTypes) {
        MemoryOperation operation = null;
        if (genericTypes instanceof ParameterizedTypeImpl) {
            operation = genericOperations.get(new ParametrizedClass(type, ((ParameterizedTypeImpl) genericTypes).getActualTypeArguments()));
        }

        if (operation == null) {
            operation = operations.get(type);

            if (operation == null) {
                if (IObject.class.isAssignableFrom(type)) {
                    return new MemoryOperation() {
                        @Override
                        public Class<?>[] getOperationClasses() {
                            return new Class<?>[]{IObject.class};
                        }

                        @Override
                        @SuppressWarnings("unchecked")
                        public Object convert(Environment env, TraceInfo trace, Memory arg) {
                            if (arg.isNull()) {
                                return null;
                            }

                            return arg.toObject((Class<? extends IObject>) type);
                        }

                        @Override
                        public Memory unconvert(Environment env, TraceInfo trace, Object arg) {
                            if (arg == null) {
                                return Memory.NULL;
                            }

                            return ObjectMemory.valueOf((IObject) arg);
                        }

                        @Override
                        public void applyTypeHinting(ParameterEntity parameter) {
                            parameter.setType(ReflectionUtils.getClassName(type));
                        }
                    };
                } else if (Enum.class.isAssignableFrom(type)) {
                    return new MemoryOperation() {
                        @Override
                        public Class<?>[] getOperationClasses() {
                            return new Class<?>[] { Enum.class };
                        }

                        @Override
                        @SuppressWarnings("unchecked")
                        public Object convert(Environment env, TraceInfo trace, Memory arg) {
                            return arg.isNull() ? null : Enum.valueOf((Class<? extends Enum>)type, arg.toString());
                        }

                        @Override
                        public Memory unconvert(Environment env, TraceInfo trace, Object arg) {
                            return arg == null ? Memory.NULL : StringMemory.valueOf(((Enum) arg).name());
                        }

                        @Override
                        public void applyTypeHinting(ParameterEntity parameter) {
                            parameter.setTypeEnum((Class<? extends Enum>)type);
                        }
                    };
                }
             }
        }

        if (operation == null) {
            return null;
        }

        if (genericTypes instanceof ParameterizedTypeImpl) {
            return operation.instance(((ParameterizedTypeImpl) genericTypes).getActualTypeArguments());
        }

        return operation;
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
        register(new VoidMemoryOperation());
        register(new MemoryMemoryOperation());
        register(new ArrayMemoryMemoryOperation());

        register(new BooleanMemoryOperation());

        register(new LongMemoryOperation());
        register(new IntegerMemoryOperation());
        register(new ShortMemoryOperation());
        register(new ByteMemoryOperation());

        register(new DoubleMemoryOperation());
        register(new FloatMemoryOperation());

        register(new StringMemoryOperation());

        register(new InvokerMemoryOperation());
        register(new ForeachIteratorMemoryOperation());

        register(new InputStreamMemoryOperation());
        register(new OutputStreamMemoryOperation());
        register(new FileMemoryOperation());
        register(new ByteArrayInputStreamMemoryOperation());

        register(new PatternMemoryOperation());
        register(new IntegerArrayMemoryOperation());

        register(new IterableMemoryOperation());

        register(new ListMemoryOperation());
        register(new SetMemoryOperation());
        register(new HashSetMemoryOperation());

        register(new MapMemoryOperation());
        register(new HashMapMemoryOperation());
    }
}
