package php.runtime.memory;

import php.runtime.Memory;

public class NativeMemory<T> extends ReferenceMemory {
    private final T object;

    public NativeMemory(T object) {
        super();
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    @Override
    public Memory toImmutable() {
        return this;
    }

    @Override
    public Memory toValue() {
        return this;
    }
}
