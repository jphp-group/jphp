package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.java.JavaObject;
import php.runtime.memory.*;

import java.util.*;

public class MemoryUtils {

    protected final static Map<Class<?>, Unconverter> UNCONVERTERS = new HashMap<Class<?>, Unconverter>(){{
        put(Double.class, new Unconverter<Double>() {
            @Override
            public Memory run(Double value) {
                return new DoubleMemory(value);
            }
        });
        put(Double.TYPE, get(Double.class));

        put(Float.class, new Unconverter<Float>() {
            @Override
            public Memory run(Float value) {
                return new DoubleMemory(value);
            }
        });
        put(Float.TYPE, get(Float.class));

        put(Long.class, new Unconverter<Long>() {
            @Override
            public Memory run(Long value) {
                return LongMemory.valueOf(value);
            }
        });
        put(Long.TYPE, get(Long.class));

        put(Integer.class, new Unconverter<Integer>() {
            @Override
            public Memory run(Integer value) {
                return LongMemory.valueOf(value);
            }
        });
        put(Integer.TYPE, get(Integer.class));

        put(Short.class, new Unconverter<Short>() {
            @Override
            public Memory run(Short value) {
                return LongMemory.valueOf(value);
            }
        });
        put(Short.TYPE, get(Short.class));

        put(Short.class, new Unconverter<Byte>() {
            @Override
            public Memory run(Byte value) {
                return LongMemory.valueOf(value);
            }
        });
        put(Byte.TYPE, get(Byte.class));

        put(Character.class, new Unconverter<Character>() {
            @Override
            public Memory run(Character value) {
                return new StringMemory(value);
            }
        });
        put(Character.TYPE, get(Character.class));

        put(Boolean.class, new Unconverter<Boolean>() {
            @Override
            public Memory run(Boolean value) {
                return value ? Memory.TRUE : Memory.FALSE;
            }
        });
        put(Boolean.TYPE, get(Boolean.class));

        put(String.class, new Unconverter<String>() {
            @Override
            public Memory run(String value) {
                return new StringMemory(value);
            }
        });

        put(Memory.class, new Unconverter<Memory>() {
            @Override
            public Memory run(Memory value) {
                return value;
            }
        });

        put(Memory[].class, new Unconverter<Memory[]>() {
            @Override
            public Memory run(Memory[] value) {
                return new ArrayMemory(false, value);
            }
        });
    }};

