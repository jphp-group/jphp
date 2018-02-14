package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.common.Pair;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.RecursiveException;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.StdClass;
import php.runtime.memory.helper.ArrayKeyMemory;
import php.runtime.memory.helper.ArrayValueMemory;
import php.runtime.memory.helper.ShortcutMemory;
import php.runtime.memory.support.*;
import php.runtime.reflection.support.ReflectionUtils;

import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ArrayMemory extends Memory implements Iterable<ReferenceMemory> {
    protected transient long lastLongIndex;
    protected transient int size;
    protected transient int copies;
    protected transient ArrayMemory original;

    protected transient ArrayMemoryList<ReferenceMemory> _list;
    protected transient ArrayMemoryMap map;

    protected transient ForeachIterator foreachIterator;

    public ArrayMemory(boolean asMap) {
        super(Type.ARRAY);

        if (asMap) {
            convertToMap();
        }

        lastLongIndex = -1;
    }

    private ArrayMemory(ArrayMemoryMap map) {
        super(Type.ARRAY);

        this.map = map;
        this.lastLongIndex = -1;
    }

    private ArrayMemory(ArrayMemoryList<ReferenceMemory> list) {
        super(Type.ARRAY);

        this.map = null;
        this._list = list;
        this.lastLongIndex = -1;
    }

    public ArrayMemory() {
        this(false);
    }

    public static ArrayMemory createListed(int expectedSize) {
        if (expectedSize <= 0) {
            return new ArrayMemory();
        }

        return new ArrayMemory(new ArrayMemoryList<>(expectedSize));
    }

    public static ArrayMemory createHashed() {
        return createHashed(8);
    }

    public static ArrayMemory createHashed(int expectedSize) {
        ArrayMemoryMap map = new ArrayMemoryMap(expectedSize < 4 ? 7 : (int) (expectedSize * 1.75));
        return new ArrayMemory(map);
    }

    private List<ReferenceMemory> getList() {
        if (_list != null) {
            return _list;
        }

        synchronized (this) {
            if (_list != null) {
                return _list;
            }

            _list = new ArrayMemoryList<>(7);
        }

        return _list;
    }

    @Deprecated
    public ArrayMemory(Collection collection) {
        this();
        for (Object el : collection) {
            if (el == null) {
                add(NULL);
                continue;
            }

            MemoryOperation operation = MemoryOperation.get(el.getClass(), null);

            if (operation != null) {
                add(operation.unconvertNoThow(null, null, el));
            }
        }
    }

    @Deprecated
    public ArrayMemory(Object... array) {
        this();
        for (Object el : array) {
            if (el == null) {
                getList().add(new ReferenceMemory());
                continue;
            }

            MemoryOperation operation = MemoryOperation.get(el.getClass(), null);

            if (operation != null) {
                getList().add(new ReferenceMemory(operation.unconvertNoThow(null, null, el)));
            }
        }
        size = array.length;
        lastLongIndex = size - 1;
    }

    @Deprecated
    public ArrayMemory(boolean toImmutable, Memory... array) {
        this();
        if (array != null) {
            for (Memory el : array) {
                getList().add(new ReferenceMemory(toImmutable ? el.toImmutable() : el));
            }
            size = array.length;

            lastLongIndex = size - 1;
        }
    }

    @Deprecated
    public ArrayMemory(String[] array) {
        this();
        for (String el : array) {
            getList().add(new ReferenceMemory(StringMemory.valueOf(el)));
        }
        size = array.length;
        lastLongIndex = size - 1;
    }

    @Deprecated
    public ArrayMemory(Map map) {
        this();
        for (Object key : map.keySet()) {
            Object el = map.get(key);
            put(ArrayMemory.toKey(MemoryUtils.valueOf(key)), MemoryUtils.valueOf(el));
        }
    }

    public static Memory valueOf() {
        return new ArrayMemory();
    }

    public static ArrayMemory valueOfRef(ArrayMemory value) {
        if (value == null)
            return new ArrayMemory();
        else
            return value;
    }

    public Set<Object> keySet() {
        if (map == null) {
            Set<Object> set = new HashSet<Object>(size());

            for (int i = 0; i < size(); i++) {
                set.add(i);
            }

            return set;
        }

        return map.keySet();
    }

    public ArrayMemory duplicate() {
        ArrayMemory result = new ArrayMemory();
        result.lastLongIndex = lastLongIndex;
        result.size = size;

        if (size() == 0) {
            return result;
        }

        if (map == null) {
            for (ReferenceMemory item : getList()) {
                result.getList().add(item.duplicate());
            }
        } else {
            result._list = null;
            result.map = new ArrayMemoryMap();

            for (Map.Entry<Object, Memory> entry : map.entrySet()) {
                result.map.put(entry.getKey(), entry.getValue().toImmutable());
            }
        }

        return result;
    }

    public ArrayMemory checkCopied() {
        if (original != null || copies > 0) {
            ArrayMemory dup = duplicate();
            this.map = dup.map;
            this._list = dup._list;
            this.lastLongIndex = dup.lastLongIndex;

            if (this.original == null) {
                this.copies--;
            } else {
                this.original.copies--;
                this.original = null;
                this.copies = 0;
            }
            return dup;
        }
        return null;
    }

    public static Object toKey(Memory key) {
        switch (key.type) {
            case STRING: {
                String key1 = key.toString();
                Memory number = StringMemory.toLong(key1);
                if (number == null)
                    return key1;
                else
                    return number;
            }
            case INT:
                return key;
            case NULL:
                return Memory.CONST_EMPTY_STRING;
            case REFERENCE:
                return toKey(key.toValue());
            default:
                return LongMemory.valueOf(key.toLong());
        }
    }

    public boolean containsLongKey(long key) {
        return containsKey(LongMemory.valueOf(key));
    }

    public boolean containsKey(Object key) {
        if (size() == 0) {
            return false;
        }

        if (_list != null) {
            long t = MemoryUtils.valueOf(key).toLong();
            return t >= 0 && t < _list.size();
        }

        return map.containsKey(key);
    }

    private void convertToMap() {
        map = new ArrayMemoryMap();
        if (_list != null && !_list.isEmpty()) {
            int i = 0;
            for (ReferenceMemory memory : _list) {
                if (memory != null) {
                    map.put(LongMemory.valueOf(i), memory.getValue());
                }
                i++;
            }
        }

        _list = null;
    }

    public void renameKey(Memory oldKey, Memory newKey) {
        checkCopied();

        if (_list != null || map == null) {
            convertToMap();
        }

        Object key1 = toKey(oldKey);
        Object key2 = toKey(newKey);

        map.put(key2, map.remove(key1));
    }

    public Memory get(Memory key) {
        return getByScalar(toKey(key));
    }

    public ReferenceMemory getOrCreate(Memory key) {
        return getByScalarOrCreate(toKey(key));
    }

    public ReferenceMemory getOrCreateAsShortcut(Memory key) {
        return getByScalarOrCreateAsShortcut(toKey(key));
    }

    public ReferenceMemory getByScalarOrCreate(Object sKey, Memory initValue) {
        ReferenceMemory value = getByScalar(sKey);

        if (value == null) {
            return put(sKey, initValue);
        }

        return value;
    }

    public ReferenceMemory getByScalarOrCreateAsShortcut(Object sKey) {
        //checkCopied();
        ReferenceMemory value = getByScalar(sKey);

        if (value == null) {
            value = new ReferenceMemory(UNDEFINED);
            return put(sKey, new ShortcutMemory(value));
        }

        if (value instanceof ShortcutMemory)
            return (ShortcutMemory) value.getValue();
        else {
            put(sKey, new ShortcutMemory(value));
            return value;
        }
    }

    public ReferenceMemory getByScalarOrCreate(Object sKey) {
        return getByScalarOrCreate(sKey, UNDEFINED);
    }

    public ReferenceMemory getByScalar(Object key) {
        if (_list != null) {
            if (key instanceof Memory) {
                int index = (int) ((Memory) key).toLong();
                if (index >= 0 && index < _list.size()) {
                    return _list.get(index);
                } else
                    return null;
            } else
                return null;
        } else if (map != null) {
            return map.getEntry(key);
        } else {
            return null;
        }
    }

    public void add(IObject object) {
        add(new ObjectMemory(object));
    }

    public void add(long value) {
        add(LongMemory.valueOf(value));
    }

    public void add(String value) {
        add(StringMemory.valueOf(value));
    }

    public void add(double value) {
        add(new DoubleMemory(value));
    }

    public void add(boolean value) {
        add(value ? TRUE : FALSE);
    }

    public void addNull() {
        add(NULL);
    }

    public ReferenceMemory add(Memory value) {
        if (value instanceof KeyValueMemory) {
            KeyValueMemory keyValue = (KeyValueMemory) value;
            return put(keyValue.getArrayKey(), keyValue.getValue().toImmutable());
        }

        ReferenceMemory ref;

        if (map == null) {
            lastLongIndex++;
            ref = new ReferenceMemory(value);
            getList().add(ref);
            size++;
        } else {
            ref = put(LongMemory.valueOf(++lastLongIndex), value);
        }

        return ref;
    }

    /**
     * @param array
     * @param recursive
     */
    public void merge(ArrayMemory array, boolean recursive, Set<Integer> done) {
        checkCopied();

        if (recursive && done == null) {
            done = new HashSet<>();
        }

        if (map == null && array.map == null) {
            for (ReferenceMemory reference : array.getList())
                getList().add(new ReferenceMemory(reference.toImmutable()));

            size = getList().size();
            lastLongIndex = size - 1;
        } else {
            if (map == null) {
                convertToMap();
            }

            if (array.map == null) {
                for (ReferenceMemory reference : array.getList()) {
                    add(reference.toImmutable());
                }
            } else {
                for (Map.Entry<Object, Memory> entry : array.map.entrySet()) {
                    Object key = entry.getKey();
                    if (key instanceof LongMemory) {
                        add(entry.getValue().toImmutable());
                    } else {
                        Memory value = entry.getValue();
                        if (recursive && value.isArray()) {
                            if (done.contains(value.getPointer()))
                                throw new RecursiveException();

                            Memory current = getByScalar(key).toImmutable();
                            if (current.isArray()) {
                                value = value.toImmutable();

                                int pointer = value.getPointer();
                                done.add(pointer); // for check recursive

                                ArrayMemory result = (ArrayMemory) value; // already array immutable above
                                result.merge((ArrayMemory) current, recursive, done);
                                put(key, result);

                                done.remove(pointer);
                            } else
                                put(key, value.toImmutable());
                        } else {
                            put(key, value.toImmutable());
                        }
                    }
                }
            }
        }
    }

    public void putAll(ArrayMemory array) {
        if (array.map == null) {
            int i = 0;
            for (ReferenceMemory memory : array.getList()) {
                if (memory != null)
                    put(LongMemory.valueOf(i), memory.toImmutable());
                i++;
            }
        } else {
            if (map == null) {
                convertToMap();
            }

            if (array.lastLongIndex > lastLongIndex)
                lastLongIndex = array.lastLongIndex;

            for (Map.Entry<Object, Memory> entry : array.map.entrySet()) {
                put(entry.getKey(), entry.getValue().toImmutable());
            }
        }
    }

    public void putAllRef(ArrayMemory array) {
        if (array.map == null) {
            int i = 0;
            for (ReferenceMemory memory : array.getList()) {
                if (memory != null)
                    put(LongMemory.valueOf(i), memory);
                i++;
            }
        } else {
            if (map == null) {
                convertToMap();
            }

            if (array.lastLongIndex > lastLongIndex) {
                lastLongIndex = array.lastLongIndex;
            }

            map.putAll(array.map);
        }
    }

    public ReferenceMemory putAsKeyString(String key, Memory value) {
        if (map == null) {
            convertToMap();
        }

        Pair<Memory, ArrayMapEntryMemory> result = map.putWithEntry(key, value);

        if (!result.hasA()) {
            size++;
        }

        return result.getB();
    }

    public ReferenceMemory put(Object key, Memory value) {
        if (key instanceof LongMemory) {
            ReferenceMemory mem = new ReferenceMemory(value);

            int index = (int) ((LongMemory) key).value;

            if (index > lastLongIndex)
                lastLongIndex = index;

            if (map == null) {
                int size = getList().size();
                if (index >= 0) {
                    if (index < size) {
                        getList().set(index, mem);
                        //this.size++;
                        return mem;
                    } else if (index == size) {
                        getList().add(mem);
                        this.size++;
                        return mem;
                    } else {
                        convertToMap();
                    }
                } else
                    convertToMap();
            }
        } else {
            if (!(key instanceof String))
                key = key.toString();

            if (map == null) {
                convertToMap();
            }
        }

        Pair<Memory, ArrayMapEntryMemory> result = map.putWithEntry(key, value);

        if (!result.hasA()) {
            size++;
        }

        return result.getB();
    }

    public Memory removeByScalar(Object key) {
        if (map == null) {
            int index = -1;
            if (key instanceof Long)
                index = ((Long) key).intValue();
            else if (key instanceof Integer)
                index = ((Integer) key);
            else if (key instanceof String) {
                Memory tmp = StringMemory.toLong((String) key);
                if (tmp != null)
                    index = (int) tmp.toLong();
            }

            if (index < 0 || _list == null || index >= getList().size())
                return null;

            if (index == size - 1) {
                size--;
                lastLongIndex = size - 1;
                ReferenceMemory remove = getList().remove(index);

                if (getList().isEmpty()) {
                    _list = null;
                }

                return remove;
            } else {
                key = (long) index;
                convertToMap();
            }
        }

        {
            if (key instanceof Long)
                key = LongMemory.valueOf((Long) key);
            else if (key instanceof Integer)
                key = LongMemory.valueOf((Integer) key);
            else if (key instanceof String) {
                Memory tmp = StringMemory.toLong((String) key);
                if (tmp != null)
                    key = tmp;
            }

            Memory memory = map.remove(key);
            if (memory != null)
                size--;

            return memory;
        }
    }

    public Memory remove(Memory key) {
        Object _key = toKey(key);
        if (map == null) {
            int index = _key instanceof LongMemory ? (int) key.toLong() : -1;
            if (index < 0 || _list == null || index >= getList().size())
                return null;

            if (index == size - 1) {
                size--;
                lastLongIndex = index - 1;
                ReferenceMemory remove = getList().remove(index);

                if (getList().isEmpty()) {
                    _list = null;
                }

                return remove;
            }

            //key = LongMemory.valueOf(index);
            convertToMap();
        }

        {
            Memory memory = map.remove(_key);
            if (memory != null)
                size--;

            return memory;
        }
    }

    public int size() {
        return size;
    }

    public void unshift(Memory value, int count) {
        checkCopied();
        if (size == 0) {
            for (int i = 0; i < count; i++)
                add(value);
        } else {
            if (map == null) {
                List<ReferenceMemory> tmp = new ArrayList<ReferenceMemory>();
                for (int i = 0; i < count; i++)
                    tmp.add(new ReferenceMemory(value));

                getList().addAll(0, tmp);
                size = getList().size();
            } else {
                ArrayMemory tmp = new ArrayMemory();
                tmp.convertToMap();

                for (int i = 0; i < count; i++)
                    tmp.add(value);

                ForeachIterator iterator = getNewIterator(null, false, false);

                while (iterator.next()) {
                    Object key = iterator.getKey();
                    if (key instanceof String)
                        tmp.put(key, iterator.getValue());
                    else
                        add(iterator.getValue());
                }

                lastLongIndex = tmp.lastLongIndex;
                map = tmp.map;
                size = tmp.size;
            }
        }
    }

    public void unshift(Memory... values) {
        checkCopied();

        if (values == null) {
            throw new NullPointerException();
        }

        if (size == 0) {
            for (Memory value : values)
                add(value);
        } else {
            if (map == null) {
                if (values.length > 1) {
                    List<ReferenceMemory> tmp = new ArrayList<ReferenceMemory>();
                    for (Memory value : values)
                        tmp.add(new ReferenceMemory(value));

                    getList().addAll(0, tmp);
                    size = getList().size();
                } else if (values.length == 1) {
                    getList().add(0, new ReferenceMemory(values[0]));
                    size = getList().size();
                }
            } else {
                ArrayMemory tmp = new ArrayMemory();
                tmp.convertToMap();

                for (Memory el : values)
                    tmp.add(el);

                ForeachIterator iterator = getNewIterator(null, false, false);

                while (iterator.next()) {
                    Object key = iterator.getKey();
                    if (key instanceof String)
                        tmp.put(key, iterator.getValue());
                    else
                        add(iterator.getValue());
                }

                lastLongIndex = tmp.lastLongIndex;
                map = tmp.map;
                size = tmp.size;
            }
        }
    }

    public Memory shift() {
        checkCopied();
        if (size < 1)
            return null;

        size -= 1;
        Memory value;
        if (map == null) {
            value = getList().get(0);
            getList().remove(0);
        } else {
            value = map.remove(map.firstKey());
        }

        return value.toValue();
    }

    public Memory pop() {
        checkCopied();
        if (size < 1)
            return null;

        Memory value;
        if (getList() != null) {
            value = getList().get(size - 1);
            getList().remove(size - 1);
        } else {
            value = map.remove(map.lastKey());
        }
        size -= 1;

        return value.toValue();
    }

    public Memory peek() {
        if (size < 1)
            return null;

        Memory value;
        if (map == null)
            value = getList().get(size - 1);
        else {
            value = map.get(map.lastKey());
        }

        return value.toValue();
    }

    public Memory peekKey() {
        if (size < 1)
            return null;

        Memory value;
        if (map == null)
            return LongMemory.valueOf(size - 1);
        else {
            Object key = map.lastKey();
            if (key instanceof Memory) {
                return (Memory) key;
            } else if (key instanceof String) {
                return StringMemory.valueOf(key.toString());
            }
        }

        return null;
    }

    public Memory getRandomElementKey(Random rnd) {
        int index = rnd.nextInt(size);
        if (map == null) {
            return LongMemory.valueOf(index);
        } else {
            Iterator<Object> keys = map.keySet().iterator();
            for (int i = 0; i < index; i++) {
                keys.next();
            }

            Object key = keys.next();
            if (key instanceof LongMemory)
                return (LongMemory) key;
            else
                return new StringMemory((String) key);
        }
    }

    public void shuffle(Random rnd) {
        checkCopied();
        if (map == null) {
            if (_list != null) {
                Collections.shuffle(getList(), rnd);
            }
        } else {
            Set<Object> keys = map.keySet();

            List<Memory> values = new ArrayList<>(map.values());
            Collections.shuffle(values, rnd);

            int i = 0;
            for (Object key : keys) {
                map.put(key, values.get(i));
                i++;
            }
        }
    }

    public void clear() {
        _list = null;
        map = null;

        size = 0;
    }

    public int compare(ArrayMemory otherRef, boolean strict) {
        return compare(otherRef, strict, null);
    }

    public int compare(ArrayMemory otherRef, boolean strict, Set<Integer> used) {
        int size1 = size(), size2 = otherRef.size();

        if (size1 < size2)
            return -1;
        else if (size1 > size2)
            return 1;

        ForeachIterator iterator = this.foreachIterator(false, false);
        ForeachIterator iterator2 = null;
        if (strict)
            iterator2 = otherRef.foreachIterator(false, false);

        if (used == null)
            used = new HashSet<Integer>();

        while (iterator.next()) {
            Memory value1 = iterator.getValue();
            Memory key = iterator.getMemoryKey();
            Memory value2;
            if (iterator2 == null)
                value2 = otherRef.get(key);
            else {
                if (!iterator2.next())
                    return -2;
                Object key2 = iterator2.getKey();
                if (!iterator.getKey().equals(key2))
                    return -2;

                value2 = iterator2.getValue();
            }

            if (value2 == null) {
                value2 = UNDEFINED;
                //return -2;
            }

            if (value1.isArray() && value2.isArray()) {
                ArrayMemory arr1 = value1.toValue(ArrayMemory.class);
                if (used.add(value2.getPointer())) {
                    int r = arr1.compare(value2.toValue(ArrayMemory.class), strict, used);
                    if (r == 0) {
                        used.remove(value2.getPointer());
                        continue;
                    }
                    return r;
                }
                used.remove(value2.getPointer());
            } else if (value1.isObject() && value2.isObject()) {
                ObjectMemory o1 = value1.toValue(ObjectMemory.class);
                ObjectMemory o2 = value2.toValue(ObjectMemory.class);

                return o1.compare(o2.value, strict, used);
            } else {
                if ((strict && value1.identical(value2)) || (!strict && value1.equal(value2)))
                    continue;

                if (value1.smaller(value2))
                    return -1;
                else
                    return 1;
            }
        }
        return 0;
    }

    @Override
    public Memory toImmutable() {
        if (copies >= 0) {
            ArrayMemory mem = new ArrayMemory();
            mem._list = _list;
            mem.original = this;
            mem.size = size;
            mem._list = _list;
            mem.map = map;
            mem.lastLongIndex = lastLongIndex;
            copies++;
            reset();
            return mem;
        } else {
            copies++;
            return this;
        }
    }

    public ArrayMemory toConstant() {
        if (copies == 0)
            copies--;
        else
            throw new RuntimeException("Cannot convert array to a constant value with copies != 0");
        return this;
    }

    public Memory[] values(boolean asImmutable) {
        Memory[] result = new Memory[size];
        int i = 0;
        for (ReferenceMemory el : this) {
            result[i++] = asImmutable ? el.toImmutable() : el.toValue();
        }
        return result;
    }

    public Memory[] values() {
        return values(false);
    }

    @Override
    public long toLong() {
        return size == 0 ? 0 : 1;
    }

    @Override
    public double toDouble() {
        return size == 0 ? 0 : 1;
    }

    @Override
    public boolean toBoolean() {
        return size != 0;
    }

    @Override
    public Memory toNumeric() {
        return size == 0 ? CONST_INT_0 : CONST_INT_1;
    }

    @Override
    public String toString() {
        return "Array";
    }

    @Override
    public Memory inc() {
        return toNumeric().inc();
    }

    @Override
    public Memory dec() {
        return toNumeric().dec();
    }

    @Override
    public Memory negative() {
        return toNumeric().negative();
    }

    @Override
    public Memory plus(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                ArrayMemory left = (ArrayMemory) toImmutable();
                ArrayMemory other = (ArrayMemory) memory;
                ForeachIterator iterator = other.foreachIterator(false, false);
                while (iterator.next()) {
                    Object key = iterator.getKey();
                    Memory origin = getByScalar(key);
                    if (origin == null) {
                        left.checkCopied();
                        left.put(key, iterator.getValue().toImmutable());
                    }
                }
                return left;
            case REFERENCE:
                return plus(memory.toValue());
            default:
                return toNumeric().plus(memory);
        }
    }

    @Override
    public Memory minus(Memory memory) {
        return toNumeric().minus(memory);
    }

    @Override
    public Memory mul(Memory memory) {
        return toNumeric().mul(memory);
    }

    @Override
    public Memory pow(Memory memory) {
        return toNumeric().pow(memory);
    }

    @Override
    public Memory div(Memory memory) {
        return toNumeric().div(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                return compare((ArrayMemory) memory, false) == 0;
            case REFERENCE:
                return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equal(memory);
    }

    @Override
    public boolean smaller(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                return compare((ArrayMemory) memory, false) == -1;
            case REFERENCE:
                return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean smallerEq(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                int r = compare((ArrayMemory) memory, false);
                return r == 0 || r == -1;
            case REFERENCE:
                return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean greater(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                return compare((ArrayMemory) memory, false) == 1;
            case REFERENCE:
                return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean greaterEq(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                int r = compare((ArrayMemory) memory, false);
                return r == 0 || r == 1;
            case REFERENCE:
                return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public void unset() {
        if (original != null) {
            original.copies--;
            original = null;
        } else
            copies--;

        /*if (_list != null) {
            for (ReferenceMemory memory : getList()) {
                if (memory.type == type) {
                    memory.unset();
                }
            }
        }

        if (map != null) {
            for (Memory memory : map.values()) {
                if (memory.type == type) {
                    memory.unset();
                }
            }
        }*/

        clear();
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, Memory index) {
        switch (index.getRealType()) {
            case OBJECT:
            case ARRAY:
                return UNDEFINED; // TODO ADD WARNING
        }
        Memory e = get(index);
        return e == null ? UNDEFINED : e;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, long index) {
        Memory e = getByScalar(LongMemory.valueOf(index));
        return e == null ? UNDEFINED : e;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, double index) {
        Memory e = getByScalar(LongMemory.valueOf((long) index));
        return e == null ? UNDEFINED : e;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, boolean index) {
        Memory e = getByScalar(index ? CONST_INT_0 : CONST_INT_1);
        return e == null ? UNDEFINED : e;
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, String index) {
        Memory number = StringMemory.toLong(index);
        Memory e = number == null ? getByScalar(index) : getByScalar(number);
        return e == null ? UNDEFINED : e;
    }

    @Override
    public void unsetOfIndex(TraceInfo trace, Memory index) {
        checkCopied();
        remove(index);
    }

    @Override
    public Memory issetOfIndex(TraceInfo trace, Memory index) {
        Memory value = get(index);
        return value == null ? NULL : value;
    }

    @Override
    public Memory refOfPush(TraceInfo trace) {
        checkCopied();
        return add(UNDEFINED);
    }

    @Override
    public Memory refOfIndexAsShortcut(TraceInfo trace, Memory index) {
        switch (index.getRealType()) {
            case OBJECT:
            case ARRAY:
                return new ReferenceMemory(); // TODO ADD WARNING
        }
        checkCopied();
        return getOrCreateAsShortcut(index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, Memory index) {
        switch (index.getRealType()) {
            case OBJECT:
            case ARRAY:
                return new ReferenceMemory(); // TODO ADD WARNING
        }
        checkCopied();
        return getOrCreate(index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, long index) {
        checkCopied();
        return getOrCreate(LongMemory.valueOf(index));
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, double index) {
        return refOfIndex(null, LongMemory.valueOf((long) index));
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, boolean index) {
        checkCopied();
        return getOrCreate(index ? CONST_INT_1 : CONST_INT_0);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, String index) {
        checkCopied();
        Memory number = StringMemory.toLong(index);
        return number == null ? getByScalarOrCreate(index) : getByScalarOrCreate(number);
    }

    @Override
    public boolean identical(Memory memory) {
        switch (memory.type) {
            case ARRAY:
                return compare((ArrayMemory) memory, true) == 0;
            case REFERENCE:
                return identical(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean identical(long value) {
        return false;
    }

    @Override
    public boolean identical(double value) {
        return false;
    }

    @Override
    public boolean identical(boolean value) {
        return false;
    }

    @Override
    public boolean identical(String value) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<ReferenceMemory> iterator() {
        if (map == null) {
            return getList().iterator();
        } else
            return (Iterator<ReferenceMemory>) map.entriesIterator();
    }

    public Stream<ReferenceMemory> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public ForeachIterator foreachIterator(boolean getReferences, boolean withPrevious) {
        return foreachIterator(getReferences, false, withPrevious, true);
    }


    public ForeachIterator foreachIterator(boolean getReferences, boolean getKeyReferences, boolean withPrevious) {
        return foreachIterator(getReferences, getKeyReferences, withPrevious, true);
    }

    public ForeachIterator foreachIterator(boolean getReferences, boolean getKeyReferences, boolean withPrevious, final boolean freeze) {
        return new ForeachIterator(getReferences, getKeyReferences, withPrevious) {
            protected int cursor = 0;
            protected int listMax;
            protected Iterator<Object> keys;

            @Override
            public void reset() {
                if (getKeyReferences && map == null)
                    ArrayMemory.this.convertToMap();

                if (map != null) {
                    if (withPrevious || getKeyReferences)
                        keys = new ArrayList<Object>(map.keySet()).listIterator();
                    else {
                        if (freeze) {
                            keys = new ArrayList<Object>(map.keySet()).iterator();
                        } else {
                            keys = map.keySet().iterator();
                        }
                    }
                } else {
                    listMax = _list == null ? 0 : getList().size();
                }
            }

            @Override
            protected boolean init() {
                if (getKeyReferences && map == null)
                    ArrayMemory.this.convertToMap();

                if (map != null) {
                    if (withPrevious || getKeyReferences) {
                        keys = new ArrayList<Object>(map.keySet()).listIterator();
                    } else {
                        keys = new ArrayList<Object>(map.keySet()).iterator();
                    }
                } else {
                    listMax = _list == null ? 0 : getList().size();
                }

                return true;
            }

            private void setCurrentValue(ReferenceMemory value) {
                if (getReferences) {
                    if (plainReferences)
                        currentValue = value;
                    else
                        currentValue = new ArrayValueMemory(getMemoryKey(), ArrayMemory.this, value);
                } else
                    currentValue = value.getValue();

                if (getKeyReferences) {
                    currentKeyMemory = new ArrayKeyMemory(ArrayMemory.this, getMemoryKey());
                }
            }

            @Override
            public boolean end() {
                if (ArrayMemory.this.size == 0)
                    return false;

                if (ArrayMemory.this.map == null) {
                    cursor = ArrayMemory.this.size - 1;
                    currentKey = (long) cursor;
                    setCurrentValue(getList().get(cursor));
                    return true;
                } else {
                    init = true;

                    ArrayList<Object> tmp = new ArrayList<Object>(map.keySet());
                    keys = tmp.listIterator(tmp.size() - 1);

                    if (keys.hasNext() && next()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            @Override
            protected boolean prevValue() {
                if (ArrayMemory.this.map == null) {
                    if (cursor <= 0) {
                        currentKey = null;
                        currentValue = null;
                        cursor--;
                        keys = null;
                        return false;
                    } else {
                        cursor--;
                        currentKey = LongMemory.valueOf((long) cursor);
                        setCurrentValue(getList().get(cursor));
                        return true;
                    }
                } else {
                    ListIterator<Object> keyIterator = (ListIterator) keys;
                    if (keyIterator.hasPrevious()) {
                        currentKey = keyIterator.previous();
                        setCurrentValue(map.getEntry(currentKey));
                        return true;
                    } else {
                        currentKey = null;
                        currentValue = null;
                        keys = null;
                        cursor = -1;
                        return false;
                    }
                }
            }

            @Override
            protected boolean nextValue() {
                if (withPrevious && (keys == null && cursor < 0))
                    return false;

                if (ArrayMemory.this.map == null) {
                    if (((cursor >= listMax && freeze) || (cursor >= size && !freeze)) || size < listMax) {
                        currentKey = null;
                        currentValue = null;
                        return false;
                    }

                    currentKey = LongMemory.valueOf((long) cursor);
                    setCurrentValue(getList().get(cursor));
                    cursor++;
                    return true;
                } else {
                    if (keys == null) {
                        ArrayList<Object> tmp = new ArrayList<Object>(map.keySet());
                        keys = tmp.listIterator(cursor - 1);
                    }

                    if (keys.hasNext()) {
                        currentKey = keys.next();
                        setCurrentValue(map.getEntry(currentKey));
                        return true;
                    } else {
                        currentKey = null;
                        currentValue = null;
                        return false;
                    }
                }
            }
        };
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return foreachIterator(getReferences, getKeyReferences, false);
    }

    public ForeachIterator getCurrentIterator() {
        if (foreachIterator == null) {
            foreachIterator = foreachIterator(false, true);
        }

        return foreachIterator;
    }

    protected void reset() {
        foreachIterator = null;
    }

    public Memory resetCurrentIterator() {
        reset();

        ForeachIterator iterator = getCurrentIterator();
        if (size == 0)
            return FALSE;
        else {
            iterator.next();
            Memory tmp = iterator.getValue();
            iterator.prev();
            return tmp;
        }
    }

    @Override
    public byte[] getBinaryBytes(Charset charset) {
        return MemoryStringUtils.getBinaryBytes(this);
    }

    public boolean isList() {
        return map == null;
    }

    public boolean isMap() {
        return map != null;
    }

    @Override
    public Memory toArray() {
        return this;
    }

    @Override
    public Memory toObject(Environment env) {
        StdClass stdClass = new StdClass(env);
        ArrayMemory props = stdClass.getProperties();
        ForeachIterator iterator = getNewIterator(env, false, false);
        while (iterator.next()) {
            props.refOfIndex(null, iterator.getMemoryKey()).assign(iterator.getValue());
        }

        return new ObjectMemory(stdClass);
    }

    public int[] toIntArray() {
        int[] r = new int[size];
        int i = 0;
        for (Memory e : this) {
            r[i] = e.toInteger();
            i++;
        }
        return r;
    }

    public long[] toLongArray() {
        long[] r = new long[size];
        int i = 0;
        for (Memory e : this) {
            r[i] = e.toLong();
            i++;
        }
        return r;
    }

    public String[] toStringArray() {
        String[] r = new String[size];
        int i = 0;
        for (Memory e : this) {
            r[i] = e.toString();
            i++;
        }
        return r;
    }

    public float[] toFloatArray() {
        float[] r = new float[size];
        int i = 0;
        for (Memory e : this) {
            r[i] = e.toFloat();
            i++;
        }
        return r;
    }

    public double[] toDoubleArray() {
        double[] r = new double[size];
        int i = 0;
        for (Memory e : this) {
            r[i] = e.toDouble();
            i++;
        }
        return r;
    }

    public boolean[] toBoolArray() {
        boolean[] r = new boolean[size];
        int i = 0;
        for (Memory e : this) {
            r[i] = e.toBoolean();
            i++;
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    public <T extends IObject> List<T> toObjectArray(Class<T> clazz) {
        List<T> r = new ArrayList<T>();

        int i = 0;
        for (Memory e : this) {
            if (e.instanceOf(clazz))
                r.add(e.toObject(clazz));
            else {
                throw new IllegalArgumentException(Messages.ERR_INVALID_ARRAY_ELEMENT_TYPE.fetch(
                        ReflectionUtils.getClassName(clazz), ReflectionUtils.getGivenName(e)
                ));
            }
        }

        return r;
    }

    public Map<String, String> toStringMap() {
        Map<String, String> r = new LinkedHashMap<String, String>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            r.put(iterator.getKey().toString(), iterator.getValue().toString());
        }

        return r;
    }

    public Map<String, Integer> toIntMap() {
        Map<String, Integer> r = new LinkedHashMap<String, Integer>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            r.put(iterator.getKey().toString(), iterator.getValue().toInteger());
        }

        return r;
    }

    public Map<String, Long> toLongMap() {
        Map<String, Long> r = new LinkedHashMap<String, Long>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            r.put(iterator.getKey().toString(), iterator.getValue().toLong());
        }

        return r;
    }

    public Map<String, Double> toDoubleMap() {
        Map<String, Double> r = new LinkedHashMap<String, Double>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            r.put(iterator.getKey().toString(), iterator.getValue().toDouble());
        }

        return r;
    }

    public Map<String, Boolean> toBooleanMap() {
        Map<String, Boolean> r = new LinkedHashMap<String, Boolean>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            r.put(iterator.getKey().toString(), iterator.getValue().toBoolean());
        }

        return r;
    }

    public <T extends IObject> Map<String, T> toObjectMap(Class<T> clazz) {
        Map<String, T> r = new LinkedHashMap<String, T>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            Memory e = iterator.getValue();

            if (e.instanceOf(clazz))
                r.put(iterator.getKey().toString(), e.toObject(clazz));
            else {
                throw new IllegalArgumentException(Messages.ERR_INVALID_ARRAY_ELEMENT_TYPE.fetch(
                        ReflectionUtils.getClassName(clazz), ReflectionUtils.getGivenName(e)
                ));
            }
        }

        return r;
    }

    public Map toMap(Environment env) {
        Map<String, Object> r = new LinkedHashMap<>();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            r.put(iterator.getKey().toString(), Memory.unwrap(env, iterator.getValue(), true));
        }

        return r;
    }

    public Object toMapOrList(Environment env) {
        if (isList()) {
            List<Object> result = new ArrayList<>();
            ForeachIterator iterator = foreachIterator(false, false);

            while (iterator.next()) {
                result.add(Memory.unwrap(env, iterator.getValue(), true));
            }

            return result;
        } else {
            return toMap(env);
        }
    }

    public static ArrayMemory ofPair(String key, Memory value) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex(key).assign(value.toImmutable());
        return r;
    }

    public static ArrayMemory ofPair(String key, String value) {
        return ofPair(key, StringMemory.valueOf(value));
    }

    public static ArrayMemory ofPair(String key, long value) {
        return ofPair(key, LongMemory.valueOf(value));
    }

    public static ArrayMemory ofPair(String key, double value) {
        return ofPair(key, DoubleMemory.valueOf(value));
    }

    public static ArrayMemory ofPair(String key, boolean value) {
        return ofPair(key, TrueMemory.valueOf(value));
    }

    public static ArrayMemory ofPair(String key, IObject value) {
        return ofPair(key, ObjectMemory.valueOf(value));
    }

    public static ArrayMemory ofShorts(short... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (int el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofIntegers(int... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (int el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofBytes(byte... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (int el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofLongs(long... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (long el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofFloats(float... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (float el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofDoubles(double... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (double el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofBooleans(boolean... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (boolean el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofChars(char... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (char el : array) result.add(StringMemory.valueOf(el));
        }

        return result;
    }

    public static ArrayMemory ofStrings(String... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (String el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofStringEnumeration(Enumeration<String> enumeration) {
        ArrayMemory result = new ArrayMemory();

        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                result.add(enumeration.nextElement());
            }
        }

        return result;
    }

    public static ArrayMemory ofStringCollection(Collection<String> list) {
        ArrayMemory result = new ArrayMemory();

        if (list != null) {
            for (String el : list) result.add(el);
        }

        return result;
    }

    public static ArrayMemory ofObjects(IObject... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (IObject el : array) result.add(el);
        }

        return result;
    }

    public static ArrayMemory of(Memory... array) {
        ArrayMemory result = new ArrayMemory();

        if (array != null) {
            for (Memory el : array) {
                result.add(el.toImmutable());
            }
        }

        return result;
    }

    public static ArrayMemory ofCollection(Collection<Memory> list) {
        ArrayMemory result = new ArrayMemory();

        for (Memory el : list) {
            result.add(el.toImmutable());
        }

        return result;
    }

    public static ArrayMemory ofMap(Map<String, Memory> map) {
        ArrayMemory result = new ArrayMemory();

        for (Map.Entry<String, Memory> entry : map.entrySet()) {
            result.putAsKeyString(entry.getKey(), entry.getValue().toImmutable());
        }

        return result;
    }

    public static ArrayMemory ofStringMap(Map<String, String> map) {
        ArrayMemory result = new ArrayMemory();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.putAsKeyString(entry.getKey(), StringMemory.valueOf(entry.getValue()));
        }

        return result;
    }

    public static <T> ArrayMemory ofBean(Environment env, T anyObject) {
        return ofBean(env, env.trace(), anyObject);
    }

    public static <T> ArrayMemory ofBean(Environment env, TraceInfo trace, T anyObject) {
        Method[] methods = anyObject.getClass().getMethods();
        Field[] fields = anyObject.getClass().getFields();

        ArrayMemory value = new ArrayMemory(true);

        for (Field field : fields) {
            int fieldModifiers = field.getModifiers();

            if (Modifier.isPublic(fieldModifiers) && !Modifier.isStatic(fieldModifiers)) {
                try {
                    String key = field.getName();
                    Object invoke = field.get(anyObject);

                    MemoryOperation operation = MemoryOperation.get(field.getType(), field.getGenericType());

                    if (operation == null) {
                        if (isLikeBean(field.getType())) {
                            value.put(key, ofNullableBean(env, invoke));
                        }

                        continue;
                    }

                    value.put(key, operation.unconvert(env, trace, invoke));
                } catch (IllegalAccessException e) {
                    throw new CriticalException(e);
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                }
            }
        }

        for (Method method : methods) {
            String name = method.getName();
            String key = null;

            if (name.startsWith("get") && name.length() > 3 && method.getParameterTypes().length == 0) {
                key = name.substring(3);

                if (name.length() == 4) {
                    key = key.toLowerCase();
                } else {
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                }
            } else if (name.startsWith("is") && name.length() > 2 && method.getParameterTypes().length == 0 &&
                    (method.getReturnType() == Boolean.class || method.getReturnType() == Boolean.TYPE)) {

                key = name.substring(2);

                if (name.length() == 3) {
                    key = key.toLowerCase();
                } else {
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                }
            }

            if (key == null) continue;
            if ("class".equals(key)) continue;

            try {
                method.setAccessible(true);
                Object invoke = method.invoke(anyObject);
                MemoryOperation operation = MemoryOperation.get(method.getReturnType(), method.getGenericReturnType());

                if (operation == null) {
                    if (isLikeBean(method.getReturnType())) {
                        value.put(key, ofNullableBean(env, invoke));
                    }

                    continue;
                }

                Memory memory = operation.unconvert(env, trace, invoke);

                value.put(key, memory);
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            } catch (InvocationTargetException e) {
                env.__throwException(e);
            } catch (Throwable throwable) {
                env.wrapThrow(throwable);
            }
        }

        return value;
    }

    public static <T> Memory ofNullableBean(Environment env, T anyObject) {
        return ofNullableBean(env, env.trace(), anyObject);
    }

    public static <T> Memory ofNullableBean(Environment env, TraceInfo trace, T anyObject) {
        if (anyObject == null) {
            return NULL;
        }

        return ofBean(env, trace, anyObject);
    }

    public static <T> boolean isLikeBean(Class<T> anyClass) {
        Constructor<?>[] constructors = anyClass.getConstructors();

        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                return true;
            }
        }

        return false;
    }

    public <T> T toBean(Environment env, Class<T> beanClass) {
        return toBean(env, env.trace(), beanClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T toBean(Environment env, T instance) {
        return toBean(env, env.trace(), instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T toBean(Environment env, TraceInfo trace, T instance) {
        Class<T> beanClass = (Class<T>) instance.getClass();

        ForeachIterator iterator = foreachIterator(false, false);

        while (iterator.next()) {
            String key = iterator.getStringKey();

            try {
                Field field = beanClass.getField(key);
                int fieldModifiers = field.getModifiers();

                if (Modifier.isPublic(fieldModifiers) && !Modifier.isStatic(fieldModifiers)) {
                    MemoryOperation operation = MemoryOperation.get(field.getType(), field.getGenericType());
                    field.set(instance, operation.convert(env, trace, iterator.getValue()));

                    continue;
                }
            } catch (NoSuchFieldException e) {
                // nop;
            } catch (Throwable throwable) {
                env.wrapThrow(throwable);
            }

            String name = key.substring(0, 1).toUpperCase() + key.substring(1);

            String getterName = "get" + name;
            String getterIsName = "is" + name;
            String setterName = "set" + name;

            try {
                Method method = null;

                try {
                    method = beanClass.getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    method = beanClass.getMethod(getterIsName);

                    if (method.getReturnType() != Boolean.TYPE && method.getReturnType() != Boolean.class) {
                        continue;
                    }
                }

                Class<?> returnType = method.getReturnType();

                MemoryOperation operation = MemoryOperation.get(returnType, method.getGenericReturnType());

                if (operation == null) {
                    continue;
                }

                method = beanClass.getMethod(setterName, returnType);

                Object value = operation.convert(env, trace, iterator.getValue());
                method.invoke(instance, value);

            } catch (NoSuchMethodException e) {
                continue;
            } catch (Throwable throwable) {
                env.wrapThrow(throwable);
            }
        }

        return instance;
    }

    public <T> T toBean(Environment env, TraceInfo trace, Class<T> beanClass) {
        try {
            Constructor<T> constructor = beanClass.getConstructor();

            T instance = constructor.newInstance();

            return toBean(env, trace, instance);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CriticalException(e);
        }
    }

    public ArrayMemory slice(int offset) {
        return slice(offset, false);
    }

    public ArrayMemory slice(int offset, int length) {
        return slice(offset, length, false);
    }

    public ArrayMemory slice(int offset, boolean saveKeys) {
        ArrayMemory result = new ArrayMemory();

        if (offset < 0) {
            offset = size() + offset;
        }

        if (isList()) {
            int i = 0;
            try {
                for (ReferenceMemory referenceMemory : getList().subList(offset, getList().size())) {
                    if (saveKeys) {
                        result.refOfIndex(i + offset).assign(referenceMemory.toImmutable());
                    } else {
                        result.add(referenceMemory.toImmutable());
                    }

                    i++;
                }
            } catch (IllegalArgumentException e) {
                throw new IndexOutOfBoundsException(e.getMessage());
            }
        } else {
            int i = 0;

            ForeachIterator iterator = foreachIterator(false, false);

            while (iterator.next()) {
                if (i >= offset) {
                    if (saveKeys || !iterator.isLongKey()) {
                        result.put(iterator.getKey(), iterator.getValue().toImmutable());
                    } else {
                        result.add(iterator.getValue().toImmutable());
                    }
                }

                i++;
            }
        }

        return result;
    }

    public ArrayMemory slice(int offset, int length, boolean saveKeys) {
        ArrayMemory result = new ArrayMemory();

        if (offset < 0) {
            offset = size() + offset;
        }

        if (length < 0) {
            length = size() + length - offset;
        }

        if (isList()) {
            int i = 0;
            try {
                for (ReferenceMemory referenceMemory : getList().subList(offset, offset + length)) {
                    if (saveKeys) {
                        result.refOfIndex(i + offset).assign(referenceMemory.toImmutable());
                    } else {
                        result.add(referenceMemory.toImmutable());
                    }

                    i++;
                }
            } catch (IllegalArgumentException e) {
                throw new IndexOutOfBoundsException(e.getMessage());
            }
        } else {
            int i = 0, count = 0;

            ForeachIterator iterator = foreachIterator(false, false);

            while (iterator.next()) {
                if (i >= offset) {
                    count++;

                    if (saveKeys || !iterator.isLongKey()) {
                        result.put(iterator.getKey(), iterator.getValue().toImmutable());
                    } else {
                        result.add(iterator.getValue().toImmutable());
                    }

                    if (count >= length) {
                        break;
                    }
                }

                i++;
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ArrayMemory that = (ArrayMemory) o;

        if (original != null ? !original.equals(that.original) : that.original != null) return false;
        if (_list != null ? !_list.equals(that._list) : that._list != null) return false;
        return map != null ? map.equals(that.map) : that.map == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (original != null ? original.hashCode() : 0);
        result = 31 * result + (_list != null ? _list.hashCode() : 0);
        result = 31 * result + (map != null ? map.hashCode() : 0);
        return result;
    }
}
