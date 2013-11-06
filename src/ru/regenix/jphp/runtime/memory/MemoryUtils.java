package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.type.HashTable;

import java.util.Collection;
import java.util.Map;

public class MemoryUtils {

    public static Memory valueOf(Object value){
        if (value instanceof Memory)
            return (Memory)value;
        if (value instanceof String)
            return new StringMemory((String)value);
        if (value instanceof Byte)
            return new LongMemory((Byte)value);
        if (value instanceof Short)
            return new LongMemory((Short)value);
        if (value instanceof Integer)
            return new LongMemory((Integer)value);
        if (value instanceof Long)
            return new LongMemory((Long)value);
        if (value instanceof Float)
            return new DoubleMemory((Float)value);
        if (value instanceof Double)
            return new DoubleMemory((Double)value);
        if (value instanceof Boolean){
            return (Boolean) value ? Memory.TRUE : Memory.FALSE;
        } else if (value == null){
            return Memory.NULL;
        } else if (value.getClass().isArray()){
            HashTable table = new HashTable();
            for(Object el : (Object[])value){
                table.add(valueOf(el));
            }
            return new ArrayMemory(table);
        } else if (value instanceof Collection){
            HashTable table = new HashTable();
            for(Object el : (Collection)value){
                table.add(valueOf(el));
            }
            return new ArrayMemory(table);
        } else if (value instanceof Map){
            HashTable table = new HashTable();
            for(Object key : ((Map) value).keySet()){
                Object el = ((Map)value).get(key);
                table.put(HashTable.toKey(valueOf(key)), valueOf(el));
            }
            return new ArrayMemory(table);
        } else if (value instanceof HashTable){
            return new ArrayMemory((HashTable)value);
        } else
            return Memory.NULL;
    }
}
