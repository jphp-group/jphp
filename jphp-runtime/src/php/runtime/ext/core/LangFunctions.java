package php.runtime.ext.core;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Runtime;
import php.runtime.annotation.Runtime.Immutable;
import php.runtime.annotation.Runtime.Reference;
import php.runtime.common.AbstractCompiler;
import php.runtime.common.GrammarUtils;
import php.runtime.common.LangMode;
import php.runtime.env.*;
import php.runtime.env.handler.ErrorHandler;
import php.runtime.env.handler.ExceptionHandler;
import php.runtime.env.handler.ShutdownHandler;
import php.runtime.env.message.SystemMessage;
import php.runtime.exceptions.ParseException;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.classes.util.WrapFlow;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.InvokeHelper;
import php.runtime.invoke.Invoker;
import php.runtime.lang.Closure;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.Resource;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.helper.ObservableMemory;
import php.runtime.reflection.*;
import php.runtime.util.StackTracer;

import java.util.Observable;
import java.util.Observer;

public class LangFunctions extends FunctionsContainer {
    private static String evalErrorMessage(ErrorException e) {
        return e.getMessage() + ", eval()'s code on line " + (e.getTraceInfo().getStartLine() + 1)
                + ", position " + (e.getTraceInfo().getStartPosition() + 1);
    }

    public static Memory eval(Environment env, TraceInfo trace, @Runtime.GetLocals ArrayMemory locals, String code)
            throws Throwable {
        try {
            return env.eval(code, locals);
        } catch (ErrorException e) {
            if (e.getType() == ErrorType.E_PARSE) {
                if (env.isHandleErrors(ErrorType.E_PARSE))
                    throw new ParseException(evalErrorMessage(e), trace);
            } else
                env.error(trace, e.getType(), evalErrorMessage(e));
        }

        return Memory.FALSE;
    }

    public static Memory time() {
        return LongMemory.valueOf(System.currentTimeMillis() / 1000);
    }

    public static void sleep(int sec) throws InterruptedException {
        Thread.sleep(sec * 1000);
    }

    public static void usleep(long microsec) throws InterruptedException {
        Thread.sleep(microsec / 1000);
    }

    public static boolean time_nanosleep(long second, int nanosecond) throws InterruptedException {
        Thread.sleep(second * 1000, nanosecond);
        return true;
    }

    public static boolean time_sleep_until(double time) throws InterruptedException {
        long now = System.currentTimeMillis();
        long to = (long) (time * 1000);

        long diff = to - now;
        if (diff >= 0) {
            Thread.sleep(diff);
            return true;
        } else {
            return false;
        }
    }

    public static Memory compact(@Runtime.GetLocals ArrayMemory locals, Memory varName, Memory... varNames) {
        ArrayMemory result = new ArrayMemory();
        Memory value = locals.valueOfIndex(varName).toValue();
        if (value != Memory.UNDEFINED)
            result.refOfIndex(varName).assign(value.toImmutable());

        if (varNames != null) {
            for (Memory el : varNames) {
                value = locals.valueOfIndex(el).toValue();
                if (value != Memory.UNDEFINED)
                    result.refOfIndex(el).assign(value.toImmutable());
            }
        }

        return result.toImmutable();
    }

    public static void register_shutdown_function(Environment env, TraceInfo trace, @Reference Memory handler,
                                                  Memory... args) {
        Invoker invoker = expectingCallback(env, trace, 1, handler);
        if (invoker != null)
            env.registerShutdownFunction(new ShutdownHandler(invoker, args));
    }

    public static int error_reporting(Environment env, int level) {
        int old = env.getErrorFlags();
        env.setErrorFlags(level);
        return old;
    }

    public static int error_reporting(Environment env) {
        return env.getErrorFlags();
    }

    public static Memory error_get_last(Environment env) {
        SystemMessage err = env.getLastMessage();
        if (err == null)
            return Memory.NULL;

        ArrayMemory result = new ArrayMemory();
        result.refOfIndex("type").assign(err.getType().value);
        result.refOfIndex("message").assign(err.getMessage());
        if (err.getTrace() != null && err.getTrace().trace != null) {
            result.refOfIndex("file").assign(err.getTrace().trace.getFileName());
            result.refOfIndex("line").assign(err.getTrace().trace.getStartLine() + 1);
            result.refOfIndex("position").assign(err.getTrace().trace.getStartPosition() + 1);
        }
        return result.toConstant();
    }

