package php.runtime.ext.support;

import php.runtime.Information;
import php.runtime.annotation.Reflection;
import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.java.JavaException;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.support.MemoryOperation;

import java.util.LinkedHashMap;
import java.util.Map;

abstract public class Extension {
    public enum Status { EXPERIMENTAL, BETA, STABLE, LEGACY, ZEND_LEGACY, DEPRECATED }

    public interface Extensible {
        Extension getExtension();
    }

    protected final Map<String, CompileConstant> constants = new LinkedHashMap<String, CompileConstant>();
    protected final Map<String, CompileFunction> functions = new LinkedHashMap<String, CompileFunction>();
    protected final Map<String, Class<?>> classes = new LinkedHashMap<String, Class<?>>();

    public String getName() {
        return getClass().getName();
    }

    public String getVersion() {
        return Information.CORE_VERSION;
    }

    abstract public Status getStatus();

    @Deprecated
    public String[] getRequiredExtensions(){
        return new String[0];
    }

    @Deprecated
    public String[] getOptionalExtensions(){
        return new String[0];
    }

    @Deprecated
    public String[] getConflictExtensions(){
        return new String[0];
    }

    public Map<String, String> getINIEntries(){
        return new HashedMap<String, String>();
    }

    abstract public void onRegister(CompileScope scope);

    public void onLoad(Environment env){
        // nop
    }

    public Map<String, CompileConstant> getConstants() {
        return constants;
    }

    public Map<String, CompileFunction> getFunctions() {
        return functions;
    }

    public Map<String, Class<?>> getClasses() {
        return classes;
    }

    @Deprecated
    public void registerNativeClass(CompileScope scope, Class<?> clazz) {
        registerClass(scope, clazz);
    }

    public void registerClass(CompileScope scope, Class<?> clazz) {
        if (BaseWrapper.class.isAssignableFrom(clazz) && !clazz.isAnnotationPresent(Reflection.NotWrapper.class)) {
            throw new CriticalException("Please use registerWrapperClass() method instead of this for wrapper classes");
        }

        if (classes.put(clazz.getName(), clazz) != null)
            throw new CriticalException("Class already registered - " + clazz.getName());
    }

    public <T> void registerWrapperClass(CompileScope scope, Class<T> clazz, Class<? extends BaseWrapper> wrapperClass) {
        if (classes.put(clazz.getName(), wrapperClass) != null)
            throw new CriticalException("Class already registered - " + clazz.getName());

        MemoryOperation.registerWrapper(clazz, wrapperClass);
    }

    public void registerMemoryOperation(Class<? extends MemoryOperation> clazz) {
        try {
            MemoryOperation.register(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CriticalException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void registerJavaException(CompileScope scope, Class<? extends JavaException> javaClass,
                                      Class<? extends Throwable>... classes) {
        registerClass(scope, javaClass);
        if (classes != null)
        for(Class<? extends Throwable> el : classes)
            scope.registerJavaException(javaClass, el);
    }

    public void registerJavaExceptionForContext(CompileScope scope, Class<? extends JavaException> javaClass,
                                                Class<? extends IObject> context) {
        registerClass(scope, javaClass);
        scope.registerJavaExceptionForContext(javaClass, context);
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
