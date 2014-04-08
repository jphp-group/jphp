package php.runtime.common;

import php.runtime.common.collections.map.HashedMap;

import java.util.Map;

public enum HintType {
    ANY, STRING, INT, DOUBLE, NUMBER, BOOLEAN, SCALAR, ARRAY, OBJECT, CALLABLE, VARARG, TRAVERSABLE;

    public String toString(){
        switch (this){
            case ARRAY: return "array";
            case INT: return "int";
            case DOUBLE: return "double";
            case NUMBER: return "number";
            case BOOLEAN: return "boolean";
            case SCALAR: return "scalar";
            case CALLABLE: return "callable";
            case STRING: return "string";
            case OBJECT: return "object";
            case VARARG: return "...";
            case TRAVERSABLE: return "traversable";
            default:
                return "";
        }
    }

    private static final Map<String, HintType> values = new HashedMap<String, HintType>();
    static {
        for(HintType e : values())
            values.put(e.toString(), e);

        values.put("integer", INT);
        values.put("float", DOUBLE);
        values.put("bool", BOOLEAN);
    }

    public static HintType of(String code){
        return values.get(code);
    }
}
