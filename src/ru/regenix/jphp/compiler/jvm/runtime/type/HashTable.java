package ru.regenix.jphp.compiler.jvm.runtime.type;

import ru.regenix.jphp.compiler.jvm.runtime.memory.ArrayItemMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.LongMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;

import java.util.*;

public class HashTable implements Iterable<Memory> {

    protected long lastLongIndex = -1;
    protected int size = 0;

    protected List<Object> keys;
    protected List<Memory> list;

    //protected Map<Memory, Memory> map;
    protected FastIntMap<Memory> map;

    public HashTable() {
        list = new ArrayList<Memory>();
        keys = new ArrayList<Object>();
    }

    public static Object toKey(Memory key){
        switch (key.type){
            case STRING: {
                try {
                    return Long.parseLong(key.toString());
                } catch (NumberFormatException e){
                    return key.toString();
                }
            }
            case INT: return ((LongMemory)key).value;
            case NULL: return 0L;
            case REFERENCE: return toKey(key.toImmutable());
            default:
                return key.toLong();
        }
    }

    public Memory get(Memory key){
        return getByScalar(toKey(key));
    }

    public Memory getByScalar(Object key){
        if (list != null){
            if (key instanceof Long){
                try {
                    return new ArrayItemMemory(this, key, list.get(((Long)key).intValue()));
                } catch (IndexOutOfBoundsException e){
                    return new ArrayItemMemory(this, key);
                }
            } else
                return new ArrayItemMemory(this, key);
        } else {
            Memory memory = map.get(key);
            if (memory == null){
                return new ArrayItemMemory(this, key);
            } else
                return new ArrayItemMemory(this, key, memory);
        }
    }

    public Memory add(Memory value){
        Object key;
        if (list != null){
            keys.add(key = keys.size());
            list.add(value);
        } else {
            lastLongIndex++;
            put(key = lastLongIndex, value);
        }
        size++;
        return new ArrayItemMemory(this, key, value);
    }

    private void convertToMap(){
        map = new FastIntMap<Memory>();
        int i = 0;
        for(Memory memory : list){
            Object key = keys.get(i);
            map.put(key, i, memory);
            i++;
        }
        list = null;
    }

    public Memory put(Object key, Memory value) {
        if (key instanceof Long){
            int index = ((Long)key).intValue();

            if (index > lastLongIndex)
                lastLongIndex = index;

            if (list != null){
                int size  = list.size();
                if (index >= 0){
                    if (index < size){
                        list.set(index, value);
                        this.size++;
                        return new ArrayItemMemory(this, key, value);
                    } else if (index == size){
                        list.add(value);
                        this.size++;
                        return new ArrayItemMemory(this, key, value);
                    } else {
                        convertToMap();
                    }
                }
            }
        } else {
            if (list != null)
                convertToMap();
        }

        map.put(key, keys.size(), value);
        keys.add(key);
        return new ArrayItemMemory(this, key, value);
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

    public int compare(HashTable other) throws UncomparableArrayException {
        int size1 = size(),
            size2 = other.size();

        if (size1 < size2)
            return -1;
        else if (size1 > size2)
            return 1;

        HashIterator iterator = this.iterator();
        while (iterator.hasNext()){
            Memory value1 = iterator.next();
            Object key    = iterator.getCurrentKey();
            Memory value2 = other.getByScalar(key);

            if (value2 == null)
                throw new UncomparableArrayException();

            if (value1.smaller(value2))
                return -1;
            else
                return 1;
        }
        return 0;
    }

    @Override
    public HashIterator iterator() {
        return new HashIterator();
    }

    public class HashIterator implements Iterator<Memory> {
        private Memory current;
        private Object currentKey;

        private ListIterator<Object> keyIterator;

        HashIterator(){
            List<Object> keys = new ArrayList<Object>();
            for(Object key : HashTable.this.keys){
                if (key != null)
                    keys.add(key);
            }
            keyIterator = keys.listIterator();
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
            return current = HashTable.this.getByScalar(currentKey);
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
