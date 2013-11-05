package ru.regenix.jphp.compiler.common;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.compile.*;

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

    public Map<String, CompileConstant> getConstants() {
        return constants;
    }

    public Map<String, CompileFunction> getFunctions() {
        return functions;
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
