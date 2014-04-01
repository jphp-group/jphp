package php.runtime.ext.support;

import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.ext.support.compile.FunctionsContainer;

import java.util.LinkedHashMap;
import java.util.Map;

abstract public class Extension {
    public String getName() {
        return getClass().getName();
    }

    abstract public String getVersion();

    public String[] getRequiredExtensions(){
        return new String[0];
    }

    public String[] getOptionalExtensions(){
        return new String[0];
    }

    public String[] getConflictExtensions(){
        return new String[0];
    }

    public Map<String, String> getINIEntries(){
        return new HashedMap<String, String>();
    }

    public void onRegister(CompileScope scope){
        // nop
    }

    public void onLoad(Environment env){
        // nop
    }

    protected Map<String, CompileConstant> constants = new LinkedHashMap<String, CompileConstant>();
    protected Map<String, CompileFunction> functions = new LinkedHashMap<String, CompileFunction>();
    protected Map<String, Class<?>> classes = new LinkedHashMap<String, Class<?>>();

    public Map<String, CompileConstant> getConstants() {
        return constants;
    }

    public Map<String, CompileFunction> getFunctions() {
        return functions;
    }

    public Map<String, Class<?>> getClasses() {
        return classes;
    }

    public void registerNativeClass(CompileScope scope, Class<?> clazz) {
        if (classes.put(clazz.getName(), clazz) != null)
            throw new RuntimeException("Class already registered - " + clazz.getName());
    }

    public void registerJavaException(CompileScope scope, Class<? extends JavaException> javaClass,
                                      Class<? extends Throwable>... classes) {
        registerNativeClass(scope, javaClass);
        if (classes != null)
        for(Class<? extends Throwable> el : classes)
            scope.registerJavaException(javaClass, el);
    }

    public void registerConstants(ConstantsContainer container){
        for(CompileConstant constant : container.getConstants()){
            constants.put(constant.name, constant);
        }
    }

    public void registerFunctions(FunctionsContainer container){
        for(CompileFunction function : container.getFunctions()){
            functions.put(function.name.toLowerCase(), function);
        }
    }
}