    public static boolean trigger_error(Environment env, TraceInfo trace, String message, int type) {
        ErrorType _type = ErrorType.valueOf(type);
        if (_type == null)
            return false;

        env.error(trace, _type, message);
        return true;
    }

    public static boolean user_error(Environment env, TraceInfo trace, String message, int type) {
        return trigger_error(env, trace, message, type);
    }

    public static boolean trigger_error(Environment env, TraceInfo trace, String message) {
        return trigger_error(env, trace, message, ErrorType.E_USER_NOTICE.value);
    }

    public static boolean user_error(Environment env, TraceInfo trace, String message) {
        return trigger_error(env, trace, message);
    }

    public static Memory set_error_handler(Environment env, TraceInfo trace, @Reference Memory handler,
                                           int flags) {
        Invoker invoker = expectingCallback(env, trace, 1, handler);
        if (invoker != null) {
            ErrorHandler last = env.getErrorHandler();
            env.setErrorHandler(new ErrorHandler(invoker, handler.toImmutable(), flags));
            return last == null ? Memory.NULL : last.invokerMemory;
        } else
            return Memory.FALSE;
    }

    public static Memory set_error_handler(Environment env, TraceInfo trace, @Reference Memory handler) {
        return set_error_handler(env, trace, handler, ErrorType.E_ALL.value | ErrorType.E_STRICT.value);
    }

    public static boolean restore_error_handler(Environment env) {
        env.setErrorHandler(env.getPreviousErrorHandler());
        return true;
    }

    public static Memory set_exception_handler(Environment env, TraceInfo trace, @Reference Memory handler) {
        Invoker invoker = expectingCallback(env, trace, 1, handler);
        if (invoker != null) {
            ExceptionHandler eh = env.getExceptionHandler();
            env.setExceptionHandler(new ExceptionHandler(invoker, handler.toImmutable()));
            return eh == null || eh.invoker == null ? Memory.NULL : eh.invokerMemory;
        } else
            return Memory.FALSE;
    }

    public static boolean restore_exception_handler(Environment env) {
        env.setExceptionHandler(env.getPreviousExceptionHandler());
        return true;
    }

    public static Memory get_defined_vars(Environment env, TraceInfo trace, @Runtime.GetLocals ArrayMemory locals) {
        return locals.toImmutable();
    }

    public static int extract(Environment env, TraceInfo trace, @Runtime.GetLocals ArrayMemory locals,
                              @Reference Memory array, int extractType) {
        return extract(env, trace, locals, array, extractType, Memory.NULL);
    }

    public static int extract(Environment env, TraceInfo trace, @Runtime.GetLocals ArrayMemory locals,
                              @Reference Memory array) {
        return extract(env, trace, locals, array, LangConstants.EXTR_OVERWRITE.toInteger(), Memory.NULL);
    }

