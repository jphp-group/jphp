package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.Resource;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class LangFunctions extends FunctionsContainer {

    public static boolean isset(@Runtime.Reference Memory memory){
        return !memory.isNull();
    }

    public static boolean empty(@Runtime.Reference Memory memory){
        return !memory.toBoolean();
    }

    @Runtime.Immutable
    public static String gettype(Memory memory){
        switch (memory.getRealType()){
            case ARRAY: return "array";
            case BOOL: return "boolean";
            case INT: return "integer";
            case DOUBLE: return "double";
            case STRING: return "string";
            case OBJECT:
                ObjectMemory object = memory.toValue(ObjectMemory.class);
                if (object.value instanceof Resource)
                    return "resource";

                return "object";
            case NULL: return "NULL";
            default:
                return "unknown type";
        }
    }

    public static boolean is_array(@Runtime.Reference Memory memory){
        return memory.isArray();
    }

    @Runtime.Immutable
    public static boolean is_bool(Memory memory){
        return memory.toValue().type == Memory.Type.BOOL;
    }

    @Runtime.Immutable
    public static boolean is_double(Memory memory){
        return memory.toValue().type == Memory.Type.DOUBLE;
    }

    @Runtime.Immutable
    public static boolean is_float(Memory memory){
        return memory.toValue().type == Memory.Type.DOUBLE;
    }

    @Runtime.Immutable
    public static boolean is_int(Memory memory){
        return memory.toValue().type == Memory.Type.INT;
    }

    @Runtime.Immutable
    public static boolean is_integer(Memory memory){
        return memory.toValue().type == Memory.Type.INT;
    }

    @Runtime.Immutable
    public static boolean is_long(Memory memory){
        return memory.toValue().type == Memory.Type.INT;
    }

    @Runtime.Immutable
    public static boolean is_null(Memory memory){
        return memory.isNull();
    }

    public static boolean is_object(@Runtime.Reference Memory memory){
        return memory.isObject() && !memory.isResource();
    }

    @Runtime.Immutable
    public static boolean is_real(Memory memory){
        return is_float(memory);
    }

    @Runtime.Immutable
    public static boolean is_string(Memory memory){
        return memory.isString();
    }

    @Runtime.Immutable
    public static boolean is_scalar(Memory memory){
        switch (memory.getRealType()){
            case BOOL:
            case NULL:
            case INT:
            case DOUBLE:
            case STRING:
                return true;
        }
        return false;
    }

    public static boolean is_resource(@Runtime.Reference Memory memory){
        return memory.isResource();
    }

    public static Memory get_resource_type(@Runtime.Reference Memory memory){
        if (memory.isObject()){
            if (((ObjectMemory)memory).value instanceof Resource)
                return new StringMemory(
                        ((Resource) ((ObjectMemory)memory).value).getResourceType()
                );
        }
        return Memory.NULL;
    }

    public static boolean is_callable(Environment env, TraceInfo trace, @Runtime.Reference Memory memory){
        return Invoker.valueOf(env, null, memory) != null;
    }

    @Runtime.Immutable
    public static boolean boolval(Memory memory){
        return memory.toBoolean();
    }

    @Runtime.Immutable
    public static String strval(Memory memory){
        return memory.toString();
    }

    @Runtime.Immutable
    public static long intval(Memory memory){
        return memory.toLong();
    }

    @Runtime.Immutable
    public static double floatval(Memory memory){
        return memory.toDouble();
    }

    @Runtime.Immutable
    public static double doubleval(Memory memory){
        return memory.toDouble();
    }

    public static boolean settype(@Runtime.Reference Memory memory, String type){
        if (memory.isReference()){
            if ("string".equals(type)){
                memory.assign(memory.toString());
            } else if ("bool".equals(type) || "boolean".equals(type)){
                memory.assign(memory.toBoolean());
            } else if ("int".equals(type) || "integer".equals(type)){
                memory.assign(memory.toLong());
            } else if ("float".equals(type) || "double".equals(type)){
                memory.assign(memory.toDouble());
            } else if ("null".equals(type)){
                memory.assign(Memory.NULL);
            } else
                return false;

            return true;
        }
        return false;
    }

    public void debug_zval_dump(Environment env, TraceInfo trace){
        env.warning(trace, "debug_zval_dump(): unsupported");
    }
}
