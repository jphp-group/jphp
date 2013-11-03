package ru.regenix.jphp.compiler.jvm.runtime.type;

import ru.regenix.jphp.compiler.jvm.runtime.memory.ArrayItemMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.LongMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;

import java.util.*;

public class HashTable {

    protected long lastLongIndex = -1;

    protected List<Memory> keys;
    protected List<Memory> list;

    protected Map<Memory, Memory> map;

    public HashTable() {
        list = new ArrayList<Memory>();
        keys = new ArrayList<Memory>();
    }

    protected Memory toKey(Memory key){
        switch (key.type){
            case STRING: {
                try {
                    return new LongMemory(Long.parseLong(key.toString()));
                } catch (NumberFormatException e){
                    return key;
                }
            }
            case INT: return key;
            case NULL: return Memory.CONST_INT_0;
            case REFERENCE: return toKey(key.toImmutable());
            default:
                return new LongMemory(key.toLong());
        }
    }

    public Memory get(Memory key){
        if (list != null){
            key = toKey(key);
            if (key.type == Memory.Type.INT){
                try {
                    return new ArrayItemMemory(this, key, list.get((int)((LongMemory)key).value));
                } catch (IndexOutOfBoundsException e){
                    return new ArrayItemMemory(this, key);
                }
            } else
                return new ArrayItemMemory(this, key);
        } else {
            Memory memory = map.get(key);
            if (memory == null){
                return new ArrayItemMemory(this, toKey(key));
            } else
                return new ArrayItemMemory(this, key, memory);
        }
    }

    public Memory add(Memory value){
        Memory key;
        if (list != null){
            keys.add(key = LongMemory.valueOf(keys.size()));
            list.add(value);
        } else {
            lastLongIndex++;
            put(key = LongMemory.valueOf(lastLongIndex), value);
        }
        return new ArrayItemMemory(this, key, value);
    }

    private void convertToMap(){
        map = new LinkedHashMap<Memory, Memory>();
        int i = 0;
        for(Memory memory : list){
            Memory key = keys.get(i);
            map.put(key, memory);
            i++;
        }
        list = null;
        keys = null;
    }

    public Memory put(Memory key, Memory value) {
        key = toKey(key);
        if (key.type == Memory.Type.INT){
            if (((LongMemory)key).value > lastLongIndex)
                lastLongIndex = ((LongMemory)key).value;

            if (list != null){
                int size  = list.size();
                int index = (int)((LongMemory)key).value;
                if (index >= 0){
                    if (index < size){
                        list.set(index, value);
                        return new ArrayItemMemory(this, key, value);
                    } else if (index == size){
                        list.add(value);
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

        map.put(key, value);
        return new ArrayItemMemory(this, key, value);
    }

    public int size(){
        return list == null ? map.size() : list.size();
    }

    public int compare(HashTable other) throws UncomparableArrayException {
        int size1 = size(),
            size2 = other.size();

        if (size1 < size2)
            return -1;
        else if (size1 > size2)
            return 1;

        if (list != null){
            for(int i = 0; i < size1; i++){
                Memory value2 = other.get(keys.get(i));
                if (value2 == null)
                    throw new UncomparableArrayException();

                Memory value1 = list.get(i);
                if (value1.smaller(value2))
                    return -1;
                else
                    return 1;
            }
        } else {
            for (Map.Entry<Memory, Memory> entry : map.entrySet()){
                Memory value2 = other.get(entry.getKey());
                if (value2 == null)
                    throw new UncomparableArrayException();

                Memory value1 = entry.getValue();
                if (value1.smaller(value2))
                    return -1;
                else if (value1.greater(value2))
                    return 1;
            }
        }

        return 0;
    }

    public static class UncomparableArrayException extends Exception {}
}