    public static int extract(Environment env, TraceInfo trace, @Runtime.GetLocals ArrayMemory locals,
                              @Reference Memory array, int extractType, Memory _prefix) {
        if (expecting(env, trace, 1, array, Memory.Type.ARRAY)) {
            boolean isRefs = (extractType & LangConstants.EXTR_REFS.toInteger()) == LangConstants.EXTR_REFS.toInteger();
            ForeachIterator iterator = array.getNewIterator(env, isRefs, false);
            int count = 0;
            if (extractType == LangConstants.EXTR_PREFIX_ALL.toInteger() || extractType == LangConstants.EXTR_PREFIX_IF_EXISTS.toInteger()
                    || extractType == LangConstants.EXTR_PREFIX_INVALID.toInteger() || extractType == LangConstants.EXTR_PREFIX_SAME.toInteger())
                if (_prefix.isNull()) {
                    env.warning(trace, "extract(): specified extract type requires the prefix parameter");
                    return 0;
                }

            String prefix = _prefix.isNull() ? "" : _prefix.concat("_");
            if (!prefix.isEmpty())
                if (!GrammarUtils.isValidName(prefix)) {
                    env.warning(trace, "extract(): prefix is not a valid identifier");
                    return 0;
                }

            while (iterator.next()) {
                Object key = iterator.getKey();
                String keyS = key.toString();
                String var;
                Memory value = iterator.getValue();

                if (!isRefs)
                    value = value.toImmutable();

                if (extractType == LangConstants.EXTR_OVERWRITE.toInteger()) {
                    if (GrammarUtils.isValidName(keyS)) {
                        locals.refOfIndex(keyS).assign(value);
                        count++;
                    }
                } else if (extractType == LangConstants.EXTR_SKIP.toInteger()) {
                    if (GrammarUtils.isValidName(keyS) && locals.valueOfIndex(keyS).isUndefined()) {
                        locals.refOfIndex(keyS).assign(value);
                        count++;
                    }
                } else if (extractType == LangConstants.EXTR_PREFIX_SAME.toInteger()) {
                    if (!locals.valueOfIndex(keyS).isUndefined()) {
                        var = prefix.concat(keyS);
                        if (GrammarUtils.isValidName(var)) {
                            locals.refOfIndex(var).assign(value);
                            count++;
                        }
                    } else if (GrammarUtils.isValidName(keyS)) {
                        locals.refOfIndex(keyS).assign(value);
                        count++;
                    }
                } else if (extractType == LangConstants.EXTR_PREFIX_ALL.toInteger()) {
                    var = prefix.concat(keyS);
                    if (GrammarUtils.isValidName(var)) {
                        locals.refOfIndex(prefix.concat(keyS)).assign(value);
                        count++;
                    }
                } else if (extractType == LangConstants.EXTR_PREFIX_INVALID.toInteger()) {
                    if (GrammarUtils.isValidName(keyS)) {
                        locals.refOfIndex(keyS).assign(value);
                        count++;
                    } else {
                        var = prefix.concat(keyS);
                        if (GrammarUtils.isValidName(var)) {
                            locals.refOfIndex(var).assign(value);
                            count++;
                        }
                    }
                } else if (extractType == LangConstants.EXTR_IF_EXISTS.toInteger()) {
                    if (GrammarUtils.isValidName(keyS))
                        if (!locals.valueOfIndex(keyS).isUndefined()) {
                            locals.refOfIndex(keyS).assign(value);
                            count++;
                        }
                } else if (extractType == LangConstants.EXTR_PREFIX_IF_EXISTS.toInteger()) {
                    if (!locals.valueOfIndex(keyS).isUndefined()) {
                        var = prefix.concat(keyS);
                        if (GrammarUtils.isValidName(var)) {
                            locals.refOfIndex(var).assign(value);
                            count++;
                        }
                    }
                }
            }

            return count;
        } else {
            return 0;
        }
    }

    public static boolean defined(Environment env, String name) {
        Memory value = env.findConstant(name);
        return value != null;
    }

    public static boolean define(Environment env, TraceInfo trace, String name, Memory value, boolean caseInSentisise) {
        return env.defineConstant(name, value, !caseInSentisise);
    }

    public static boolean define(Environment env, TraceInfo trace, String name, Memory value) {
        return define(env, trace, name, value, false);
    }

    public static Memory constant(Environment env, TraceInfo trace, String name) {
        int pos;
        if ((pos = name.indexOf("::")) > -1) {
            String className = name.substring(0, pos);
            String constName = name.substring(pos + 2);
            ClassEntity entity = env.fetchClass(className, true);
            if (entity == null)
                return Memory.NULL;

            ConstantEntity constant = entity.findConstant(constName);
            InvokeHelper.checkAccess(env, trace, constant, false);
            return constant == null ? Memory.NULL : constant.getValue();
        } else {
            Memory value = env.findConstant(name);
            if (value == null)
                return Memory.NULL;
            else
                return value;
        }
    }

    @Immutable
    public static String gettype(Memory memory) {
        switch (memory.getRealType()) {
            case ARRAY:
                return "array";
            case BOOL:
                return "boolean";
            case INT:
                return "integer";
            case DOUBLE:
                return "double";
            case STRING:
                return "string";
            case OBJECT:
                if (memory.isResource())
                    return "resource";

                return "object";
            case NULL:
                return "NULL";
            default:
                return "unknown type";
        }
    }

