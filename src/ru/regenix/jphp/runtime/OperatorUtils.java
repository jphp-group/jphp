package ru.regenix.jphp.runtime;

public class OperatorUtils {

    public static boolean toBoolean(double value){
        return value != 0.0;
    }

    public static boolean toBoolean(long value){
        return value != 0;
    }

    public static boolean toBoolean(String value){
        return value != null && !value.equals("0") && !value.isEmpty();
    }
}
