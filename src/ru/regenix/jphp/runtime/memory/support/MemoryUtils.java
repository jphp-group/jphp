package ru.regenix.jphp.runtime.memory.support;

import ru.regenix.jphp.runtime.memory.*;

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
            return LongMemory.valueOf((Byte)value);
        if (value instanceof Short)
            return LongMemory.valueOf((Short)value);
        if (value instanceof Integer)
            return LongMemory.valueOf((Integer)value);
        if (value instanceof Long)
            return LongMemory.valueOf((Long)value);
        if (value instanceof Float)
            return new DoubleMemory((Float)value);
        if (value instanceof Double)
            return new DoubleMemory((Double)value);
        if (value instanceof Boolean){
            return (Boolean) value ? Memory.TRUE : Memory.FALSE;
        } else if (value == null){
            return Memory.NULL;
        } else if (value instanceof Collection){
            return new ArrayMemory((Collection)value);
        } else if (value instanceof Map){
            return new ArrayMemory((Map) value);
        } else if (value.getClass().isArray()){
                return new ArrayMemory((Object[])value);
        } else
            return Memory.NULL;
    }
}
