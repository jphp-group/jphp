package php.runtime.memory;

import php.runtime.Memory;

public class PromiseMemory extends ReferenceMemory {
    protected final Handler handler;

    public PromiseMemory(Memory value, Handler handler) {
        super(value);
        this.handler = handler;
    }

    public PromiseMemory(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public Memory toImmutable() {
        throw new IllegalStateException("Promise values should be passed by reference");
    }

    @Override
    public Memory assign(Memory memory) {
        if (handler != null) {
            handler.assign(memory);
        }

        return super.assign(memory);
    }

    @Override
    public Memory assign(long memory) {
        if (handler != null) {
            handler.assign(LongMemory.valueOf(memory));
        }

        return super.assign(memory);
    }

    @Override
    public Memory assign(String memory) {
        if (handler != null) {
            handler.assign(StringMemory.valueOf(memory));
        }

        return super.assign(memory);
    }

    @Override
    public Memory assign(boolean memory) {
        if (handler != null) {
            handler.assign(TrueMemory.valueOf(memory));
        }

        return super.assign(memory);
    }

    @Override
    public Memory assign(double memory) {
        if (handler != null) {
            handler.assign(DoubleMemory.valueOf(memory));
        }

        return super.assign(memory);
    }

    @Override
    public Memory assignRef(Memory reference) {
        Memory value = super.assignRef(reference);

        if (handler != null) {
            handler.assign(value);
        }

        return value;
    }

    public interface Handler {
        void assign(Memory memory);
    }
}
