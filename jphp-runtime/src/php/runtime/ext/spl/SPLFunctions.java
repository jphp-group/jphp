package php.runtime.ext.spl;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.SplClassLoader;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.invoke.Invoker;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.Set;

public class SPLFunctions extends FunctionsContainer {

    public static long iterator_apply(Environment env, TraceInfo trace, Memory object, Memory callback, Memory args)
            throws Throwable {
        if (expectingImplement(env, trace, 1, object, Traversable.class)){
            Invoker invoker = expectingCallback(env, trace, 2, callback);
            if (invoker == null)
                return 0;

            if (args != null && !expecting(env, trace, 3, args, Memory.Type.ARRAY)){
                return 0;
            }

            ForeachIterator iterator = object.getNewIterator(env, false, false);
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
            throws Throwable {
        return iterator_apply(env, trace, object, callback, null);
    }

    public static long iterator_count(Environment env, TraceInfo trace, Memory object) {
        if (expectingImplement(env, trace, 1, object, Traversable.class)){
            IObject tmp = object.toValue(ObjectMemory.class).value;
            if (tmp instanceof Countable){
                return ((Countable)tmp).count(env).toLong();
            } else {
                ForeachIterator iterator = object.getNewIterator(env, true, false);
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
            ForeachIterator iterator = object.getNewIterator(env, false, false);
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

    public static Memory class_parents(Environment env, TraceInfo trace, Memory object, boolean autoLoad){
        ClassEntity entity;
        if (object.isObject()){
            entity = object.toValue(ObjectMemory.class).getReflection();
        } else {
            entity = env.fetchClass(object.toString(), autoLoad);
        }

        if (entity == null) {
            env.warning(trace, "class_parents(): Class %s does not exist and could not be loaded", object.toString());
            return Memory.FALSE;
        }

        ArrayMemory result = new ArrayMemory();
        do {
            entity = entity.getParent();
            if (entity == null) break;
            result.refOfIndex(entity.getName()).assign(entity.getName());
        } while (true);

        return result.toConstant();
    }

    public static Memory class_parents(Environment env, TraceInfo trace, Memory object){
        return class_parents(env, trace, object, true);
    }

    public static Memory class_implements(Environment env, TraceInfo trace, Memory object, boolean autoLoad){
        ClassEntity entity;
        if (object.isObject()){
            entity = object.toValue(ObjectMemory.class).getReflection();
        } else {
            entity = env.fetchClass(object.toString(), autoLoad);
        }

        if (entity == null) {
            env.warning(trace, "class_implements(): Class %s does not exist and could not be loaded", object.toString());
            return Memory.FALSE;
        }

        ArrayMemory result = new ArrayMemory();
        do {
            for (ClassEntity el : entity.getInterfaces().values()){
                result.refOfIndex(el.getName()).assign(el.getName());
            }
            entity = entity.getParent();
            if (entity == null) break;
        } while (true);

        return result.toConstant();
    }

    public static Memory class_implements(Environment env, TraceInfo trace, Memory object){
        return class_implements(env, trace, object, true);
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

        env.registerAutoloader(new SplClassLoader(invoker, callback), prepend);
        return true;
    }

    public static boolean spl_autoload_register(Environment env, TraceInfo trace, Memory callback, boolean _throw){
        return spl_autoload_register(env, trace, callback, _throw, false);
    }

    public static boolean spl_autoload_register(Environment env, TraceInfo trace, Memory callback){
        return spl_autoload_register(env, trace, callback, true, false);
    }

    public static boolean spl_autoload_register(Environment env, TraceInfo trace){
        return spl_autoload_register(env, trace, new StringMemory("spl_autoload"), true, false);
    }

    private static final StringMemory __autoloadMethod = new StringMemory("__autoload");

    public static Memory spl_autoload_functions(Environment env){
        ArrayMemory result = new ArrayMemory();
        for (SplClassLoader loader : env.getClassLoaders()){
            result.add(loader.getCallback().toImmutable());
        }

        if (result.size() == 0){
            if (env.__autoload == null){
                Invoker invoker = Invoker.valueOf(env, null, __autoloadMethod);
                if (invoker != null) {
                    env.__autoload = new SplClassLoader(invoker, __autoloadMethod);
                    result.add(env.__autoload.getCallback().toImmutable());
                }
            }
        }

        return result.toConstant();
    }

    public static boolean spl_autoload_unregister(Environment env, TraceInfo trace, Memory callback){
        Invoker invoker = expectingCallback(env, trace, 1, callback);
        if (invoker == null)
            return false;

        return env.unRegisterAutoloader(new SplClassLoader(invoker, callback));
    }

    public static final StringMemory defaultExtensions = new StringMemory(".inc,.php");

    public static String spl_autoload_extensions(Environment env, String extensions){
        env.getOrCreateStatic("spl$autoload_extensions", defaultExtensions).assign(extensions);
        return extensions;
    }

    public static String spl_autoload_extensions(Environment env){
        return env.getOrCreateStatic("spl$autoload_extensions", defaultExtensions).toString();
    }

    public static void __$jphp_spl_autoload(Environment env, String className, String fileExtensions){
        if (env.__autoload == null){
            Invoker invoker = Invoker.valueOf(env, null, __autoloadMethod);
            if (invoker != null)
                env.__autoload = new SplClassLoader(invoker, __autoloadMethod);
        }

        if (env.__autoload != null)
            env.__autoload.load(new StringMemory(className), new StringMemory(fileExtensions));
    }

    public static void __$jphp_spl_autoload(Environment env, String className){
        __$jphp_spl_autoload(env, className, spl_autoload_extensions(env));
    }

    public static void spl_autoload(Environment env, String className, String fileExtensions) throws Throwable {
        String[] extensions = StringUtils.split(fileExtensions, ",", 255);
        Set<String> includePaths = env.getIncludePaths();

        for(String path : includePaths){
            for(String e : extensions){
                File file = new File(path, className + e);
                if (file.exists()){
                    env.__include(file.getPath());
                    return;
                }
            }
        }
    }

    public static void spl_autoload(Environment env, String className) throws Throwable {
        spl_autoload(env, className, spl_autoload_extensions(env));
    }
}
