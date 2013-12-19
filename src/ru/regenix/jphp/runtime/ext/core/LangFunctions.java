package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.Resource;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.runtime.util.StackTracer;

import java.lang.reflect.InvocationTargetException;

public class LangFunctions extends FunctionsContainer {

    protected final static LangConstants constants = new LangConstants();

    public static boolean defined(Environment env, String name){
        Memory value = env.findConstant(name);
        return value != null;
    }

    public static boolean define(Environment env, TraceInfo trace, String name, Memory value, boolean caseInSentisise){
        return env.defineConstant(name, value, !caseInSentisise);
    }

    public static boolean define(Environment env, TraceInfo trace, String name, Memory value){
        return define(env, trace, name, value, false);
    }

    public static Memory constant(Environment env, TraceInfo trace, String name){
        int pos;
        if ((pos = name.indexOf("::")) > -1){
            String className = name.substring(0, pos);
            String constName = name.substring(pos + 2);
            ClassEntity entity = env.fetchClass(className);
            if (entity == null)
                return Memory.NULL;

            ConstantEntity constant = entity.findConstant(constName);
            return constant == null ? Memory.NULL : constant.getValue();
        } else {
            Memory value = env.findConstant(name);
            if (value == null)
                return Memory.NULL;
            else
                return value;
        }
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
                if (memory.isResource())
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
        return memory.isObject();
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

    public static Memory func_get_args(Environment env, TraceInfo trace){
        if (env.getCallStackTop() == 0){
            return Memory.FALSE;
        }

        return new ArrayMemory(true, env.peekCall(0).args).toConstant();
    }

    public static Memory func_num_args(Environment env, TraceInfo trace){
        if (env.getCallStackTop() == 0){
            return Memory.FALSE;
        }

        return LongMemory.valueOf(env.peekCall(0).args.length);
    }

    public static Memory func_get_arg(Environment env, TraceInfo trace, int argNum){
        if (env.getCallStackTop() == 0){
            return Memory.FALSE;
        }
        if (argNum < 0)
            return Memory.FALSE;

        Memory[] args = env.peekCall(0).args;
        if (argNum < args.length)
            return args[argNum];
        else
            return Memory.FALSE;
    }

    private static Memory _call_user_func(Environment env, TraceInfo trace, Memory function, Memory... args)
            throws InvocationTargetException, IllegalAccessException {
        Invoker invoker = Invoker.valueOf(env, null, function);
        if (invoker == null){
            env.warning(trace, "expects parameter 1 to be a valid callback");
            return Memory.FALSE;
        }

        invoker.pushCall(null, args);
        Memory result = Memory.FALSE;

        try {
            result = invoker.call(args);
        } finally {
            invoker.popCall();
        }
        return result;
    }

    public static Memory call_user_func(Environment env, TraceInfo trace, Memory function, Memory... args)
            throws InvocationTargetException, IllegalAccessException {
        Memory[] passed;
        if (args == null){
            passed = new Memory[]{function};
        } else {
            passed = new Memory[args.length + 1];
            System.arraycopy(args, 0, passed, 1, args.length);
            passed[0] = function;
        }

        env.pushCall(trace, null, passed, "call_user_func", null);
        try {
            return _call_user_func(env, trace, function, args);
        } finally {
            env.popCall();
        }
    }

    public static Memory call_user_func_array(Environment env, TraceInfo trace, Memory function, Memory args)
            throws InvocationTargetException, IllegalAccessException {
        if (expecting(env, trace, 2, args, Memory.Type.ARRAY)){
            Memory[] passed = new Memory[]{function, args};
            env.pushCall(trace, null, passed, "call_user_func_array", null);
            try {
                return call_user_func(env, trace, function, ((ArrayMemory) args).values(true));
            } finally {
                env.popCall();
            }
        }
        return Memory.FALSE;
    }

    public static Memory debug_backtrace(Environment env, TraceInfo trace, int options, int limit){
        boolean provideObject = (options & constants.DEBUG_BACKTRACE_PROVIDE_OBJECT)
                == constants.DEBUG_BACKTRACE_PROVIDE_OBJECT;
        boolean ignoreArgs = (options & constants.DEBUG_BACKTRACE_IGNORE_ARGS)
                == constants.DEBUG_BACKTRACE_IGNORE_ARGS;

        ArrayMemory result = new ArrayMemory();
        for(int i = 0; i < env.getCallStackTop(); i++){
            if (limit != 0 && i >= limit)
                break;

            CallStackItem item = env.peekCall(i);
            ArrayMemory el = new ArrayMemory();

            if (item.trace != null) {
                if (item.trace.getFile() != null)
                    el.refOfIndex("file").assign(item.trace.getFile().getPath());

                el.refOfIndex("line").assign(item.trace.getStartLine() + 1);
            }

            el.refOfIndex("function").assign(item.function);

            if (item.clazz != null) {
                el.refOfIndex("class").assign(item.clazz);
                el.refOfIndex("type").assign("::");
            }

            if (item.object != null){
                if (provideObject){
                    el.refOfIndex("object").assign(new ObjectMemory(item.object));
                }
                el.refOfIndex("type").assign("->");
            }

            if (!ignoreArgs){
                el.refOfIndex("args").assign(new ArrayMemory(true, item.args));
            }

            if (item.trace != null)
                el.refOfIndex("position").assign(item.trace.getStartPosition() + 1);

            result.add(el);
        }

        return result.toConstant();
    }

    public static Memory debug_backtrace(Environment env, TraceInfo trace, int options){
        return debug_backtrace(env, trace, options, 0);
    }

    public static Memory debug_backtrace(Environment env, TraceInfo trace){
        return debug_backtrace(env, trace, constants.DEBUG_BACKTRACE_PROVIDE_OBJECT, 0);
    }


    public static void debug_print_backtrace(Environment env, TraceInfo trace, int options, int limit){
        boolean provideObject = (options & constants.DEBUG_BACKTRACE_PROVIDE_OBJECT)
                == constants.DEBUG_BACKTRACE_PROVIDE_OBJECT;
        boolean ignoreArgs = (options & constants.DEBUG_BACKTRACE_IGNORE_ARGS)
                == constants.DEBUG_BACKTRACE_IGNORE_ARGS;

        StackTracer stackTracer = new StackTracer(env, limit);
        env.echo(stackTracer.toString(!ignoreArgs));
    }

    public static void debug_print_backtrace(Environment env, TraceInfo trace, int options){
        debug_print_backtrace(env, trace, options, 0);
    }

    public static void debug_print_backtrace(Environment env, TraceInfo trace){
        debug_print_backtrace(env, trace, constants.DEBUG_BACKTRACE_PROVIDE_OBJECT, 0);
    }

    public static boolean function_exists(Environment env, String name){
        name = name.toLowerCase();
        FunctionEntity function = env.scope.findUserFunction(name);
        return function != null && (function.isInternal() || env.isLoadedFunction(name));
    }
}
