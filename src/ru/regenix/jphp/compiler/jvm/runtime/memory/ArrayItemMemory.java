package ru.regenix.jphp.compiler.jvm.runtime.memory;

import ru.regenix.jphp.compiler.jvm.runtime.type.HashTable;

public class ArrayItemMemory extends ReferenceMemory {

    protected HashTable table;
    protected Memory key;

    public ArrayItemMemory(HashTable table, Memory key, Memory value){
        super(value);
        this.table = table;
        this.key   = key;
    }

    public ArrayItemMemory(HashTable table, Memory key) {
        super();
        this.table = table;
        this.key = key;
    }

    @Override
    public void assign(Memory memory) {
        if (value.type == Type.REFERENCE)
            value.assign(memory);
        else
            table.put(key, memory.toImmutable());
    }

    @Override
    public void assign(long value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            this.table.put(key, new LongMemory(value));
    }

    @Override
    public void assign(String value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            table.put(key, new StringMemory(value));
    }

    @Override
    public void assign(boolean value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            table.put(key, value ? TRUE : FALSE);
    }

    @Override
    public void assign(double value) {
        if (this.value.type == Type.REFERENCE)
            this.value.assign(value);
        else
            table.put(key, new DoubleMemory(value));
    }

    @Override
    public void assignRef(Memory memory) {
        if (memory.isImmutable())
            table.put(key, memory);
        else
            table.put(key, new ReferenceMemory(memory));
    }
}
