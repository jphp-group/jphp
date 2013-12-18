package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.memory.support.Memory;

public class StringBuilderMemory extends StringMemory {
    StringBuilder builder = null;
    StringMemory cache = null;

    public StringBuilderMemory(){
        super("");
    }

    public StringBuilderMemory(String value) {
        super(value);
    }

    public StringBuilderMemory(char ch) {
        super(ch);
    }

    @Override
    public Memory toImmutable() {
        if (cache != null)
            return cache;
        return cache = new StringMemory(toString());
    }

    @Override
    public String toString() {
        if (builder != null){
            value = builder.toString();
            builder = null;
        }
        return value;
    }


    @Override
    public Memory toNumeric(){
        return toImmutable().toNumeric();
    }

    private void resolveBuilder(){
        cache = null;
        if (this.builder == null){
            this.builder = new StringBuilder(this.value);
            this.value = null;
        }
    }

    public void append(Memory memory){
        resolveBuilder();
        switch (memory.type){
            case BOOL:
                if (memory instanceof FalseMemory)
                    break;
                else
                    builder.append(memory.toString());
                break;
            case NULL: break;
            case INT: builder.append(((LongMemory)memory).value); break;
            case DOUBLE: builder.append(((DoubleMemory)memory).value); break;
            case STRING: builder.append(memory.toString()); break;
            case REFERENCE: append(memory.toImmutable()); break;
            default:
                builder.append(memory.toString());
        }
    }

    public void append(String value){
        resolveBuilder();
        this.builder.append(value);
    }

    public void append(long value){
        resolveBuilder();
        builder.append(value);
    }

    public void append(double value){
        resolveBuilder();
        builder.append(value);
    }

    public void append(boolean value){
        if (value){
            resolveBuilder();
            builder.append(boolToString(value));
        }
    }

    public int length(){
        if (builder == null)
            return value.length();
        else
            return builder.length();
    }
}
