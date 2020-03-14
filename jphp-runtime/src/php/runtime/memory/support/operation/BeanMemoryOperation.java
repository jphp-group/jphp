package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanMemoryOperation extends MemoryOperation {
    private final Class<?> beanClass;
    private final Class<?>[] operationClasses;

    protected static final Map<Class<?>, BeanMemoryOperation> instances = new ConcurrentHashMap<>();

    public static boolean isLikeBean(Class<?> aClass) {
        return ArrayMemory.isLikeBean(aClass);
    }

    public static BeanMemoryOperation of(Class<?> beanClass, Class<?>... aliasBeanClasses) {
        if (aliasBeanClasses == null || aliasBeanClasses.length == 0) {
            return instances.computeIfAbsent(beanClass, aClass -> new BeanMemoryOperation(aClass));
        } else {
            return new BeanMemoryOperation(beanClass, aliasBeanClasses);
        }
    }

    protected BeanMemoryOperation(Class<?> beanClass, Class<?>... aliasBeanClasses) {
        this.beanClass = beanClass;

        if (!isLikeBean(beanClass)) {
            throw new IllegalArgumentException(beanClass + " cannot be bean class");
        }

        operationClasses = new Class[1 + (aliasBeanClasses == null ? 0 : aliasBeanClasses.length)];
        operationClasses[0] = beanClass;

        if (aliasBeanClasses != null) {
            System.arraycopy(aliasBeanClasses, 0, operationClasses, 1, aliasBeanClasses.length);
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return operationClasses;
    }

    @Override
    public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return ((ArrayMemory) arg).toBean(env, trace, beanClass);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
        return ArrayMemory.ofNullableBean(env, trace, arg);
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(TypeChecker.of(HintType.ARRAY));
    }
}
