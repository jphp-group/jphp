package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.exceptions.CriticalException;
import php.runtime.memory.support.MemoryOperation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

abstract public class GenericMemoryOperation<T> extends MemoryOperation<T> {
    protected MemoryOperation[] operations;

    protected GenericMemoryOperation() {
        this((Type[]) null);
    }

    public GenericMemoryOperation(Type... genericTypes) {
        if (genericTypes != null) {
            this.operations = new MemoryOperation[genericTypes.length];

            for (int i = 0; i < genericTypes.length; i++) {
                Class<?> genericType = (Class<?>)genericTypes[i];
                this.operations[i] = MemoryOperation.get(genericType, null);
                if (this.operations[i] == null) {
                    throw new CriticalException("Unsupported type binding - " + genericType);
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
