package ru.regenix.jphp.runtime.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MemoryUtils {

    public static Object toValue(Memory value, Class<?> type){
        if (type == Double.TYPE || type == Double.class)
            return value.toDouble();
        if (type == Float.TYPE || type == Float.class)
            return (float)value.toDouble();
        if (type == Long.TYPE || type == Long.class)
            return value.toLong();
        if (type == Integer.TYPE || type == Integer.class)
            return (int)value.toLong();
        if (type == Short.TYPE || type == Short.class)
            return (short)value.toLong();
        if (type == Byte.TYPE || type == Byte.class)
            return (byte)value.toLong();
        if (type == String.class)
            return value.toString();
        if (type == Boolean.TYPE || type == Boolean.class)
            return value.toBoolean();
        if (type == Memory.class)
            return value;
        if (type == Memory[].class){
            if (value.isArray()){
                List<Memory> result = new ArrayList<Memory>();
                for(Memory one : (ArrayMemory)value){
                    result.add(one.toImmutable());
                }
                return result.toArray(new Memory[]{});
            } else {
                return new ArrayMemory();
            }
        }

        throw new IllegalArgumentException("Unexpected class type: " + type.getName());
    }

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
            ArrayMemory array = new ArrayMemory();
            for(Object el : (Object[])value){
                array.add(valueOf(el));
            }
            return array;
        } else if (value instanceof Collection){
            ArrayMemory array = new ArrayMemory();
            for(Object el : (Collection)value){
                array.add(valueOf(el));
            }
            return array;
        } else if (value instanceof Map){
            ArrayMemory table = new ArrayMemory();
            for(Object key : ((Map) value).keySet()){
                Object el = ((Map)value).get(key);
                table.put(ArrayMemory.toKey(valueOf(key)), valueOf(el));
            }
            return table;
        } else
            return Memory.NULL;
    }
}
