package ru.regenix.jphp.runtime.lang;


import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

abstract public class ForeachIterator {
    protected Object currentKey;
    protected Memory currentValue;
    protected boolean init = false;
    protected final boolean getReferences;
    protected final boolean withPrevious;

    abstract protected boolean init();
    abstract protected boolean nextValue();
    abstract protected boolean prevValue();

    public ForeachIterator(boolean getReferences, boolean withPrevious) {
        this.getReferences = getReferences;
        this.withPrevious = withPrevious;
    }

    public boolean prev(){
        if (!init || !withPrevious) {
            this.currentKey = null;
            this.currentValue = null;
            return false;
        } else
            return prevValue();
    }

    public boolean next(){
        if (!init){
            init = true;
            if (!init())
                return false;
        }

        return nextValue();
    }

    public boolean end(){
        return false;
    }

    public Object getCurrentKey() {
        return currentKey;
    }

    public Memory getCurrentMemoryKey(){
        if (currentKey instanceof String)
            return new StringMemory((String)currentKey);
        if (currentKey instanceof Long)
            return LongMemory.valueOf((Long)currentKey);
        if (currentKey instanceof Memory)
            return (Memory) currentKey;

        return Memory.NULL;
    }

    public Memory getCurrentValue() {
        return currentValue;
    }
}
