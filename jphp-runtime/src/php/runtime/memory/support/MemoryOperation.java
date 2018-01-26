package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.operation.*;
import php.runtime.memory.support.operation.array.*;
import php.runtime.memory.support.operation.collection.EnumerationMemoryOperation;
import php.runtime.memory.support.operation.collection.HashSetMemoryOperation;
import php.runtime.memory.support.operation.collection.ListMemoryOperation;
import php.runtime.memory.support.operation.collection.SetMemoryOperation;
import php.runtime.memory.support.operation.iterator.IterableMemoryOperation;
import php.runtime.memory.support.operation.map.HashMapMemoryOperation;
import php.runtime.memory.support.operation.map.LinkedHashMapMemoryOperation;
import php.runtime.memory.support.operation.map.MapMemoryOperation;
import php.runtime.memory.support.operation.map.PropertiesMemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

abstract public class MemoryOperation<T> {
    protected final static Map<Class<?>, Class<? extends BaseWrapper>> wrappers = new HashMap<Class<?>, Class<? extends BaseWrapper>>();
    protected final static Map<Class<? extends BaseWrapper>, Class<?>> wrappersOut = new HashMap<Class<? extends BaseWrapper>, Class<?>>();

    protected final static Map<Class<?>, MemoryOperation> operations = new HashMap<Class<?>, MemoryOperation>();
    protected final static Map<ParametrizedClass, MemoryOperation> genericOperations = new HashMap<ParametrizedClass, MemoryOperation>();

    abstract public Class<?>[] getOperationClasses();

    final public T convertNoThrow(Environment env, TraceInfo trace, Memory arg) {
        try {
            return convert(env, trace, arg);
        } catch (Throwable throwable) {
            env.forwardThrow(throwable);
            return null;
        }
    }

    final public Memory unconvertNoThow(Environment env, TraceInfo trace, T arg) {
        try {
            return unconvert(env, trace, arg);
        } catch (Throwable throwable) {
            env.forwardThrow(throwable);
            return Memory.NULL;
        }
    }

    abstract public T convert(Environment env, TraceInfo trace, Memory arg) throws Throwable;
    abstract public Memory unconvert(Environment env, TraceInfo trace, T arg) throws Throwable;

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

