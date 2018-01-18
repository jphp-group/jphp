package php.runtime.ext.core;

import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.annotation.Runtime;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.output.PrintR;
import php.runtime.memory.output.Printer;
import php.runtime.memory.output.VarDump;
import php.runtime.memory.output.VarExport;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.FunctionEntity;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
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

        for(ConstantEntity constant : env.getConstants().values()){
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
        for(ClassEntity classEntity : env.getClasses()){
            if (classEntity.getType() == ClassEntity.Type.CLASS)
                array.add(classEntity.getName());
        }

        return array.toConstant();
    }

    public static Memory get_declared_interfaces(Environment env){
        ArrayMemory array = new ArrayMemory();
        for(ClassEntity classEntity : env.getClasses()){
            if (classEntity.getType() == ClassEntity.Type.INTERFACE)
                array.add(classEntity.getName());
        }

        return array.toConstant();
    }

    public static Memory get_declared_traits(Environment env) {
        ArrayMemory array = new ArrayMemory();
        for(ClassEntity classEntity : env.getClasses()){
            if (classEntity.isTrait())
                array.add(classEntity.getName());
        }

        return array.toConstant();
    }

    public static Memory get_defined_functions(Environment env){
        ArrayMemory array = new ArrayMemory();
        ArrayMemory item = (ArrayMemory)array.refOfIndex("internal").assign(new ArrayMemory());
        for(FunctionEntity entity : env.getFunctions()){
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
        Extension ext = env.scope.getExtension(name);
        if (ext == null) {
            return Memory.FALSE;
        }
        return ArrayMemory.ofStringCollection(ext.getFunctions().keySet());
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
        return ArrayMemory.ofStringCollection(env.getModuleManager().getCachedPaths());
    }

    public static Memory get_required_files(Environment env){
        return get_included_files(env);
    }

    public static Memory getmypid() {
        // Should return something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');

        String pid = jvmName.substring(0, index);
        return StringMemory.toLong(pid);
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
        Printer printer = new PrintR(env, writer);
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
        VarDump printer = new VarDump(env, writer);

        printer.print(value);
        if (values != null)
            for(Memory el : values)
                printer.print(el);

        env.echo(writer.toString());
        return Memory.TRUE;
    }

    public static Memory var_export(Environment env, TraceInfo trace, @Runtime.Reference Memory value, boolean returned){
        StringWriter writer = new StringWriter();
        VarExport printer = new VarExport(env, writer);

        printer.print(value);
        if (printer.isRecursionExists()){
            env.warning(trace, "var_export does not handle circular references");
        }

        if (returned){
            return new StringMemory(writer.toString());
        } else {
            env.echo(writer.toString());
            return Memory.TRUE;
        }
    }

    public static Memory var_export(Environment env, TraceInfo trace, @Runtime.Reference Memory value){
        return var_export(env, trace, value, false);
    }

    public static String set_include_path(Environment env, String value){
        String old = env.getConfigValue("include_path", Memory.CONST_EMPTY_STRING).toString();
        env.setConfigValue("include_path", new StringMemory(value));
        return old;
    }

    public static String get_include_path(Environment env){
        return env.getConfigValue("include_path", Memory.CONST_EMPTY_STRING).toString();
    }

    public static void restore_include_path(Environment env){
        env.restoreConfigValue("include_path");
    }
}
