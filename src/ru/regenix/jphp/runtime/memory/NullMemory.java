package ru.regenix.jphp.runtime.memory;

public class NullMemory extends FalseMemory {

    public final static NullMemory INSTANCE = new NullMemory();

    private int value;

    protected NullMemory() {
        super(Type.NULL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NullMemory that = (NullMemory) o;

        if (value != that.value) return false;

        return true;
    }
}