    public static boolean is_array(@Reference Memory memory) {
        return memory.isArray();
    }

    public static boolean is_iterable(@Reference Memory memory) {
        return memory.isTraversable();
    }

    @Immutable
    public static boolean is_bool(Memory memory) {
        return memory.toValue().type == Memory.Type.BOOL;
    }

    @Immutable
    public static boolean is_double(Memory memory) {
        return memory.toValue().type == Memory.Type.DOUBLE;
    }

    @Immutable
    public static boolean is_float(Memory memory) {
        return memory.toValue().type == Memory.Type.DOUBLE;
    }

    @Immutable
    public static boolean is_int(Memory memory) {
        return memory.toValue().type == Memory.Type.INT;
    }

    @Immutable
    public static boolean is_integer(Memory memory) {
        return memory.toValue().type == Memory.Type.INT;
    }

    @Immutable
    public static boolean is_long(Memory memory) {
        return memory.toValue().type == Memory.Type.INT;
    }

    @Immutable
    public static boolean is_null(Memory memory) {
        return memory.isNull();
    }

    public static boolean is_object(@Reference Memory memory) {
        return memory.isObject();
    }

    @Immutable
    public static boolean is_real(Memory memory) {
        return is_float(memory);
    }

    @Immutable
    public static boolean is_string(Memory memory) {
        return memory.isString();
    }

    @Immutable
    public static boolean is_numeric(Memory memory) {
        return StringMemory.toNumeric(memory.toString(), false, null) != null;
    }

    @Immutable
    public static boolean is_scalar(Memory memory) {
        switch (memory.getRealType()) {
            case BOOL:
            case NULL:
            case INT:
            case DOUBLE:
            case STRING:
                return true;
        }
        return false;
    }

    public static boolean is_resource(@Reference Memory memory) {
        return memory.isResource();
    }

    public static Memory get_resource_type(@Reference Memory memory) {
        if (memory.isObject()) {
            if (((ObjectMemory) memory).value instanceof Resource)
                return new StringMemory(
                        ((Resource) ((ObjectMemory) memory).value).getResourceType()
                );
        }
        return Memory.NULL;
    }

    public static boolean is_callable(Environment env, TraceInfo trace, @Reference Memory memory)
            throws Throwable {
        // optimize
        if (memory.isObject() && memory.toValue(ObjectMemory.class).value instanceof Closure)
            return true;
        Invoker invoker = Invoker.valueOf(env, null, memory);
        return invoker != null && invoker.canAccess(env) == 0;
    }

    @Immutable
    public static boolean boolval(Memory memory) {
        return memory.toBoolean();
    }

    @Immutable
    public static String strval(Memory memory) {
        return memory.toString();
    }

    @Immutable
    public static long intval(Memory memory) {
        return memory.toLong();
    }

    @Immutable
    public static double floatval(Memory memory) {
        return memory.toDouble();
    }

    @Immutable
    public static double doubleval(Memory memory) {
        return memory.toDouble();
    }

    public static boolean settype(@Reference Memory memory, String type) {
        if (memory.isReference()) {
            if ("string".equals(type)) {
                memory.assign(memory.toString());
            } else if ("bool".equals(type) || "boolean".equals(type)) {
                memory.assign(memory.toBoolean());
            } else if ("int".equals(type) || "integer".equals(type)) {
                memory.assign(memory.toLong());
            } else if ("float".equals(type) || "double".equals(type)) {
                memory.assign(memory.toDouble());
            } else if ("null".equals(type)) {
                memory.assign(Memory.NULL);
            } else
                return false;

            return true;
        }
        return false;
    }

    public void debug_zval_dump(Environment env, TraceInfo trace) {
        env.warning(trace, "debug_zval_dump(): unsupported");
    }

    public static Memory func_get_args(Environment env, TraceInfo trace) {
        if (env.getCallStackTop() == 0) {
            return Memory.FALSE;
        }

        Memory[] args = env.peekCall(0).args;

        if (args == null) {
            return new ArrayMemory().toConstant();
        }

        return ArrayMemory.of(args);
    }

