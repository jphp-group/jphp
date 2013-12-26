package ru.regenix.jphp.common;

public enum HintType {
    ANY, BOOLEAN, INT, DOUBLE, STRING, ARRAY, OBJECT, CALLABLE, VARARG;

    public String toString(){
        switch (this){
            case BOOLEAN: return "boolean";
            case INT: return "int";
            case DOUBLE: return "float";
            case STRING: return "string";
            case ARRAY: return "array";
            case OBJECT: return "object";
            case CALLABLE: return "callable";
            default:
                return "";
        }
    }
}
