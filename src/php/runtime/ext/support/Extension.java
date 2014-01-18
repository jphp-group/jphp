package php.runtime.ext.support;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.reflection.ClassEntity;

import java.util.HashMap;
import java.util.Map;

abstract public class Extension {
    abstract public String getName();
    abstract public String getVersion();

    public void onRegister(CompileScope scope){
        // nop
    }

    protected Map<String, CompileConstant> constants = new HashMap<String, CompileConstant>();
    protected Map<String, CompileFunction> functions = new HashMap<String, CompileFunction>();
    protected Map<String, ClassEntity> classes = new HashMap<String, ClassEntity>();

    public Map<String, CompileConstant> getConstants() {
        return constants;
    }

    public Map<String, CompileFunction> getFunctions() {
        return functions;
    }

    public Map<String, ClassEntity> getClasses() {
        return classes;
    }

    public void registerNativeClass(CompileScope scope, Class<?> clazz){
        ClassEntity classEntity = new ClassEntity(this, scope, clazz);
        classes.put(classEntity.getLowerName(), classEntity);
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
