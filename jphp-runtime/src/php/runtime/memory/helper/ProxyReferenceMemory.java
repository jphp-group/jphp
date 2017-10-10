package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.memory.ReferenceMemory;

public class ProxyReferenceMemory extends ReferenceMemory {
    public interface Handler {
        Memory get();
        void set(Memory value);
    }

    protected Handler handler;

    public ProxyReferenceMemory(Handler handler) {
        super(Memory.NULL);

        this.handler = handler;
    }

    @Override
    public Memory setValue(Memory value) {
        handler.set(value);
        return value;
    }

    @Override
    public Memory getValue() {
        return handler.get();
    }
}
