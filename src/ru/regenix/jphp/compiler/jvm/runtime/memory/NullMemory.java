package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class NullMemory extends FalseMemory {

    public static NullMemory INSTANCE = new NullMemory();

    protected NullMemory() {
        super(Type.NULL);
    }
}
