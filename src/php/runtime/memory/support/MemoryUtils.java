package php.runtime.memory.support;

import php.runtime.Memory;
import ru.regenix.jphp.common.HintType;
import php.runtime.memory.*;

import java.util.*;

public class MemoryUtils {

    protected final static Map<Class<?>, Converter> CONVERTERS = new HashMap<Class<?>, Converter>(){{
        // double
        put(Double.class, new Converter<Double>() {
            @Override
            public Double run(Memory value) {
                return value.toDouble();
            }
        });
        put(Double.TYPE, get(Double.class));

        // float
        put(Float.class, new Converter<Float>() {
            @Override
            public Float run(Memory value) {
                return (float)value.toDouble();
            }
        });
        put(Float.TYPE, get(Float.class));

        // long
        put(Long.class, new Converter<Long>() {
            @Override
            public Long run(Memory value) {
                return value.toLong();
            }
        });
        put(Long.TYPE, get(Long.class));

        // int
        put(Integer.class, new Converter<Integer>() {
            @Override
            public Integer run(Memory value) {
                return (int)value.toLong();
            }
        });
        put(Integer.TYPE, get(Integer.class));

        // short
        put(Short.class, new Converter<Short>() {
            @Override
            public Short run(Memory value) {
                return (short)value.toLong();
            }
        });
        put(Short.TYPE, get(Short.class));

        // byte
        put(Byte.class, new Converter<Byte>() {
            @Override
            public Byte run(Memory value) {
                return (byte)value.toLong();
            }
        });
        put(Byte.TYPE, get(Byte.class));

        // char
        put(Character.class, new Converter<Character>() {
            @Override
            public Character run(Memory value) {
                return value.toChar();
            }
        });
        put(Character.TYPE, get(Character.class));

        // bool
        put(Boolean.class, new Converter<Boolean>() {
            @Override
            public Boolean run(Memory value) {
                return value.toBoolean();
            }
        });
        put(Boolean.TYPE, get(Boolean.class));

        // string
        put(String.class, new Converter<String>() {
            @Override
            public String run(Memory value) {
                return value.toString();
            }
        });

        put(Memory.class, new Converter<Memory>() {
            @Override
            public Memory run(Memory value) {
                return value;
            }
        });

        put(Memory[].class, new Converter<Memory[]>() {
            @Override
            public Memory[] run(Memory value) {
                if (value.isArray()){
                    List<Memory> result = new ArrayList<Memory>();
                    for(Memory one : (ArrayMemory)value){
                        result.add(one.toImmutable());
                    }
                    return result.toArray(new Memory[]{});
                } else {
                    return null;
                }
            }
        });
    }};

    public static Converter<?> getConverter(Class<?> type){
        return CONVERTERS.get(type);
    }

    public static Converter<?>[] getConverters(Class<?>[] types){
        Converter<?>[] result = new Converter[types.length];
        for(int i = 0; i < types.length; i++){
            result[i] = getConverter(types[i]);
        }

        return result;
    }

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
        if (type == Character.TYPE || type == Character.class)
            return value.toChar();
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
                return null;
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

    public static Memory valueOf(String value, HintType type){
        switch (type){
            case STRING: return new StringMemory(value);
            case ANY: return value.equals("NULL") ? Memory.NULL : new StringMemory(value);
            case INT: {
                try {
                    return new DoubleMemory(Double.parseDouble(value));
                } catch (NumberFormatException e){
                    return LongMemory.valueOf(Long.parseLong(value));
                }
            }
            case BOOLEAN:
                return new StringMemory(value).toBoolean() ? Memory.TRUE : Memory.FALSE;
            case CALLABLE:
                return new StringMemory(value);
            default:
                throw new IllegalArgumentException("Unsupported type - " + type);
        }
    }

    public static interface Converter<T> {
        T run(Memory value);
    }
}
