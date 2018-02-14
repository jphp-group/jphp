package php.runtime.lang;


import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

abstract public class ForeachIterator implements Iterable<Memory> {
    protected Object currentKey;
    protected Memory currentKeyMemory;
    protected Memory currentValue;
    protected boolean init = false;
    protected final boolean getReferences;
    protected final boolean getKeyReferences;
    protected final boolean withPrevious;
    protected boolean plainReferences = false;

    abstract protected boolean init();

    abstract protected boolean nextValue();

    abstract protected boolean prevValue();

    protected TraceInfo trace = TraceInfo.UNKNOWN;

    public static ForeachIterator of(final Environment env, final Map map) {
        return new ForeachIterator(false, false, false) {
            private Iterator<Map.Entry<Object, Object>> entries;

            @Override
            protected boolean init() {
                reset();
                return !map.isEmpty();
            }

            @Override
            protected boolean nextValue() {
                if (entries.hasNext()) {
                    Map.Entry<Object, Object> entry = entries.next();

                    currentKey = entry.getKey();
                    currentKeyMemory = currentKey == null ? Memory.NULL : StringMemory.valueOf(currentKey.toString());

                    currentValue = Memory.wrap(env, entry.getValue());

                    if (!getReferences) {
                        currentValue = currentValue.toValue();
                    }

                    return true;
                }

                return false;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }

            @Override
            public void reset() {
                entries = map.entrySet().iterator();
            }
        };
    }

    public static ForeachIterator of(final Environment env, final Iterable iterable) {
        return new ForeachIterator(false, false, false) {
            protected Iterator iterator;

            @Override
            protected boolean init() {
                iterator = iterable.iterator();

                currentKeyMemory = LongMemory.CONST_INT_M1;
                currentKey = currentKeyMemory.toLong();
                return true;
            }

            @Override
            protected boolean nextValue() {
                if (!iterator.hasNext()) {
                    return false;
                }

                Object next = iterator.next();

                currentKeyMemory = currentKeyMemory == null ? Memory.CONST_INT_0 : currentKeyMemory.inc();
                currentKey = currentKeyMemory.toLong();
                currentValue = Memory.wrap(env, next);

                return true;
            }

            @Override
            protected boolean prevValue() {
                return false;
            }

            @Override
            public void reset() {
                iterator = iterable.iterator();
            }
        };
    }

    public ForeachIterator(boolean getReferences, boolean getKeyReferences, boolean withPrevious) {
        this.getReferences = getReferences;
        this.withPrevious = withPrevious;
        this.getKeyReferences = getKeyReferences;
    }

    public void setPlainReferences(boolean plainReferences) {
        this.plainReferences = plainReferences;
    }

    public boolean prev() {
        currentKeyMemory = null;
        if (!init || !withPrevious) {
            this.currentKey = null;
            this.currentValue = null;
            return false;
        } else
            return prevValue();
    }

    public boolean next() {
        currentKeyMemory = null;
        if (!init) {
            init = true;
            if (!init())
                return false;
        }

        return nextValue();
    }

    public boolean end() {
        return false;
    }

    public Object getKey() {
        return currentKey;
    }

    public String getStringKey() {
        Object key = getKey();
        return key == null ? null : key.toString();
    }

    abstract public void reset();

    public boolean isLongKey() {
        return currentKey instanceof Long || (currentKey instanceof Memory && ((Memory) currentKey).isNumber());
    }

    public Memory getMemoryKey() {
        if (currentKeyMemory != null)
            return currentKeyMemory;

        if (currentKey instanceof String)
            return currentKeyMemory = new StringMemory((String) currentKey);
        if (currentKey instanceof Long)
            return currentKeyMemory = LongMemory.valueOf((Long) currentKey);
        if (currentKey instanceof Memory)
            return currentKeyMemory = (Memory) currentKey;

        return currentKeyMemory = Memory.NULL;
    }

    public Memory getValue() {
        return currentValue;
    }

    public TraceInfo getTrace() {
        return trace;
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    public Stream<Memory> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Iterator<Memory> iterator() {
        return new Iterator<Memory>() {
            protected Boolean hasNext;

            @Override
            public boolean hasNext() {
                if (hasNext == null) {
                    hasNext = ForeachIterator.this.next();
                }
                return hasNext;
            }

            @Override
            public Memory next() {
                if (hasNext != null) {
                    hasNext = null;
                    return ForeachIterator.this.getValue();
                } else {
                    ForeachIterator.this.next();
                    return ForeachIterator.this.getValue();
                }
            }

            @Override
            public void remove() {
                throw new IllegalStateException("Unsupported remove() method");
            }
        };
    }
}
