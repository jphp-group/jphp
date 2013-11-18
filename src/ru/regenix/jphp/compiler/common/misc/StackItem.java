package ru.regenix.jphp.compiler.common.misc;

import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.Memory;

/**
* User: Dim-S (dz@dim-s.net)
* Date: 10.11.13
*/
public class StackItem {

    public enum Type {
        NULL, BOOL, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING, ARRAY, REFERENCE, CLASS;

        public Class toClass(){
            switch (this){
                case DOUBLE: return Double.TYPE;
                case FLOAT: return Float.TYPE;
                case NULL: return Object.class;
                case BOOL: return Boolean.TYPE;
                case SHORT: return Short.TYPE;
                case INT: return Integer.TYPE;
                case LONG: return Long.TYPE;
                case STRING: return String.class;
                case ARRAY: return ArrayMemory.class;
                case REFERENCE: return Memory.class;
            }

            return null;
        }

        public int size(){
            switch (this){
                case DOUBLE:
                case LONG: return 2;
                default: return 1;
            }
        }

        public static Type valueOf(Memory.Type type){
            return valueOf(type.toClass());
        }

        public static Type valueOf(Class clazz){
            if (clazz == Byte.TYPE)
                return BYTE;
            if (clazz == Short.TYPE)
                return SHORT;
            if (clazz == Integer.TYPE)
                return INT;
            if (clazz == Long.TYPE)
                return LONG;
            if (clazz == Double.TYPE)
                return DOUBLE;
            if (clazz == Float.TYPE)
                return FLOAT;
            if (clazz == String.class)
                return STRING;
            if (clazz == Boolean.TYPE)
                return BOOL;
            if (clazz == ArrayMemory.class)
                return ARRAY;

            return REFERENCE;
        }

        public boolean isConstant(){
            return this != REFERENCE && this != ARRAY /*&& this != OBJECT*/;
        }
    }

    public final Type type;
    public final int size;
    public boolean immutable;

    public final Memory memory;
    public final ValueExprToken token;

    public StackItem(ValueExprToken token, Type type, boolean immutable) {
        this.type = type;
        this.size = type.size();
        this.immutable = immutable;
        this.token = token;
        this.memory = null;
    }

    public StackItem(ValueExprToken token, Type type) {
        this(token, type, type.isConstant());
    }

    public StackItem(Memory value){
        this.type = Type.valueOf(value.type);
        this.size = type.size();
        this.immutable = true;
        this.memory = value;
        this.token = null;
    }


    public StackItem(ValueExprToken token, Memory value) {
        this.type = Type.REFERENCE;
        this.size = type.size();
        this.immutable = true;
        this.memory = value;
        this.token = token;
    }

    public Memory getMemory() {
        return memory;
    }

    public ValueExprToken getToken() {
        return token;
    }

    public boolean isKnown(){
        return memory != null || token != null;
    }

    public boolean isConstant(){
        return memory != null;
    }
}
