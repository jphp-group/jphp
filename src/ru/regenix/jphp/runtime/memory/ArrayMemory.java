package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.lang.NullSkipIterator;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryStringUtils;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;

import java.util.*;

public class ArrayMemory extends Memory implements Iterable<ReferenceMemory> {

    protected long lastLongIndex;
    protected int size;
    protected int copies;
    protected ArrayMemory original;

    protected List<ReferenceMemory> list;
    protected Map<Object, ReferenceMemory> map;

    public ArrayMemory() {
        super(Type.ARRAY);
        list = new ArrayList<ReferenceMemory>();
        lastLongIndex = -1;
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
            result.map = new LinkedHashMap<Object, ReferenceMemory>(map);
            for(Map.Entry<Object, ReferenceMemory> entry : map.entrySet()){
                result.map.put(entry.getKey(), entry.getValue().duplicate());
            }
        }

        return result;
    }

    public void checkCopied(){
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
        }
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
            case NULL: return Memory.CONST_INT_0;
            case REFERENCE: return toKey(key.toImmutable());
            default:
                return LongMemory.valueOf(key.toLong());
        }
    }

    private void convertToMap(){
        map = new LinkedHashMap<Object, ReferenceMemory>();
        int i = 0;
        for(ReferenceMemory memory : list){
            if (memory != null){
                map.put(LongMemory.valueOf(i), memory);
            }
            i++;
        }
        list = null;
    }

    public ReferenceMemory get(Memory key){
        return getByScalar(toKey(key));
    }

    public ReferenceMemory getOrCreate(Memory key){
        return getByScalarOrCreate(toKey(key));
    }

    public ReferenceMemory getByScalarOrCreate(Object sKey){
        ReferenceMemory value = getByScalar(sKey);
        if (value == null)
            return put(sKey, NULL);

        return value;
    }

    public ReferenceMemory getByScalar(Object key){
        if (list != null){
            if (key instanceof Memory){
                int index = (int)((Memory) key).toLong();
                if (index < list.size()){
                    return list.get(index);
                } else
                    return null;
            } else
                return null;
        } else {
            return map.get(key);
        }
    }

    public void add(Memory value){
        lastLongIndex++;
        if (list != null){
            list.add(new ReferenceMemory(value));
        } else {
            put(LongMemory.valueOf(lastLongIndex), value);
        }
        size++;
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
                        this.size++;
                        return mem;
                    } else if (index == size){
                        list.add(mem);
                        this.size++;
                        return mem;
                    } else {
                        convertToMap();
                    }
                }
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

            Memory result = list.get(index);
            list.set(index, null);
            size--;
            return result;
        } else {
            if (key instanceof Long)
                key = LongMemory.valueOf((Long)key);

            Memory memory = map.remove(key);
            if (memory != null)
                size--;

            return memory;
        }
    }

    public Memory remove(Memory key){
        if (list != null){
            int index = (int) key.toLong();
            if (index < 0 || index >= list.size())
                return null;

            Memory result = list.get(index);
            list.set(index, null);
            size--;
            return result;
        } else {
            Memory memory = map.remove(toKey(key));
            if (memory != null)
                size--;

            return memory;
        }
    }

    public int size(){
        return size;
    }

    public void clear(){
        if (list != null){
            list = new ArrayList<ReferenceMemory>();
        }

        if (map != null){
            map = new LinkedHashMap<Object, ReferenceMemory>();
        }

        size = 0;
    }

    public int compare(ArrayMemory otherRef) throws UncomparableArrayException {
        int size1 = size(),
            size2 = otherRef.size();

        if (size1 < size2)
            return -1;
        else if (size1 > size2)
            return 1;

        //Iterator<ReferenceMemory> iterator = this.iterator();
        // TODO
        /*while (iterator.hasNext()){
            ReferenceMemory value1 = iterator.next();
            //Object key    = value1.key;
            Memory value2 = otherRef.getByScalar(key);

            if (value2 == null)
                throw new UncomparableArrayException();

            if (value1.smaller(value2))
                return -1;
            else
                return 1;
        }*/
        return 0;
    }

    @Override
    public Memory toImmutable() {
        ArrayMemory mem = new ArrayMemory();
        mem.list = list;
        mem.original = this;
        mem.size = size;
        mem.list = list;
        mem.map  = map;
        mem.lastLongIndex = lastLongIndex;
        copies++;
        return mem;
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
        return toNumeric().plus(memory);
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
    public Memory mod(Memory memory) {
        return toNumeric().mod(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        if (memory.type == Type.ARRAY){
            try {
                return compare((ArrayMemory)memory) == 0;
            } catch (UncomparableArrayException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean notEqual(Memory memory) {
        return !equals(memory);
    }

    @Override
    public boolean smaller(Memory memory) {
        if (memory.type == Type.ARRAY){
            try {
                return compare((ArrayMemory)memory) < 0;
            } catch (UncomparableArrayException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean smallerEq(Memory memory) {
        if (memory.type == Type.ARRAY){
            try {
                return compare((ArrayMemory)memory) <= 0;
            } catch (UncomparableArrayException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean greater(Memory memory) {
        if (memory.type == Type.ARRAY){
            try {
                return compare((ArrayMemory)memory) > 0;
            } catch (UncomparableArrayException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean greaterEq(Memory memory) {
        if (memory.type == Type.ARRAY){
            try {
                return compare((ArrayMemory)memory) >= 0;
            } catch (UncomparableArrayException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return size() == 0 ? 0 : 1;
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
    public Memory valueOfIndex(Memory index) {
        Memory e = get(index);
        return e == null ? NULL : e;
    }

    @Override
    public Memory valueOfIndex(long index) {
        Memory e = getByScalar(LongMemory.valueOf(index));
        return e == null ? NULL : e;
    }

    @Override
    public Memory valueOfIndex(double index) {
        Memory e = getByScalar(LongMemory.valueOf((long)index));
        return e == null ? NULL : e;
    }

    @Override
    public Memory valueOfIndex(boolean index) {
        Memory e = getByScalar(index ? CONST_INT_0 : CONST_INT_1);
        return e == null ? NULL : e;
    }

    @Override
    public Memory valueOfIndex(String index) {
        Memory e = getByScalar(index);
        return e == null ? NULL : e;
    }

    @Override
    public Memory refOfIndex(Memory index) {
        checkCopied();
        return getOrCreate(index);
    }

    @Override
    public Memory refOfIndex(long index) {
        checkCopied();
        return getOrCreate(LongMemory.valueOf(index));
    }

    @Override
    public Memory refOfIndex(double index) {
        return refOfIndex(LongMemory.valueOf((long) index));
    }

    @Override
    public Memory refOfIndex(boolean index) {
        checkCopied();
        return getOrCreate(index ? CONST_INT_1 : CONST_INT_0);
    }

    @Override
    public Memory refOfIndex(String index) {
        checkCopied();
        Memory number = StringMemory.toLong(index);
        return number == null ? getByScalarOrCreate(index) : getByScalar(number);
    }

    @Override
    public Iterator<ReferenceMemory> iterator() {
        if (list != null) {
            return new NullSkipIterator<ReferenceMemory>( list );
        } else
            return map.values().iterator();
    }


    public class HashIterator implements Iterator<ReferenceMemory> {
        private ReferenceMemory current;
        private Object currentKey;

        private Iterator<Object> keyIterator;
        private int cursor = 0;

        HashIterator(){
            if (ArrayMemory.this.list != null){
                keyIterator = null;
                cursor = -1;
            } else {
                keyIterator = map.keySet().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (keyIterator == null){
                for(int i = cursor; i++ < ArrayMemory.this.list.size(); i++){
                    if (ArrayMemory.this.list.get(i) != null)
                        return true;
                }
                return false;
            } else {
                return keyIterator.hasNext();
            }
        }

        @Override
        public ReferenceMemory next() {
            if (!hasNext())
                return null;

            currentKey = keyIterator.next();
            return current = ArrayMemory.this.getByScalar(currentKey);
        }

        public Object getCurrentKey() {
            return currentKey;
        }

        public Memory getCurrent() {
            return current;
        }

        @Override
        public void remove() {
            ArrayMemory.this.removeByScalar(currentKey);
        }
    }

    @Override
    public byte[] getBinaryBytes() {
        return MemoryStringUtils.getBinaryBytes(this);
    }

    public static class UncomparableArrayException extends Exception {}
}
