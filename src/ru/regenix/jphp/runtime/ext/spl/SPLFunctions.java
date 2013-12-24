package ru.regenix.jphp.runtime.ext.spl;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.SplClassLoader;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.lang.spl.Countable;
import ru.regenix.jphp.runtime.lang.spl.Traversable;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import java.lang.reflect.InvocationTargetException;

public class SPLFunctions extends FunctionsContainer {

    public static long iterator_apply(Environment env, TraceInfo trace, Memory object, Memory callback, Memory args)
            throws InvocationTargetException, IllegalAccessException {
        if (expectingImplement(env, trace, 1, object, Traversable.class)){
            Invoker invoker = expectingCallback(env, trace, 2, callback);
            if (invoker == null)
                return 0;

            if (args != null && !expecting(env, trace, 3, args, Memory.Type.ARRAY)){
                return 0;
            }

            ForeachIterator iterator = object.getNewIterator(false, false);
            Memory[] values = args == null ? null : args.toValue(ArrayMemory.class).values(true);
            int i = 0;
            while (iterator.next()){
                if (!invoker.call(values).toBoolean())
                    break;
                i++;
            }
            return i;
        } else
            return 0;
    }

    public static long iterator_apply(Environment env, TraceInfo trace, Memory object, Memory callback)
            throws InvocationTargetException, IllegalAccessException {
        return iterator_apply(env, trace, object, callback, null);
    }

    public static long iterator_count(Environment env, TraceInfo trace, Memory object) {
        if (expectingImplement(env, trace, 1, object, Traversable.class)){
            PHPObject tmp = object.toValue(ObjectMemory.class).value;
            if (tmp instanceof Countable){
                return ((Countable)tmp).count(env).toLong();
            } else {
                ForeachIterator iterator = object.getNewIterator(true, false);
                int i = 0;
                while (iterator.next()) i++;
                return i;
            }
        } else
            return 0;
    }

    public static Memory iterator_to_array(Environment env, TraceInfo trace, Memory object, boolean useKeys){
        if (expectingImplement(env, trace, 1, object, Traversable.class)){
            ArrayMemory result = new ArrayMemory();
            ForeachIterator iterator = object.getNewIterator(false, false);
            while (iterator.next()){
                if (useKeys){
                    result.refOfIndex(iterator.getMemoryKey()).assign(iterator.getValue());
                } else {
                    result.add(iterator.getValue());
                }
            }
            return result.toConstant();
        }
        return Memory.NULL;
    }

    public static Memory iterator_to_array(Environment env, TraceInfo trace, Memory object){
        return iterator_to_array(env, trace, object, true);
    }

    public static Memory class_parents(Environment env, Memory object, boolean autoLoad){
        ClassEntity entity;
        if (object.isObject()){
            entity = object.toValue(ObjectMemory.class).getSelfClass();
        } else {
            entity = env.fetchClass(object.toString(), false, autoLoad);
        }

        if (entity == null)
            return Memory.FALSE;

        ArrayMemory result = new ArrayMemory();
        do {
            entity = entity.getParent();
            if (entity == null) break;
            result.refOfIndex(entity.getName()).assign(entity.getName());
        } while (true);

        return result.toConstant();
    }

    public static Memory class_parents(Environment env, Memory object){
        return class_parents(env, object, true);
    }

    public static Memory spl_object_hash(Environment env, TraceInfo trace, Memory object){
        if (expecting(env, trace, 1, object, Memory.Type.OBJECT)){
            return LongMemory.valueOf(object.getPointer());
        } else
            return Memory.FALSE;
    }

    public static boolean spl_autoload_register(Environment env, TraceInfo trace, Memory callback, boolean _throw,
                                                boolean prepend){
        Invoker invoker = expectingCallback(env, trace, 1, callback);
        if (invoker == null)
            return false;

        env.autoloadRegister(new SplClassLoader(invoker, callback), prepend);
        return true;
    }

    public static boolean spl_autoload_register(Environment env, TraceInfo trace, Memory callback, boolean _throw){
        return spl_autoload_register(env, trace, callback, _throw, false);
    }

    public static boolean spl_autoload_register(Environment env, TraceInfo trace, Memory callback){
        return spl_autoload_register(env, trace, callback, true, false);
    }

    public static Memory spl_autoload_functions(Environment env){
        ArrayMemory result = new ArrayMemory();
        for (SplClassLoader loader : env.getClassLoaders()){
            result.add(loader.getCallback().toImmutable());
        }
        return result.toConstant();
    }

    public static boolean spl_autoload_unregister(Environment env, TraceInfo trace, Memory callback){
        Invoker invoker = expectingCallback(env, trace, 1, callback);
        if (invoker == null)
            return false;

        return env.autoloadUnregister(new SplClassLoader(invoker, callback));
    }

    public static String spl_autoload_extensions(Environment env, String extensions){
        env.getOrCreateStatic("spl$autoload_extensions", Memory.CONST_EMPTY_STRING).assign(extensions);
        return extensions;
    }

    public static String spl_autoload_extensions(Environment env){
        return env.getOrCreateStatic("spl$autoload_extensions", Memory.CONST_EMPTY_STRING).toString();
    }

    public static void spl_autoload(Environment env, String className, String fileExtensions){

    }

    public static void spl_autoload(Environment env, String className){
        spl_autoload(env, className,
                env.getOrCreateStatic("spl$autoload_extensions", Memory.CONST_EMPTY_STRING).toString());
    }
}