    public static Memory func_num_args(Environment env, TraceInfo trace) {
        if (env.getCallStackTop() == 0) {
            return Memory.FALSE;
        }

        Memory[] args = env.peekCall(0).args;

        if (args == null) {
            return Memory.CONST_INT_0;
        }

        return LongMemory.valueOf(args.length);
    }

    public static Memory func_get_arg(Environment env, TraceInfo trace, int argNum) {
        if (env.getCallStackTop() == 0) {
            return Memory.FALSE;
        }
        if (argNum < 0)
            return Memory.FALSE;

        Memory[] args = env.peekCall(0).args;

        if (args == null) {
            return  Memory.FALSE;
        }

        if (argNum < args.length)
            return args[argNum];
        else
            return Memory.FALSE;
    }

    private static Memory _call_user_func(Environment env, TraceInfo trace, Memory function, Memory... args)
            throws Throwable {
        Invoker invoker = expectingCallback(env, trace, 1, function);
        if (invoker == null) {
            return Memory.FALSE;
        }
        invoker.setTrace(trace);

        return invoker.call(args);
    }

    public static Memory call_user_func(Environment env, TraceInfo trace, Memory function, Memory... args)
            throws Throwable {
        Memory[] passed;
        if (args == null) {
            passed = new Memory[]{function};
        } else {
            passed = new Memory[args.length + 1];
            System.arraycopy(args, 0, passed, 1, args.length);
            passed[0] = function;
        }

        env.pushCall(trace, null, passed, "call_user_func", null, null);
        try {
            return _call_user_func(env, trace, function, args);
        } finally {
            env.popCall();
        }
    }

    public static Memory call_user_func_array(Environment env, TraceInfo trace, Memory function, Memory args)
            throws Throwable {
        if (expecting(env, trace, 2, args, Memory.Type.ARRAY)) {
            Memory[] passed = new Memory[]{function, args};
            env.pushCall(trace, null, passed, "call_user_func_array", null, null);
            try {
                return _call_user_func(env, trace, function, ((ArrayMemory) args).values(false));
            } finally {
                env.popCall();
            }
        }
        return Memory.FALSE;
    }

    public static Memory debug_backtrace(Environment env, TraceInfo trace, int options, int limit) {
        boolean provideObject = (options & LangConstants.DEBUG_BACKTRACE_PROVIDE_OBJECT.toInteger())
                == LangConstants.DEBUG_BACKTRACE_PROVIDE_OBJECT.toInteger();
        boolean ignoreArgs = (options & LangConstants.DEBUG_BACKTRACE_IGNORE_ARGS.toInteger())
                == LangConstants.DEBUG_BACKTRACE_IGNORE_ARGS.toInteger();

        ArrayMemory result = new ArrayMemory();
        for (int i = 0; i < env.getCallStackTop(); i++) {
            if (limit != 0 && i >= limit)
                break;

            CallStackItem item = env.peekCall(i);
            ArrayMemory el = item.toArray(provideObject, ignoreArgs);
            result.add(el);
        }

        return result.toConstant();
    }

    public static Memory debug_backtrace(Environment env, TraceInfo trace, int options) {
        return debug_backtrace(env, trace, options, 0);
    }

    public static Memory debug_backtrace(Environment env, TraceInfo trace) {
        return debug_backtrace(env, trace, LangConstants.DEBUG_BACKTRACE_PROVIDE_OBJECT.toInteger(), 0);
    }


    public static void debug_print_backtrace(Environment env, TraceInfo trace, int options, int limit) {
        boolean provideObject = (options & LangConstants.DEBUG_BACKTRACE_PROVIDE_OBJECT.toInteger())
                == LangConstants.DEBUG_BACKTRACE_PROVIDE_OBJECT.toInteger();
        boolean ignoreArgs = (options & LangConstants.DEBUG_BACKTRACE_IGNORE_ARGS.toInteger())
                == LangConstants.DEBUG_BACKTRACE_IGNORE_ARGS.toInteger();

        StackTracer stackTracer = new StackTracer(env, limit);
        env.echo(stackTracer.toString(!ignoreArgs));
    }

    public static void debug_print_backtrace(Environment env, TraceInfo trace, int options) {
        debug_print_backtrace(env, trace, options, 0);
    }

