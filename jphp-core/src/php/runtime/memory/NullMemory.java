package php.runtime.memory;

import php.runtime.Memory;

public class NullMemory extends FalseMemory {

    public final static NullMemory INSTANCE = new NullMemory();

    protected NullMemory() {
        super(Type.NULL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.getRealType() == Type.NULL;
    }

    @Override
    public boolean identical(boolean value) {
        return false;
    }

    @Override
    public Memory inc() {
        return CONST_INT_1;
    }

    @Override
    public Memory dec() {
        return CONST_INT_M1;
    }
}
