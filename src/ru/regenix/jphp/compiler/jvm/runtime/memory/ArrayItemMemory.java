package ru.regenix.jphp.compiler.jvm.runtime.memory;

import ru.regenix.jphp.compiler.jvm.runtime.type.HashTable;

public class ArrayItemMemory extends ReferenceMemory {

    HashTable table;
    Object key;
    ReferenceMemory reference;

    public ArrayItemMemory(HashTable table, Object key, Memory value){
        super(value);
        this.table = table;
        this.key   = key;
    }

    public ArrayItemMemory(HashTable table, Object key) {
        super();
        this.table = table;
        this.key = key;
    }

    public ArrayItemMemory(Object key, ReferenceMemory reference){
        super();
        this.table = new HashTable();
        this.key = key;
        this.reference = reference;
    }

    public ArrayItemMemory(Object key) {
        this(key, null);
    }

    @Override
    public Memory assign(Memory memory) {
        if (value.type == Type.REFERENCE)
            return value.assign(memory);
        else {
            Memory mem = memory.toImmutable();
            table.put(key, mem);
            if (reference != null)
                reference.assignRef(new ArrayMemory(table));

            return mem;
        }
    }

    @Override
    public Memory assign(long value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            Memory mem = LongMemory.valueOf(value);
            this.table.put(key, mem);
            if (reference != null)
                reference.assignRef(new ArrayMemory(table));

            return mem;
        }
    }

    @Override
    public Memory assign(String value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            Memory mem = new StringMemory(value);
            table.put(key, mem);
            if (reference != null)
                reference.assignRef(new ArrayMemory(table));

            return mem;
        }
    }

    @Override
    public Memory assign(boolean value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            table.put(key, value ? TRUE : FALSE);
            if (reference != null)
                reference.assignRef(new ArrayMemory(table));

            return value ? TRUE : FALSE;
        }
    }

    @Override
    public Memory assign(double value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            DoubleMemory mem = new DoubleMemory(value);
            table.put(key, mem);
            if (reference != null)
                reference.assignRef(new ArrayMemory(table));

            return mem;
        }
    }

    @Override
    public void assignRef(Memory memory) {
        if (memory.isImmutable())
            table.put(key, memory);
        else {
            table.put(key, new ReferenceMemory(memory));
            if (reference != null)
                reference.assignRef(new ArrayMemory(table));
        }
    }

    @Override
    public void unset() {
        table.remove(key);
    }
}
