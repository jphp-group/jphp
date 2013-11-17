package ru.regenix.jphp.compiler.common;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.compile.*;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import java.util.HashMap;
import java.util.Map;

abstract public class Extension {
    abstract public String getName();
    abstract public String getVersion();

    public void onRegister(CompileScope scope){
        // nop
    }

    private Map<String, CompileConstant> constants = new HashMap<String, CompileConstant>();
    private Map<String, CompileFunction> functions = new HashMap<String, CompileFunction>();
    private Map<String, ClassEntity> classes = new HashMap<String, ClassEntity>();

    public Map<String, CompileConstant> getConstants() {
        return constants;
    }

    public Map<String, CompileFunction> getFunctions() {
        return functions;
    }

    public Map<String, ClassEntity> getClasses() {
        return classes;
    }

    public void registerNativeClass(Class<?> clazz){
        ClassEntity classEntity = new ClassEntity(this, clazz);
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
