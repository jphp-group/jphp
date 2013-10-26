package ru.regenix.jphp.compiler.jvm;


import ru.regenix.jphp.compiler.jvm.runtime._Memory;

final public class Constants {

    private Constants(){}

    public final static char NAME_DELIMITER = '/';
    public final static String NAME_DELIMITER_S = String.valueOf(NAME_DELIMITER);

    public final static String INIT_METHOD = "<init>";
    public final static String STATIC_INIT_METHOD = "<cinit>";
    public final static String OBJECT_CLASS = toClassName(Object.class);
    public final static String MEMORY_CLASS = toClassName(_Memory.class);

    public static String toClassName(String name){
        if (name.startsWith("\\"))
            name = name.substring(1);

        return name.replaceAll("\\\\", NAME_DELIMITER_S).replaceAll("\\.", NAME_DELIMITER_S);
    }

    public static String toClassName(Class clazz){
        return toClassName(clazz.getName());
    }
}
