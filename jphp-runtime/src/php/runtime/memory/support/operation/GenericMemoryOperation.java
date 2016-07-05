package php.runtime.memory.support.operation;

import php.runtime.exceptions.CriticalException;
import php.runtime.memory.support.MemoryOperation;

import java.io.File;
import java.lang.reflect.*;

abstract public class GenericMemoryOperation<T> extends MemoryOperation<T> {
    protected MemoryOperation[] operations;

    protected GenericMemoryOperation() {
        this((Type[]) null);
    }

    public static Class<?> getRawType(ParameterizedType type) {
        try {
            return Class.forName(type.getRawType().toString().split(" ", 2)[1]);
        /*} catch (NoSuchMethodException e) {
            Field field = null;
            try {
                field = type.getClass().getDeclaredField("rawTypeName");
                field.setAccessible(true);

                try {
                    return (Class<?>) field.get(type);
                } catch (IllegalAccessException e1) {
                    throw new CriticalException(e1);
                }
            } catch (NoSuchFieldException e1) {
                throw new CriticalException(e1);
            }
          */
        } catch (ClassNotFoundException /*| IllegalAccessException | InvocationTargetException*/ e) {
            throw new CriticalException(e);
        }
    }

    public GenericMemoryOperation(Type... genericTypes) {
        if (genericTypes != null) {
            this.operations = new MemoryOperation[genericTypes.length];

            for (int i = 0; i < genericTypes.length; i++) {
                if (genericTypes[i] instanceof ParameterizedType) {
                    ParameterizedType genericType = (ParameterizedType) genericTypes[i];

                    this.operations[i] = MemoryOperation.get(getRawType(genericType), genericType);

                    if (this.operations[i] == null) {
                        throw new CriticalException("Unsupported generic type binding - " + genericType);
                    }
                } else {
                    Class<?> genericType = (Class<?>) genericTypes[i];
                    this.operations[i] = MemoryOperation.get(genericType, null);

                    if (this.operations[i] == null) {
                        throw new CriticalException("Unsupported type binding - " + genericType);
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GenericMemoryOperation<T> instance(Type... genericTypes) {
        if (genericTypes != null && genericTypes.length > 0) {
            try {
                return this.getClass().getConstructor(Type[].class).newInstance(new Object[]{ genericTypes });
            } catch (InstantiationException e) {
                throw new CriticalException(e);
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            } catch (InvocationTargetException e) {
                throw new CriticalException(e);
            } catch (NoSuchMethodException e) {
                throw new CriticalException(e);
            }
        }

        return (GenericMemoryOperation<T>) super.instance(genericTypes);
    }
}
