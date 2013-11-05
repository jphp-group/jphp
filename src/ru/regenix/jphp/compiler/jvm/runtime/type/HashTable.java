package ru.regenix.jphp.compiler.jvm.runtime.type;

import ru.regenix.jphp.compiler.jvm.runtime.memory.ArrayItemMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.ArrayMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.LongMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HashTable implements Iterable<Memory> {

    protected long lastLongIndex = -1;
    protected int size = 0;

    protected int copies = 0;

    protected List<Object> keys;
    protected List<Memory> list;

    //protected Map<Memory, Memory> map;
    protected FastIntMap<Memory> map;

    public HashTable() {
        list = new ArrayList<Memory>();
        keys = null;
    }

    public boolean isCopied() {
        return copies > 1;
    }

    public void addCopy() {
        copies++;
    }

    public static Object toKey(Memory key){
        switch (key.type){
            case STRING: {
                try {
                    return LongMemory.valueOf(Long.parseLong(key.toString()));
                } catch (NumberFormatException e){
                    return key.toString();
                }
            }
            case INT: return key;
            case NULL: return Memory.CONST_INT_0;
            case REFERENCE: return toKey(key.toImmutable());
            default:
                return LongMemory.valueOf(key.toLong());
        }
    }

    public ArrayItemMemory get(ArrayMemory ref, Memory key){
        return getByScalar(ref, toKey(key));
    }

    public ArrayItemMemory getByScalar(ArrayMemory ref, Object key){
        if (list != null){
            if (key instanceof Memory){
                int index = (int)((Memory) key).toLong();
                if (index < list.size()){
                    return new ArrayItemMemory(ref, key, list.get(index));
                } else
                    return new ArrayItemMemory(ref, key);
            } else
                return new ArrayItemMemory(ref, key);
        } else {
            Memory memory = map.get(key);
            if (memory == null){
                return new ArrayItemMemory(ref, key);
            } else
                return new ArrayItemMemory(ref, key, memory);
        }
    }

    public void add(Memory value){
        if (list != null){
            list.add(value);
        } else {
            lastLongIndex++;
            put(LongMemory.valueOf(lastLongIndex), value);
        }
        size++;
    }

    private void convertToMap(){
        map = new FastIntMap<Memory>();
        keys = new ArrayList<Object>();
        int i = 0;
        for(Memory memory : list){
            Object key = keys.get(i);
            map.put(key, i, memory);
            i++;
        }
        list = null;
    }

    public void put(Object key, Memory value) {
        if (key instanceof LongMemory){
            int index = (int)((LongMemory)key).value;

            if (index > lastLongIndex)
                lastLongIndex = index;

            if (list != null){
                int size  = list.size();
                if (index >= 0){
                    if (index < size){
                        list.set(index, value);
                        this.size++;
                        return;
                    } else if (index == size){
                        list.add(value);
                        this.size++;
                        return;
                    } else {
                        convertToMap();
                    }
                }
            }
        } else {
            if (list != null)
                convertToMap();
        }

        Memory last = map.put(key, size, value);
        if (last == null){
            keys.add(key);
            size++;
        }
    }

    public Memory remove(Object key){
        if (list != null){
            if (key instanceof Long){
                int index = ((Long)key).intValue();
                try {
                    Memory result = list.get(index);
                    list.set(index, null);
                    keys.set(index, null);
                    size--;
                    return result;
                } catch (IndexOutOfBoundsException e){
                    return null;
                }
            } else {
                return null;
            }
        } else {
            FastIntMap.Entry entry = map.getEntry(key);
            if (key == null)
                return null;

            map.remove(key);
            keys.set(entry.keyIndex, null);
            size--;
            return (Memory) entry.value;
        }
    }

    public int size(){
        return size;
    }

    public void clear(){
        if (list != null)
            list.clear();

        if (map != null)
                map.clear();

        keys.clear();
        size = 0;
    }

    public int compare(ArrayMemory ref, ArrayMemory otherRef) throws UncomparableArrayException {
        return compare(ref, otherRef, otherRef.value);
    }

    public int compare(ArrayMemory ref, ArrayMemory otherRef, HashTable other) throws UncomparableArrayException {
        int size1 = size(),
            size2 = other.size();

        if (size1 < size2)
            return -1;
        else if (size1 > size2)
            return 1;

        HashIterator iterator = this.iterator(ref);
        while (iterator.hasNext()){
            Memory value1 = iterator.next();
            Object key    = iterator.getCurrentKey();
            Memory value2 = other.getByScalar(otherRef, key);

            if (value2 == null)
                throw new UncomparableArrayException();

            if (value1.smaller(value2))
                return -1;
            else
                return 1;
        }
        return 0;
    }

    public HashIterator iterator(ArrayMemory ref) {
        return new HashIterator(ref);
    }

    @Override
    public Iterator<Memory> iterator() {
        return iterator(null);
    }

    public static HashTable copyOf(HashTable value) {
        HashTable result = new HashTable();
        if (value.list != null){
            result.list.addAll(value.list);
        } else {
            result.list = null;
            result.map = value.map.duplicate();
            result.keys = new ArrayList<Object>(value.keys);
        }
        result.size = value.size;
        result.lastLongIndex = value.lastLongIndex;
        return result;
    }

    public class HashIterator implements Iterator<Memory> {
        private Memory current;
        private Object currentKey;
        private ArrayMemory ref;

        private ListIterator<Object> keyIterator;

        HashIterator(ArrayMemory ref){
            List<Object> keys = new ArrayList<Object>();
            for(Object key : HashTable.this.keys){
                if (key != null)
                    keys.add(key);
            }
            keyIterator = keys.listIterator();
            this.ref = ref;
        }

        @Override
        public boolean hasNext() {
            return keyIterator.hasNext();
        }

        @Override
        public Memory next() {
            if (!hasNext())
                return null;

            currentKey = keyIterator.next();
            return current = HashTable.this.getByScalar(ref, currentKey);
        }

        public Object getCurrentKey() {
            return currentKey;
        }

        public Memory getCurrent() {
            return current;
        }

        @Override
        public void remove() {
            HashTable.this.remove(currentKey);
        }
    }


    public static class UncomparableArrayException extends Exception {}
}
