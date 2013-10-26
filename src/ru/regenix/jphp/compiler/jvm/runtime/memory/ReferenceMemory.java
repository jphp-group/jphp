package ru.regenix.jphp.compiler.jvm.runtime.memory;

public class ReferenceMemory extends Memory {

    public Memory value;

    public ReferenceMemory(Memory value) {
        this.value = value == null ? Memory.NULL : value;
    }

    public ReferenceMemory() {
        this.value = Memory.NULL;
    }

    @Override
    public Type getType() {
        return Type.REFERENCE; // value.getType();
    }

    @Override
    public long toLong() {
        return value.toLong();
    }

    @Override
    public double toDouble() {
        return value.toDouble();
    }

    @Override
    public boolean toBoolean() {
        return value.toBoolean();
    }

    @Override
    public Memory toNumeric() {
        return value.toNumeric();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Memory plus(Memory memory) {
        return value.plus(memory);
    }

    @Override
    public Memory minus(Memory memory) {
        return value.minus(memory);
    }

    @Override
    public Memory mul(Memory memory) {
        return value.mul(memory);
    }

    @Override
    public Memory div(Memory memory) {
        return value.div(memory);
    }

    @Override
    public Memory mod(Memory memory) {
        return value.mod(memory);
    }

    @Override
    public Memory concat(Memory memory) {
        return value.concat(memory);
    }

    @Override
    public Memory toImmutable() {
        return value;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Memory assign(Memory memory) {
        if(!value.isImmutable())
            return value.assign(memory);

        return value = memory.toImmutable();
    }

    @Override
    public Memory assignRef(Memory memory) {
        return value = memory;
    }
}
