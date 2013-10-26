package ru.regenix.jphp.compiler.jvm.runtime;

public class _Memory {

    public enum NumericType { INVALID, INT, DOUBLE }
    public enum Type { NULL, BOOL, INT, DOUBLE, STRING, ARRAY, OBJECT }

    public static final _Memory NULL = new _Memory(Type.NULL);
    public static final _Memory TRUE = new _Memory(true);
    public static final _Memory FALSE = new _Memory(false);
    public static final _Memory CONST_INT_0 = new _Memory(0);
    public static final _Memory CONST_INT_1 = new _Memory(1);
    public static final _Memory CONST_DOUBLE_0 = new _Memory(0.0);
    public static final _Memory CONST_DOUBLE_1 = new _Memory(1.0);

    protected Type type;

    protected boolean boolValue;
    protected long longValue;
    protected double doubleValue;

    protected Object value;

    public _Memory(Type type) {
        this.type = type;
    }

    public _Memory(boolean value){
        this(Type.BOOL);
        this.boolValue = value;
    }

    public _Memory(int value){
        this(Type.INT);
        this.longValue = value;
    }

    public _Memory(long value){
        this(Type.INT);
        this.longValue = value;
    }

    public _Memory(double v) {
        this(Type.DOUBLE);
        this.doubleValue = v;
    }

    public _Memory(byte value){
        this((long)value);
    }

    public _Memory(short value){
        this((long)value);
    }

    public _Memory(String value){
        this(Type.STRING);
        this.value = value;
    }

    public _Memory toNumeric(){
        switch (type){
            case BOOL: return boolValue ? CONST_INT_1 : CONST_INT_0;
            case INT: return this;
            case DOUBLE: return this;
            case ARRAY: return CONST_INT_0; // TODO getSize
            case STRING: {
                String str = (String)value;
                if (str.matches("^[0-9]+.[0-9]+"))
                    return new _Memory(TypeUtils.S2D(str));
                if (str.matches("^[0-9]+")){
                    return new _Memory(TypeUtils.S2L(str));
                }
                return CONST_INT_0;
            }
            default:
                return CONST_INT_0;
        }
    }


    public _Memory plus(_Memory memory){
        switch (memory.type){
            case INT: {
                switch (memory.type){
                    case INT: return new _Memory(longValue + memory.longValue);
                    case DOUBLE: return new _Memory(longValue + memory.doubleValue);
                    case BOOL: return new _Memory(longValue + (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(longValue);
                    default:
                        return plus(memory.toNumeric());
                }
            }
            case DOUBLE: {
                switch (memory.type){
                    case INT: return new _Memory(doubleValue + memory.longValue);
                    case DOUBLE: return new _Memory(doubleValue + memory.doubleValue);
                    case BOOL: return new _Memory(doubleValue + (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(doubleValue);
                    default:
                        return plus(memory.toNumeric());
                }
            }
            case BOOL: return plus(boolValue ? CONST_INT_1 : CONST_INT_0);
            default:
                return toNumeric().plus(memory);
        }
    }

    public _Memory minus(_Memory memory){
        switch (memory.type){
            case INT: {
                switch (memory.type){
                    case INT: return new _Memory(longValue - memory.longValue);
                    case DOUBLE: return new _Memory(longValue - memory.doubleValue);
                    case BOOL: return new _Memory(longValue - (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(longValue);
                    default:
                        return minus(memory.toNumeric());
                }
            }
            case DOUBLE: {
                switch (memory.type){
                    case INT: return new _Memory(doubleValue - memory.longValue);
                    case DOUBLE: return new _Memory(doubleValue - memory.doubleValue);
                    case BOOL: return new _Memory(doubleValue - (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(doubleValue);
                    default:
                        return minus(memory.toNumeric());
                }
            }
            case BOOL: return minus(boolValue ? CONST_INT_1 : CONST_INT_0);
            default:
                return toNumeric().minus(memory);
        }
    }

    public _Memory mul(_Memory memory){
        switch (memory.type){
            case INT: {
                switch (memory.type){
                    case INT: return new _Memory(longValue * memory.longValue);
                    case DOUBLE: return new _Memory(longValue * memory.doubleValue);
                    case BOOL: return new _Memory(longValue * (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(longValue);
                    default:
                        return mul(memory.toNumeric());
                }
            }
            case DOUBLE: {
                switch (memory.type){
                    case INT: return new _Memory(doubleValue * memory.longValue);
                    case DOUBLE: return new _Memory(doubleValue * memory.doubleValue);
                    case BOOL: return new _Memory(doubleValue * (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(doubleValue);
                    default:
                        return mul(memory.toNumeric());
                }
            }
            case BOOL: return minus(boolValue ? CONST_INT_1 : CONST_INT_0);
            default:
                return toNumeric().mul(memory);
        }
    }

    public _Memory div(_Memory memory){
        switch (memory.type){
            case INT: {
                switch (memory.type){
                    case INT: return new _Memory(longValue * memory.longValue);
                    case DOUBLE: return new _Memory(longValue * memory.doubleValue);
                    case BOOL: return new _Memory(longValue * (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(longValue);
                    default:
                        return mul(memory.toNumeric());
                }
            }
            case DOUBLE: {
                switch (memory.type){
                    case INT: return new _Memory(doubleValue * memory.longValue);
                    case DOUBLE: return new _Memory(doubleValue * memory.doubleValue);
                    case BOOL: return new _Memory(doubleValue * (memory.boolValue ? 1 : 0));
                    case ARRAY: return new _Memory(doubleValue);
                    default:
                        return mul(memory.toNumeric());
                }
            }
            case BOOL: return minus(boolValue ? CONST_INT_1 : CONST_INT_0);
            default:
                return toNumeric().mul(memory);
        }
    }
}