    public static <T> Class<? extends BaseWrapper> getWrapper(Class<T> clazz) {
        return wrappers.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassOfWrapper(Class<? extends BaseWrapper<T>> clazz) {
        return (Class<T>) wrappersOut.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static MemoryOperation get(final Class<?> type, Type genericTypes) {
        return get(type, genericTypes, false);
    }

    @SuppressWarnings("unchecked")
    public static MemoryOperation get(final Class<?> type, Type genericTypes, boolean includeParents) {
        MemoryOperation operation = null;
        if (genericTypes instanceof ParameterizedType) {
            operation = genericOperations.get(new ParametrizedClass(type, ((ParameterizedType) genericTypes).getActualTypeArguments()));
        }

        if (operation == null) {
            operation = operations.get(type);

            if (operation == null) {
                if (type.isArray()) {
                    MemoryOperation arrayMemoryOperation = new ArrayMemoryOperation(type);
                    register(arrayMemoryOperation);

                    return arrayMemoryOperation;
                }

                if (Enum.class.isAssignableFrom(type)) {
                    return new MemoryOperation() {
                        @Override
                        public Class<?>[] getOperationClasses() {
                            return new Class<?>[]{Enum.class};
                        }

                        @Override
                        @SuppressWarnings("unchecked")
                        public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
                            return arg.isNull() ? null : Enum.valueOf((Class<? extends Enum>) type, arg.toString());
                        }

                        @Override
                        public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
                            return arg == null ? Memory.NULL : StringMemory.valueOf(((Enum) arg).name());
                        }

                        @Override
                        public void applyTypeHinting(ParameterEntity parameter) {
                            parameter.setTypeEnum((Class<? extends Enum>) type);
                        }
                    };
                }

                final Class<? extends BaseWrapper> wrapperClass = wrappers.get(type);

                if (wrapperClass != null) {
                    Constructor<BaseWrapper> constructor;
                    try {
                        constructor = (Constructor<BaseWrapper>) wrapperClass.getConstructor(Environment.class, type);
                    } catch (NoSuchMethodException e) {
                        try {
                            constructor = (Constructor<BaseWrapper>) wrapperClass.getConstructor(Environment.class, Object.class);
                        } catch (NoSuchMethodException e1) {
                            throw new CriticalException(e);
                        }
                    }

                    final Constructor<BaseWrapper> finalConstructor = constructor;

                    return new MemoryOperation() {
                        @Override
                        public Class<?>[] getOperationClasses() {
                            return new Class<?>[0];
                        }

                        @Override
                        public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
                            if (arg.isNull()) {
                                return null;
                            }

                            return arg.toObject(BaseWrapper.class).getWrappedObject();
                        }

                        @Override
                        public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
                            if (arg == null) {
                                return Memory.NULL;
                            }

                            Constructor<BaseWrapper> constructorContext = finalConstructor;

                            Class<? extends BaseWrapper> wrapperClassContext = wrapperClass;

                            if (arg.getClass() != type) {
                                wrapperClassContext = wrappers.get(arg.getClass());
                            }

                            if (wrapperClassContext != null && wrapperClassContext != wrapperClass) {
                                constructorContext = (Constructor<BaseWrapper>) wrapperClassContext.getConstructor(Environment.class, arg.getClass());
                            }

                            try {
                                BaseWrapper instance = constructorContext.newInstance(env, arg);
                                return ObjectMemory.valueOf(instance.__getOriginInstance());
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                throw new CriticalException(e);
                            }
                        }

                        @Override
                        public void applyTypeHinting(ParameterEntity parameter) {
                            parameter.setTypeNativeClass(type);
                        }
                    };
                } else if (IObject.class.isAssignableFrom(type)) {
                    return new MemoryOperation() {
                        @Override
                        public Class<?>[] getOperationClasses() {
                            return new Class<?>[]{IObject.class};
                        }

                        @Override
                        @SuppressWarnings("unchecked")
                        public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
                            if (arg.isNull()) {
                                return null;
                            }

                            return arg.toObject((Class<? extends IObject>) type);
                        }

                        @Override
                        public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
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
                } else {
                    Class<?> superType = type.getSuperclass();

                    if (Object.class != superType && (includeParents || type.isAnonymousClass())) {
                        return get(superType, type.getGenericSuperclass(), includeParents);
                    }
                }
             }
        }

        if (operation == null) {
            return null;
        }

        if (genericTypes instanceof ParameterizedType) {
            return operation.instance(((ParameterizedType) genericTypes).getActualTypeArguments());
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

    public static <T> void registerWrapper(Class<T> clazz, Class<? extends BaseWrapper> wrapperClass) {
        wrappers.put(clazz, wrapperClass);
        wrappersOut.put(wrapperClass, clazz);
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
        register(new ObjectMemoryOperation());

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
        register(new CharSequenceMemoryOperation());
        register(new CharacterMemoryOperation());

        register(new InvokerMemoryOperation());
        register(new ForeachIteratorMemoryOperation());

        register(new InputStreamMemoryOperation());
        register(new OutputStreamMemoryOperation());
        register(new FileMemoryOperation());
        register(new ByteArrayInputStreamMemoryOperation());

        register(new PatternMemoryOperation());

        register(new IterableMemoryOperation());

        register(new ListMemoryOperation());
        register(new EnumerationMemoryOperation());
        register(new SetMemoryOperation());
        register(new HashSetMemoryOperation());

        register(new MapMemoryOperation());
        register(new HashMapMemoryOperation());
        register(new LinkedHashMapMemoryOperation());
        register(new PropertiesMemoryOperation());

        register(new UrlMemoryOperation());
        register(new UriMemoryOperation());
        register(new BinaryMemoryOperation());

        register(new NumberMemoryOperation());
        register(new BigDecimalOperation());
        register(new BigIntegerOperation());

        register(new ClassMemoryOperation());
        register(new LocaleMemoryOperation());
        register(new DateMemoryOperation());
        register(new TimeZoneMemoryOperation());
        register(new ScannerMemoryOperation());

        register(new ThreadMemoryOperation());
        register(new ThreadGroupMemoryOperation());

        register(new FloatArrayMemoryOperation());
        register(new DoubleArrayMemoryOperation());
        register(new LongArrayMemoryOperation());
        register(new IntegerArrayMemoryOperation());
        register(new ShortArrayMemoryOperation());
        register(new BooleanArrayMemoryOperation());
        register(new CharArrayMemoryOperation());
    }
}