    public static void debug_print_backtrace(Environment env, TraceInfo trace) {
        debug_print_backtrace(env, trace, LangConstants.DEBUG_BACKTRACE_PROVIDE_OBJECT.toInteger(), 0);
    }

    public static boolean function_exists(Environment env, String name) {
        FunctionEntity function = env.fetchFunction(name);
        return function != null;
    }

    public static boolean class_exists(Environment env, String name, boolean autoload) {
        ClassEntity entity = env.fetchClass(name, autoload);
        return entity != null && entity.isClass();
    }

    public static boolean class_exists(Environment env, String name) {
        return class_exists(env, name, true);
    }

    public static boolean interface_exists(Environment env, String name, boolean autoload) {
        ClassEntity entity = env.fetchClass(name, autoload);
        return entity != null && entity.isInterface();
    }

    public static boolean interface_exists(Environment env, String name) {
        return interface_exists(env, name, true);
    }

    public static boolean trait_exists(Environment env, String name, boolean autoload) {
        ClassEntity entity = env.fetchClass(name, autoload);
        return entity != null && entity.isTrait();
    }

    public static boolean trait_exists(Environment env, String name) {
        return trait_exists(env, name, true);
    }

    public static boolean method_exists(Environment env, Memory clazz, String method) {
        ClassEntity classEntity;
        if (clazz.isObject()) {
            ObjectMemory tmp = clazz.toValue(ObjectMemory.class);
            classEntity = tmp.getReflection();
        } else {
            String name = clazz.toString();
            String nameL = name.toLowerCase();
            classEntity = env.fetchClass(name, nameL, true);
            if (classEntity == null)
                classEntity = env.fetchMagicClass(name, nameL);
        }

        return classEntity != null && classEntity.findMethod(method.toLowerCase()) != null;
    }

    public static Memory property_exists(Environment env, Memory clazz, String property) throws Throwable {
        ClassEntity classEntity;
        IObject object = null;
        boolean isMagic = false;
        if (clazz.isObject()) {
            ObjectMemory tmp = clazz.toValue(ObjectMemory.class);
            classEntity = tmp.getReflection();
            object = tmp.value;
        } else {
            String name = clazz.toString();
            String nameL = name.toLowerCase();

            classEntity = env.fetchClass(name, nameL, true);
            if (classEntity == null) {
                classEntity = env.fetchMagicClass(name, nameL);
                isMagic = true;
            }
        }

        if (classEntity == null) {
            return Memory.FALSE;
        }

        if (object != null) {
            ArrayMemory props = object.getProperties();

            ClassEntity context = env.getLastClassOnStack();
            PropertyEntity entity = classEntity.isInstanceOf(context)
                    ? context.properties.get(property) : classEntity.properties.get(property);

            int accessFlags = entity == null ? 0 : entity.canAccess(env);
            if (accessFlags != 0)
                return Memory.FALSE;

            return (props != null && props.getByScalar(entity == null ? property : entity.getSpecificName()) != null)
                    ? Memory.TRUE : Memory.FALSE;
        } else {
            PropertyEntity entity = classEntity.properties.get(property);
            if (isMagic) {
                int accessFlags = entity == null ? 0 : entity.canAccess(env);
                if (accessFlags != 0)
                    return Memory.FALSE;
            }

            return entity != null ? Memory.TRUE : Memory.FALSE;
        }
    }

    public static Memory is_a(Environment env, TraceInfo trace, Memory object, String className,
                              boolean allowedString) {
        ClassEntity classEntity = null;
        ClassEntity parentClass;

        if (allowedString && !object.isObject()) {
            String name = object.toString();
            String nameL = name.toLowerCase();

            classEntity = env.fetchClass(name, nameL, false);
            if (classEntity == null)
                classEntity = env.fetchMagicClass(name, nameL);

        } else if (expecting(env, trace, 1, object, Memory.Type.OBJECT)) {
            classEntity = object.toValue(ObjectMemory.class).getReflection();
        }
        if (classEntity == null)
            return Memory.FALSE;

        parentClass = env.fetchClass(className, false);
        if (parentClass == null)
            return Memory.FALSE;

        return classEntity.isInstanceOf(parentClass) ? Memory.TRUE : Memory.FALSE;
    }

