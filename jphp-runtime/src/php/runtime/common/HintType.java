package php.runtime.common;

import php.runtime.common.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

public enum HintType {
    ANY, STRING, INT, DOUBLE, NUMBER, BOOLEAN, ARRAY, OBJECT, CALLABLE, VARARG, TRAVERSABLE, ITERABLE, VOID, SELF;

    public String toString(){
        switch (this){
            case ARRAY: return "array";
            case INT: return "int";
            case DOUBLE: return "float";
            case BOOLEAN: return "bool";
            case CALLABLE: return "callable";
            case STRING: return "string";
            case OBJECT: return "object";
            case VARARG: return "...";
            case TRAVERSABLE: return "traversable";
            case ITERABLE: return "iterable";
            case VOID: return "void";
            case SELF: return "self";
            default:
                return "";
        }
    }

    private static final Map<String, HintType> values = new HashMap<>();

    static {
        for(HintType e : values()) {
            values.put(e.toString(), e);
        }
    }

    public static HintType of(String code){
        return values.get(code);
    }
}
