package ru.regenix.jphp.common;

public enum HintType {
    ANY, BOOLEAN, NUMERIC, STRING, ARRAY, OBJECT, CALLABLE, VARARG;

    public String toString(){
        switch (this){
            case BOOLEAN: return "boolean";
            case NUMERIC: return "numeric";
            case STRING: return "string";
            case ARRAY: return "array";
            case OBJECT: return "object";
            case CALLABLE: return "callable";
            default:
                return "";
        }
    }
}
