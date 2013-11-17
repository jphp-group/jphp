package ru.regenix.jphp.compiler.jvm;


import org.objectweb.asm.Type;
import ru.regenix.jphp.runtime.memory.Memory;

final public class Constants {

    private Constants(){}

    public final static char NAME_DELIMITER = '_';
    public final static String NAME_DELIMITER_S = String.valueOf(NAME_DELIMITER);

    public final static String INIT_METHOD = "<init>";
    public final static String STATIC_INIT_METHOD = "<clinit>";
    public final static String OBJECT_CLASS = Type.getInternalName(Object.class);
    public final static String MEMORY_CLASS = Type.getInternalName(Memory.class);

}