    protected final static Map<Class<?>, Converter> CONVERTERS = new HashMap<Class<?>, Converter>(){{
        // double
        put(Double.class, new Converter<Double>() {
            @Override
            public Double run(Environment env, TraceInfo trace, Memory value) {
                return value.toDouble();
            }
        });
        put(Double.TYPE, get(Double.class));

        // float
        put(Float.class, new Converter<Float>() {
            @Override
            public Float run(Environment env, TraceInfo trace, Memory value) {
                return (float)value.toDouble();
            }
        });
        put(Float.TYPE, get(Float.class));

        // long
        put(Long.class, new Converter<Long>() {
            @Override
            public Long run(Environment env, TraceInfo trace, Memory value) {
                return value.toLong();
            }
        });
        put(Long.TYPE, get(Long.class));

        // int
        put(Integer.class, new Converter<Integer>() {
            @Override
            public Integer run(Environment env, TraceInfo trace, Memory value) {
                return (int)value.toLong();
            }
        });
        put(Integer.TYPE, get(Integer.class));

        // short
        put(Short.class, new Converter<Short>() {
            @Override
            public Short run(Environment env, TraceInfo trace, Memory value) {
                return (short)value.toLong();
            }
        });
        put(Short.TYPE, get(Short.class));

        // byte
        put(Byte.class, new Converter<Byte>() {
            @Override
            public Byte run(Environment env, TraceInfo trace, Memory value) {
                return (byte)value.toLong();
            }
        });
        put(Byte.TYPE, get(Byte.class));

        // char
        put(Character.class, new Converter<Character>() {
            @Override
            public Character run(Environment env, TraceInfo trace, Memory value) {
                return value.toChar();
            }
        });
        put(Character.TYPE, get(Character.class));

        // bool
        put(Boolean.class, new Converter<Boolean>() {
            @Override
            public Boolean run(Environment env, TraceInfo trace, Memory value) {
                return value.toBoolean();
            }
        });
        put(Boolean.TYPE, get(Boolean.class));

        // string
        put(String.class, new Converter<String>() {
            @Override
            public String run(Environment env, TraceInfo trace, Memory value) {
                return value.toString();
            }
        });

        put(Memory.class, new Converter<Memory>() {
            @Override
            public Memory run(Environment env, TraceInfo trace, Memory value) {
                return value;
            }
        });

        put(Memory[].class, new Converter<Memory[]>() {
            @Override
            public Memory[] run(Environment env, TraceInfo trace, Memory value) {
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

    public static Unconverter getUnconverter(Class<?> type){
        return UNCONVERTERS.get(type);
    }

    public static Object fromMemory(Memory value, Class<?> type){
        if (value.instanceOf("php\\lang\\JavaObject"))
            return ((JavaObject)value.toValue(ObjectMemory.class).value).getObject();

        Converter converter = getConverter(type);
        if (converter != null)
            return converter.run(value);
        else
            return value;
    }

    @Deprecated
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

    @Deprecated
    public static Memory valueOf(Object value){
        return valueOf(null, value);
    }

    @Deprecated
    public static Memory valueOf(Environment env, Object value){
        if (value == null)
            return Memory.NULL;

        Unconverter unconverter = getUnconverter(value.getClass());
        if (unconverter != null) {
            return unconverter.run(value);
        } else {
            if (value instanceof Memory)
                return (Memory)value;

            if (env == null)
                if (value instanceof Collection){
                    ArrayMemory result = new ArrayMemory();
                    for (Object el : (Collection)value)
                        result.add(valueOf(el));

                    return result;
                } else if (value instanceof Map){
                    ArrayMemory result = new ArrayMemory();
                    for (Map.Entry el : ((Map<?, ?>)value).entrySet())
                        result.refOfIndex(valueOf(el.getKey())).assign(valueOf(el.getValue()));

                    return result;
                } else if (value.getClass().isArray()){
                    ArrayMemory result = new ArrayMemory();
                    for (Object el : (Object[])value)
                        result.add(valueOf(el));

                    return result;
                }

            if (env != null)
                return new ObjectMemory(JavaObject.of(env, value));
            else
                return null;
            //}
        }
    }

    @Deprecated
    public static Memory valueOf(String value, HintType type){
        switch (type){
            case STRING: return new StringMemory(value);
            case ANY:
                if (value.equals("false"))
                    return Memory.FALSE;
                if (value.equals("true"))
                    return Memory.TRUE;
                else if (value.equalsIgnoreCase("null"))
                    return Memory.NULL;

                Memory m = StringMemory.toNumeric(value, false, null);
                return m != null ? m : new StringMemory(value);
            case DOUBLE:
                return new DoubleMemory(Double.parseDouble(value));
            case INT: {
                return LongMemory.valueOf(Long.parseLong(value));
            }
            case ARRAY:
                return new ArrayMemory();
            case BOOLEAN:
                return new StringMemory(value).toBoolean() ? Memory.TRUE : Memory.FALSE;
            case CALLABLE:
                return new StringMemory(value);
            default:
                throw new IllegalArgumentException("Unsupported type - " + type);
        }
    }

    public static Memory valueForList(Memory memory, long index) {
        if (memory.isArray()) {
            return memory.valueOfIndex(index);
        } else {
            return Memory.NULL;
        }
    }

    public static Memory valueForList(Memory memory, String index) {
        if (memory.isArray()) {
            return memory.valueOfIndex(index);
        } else {
            return Memory.NULL;
        }
    }

    public static Memory valueForList(Memory memory, Memory index) {
        if (memory.isArray()) {
            return memory.valueOfIndex(index);
        } else {
            return Memory.NULL;
        }
    }

    abstract public static class Converter<T> {
        abstract public T run(Environment env, TraceInfo trace, Memory value);

        final public T run(Memory value) {
            return run(null, null, value);
        }
    }

    public static interface Unconverter<T> {
        Memory run(T value);
    }
}
