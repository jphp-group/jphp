package org.develnext.jphp.core.common;


import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kyrylo Holodnov
 */
public class ObjectSizeCalculator {
    private static final int REFERENCE_SIZE;
    private static final int HEADER_SIZE;
    private static final int LONG_SIZE = 8;
    private static final int INT_SIZE = 4;
    private static final int BYTE_SIZE = 1;
    private static final int BOOLEAN_SIZE = 1;
    private static final int CHAR_SIZE = 2;
    private static final int SHORT_SIZE = 2;
    private static final int FLOAT_SIZE = 4;
    private static final int DOUBLE_SIZE = 8;
    private static final int ALIGNMENT = 8;

    static {
        try {
            if (System.getProperties().get("java.vm.name").toString().contains("64")) {
                //java.vm.name is something like "Java HotSpot(TM) 64-Bit Server VM"
                REFERENCE_SIZE = 8;
                HEADER_SIZE = 16;
            } else {
                REFERENCE_SIZE = 4;
                HEADER_SIZE = 8;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AssertionError(ex);
        }
    }

    public static long sizeOf(Object o) throws IllegalAccessException {
        return sizeOf(o, new HashSet());
    }

    private static long sizeOf(Object o, Set visited) throws IllegalAccessException {
        if (o == null) {
            return 0;
        }
        ObjectWrapper objectWrapper = new ObjectWrapper(o);
        if (visited.contains(objectWrapper)) {
            //We have reference graph with cycles.
            return 0;
        }
        visited.add(objectWrapper);
        long size = HEADER_SIZE;
        Class clazz = o.getClass();
        if (clazz.isArray()) {
            if (clazz == long[].class) {
                long[] objs = (long[]) o;
                size += objs.length * LONG_SIZE;
            } else if (clazz == int[].class) {
                int[] objs = (int[]) o;
                size += objs.length * INT_SIZE;
            } else if (clazz == byte[].class) {
                byte[] objs = (byte[]) o;
                size += objs.length * BYTE_SIZE;
            } else if (clazz == boolean[].class) {
                boolean[] objs = (boolean[]) o;
                size += objs.length * BOOLEAN_SIZE;
            } else if (clazz == char[].class) {
                char[] objs = (char[]) o;
                size += objs.length * CHAR_SIZE;
            } else if (clazz == short[].class) {
                short[] objs = (short[]) o;
                size += objs.length * SHORT_SIZE;
            } else if (clazz == float[].class) {
                float[] objs = (float[]) o;
                size += objs.length * FLOAT_SIZE;
            } else if (clazz == double[].class) {
                double[] objs = (double[]) o;
                size += objs.length * DOUBLE_SIZE;
            } else {
                Object[] objs = (Object[]) o;
                for (int i = 0; i < objs.length; i++) {
                    size += sizeOf(objs[i], visited) + REFERENCE_SIZE;
                }
            }
            size += INT_SIZE;
        } else {
            Field[] fields = o.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (java.lang.reflect.Modifier.isStatic(fields[i].getModifiers())) {
                    continue;
                }
                fields[i].setAccessible(true);
                String fieldType = fields[i].getGenericType().toString();
                if (fieldType.equals("long")){
                    size += LONG_SIZE;
                }else if (fieldType.equals("int")){
                    size += INT_SIZE;
                }else if (fieldType.equals("byte")){
                    size += BYTE_SIZE;
                }else if (fieldType.equals("boolean")){
                    size += BOOLEAN_SIZE;
                }else if (fieldType.equals("char")){
                    size += CHAR_SIZE;
                }else if (fieldType.equals("short")){
                    size += SHORT_SIZE;
                }else if (fieldType.equals("float")){
                    size += FLOAT_SIZE;
                }else if (fieldType.equals("double")){
                    size += DOUBLE_SIZE;
                }else{
                    size += sizeOf(fields[i].get(o), visited) + REFERENCE_SIZE;
                }
            }
        }
        if ((size % ALIGNMENT) != 0) {
            size = ALIGNMENT * (size / ALIGNMENT) + ALIGNMENT;
        }
        return size;
    }

    private static final class ObjectWrapper {

        private Object object;

        public ObjectWrapper(Object object) {
            this.object = object;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != ObjectWrapper.class)) {
                return false;
            }
            return object == ((ObjectWrapper) obj).object;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + System.identityHashCode(object);
            return hash;
        }
    }
}