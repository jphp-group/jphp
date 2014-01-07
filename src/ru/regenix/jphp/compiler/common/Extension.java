package ru.regenix.jphp.compiler.common;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.compiler.common.compile.ConstantsContainer;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

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
