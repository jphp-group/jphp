package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.Information;
import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.output.PrintR;
import ru.regenix.jphp.runtime.memory.output.Printer;
import ru.regenix.jphp.runtime.memory.output.VarDump;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

public class InfoFunctions extends FunctionsContainer {

    public static Memory phpversion(Environment env, String extension){
        if (extension == null || extension.isEmpty())
            return new StringMemory(Information.LIKE_PHP_VERSION);

        Extension ext = env.scope.getExtension(extension);
        if (ext == null)
            return Memory.NULL;
        else
            return new StringMemory(ext.getVersion());
    }

    public static Memory phpversion(Environment env){
        return phpversion(env, null);
    }

    /** SKIP **/
    public static int gc_collect_cycles(){
        System.gc();
        return 0;
    }

    public static void gc_disable(){ /* NOP */ }
    public static void gc_enable(){ /* NOP */ }
    public static boolean gc_enabled(){ return true; }

    public static boolean get_magic_quotes_gpc(){ return false; }
    public static boolean get_magic_quotes_runtime(){ return false; }

    public static boolean set_magic_quotes_runtime(){ return false; }

    public static String get_current_user(){
        return System.getProperty("user.name");
    }

    public static Memory get_defined_constants(Environment env, boolean capitalize){
        Set<String> exists = new HashSet<String>();

        ArrayMemory result = new ArrayMemory();
        for(String ext : env.scope.getExtensions()){
            Extension extension = env.scope.getExtension(ext);

            ArrayMemory item = result;
            if (capitalize)
                item = (ArrayMemory) result.refOfIndex(ext).assign(new ArrayMemory());

            for(CompileConstant constant : extension.getConstants().values()){
                item.put(constant.name, constant.value);
                exists.add(constant.name);
            }
        }

        ArrayMemory item = result;
        if (capitalize)
            item = (ArrayMemory) result.refOfIndex("user").assign(new ArrayMemory());

        for(ConstantEntity constant : env.scope.getConstants()){
            if (!exists.contains(constant.getName()))
                item.put(constant.getName(), constant.getValue());
        }

        return result;
    }

    public static Memory get_defined_constants(Environment env){
        return get_defined_constants(env, false);
    }

    public static Memory get_declared_classes(Environment env){
        ArrayMemory array = new ArrayMemory();
        for(ClassEntity classEntity : env.classMap.values()){
            if (classEntity.getType() == ClassEntity.Type.CLASS)
                array.add(new StringMemory(classEntity.getName()));
        }

        return array.toConstant();
    }

    public static Memory get_declared_interfaces(Environment env){
        ArrayMemory array = new ArrayMemory();
        for(ClassEntity classEntity : env.classMap.values()){
            if (classEntity.getType() == ClassEntity.Type.INTERFACE)
                array.add(new StringMemory(classEntity.getName()));
        }

        return array.toConstant();
    }

    public static Memory get_defined_functions(Environment env){
        ArrayMemory array = new ArrayMemory();
        ArrayMemory item = (ArrayMemory)array.refOfIndex("internal").assign(new ArrayMemory());
        for(FunctionEntity entity : env.functionMap.values()){
            if (entity.isInternal())
                item.add(new StringMemory(entity.getName()));
        }

        item = (ArrayMemory)array.refOfIndex("user").assign(new ArrayMemory());
        for(FunctionEntity entity : env.getLoadedFunctions().values()){
            if (!entity.isInternal())
                item.add(new StringMemory(entity.getName()));
        }

        return array.toConstant();
    }

    public static boolean extension_loaded(Environment env, String name){
        return env.scope.getExtension(name) != null;
    }

    public static Memory get_loaded_extensions(Environment env){
        return MemoryUtils.valueOf(env.scope.getExtensions());
    }

    public static Memory get_extension_funcs(Environment env, String name){
        return new ArrayMemory(env.scope.getExtension(name).getFunctions().keySet());
    }

    public static Memory ini_get(Environment env, String name){
        return env.getConfigValue(name, Memory.NULL);
    }

    public static Memory ini_get_all(Environment env, String extension, boolean includingGlobal){
        return env.getConfigValues(extension, includingGlobal);
    }

    public static Memory ini_get_all(Environment env, String extension){
        return ini_get_all(env, extension, true);
    }

    public static void ini_set(Environment env, String name, Memory value){
        env.setConfigValue(name, value);
    }

    public static void ini_alter(Environment env, String name, Memory value){
        ini_set(env, name, value);
    }

    public static void ini_restore(Environment env, String name){
        env.restoreConfigValue(name);
    }

    public static Memory get_included_files(Environment env){
        return new ArrayMemory(env.getIncluded().keySet());
    }

    public static Memory get_required_files(Environment env){
        return get_included_files(env);
    }

    public static long getmypid(){
        return Thread.currentThread().getId();
    }

    @Runtime.Immutable
    public static String zend_version(){
        return Information.LIKE_ZEND_VERSION;
    }

    public static long zend_thread_id(){
        return Thread.currentThread().getId();
    }

    public static String sys_get_temp_dir(){
        return System.getProperty("java.io.tmpdir");
    }

    public static Memory print_r(Environment env, @Runtime.Reference Memory value, boolean returned){
        StringWriter writer = new StringWriter();
        Printer printer = new PrintR(writer);
        printer.print(value);

        if (returned){
            return new StringMemory(writer.toString());
        } else {
            env.echo(writer.toString());
            return Memory.TRUE;
        }
    }

    public static Memory print_r(Environment env, @Runtime.Reference Memory value){
        return print_r(env, value, false);
    }

    public static Memory var_dump(Environment env, @Runtime.Reference Memory value, @Runtime.Reference Memory... values){
        StringWriter writer = new StringWriter();
        VarDump printer = new VarDump(writer);

        printer.print(value);
        if (values != null)
            for(Memory el : values)
                printer.print(el);

        env.echo(writer.toString());
        return Memory.TRUE;
    }
}