    public static Memory is_a(Environment env, TraceInfo trace, Memory object, String className) {
        return is_a(env, trace, object, className, false);
    }

    public static Memory is_subclass_of(Environment env, TraceInfo trace, Memory object, String className,
                                        boolean allowedString) {
        ClassEntity classEntity = null;
        ClassEntity parentClass;

        if (allowedString && !object.isObject()) {
            String name = object.toString();
            String nameL = name.toLowerCase();

            classEntity = env.fetchClass(name, nameL, true);
            if (classEntity == null)
                classEntity = env.fetchMagicClass(name, nameL);

        } else if (expecting(env, trace, 1, object, Memory.Type.OBJECT)) {
            classEntity = object.toValue(ObjectMemory.class).getReflection();
        }
        parentClass = env.fetchClass(className, true);

        if (classEntity == null) {
            return Memory.NULL;
        } else if (parentClass == null) {
            return Memory.NULL;
        } else {
            if (object.isObject() && object.toValue(ObjectMemory.class).value instanceof Closure)
                return Memory.FALSE;

            return classEntity.isInstanceOf(parentClass)
                    && !classEntity.equals(parentClass) ? Memory.TRUE : Memory.FALSE;
        }
    }

    public static Memory is_subclass_of(Environment env, TraceInfo trace, Memory object, String className) {
        return is_subclass_of(env, trace, object, className, true);
    }

    public static Memory get_class(Environment env, TraceInfo trace, Memory object) {
        if (object.isNull()) {
            if (object == Memory.UNDEFINED) {
                return Memory.FALSE;
            }

            CallStackItem item = env.peekCall(0);
            if (item.clazz != null) {
                if (item.classEntity == null)
                    item.classEntity = env.fetchClass(item.clazz, false);

                if (item.classEntity == null)
                    return Memory.FALSE;
                else {
                    MethodEntity method = item.classEntity.findMethod(item.function);
                    if (method == null)
                        return Memory.FALSE;
                    return new StringMemory(method.getClazz().getName());
                }
            }
        } else if (expecting(env, trace, 1, object, Memory.Type.OBJECT)) {
            return new StringMemory(object.toValue(ObjectMemory.class).getReflection().getName());
        }

        return Memory.FALSE;
    }

    public static Memory get_class(Environment env, TraceInfo trace) {
        return get_class(env, trace, Memory.NULL);
    }

    public static Memory get_called_class(Environment env) {
        String name = env.getLateStatic();
        return name == null || name.isEmpty() ? Memory.FALSE : new StringMemory(name);
    }

    public static Memory get_class_methods(Environment env, TraceInfo trace, Memory value) {
        ClassEntity entity;
        if (value.isString()) {
            entity = env.fetchClass(value.toString(), true);
        } else if (value.isObject()) {
            entity = value.toValue(ObjectMemory.class).getReflection();
        } else {
            env.warning(
                    trace, "get_class_methods(): Argument 1 must be string or object, %s given",
                    value.getRealType().toString()
            );
            return Memory.NULL;
        }

        if (entity == null)
            return Memory.NULL;

        ClassEntity context = env.getLastClassOnStack();

        ArrayMemory result = new ArrayMemory();
        for (MethodEntity el : entity.getMethods().values()) {
            if (el.canAccess(env, context) == 0)
                result.refOfPush().assign(el.getName());
        }

        return result.toConstant();
    }

    public static Memory get_class_vars(Environment env, TraceInfo trace, Memory value) {
        ClassEntity entity;
        if (value.isString()) {
            entity = env.fetchClass(value.toString(), true);
        } else if (value.isObject()) {
            entity = value.toValue(ObjectMemory.class).getReflection();
        } else {
            env.warning(
                    trace, "get_class_vars(): Argument 1 must be string or object, %s given",
                    value.getRealType().toString()
            );
            return Memory.NULL;
        }

        if (entity == null)
            return Memory.NULL;

        ClassEntity context = env.getLastClassOnStack();

        ArrayMemory result = new ArrayMemory();
        for (PropertyEntity el : entity.getProperties()) {
            if (el.canAccess(env, context) == 0)
                result.refOfIndex(el.getName()).assign(el.getDefaultValue(env));
        }

        for (PropertyEntity el : entity.getStaticProperties()) {
            if (el.canAccess(env, context) == 0)
                result.refOfIndex(el.getName()).assign(el.getDefaultValue(env));
        }

        return result.toConstant();
    }

