package php.runtime.memory;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.common.collections.map.LinkedMap;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.RecursiveException;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.StdClass;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.helper.ArrayKeyMemory;
import php.runtime.memory.helper.ArrayValueMemory;
import php.runtime.memory.helper.ShortcutMemory;
import php.runtime.memory.support.MemoryStringUtils;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.support.ReflectionUtils;

import java.util.*;

public class ArrayMemory extends Memory implements Iterable<ReferenceMemory>, Traversable {
    protected long lastLongIndex;
    protected int size;
    protected int copies;
    protected ArrayMemory original;

    protected List<ReferenceMemory> list;
    protected LinkedMap<Object, ReferenceMemory> map;

    protected ThreadLocal<ForeachIterator> foreachIterator = new ThreadLocal<ForeachIterator>();

    public ArrayMemory(boolean asMap) {
        super(Type.ARRAY);
        if (asMap)
            convertToMap();
        else {
            list = new ArrayList<ReferenceMemory>();
        }
        lastLongIndex = -1;
    }

    public ArrayMemory(){
        this(false);
    }

    public ArrayMemory(Collection collection){
        this();
        for(Object el : collection){
            add(MemoryUtils.valueOf(el));
        }
    }

    public ArrayMemory(Object... array){
        this();
        for(Object el : array){
            list.add(new ReferenceMemory(MemoryUtils.valueOf(el)));
        }
        size = array.length;
        lastLongIndex = size - 1;
    }

    public ArrayMemory(boolean toImmutable, Memory... array){
        this();
        if (array != null){
            for(Memory el : array){
                list.add(new ReferenceMemory(toImmutable ? el.toImmutable() : el));
            }
            size = array.length;

            lastLongIndex = size - 1;
        }
    }

    public ArrayMemory(String[] array){
        this();
        for(String el : array) {
            list.add(new ReferenceMemory(new StringMemory(el)));
        }
        size = array.length;
        lastLongIndex = size - 1;
    }

    public ArrayMemory(Map map){
        this();
        for(Object key : map.keySet()){
            Object el = map.get(key);
            put(ArrayMemory.toKey(MemoryUtils.valueOf(key)), MemoryUtils.valueOf(el));
        }
    }

    public static Memory valueOf(){
        return new ArrayMemory();
    }

    public static ArrayMemory valueOfRef(ArrayMemory value){
        if (value == null)
            return new ArrayMemory();
        else
            return value;
    }

    public ArrayMemory duplicate(){
        ArrayMemory result = new ArrayMemory();
        result.lastLongIndex = lastLongIndex;
        result.size = size;
        if (list != null){
            for(ReferenceMemory item : list){
                result.list.add(item.duplicate());
            }
        } else {
            result.list = null;
            result.map = new LinkedMap<Object, ReferenceMemory>(map);
            for(Map.Entry<Object, ReferenceMemory> entry : map.entrySet()){
                result.map.put(entry.getKey(), entry.getValue().duplicate());
            }
        }

        return result;
    }

