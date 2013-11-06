package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.type.HashTable;

public class ArrayItemMemory extends ReferenceMemory {

    ArrayMemory table;
    Object key;
    ReferenceMemory reference;

    public ArrayItemMemory(ArrayMemory table, Object key, Memory value){
        super(value);
        this.table = table;
        this.key   = key;
    }

    public ArrayItemMemory(ArrayMemory table, Object key) {
        super();
        this.table = table;
        this.key = key;
    }

    public ArrayItemMemory(Object key, ReferenceMemory reference){
        super();
        this.table = new ArrayMemory();
        this.key = key;
        this.reference = reference;
    }

    public ArrayItemMemory(Object key) {
        this(key, null);
    }

    private void checkCopied(){
        if (table.value.isCopied()){
            table.value = HashTable.copyOf(table.value);
        }
    }

    @Override
    public Memory assign(Memory memory) {
        if (value.type == Type.REFERENCE)
            return value.assign(memory);
        else {
            Memory mem = memory.toImmutable();
            table.value.put(key, mem);
            if (reference != null)
                reference.assignRef(table);
            return mem;
        }
    }

    @Override
    public Memory assign(long value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            checkCopied();
            Memory mem = LongMemory.valueOf(value);
            table.value.put(key, mem);
            if (reference != null)
                reference.assignRef(table);

            return mem;
        }
    }

    @Override
    public Memory assign(String value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            checkCopied();
            Memory mem = new StringMemory(value);
            table.value.put(key, mem);
            if (reference != null)
                reference.assignRef(table);

            return mem;
        }
    }

    @Override
    public Memory assign(boolean value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            checkCopied();
            table.value.put(key, value ? TRUE : FALSE);
            if (reference != null)
                reference.assignRef(table);

            return value ? TRUE : FALSE;
        }
    }

    @Override
    public Memory assign(double value) {
        if (this.value.type == Type.REFERENCE)
            return this.value.assign(value);
        else {
            checkCopied();
            DoubleMemory mem = new DoubleMemory(value);
            table.value.put(key, mem);
            if (reference != null)
                reference.assignRef(table);

            return mem;
        }
    }

    @Override
    public void assignRef(Memory memory) {
        if (memory.isImmutable())
            table.value.put(key, memory);
        else {
            checkCopied();
            table.value.put(key, new ReferenceMemory(memory));
            if (reference != null)
                reference.assignRef(table);
        }
    }

    @Override
    public void unset() {
        checkCopied();
        table.value.remove(key);
    }
}