    public static Memory get_object_vars(Environment env, TraceInfo trace, Memory object) {
        if (expecting(env, trace, 1, object, Memory.Type.OBJECT)) {
            ObjectMemory o = object.toValue(ObjectMemory.class);
            ArrayMemory props = o.value.getProperties();
            ClassEntity entity = o.getReflection();

            ClassEntity context = env.getLastClassOnStack();
            ForeachIterator iterator = props.foreachIterator(false, false);

            ArrayMemory result = new ArrayMemory();
            while (iterator.next()) {
                PropertyEntity prop = entity.findProperty(iterator.getKey().toString());
                if (prop == null || prop.canAccess(env, context) == 0)
                    result.refOfIndex(prop == null ? iterator.getKey().toString() : prop.getName())
                            .assign(iterator.getValue());
            }
            return result.toConstant();
        } else
            return Memory.NULL;
    }

    public static Memory get_parent_class(Memory object) {
        if (object.isObject()) {
            ClassEntity classEntity = object.toValue(ObjectMemory.class).getReflection().getParent();
            if (classEntity == null)
                return Memory.FALSE;
            else
                return new StringMemory(classEntity.getName());
        } else {
            return Memory.FALSE;
        }
    }

    public static Memory get_parent_class(Environment env) {
        CallStackItem item = env.peekCall(0);
        if (item.clazz != null) {
            if (item.classEntity == null)
                item.classEntity = env.fetchClass(item.clazz, false);

            if (item.classEntity == null)
                return Memory.FALSE;
            else {
                MethodEntity method = item.classEntity.findMethod(item.function);
                if (method == null)
                    return Memory.FALSE;

                ClassEntity parent = method.getClazz().getParent();
                return parent == null ? Memory.FALSE : new StringMemory(parent.getName());
            }
        }
        return Memory.FALSE;
    }

    public static Memory flow(Environment env, Memory result, Memory... others) {
        WrapFlow flow = WrapFlow.of(env, result).toObject(WrapFlow.class);

        if (others != null) {
            for (Memory other : others) {
                flow = flow.append(env, other).toObject(WrapFlow.class);
            }
        }

        return ObjectMemory.valueOf(flow);
    }


    public static Memory observable() {
        return new ObservableMemory();
    }

    public static Memory observable(Memory value) {
        return new ObservableMemory(value);
    }

    public static void addObserver(Environment env, TraceInfo trace, Memory observer, Memory handler) {
        Invoker invoker = expectingCallback(env, trace, 2, handler);

        if (invoker != null) {
            if (observer instanceof ObservableMemory) {
                ((ObservableMemory) observer).addObserver(new ObservableMemory.Observer() {
                    @Override
                    public void update(ObservableMemory observable, Memory oldValue, Memory newValue) {
                        invoker.callNoThrow(newValue, oldValue);
                    }
                });
            } else {
                env.error(trace, ErrorType.E_ERROR, "addObserver(): first argument must be observable");
            }
        }
    }

    /*public static Memory define_package(Environment env, TraceInfo trace, String name, Memory info) {
        if (env.scope.getLangMode() != LangMode.MODERN) {
            env.error(trace, ErrorType.E_NOTICE, "define_package(): Packages are available only in modern language mode");
            return Memory.FALSE;
        }

        if (!expecting(env, trace, 1, info, Memory.Type.ARRAY)) {
            return Memory.FALSE;
        }


        PackageManager manager = env.getPackageManager();

        if (manager.has(name)) {
            env.error(trace, "define_package(): Package '" + name + "' already defined");
            return Memory.FALSE;
        }

        php.runtime.env.Package aPackage = manager.fetch(name, trace);

        for (Memory cls : classes.toValue(ArrayMemory.class)) {
            aPackage.addClass(cls.toString());
        }

        for (Memory func : functions.toValue(ArrayMemory.class)) {
            aPackage.addFunction(func.toString());
        }

        for (Memory c : constants.toValue(ArrayMemory.class)) {
            aPackage.addConstant(c.toString());
        }

        return Memory.TRUE;
    }*/
}