    public ArrayMemory checkCopied(){
        if (original != null || copies > 0) {
            ArrayMemory dup = duplicate();
            this.map  = dup.map;
            this.list = dup.list;
            this.lastLongIndex = dup.lastLongIndex;

            if (this.original == null){
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

    public static Object toKey(Memory key){
        switch (key.type){
            case STRING: {
                String key1 = key.toString();
                Memory number = StringMemory.toLong(key1);
                if (number == null)
                    return key1;
                else
                    return number;
            }
            case INT: return key;
            case NULL: return Memory.CONST_EMPTY_STRING;
            case REFERENCE: return toKey(key.toValue());
            default:
                return LongMemory.valueOf(key.toLong());
        }
    }

    private void convertToMap(){
        map = new LinkedMap<Object, ReferenceMemory>();
        if (list != null){
            int i = 0;
            for(ReferenceMemory memory : list){
                if (memory != null){
                    map.put(LongMemory.valueOf(i), memory);
                }
                i++;
            }
        }
        list = null;
    }

    public void renameKey(Memory oldKey, Memory newKey){
        checkCopied();
        if (list != null)
            convertToMap();

        Object key1 = toKey(oldKey);
        Object key2 = toKey(newKey);

        map.put(key2, map.remove(key1));
    }

    public ReferenceMemory get(Memory key){
        return getByScalar(toKey(key));
    }

    public ReferenceMemory getOrCreate(Memory key){
        return getByScalarOrCreate(toKey(key));
    }

    public ReferenceMemory getOrCreateAsShortcut(Memory key){
        return getByScalarOrCreateAsShortcut(toKey(key));
    }

    public ReferenceMemory getByScalarOrCreate(Object sKey, Memory initValue){
        ReferenceMemory value = getByScalar(sKey);
        if (value == null)
            return put(sKey, initValue);

        return value;
    }

    public ReferenceMemory getByScalarOrCreateAsShortcut(Object sKey){
        //checkCopied();
        ReferenceMemory value = getByScalar(sKey);
        if (value == null) {
            value = new ReferenceMemory(UNDEFINED);
            return put(sKey, new ShortcutMemory(value));
        }

        if (value instanceof ShortcutMemory)
            return (ShortcutMemory)value.value;
        else {
            put(sKey, new ShortcutMemory(value));
            return value;
        }
    }

    public ReferenceMemory getByScalarOrCreate(Object sKey){
        return getByScalarOrCreate(sKey, UNDEFINED);
    }

    public ReferenceMemory getByScalar(Object key){
        if (list != null){
            if (key instanceof Memory){
                int index = (int)((Memory) key).toLong();
                if (index >= 0 && index < list.size()){
                    return list.get(index);
                } else
                    return null;
            } else
                return null;
        } else {
            return map.get(key);
        }
    }

    public ReferenceMemory add(Memory value){
        if (value instanceof KeyValueMemory){
            KeyValueMemory keyValue = (KeyValueMemory)value;
            return put(toKey(keyValue.key), keyValue.value);
        }

        ReferenceMemory ref;
        if (list != null){
            lastLongIndex++;
            ref = new ReferenceMemory(value);
            list.add(ref);
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
    public void merge(ArrayMemory array, boolean recursive, Set<Integer> done){
        checkCopied();
        if (recursive && done == null)
            done = new HashSet<Integer>();

        if (list != null && array.list != null){
            for(ReferenceMemory reference : array.list)
                list.add(new ReferenceMemory(reference.toImmutable()));

            size = list.size();
            lastLongIndex = size - 1;
        } else {
            if (list != null)
                convertToMap();

            if (array.list != null){
                for(ReferenceMemory reference : array.list){
                    add(reference.toImmutable());
                }
            } else {
                for(Map.Entry<Object, ReferenceMemory> entry : array.map.entrySet()){
                    Object key = entry.getKey();
                    if (key instanceof LongMemory){
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

                                ArrayMemory result = (ArrayMemory)value; // already array immutable above
                                result.merge((ArrayMemory)current, recursive, done);
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

    public void putAll(ArrayMemory array){
        if (array.list != null){
            int i = 0;
            for(ReferenceMemory memory : array.list){
                if (memory != null)
                    put(LongMemory.valueOf(i), memory.toImmutable());
                i++;
            }
        } else {
            if (list != null)
                convertToMap();

            if (array.lastLongIndex > lastLongIndex)
                lastLongIndex = array.lastLongIndex;

            for(Map.Entry<Object, ReferenceMemory> entry : array.map.entrySet()){
                put(entry.getKey(), entry.getValue().toImmutable());
            }
        }
    }

    public void putAllRef(ArrayMemory array){
        if (array.list != null){
            int i = 0;
            for(ReferenceMemory memory : array.list){
                if (memory != null)
                    put(LongMemory.valueOf(i), memory);
                i++;
            }
        } else {
            if (list != null)
                convertToMap();

            if (array.lastLongIndex > lastLongIndex)
                lastLongIndex = array.lastLongIndex;

            map.putAll(array.map);
        }
    }

    public ReferenceMemory putAsKeyString(String key, Memory value){
        ReferenceMemory mem = new ReferenceMemory(value);
        if (list != null)
            convertToMap();

        Memory last = map.put(key, mem);
        if (last == null){
            size++;
        }
        return mem;
    }

    public ReferenceMemory put(Object key, Memory value) {
        ReferenceMemory mem = new ReferenceMemory(value);

        if (key instanceof LongMemory){
            int index = (int)((LongMemory)key).value;

            if (index > lastLongIndex)
                lastLongIndex = index;

            if (list != null){
                int size  = list.size();
                if (index >= 0){
                    if (index < size){
                        list.set(index, mem);
                        //this.size++;
                        return mem;
                    } else if (index == size){
                        list.add(mem);
                        this.size++;
                        return mem;
                    } else {
                        convertToMap();
                    }
                } else
                    convertToMap();
            }
        } else {
            if (list != null)
                convertToMap();
        }

        Memory last = map.put(key, mem);
        if (last == null){
            size++;
        }
        return mem;
    }

    public Memory removeByScalar(Object key){
        if (list != null){
            int index = -1;
            if (key instanceof Long)
                index = ((Long) key).intValue();
            else if (key instanceof Integer)
                index = ((Integer) key);
            else if (key instanceof String){
                Memory tmp = StringMemory.toLong((String)key);
                if (tmp != null)
                    index = (int) tmp.toLong();
            }

            if (index < 0 || index >= list.size())
                return null;

            if (index == size - 1) {
                size--;
                lastLongIndex = size - 1;
                return list.remove(index);
            } else {
                key = (long)index;
                convertToMap();
            }
        }

        {
            if (key instanceof Long)
                key = LongMemory.valueOf((Long)key);
            else if (key instanceof Integer)
                key = LongMemory.valueOf((Integer)key);
            else if (key instanceof String){
                Memory tmp = StringMemory.toLong((String)key);
                if (tmp != null)
                    key = tmp;
            }

            Memory memory = map.remove(key);
            if (memory != null)
                size--;

            return memory;
        }
    }

    public Memory remove(Memory key){
        Object _key = toKey(key);
        if (list != null){
            int index = _key instanceof LongMemory ? (int) key.toLong() : -1;
            if (index < 0 || index >= list.size())
                return null;

            if (index == size - 1){
                size--;
                lastLongIndex = index - 1;
                return list.remove(index);
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

    public int size(){
        return size;
    }

    public void unshift(Memory value, int count){
        checkCopied();
        if (size == 0) {
            for(int i = 0; i < count; i++)
                add(value);
        }  else {
            if (list != null) {
                List<ReferenceMemory> tmp = new ArrayList<ReferenceMemory>();
                for(int i = 0; i < count; i++)
                    tmp.add(new ReferenceMemory(value));

                list.addAll(0, tmp);
                size = list.size();
            } else {
                ArrayMemory tmp = new ArrayMemory();
                tmp.convertToMap();

                for(int i = 0; i < count; i++)
                    tmp.add(value);

                ForeachIterator iterator = getNewIterator(null, false, false);

                while (iterator.next()){
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

    public void unshift(Memory... values){
        checkCopied();
        if (size == 0) {
            for (Memory value : values)
                add(value);
        }  else {
            if (list != null) {
                List<ReferenceMemory> tmp = new ArrayList<ReferenceMemory>();
                for(Memory value : values)
                    tmp.add(new ReferenceMemory(value));

                list.addAll(0, tmp);
                size = list.size();
            } else {
                ArrayMemory tmp = new ArrayMemory();
                tmp.convertToMap();

                for(Memory el : values)
                    tmp.add(el);

                ForeachIterator iterator = getNewIterator(null, false, false);

                while (iterator.next()){
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

    public Memory shift(){
        checkCopied();
        if (size < 1)
            return null;

        size -= 1;
        Memory value;
        if (list != null){
            value = list.get(0);
            list.remove(0);
        } else {
            value = map.remove(map.firstKey());
        }

        return value.toValue();
    }

    public Memory pop(){
        checkCopied();
        if (size < 1)
            return null;

        size -= 1;
        Memory value;
        if (list != null){
            value = list.get(size - 1);
            list.remove(size - 1);
        } else {
            value = map.remove(map.lastKey());
        }
        return value.toValue();
    }

    public Memory peek(){
        if (size < 1)
            return null;

        Memory value;
        if (list != null)
            value = list.get(size - 1);
        else {
            value = map.get(map.lastKey());
        }

        return value.toValue();
    }

    public Memory getRandomElementKey(Random rnd){
        int index = rnd.nextInt(size);
        if (list != null){
            return LongMemory.valueOf(index);
        } else {
            Iterator<Object> keys = map.keySet().iterator();
            for(int i = 0; i < index; i++){
                keys.next();
            }

            Object key = keys.next();
            if (key instanceof LongMemory)
                return (LongMemory)key;
            else
                return new StringMemory((String)key);
        }
    }

    public void shuffle(Random rnd){
        checkCopied();
        if (list != null){
            Collections.shuffle(list, rnd);
        } else {
            Set<Object> keys = map.keySet();

            List<ReferenceMemory> values = new ArrayList<ReferenceMemory>(map.values());
            Collections.shuffle(values, rnd);

            int i = 0;
            for(Object key : keys){
                map.put(key, values.get(i));
                i++;
            }
        }
    }

    public void clear(){
        if (list != null){
            list = new ArrayList<ReferenceMemory>();
        }

        if (map != null){
            map = new LinkedMap<Object, ReferenceMemory>();
        }

        size = 0;
    }

    public int compare(ArrayMemory otherRef, boolean strict) {
        return compare(otherRef, strict, null);
    }

    public int compare(ArrayMemory otherRef, boolean strict, Set<Integer> used) {
        int size1 = size(),
                size2 = otherRef.size();

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

        while (iterator.next()){
            Memory value1 = iterator.getValue();
            Memory key    = iterator.getMemoryKey();
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

            if (value2 == null)
                return -2;

            if (value1.isArray() && value2.isArray()){
                ArrayMemory arr1 = value1.toValue(ArrayMemory.class);
                if (used.add(value2.getPointer())){
                    int r = arr1.compare(value2.toValue(ArrayMemory.class), strict, used);
                    if (r == 0) {
                        used.remove(value2.getPointer());
                        continue;
                    }
                    return r;
                }
                used.remove(value2.getPointer());
            } else if (value1.isObject() && value2.isObject()){
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
        if (copies >= 0){
            ArrayMemory mem = new ArrayMemory();
            mem.list = list;
            mem.original = this;
            mem.size = size;
            mem.list = list;
            mem.map  = map;
            mem.lastLongIndex = lastLongIndex;
            copies++;
            resetCurrentIterator();
            return mem;
        } else {
            copies++;
            return this;
        }
    }

    public ArrayMemory toConstant(){
        if (copies == 0)
            copies--;
        else
            throw new RuntimeException("Cannot convert array to a constant value with copies != 0");
        return this;
    }

    public Memory[] values(boolean asImmutable){
        Memory[] result = new Memory[size];
        int i = 0;
        for(ReferenceMemory el : this){
            result[i++] = asImmutable ? el.toImmutable() : el.toValue();
        }
        return result;
    }

    public Memory[] values(){
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
        switch (memory.type){
            case ARRAY:
                ArrayMemory left  = (ArrayMemory)toImmutable();
                ArrayMemory other = (ArrayMemory)memory;
                ForeachIterator iterator = other.foreachIterator(false, false);
                while (iterator.next()){
                    Object key = iterator.getKey();
                    Memory origin = getByScalar(key);
                    if (origin == null){
                        left.checkCopied();
                        left.put(key, iterator.getValue().toImmutable());
                    }
                }
                return left;
            case REFERENCE: return plus(memory.toValue());
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
    public Memory div(Memory memory) {
        return toNumeric().div(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        switch (memory.type){
            case ARRAY:
                return compare((ArrayMemory)memory, false) == 0;
            case REFERENCE: return equal(memory.toValue());
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
        switch (memory.type){
            case ARRAY:
                return compare((ArrayMemory)memory, false) == -1;
            case REFERENCE: return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean smallerEq(Memory memory) {
        switch (memory.type){
            case ARRAY:
                int r = compare((ArrayMemory)memory, false);
                return r == 0 || r == -1;
            case REFERENCE: return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean greater(Memory memory) {
        switch (memory.type){
            case ARRAY:
                return compare((ArrayMemory)memory, false) == 1;
            case REFERENCE: return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public boolean greaterEq(Memory memory) {
        switch (memory.type){
            case ARRAY:
                int r = compare((ArrayMemory)memory, false);
                return r == 0 || r == 1;
            case REFERENCE: return equal(memory.toValue());
            default:
                return false;
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public void unset() {
        if (original != null){
            original.copies--;
            original = null;
        } else
            copies--;

        if (list != null){
            for(ReferenceMemory memory : list)
                if (memory.type == type)
                    memory.unset();
        }

        if (map != null){
            for(ReferenceMemory memory : map.values())
                if (memory.type == type)
                    memory.unset();
        }

        clear();
    }

    @Override
    public Memory valueOfIndex(TraceInfo trace, Memory index) {
        switch (index.getRealType()){
            case OBJECT:
            case ARRAY: return UNDEFINED; // TODO ADD WARNING
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
        Memory e = getByScalar(index);
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
    public Memory refOfPush(TraceInfo trace){
        checkCopied();
        return add(UNDEFINED);
    }

    @Override
    public Memory refOfIndexAsShortcut(TraceInfo trace, Memory index) {
        switch (index.getRealType()){
            case OBJECT:
            case ARRAY: return new ReferenceMemory(); // TODO ADD WARNING
        }
        checkCopied();
        return getOrCreateAsShortcut(index);
    }

    @Override
    public Memory refOfIndex(TraceInfo trace, Memory index) {
        switch (index.getRealType()){
            case OBJECT:
            case ARRAY: return new ReferenceMemory(); // TODO ADD WARNING
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
        switch (memory.type){
            case ARRAY:
                return compare((ArrayMemory)memory, true) == 0;
            case REFERENCE: return equal(memory.toValue());
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
    public Iterator<ReferenceMemory> iterator() {
        if (list != null) {
            return list.iterator();
        } else
            return map.values().iterator();
    }

    public ForeachIterator foreachIterator(boolean getReferences, boolean withPrevious) {
        return foreachIterator(getReferences, false, withPrevious);
    }

    public ForeachIterator foreachIterator(boolean getReferences, boolean getKeyReferences, boolean withPrevious) {
        return new ForeachIterator(getReferences, getKeyReferences, withPrevious){
            protected int cursor = 0;
            protected int listMax;
            protected Iterator<Object> keys;

            @Override
            protected boolean init() {
                if (getKeyReferences && list != null)
                    ArrayMemory.this.convertToMap();

                if (list == null) {
                    if (withPrevious || getKeyReferences)
                        keys = new ArrayList<Object>(map.keySet()).listIterator();
                    else
                        keys = new ArrayList<Object>(map.keySet()).iterator();
                } else {
                    listMax = list.size();
                }
                return true;
            }

            private void setCurrentValue(ReferenceMemory value){
                if (getReferences) {
                    if (plainReferences)
                        currentValue = value;
                    else
                        currentValue = new ArrayValueMemory(getMemoryKey(), ArrayMemory.this, value);
                }  else
                    currentValue = value.value;

                if (getKeyReferences) {
                    currentKeyMemory = new ArrayKeyMemory(ArrayMemory.this, getMemoryKey());
                }
            }

            @Override
            public boolean end() {
                if (ArrayMemory.this.size == 0)
                    return false;

                if (ArrayMemory.this.list != null){
                    cursor = ArrayMemory.this.size - 1;
                    currentKey = (long)cursor;
                    setCurrentValue(list.get(cursor));
                    return true;
                } else {
                    ArrayList<Object> tmp = new ArrayList<Object>(map.keySet());
                    keys = tmp.listIterator(tmp.size() - 1);
                    return true;
                }
            }

            @Override
            protected boolean prevValue() {
                if (ArrayMemory.this.list != null) {
                    if (cursor <= 0){
                        currentKey = null;
                        currentValue = null;
                        cursor--;
                        keys = null;
                        return false;
                    } else {
                        cursor--;
                        currentKey = (long)cursor;
                        setCurrentValue(list.get(cursor));
                        return true;
                    }
                } else {
                    ListIterator<Object> keyIterator = (ListIterator) keys;
                    if (keyIterator.hasPrevious()) {
                        currentKey = keyIterator.previous();
                        setCurrentValue(map.get(currentKey));
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

                if (ArrayMemory.this.list != null) {
                    if (cursor >= listMax || size < listMax) {
                        currentKey = null;
                        currentValue = null;
                        return false;
                    }

                    currentKey = (long)cursor;
                    setCurrentValue(list.get(cursor));
                    cursor++;
                    return true;
                } else {
                    if (keys == null) {
                        ArrayList<Object> tmp = new ArrayList<Object>(map.keySet());
                        keys = tmp.listIterator(cursor - 1);
                    }

                    if (keys.hasNext()) {
                        currentKey = keys.next();
                        setCurrentValue(map.get(currentKey));
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
        if (foreachIterator.get() == null) {
            ForeachIterator iterator = foreachIterator(false, true);
            foreachIterator.set(iterator);
        }

        return foreachIterator.get();
    }

    public Memory resetCurrentIterator(){
        foreachIterator.set(null);
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
    public byte[] getBinaryBytes() {
        return MemoryStringUtils.getBinaryBytes(this);
    }

    public boolean isList(){
        return list != null;
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
        while (iterator.next()){
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
        for(Memory e : this) {
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
}
