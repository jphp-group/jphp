package ru.regenix.jphp.compiler;

import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.loader.RuntimeClassLoader;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompileScope {

    public final Map<String, ClassEntity> classMap;
    public final Map<String, MethodEntity> methodMap;

    public final Map<String, FunctionEntity> functionMap;
    public final Map<String, ConstantEntity> constantMap;

    protected Map<String, Extension> extensions;

    protected Map<String, CompileConstant> compileConstantMap;
    protected Map<String, CompileFunction> compileFunctionMap;

    protected RuntimeClassLoader classLoader;

    public CompileScope() {
        classLoader = new RuntimeClassLoader(Thread.currentThread().getContextClassLoader());

        classMap = new HashMap<String, ClassEntity>();
        methodMap = new HashMap<String, MethodEntity>();
        functionMap = new HashMap<String, FunctionEntity>();
        constantMap = new HashMap<String, ConstantEntity>();

        extensions = new LinkedHashMap<String, Extension>();
        compileConstantMap = new HashMap<String, CompileConstant>();
        compileFunctionMap = new HashMap<String, CompileFunction>();
    }

    public void registerExtension(Extension extension){
        extension.onRegister(this);
        compileConstantMap.putAll(extension.getConstants());
        compileFunctionMap.putAll(extension.getFunctions());
        extensions.put(extension.getName(), extension);
    }

    public void addUserClass(ClassEntity clazz){
        classMap.put(clazz.getLowerName(), clazz);
        for(MethodEntity method : clazz.getMethods().values()){
            methodMap.put(method.getKey(), method);
        }
    }

    public void addUserFunction(FunctionEntity function){
        functionMap.put(function.getLowerName(), function);
    }

    public void addUserConstant(ConstantEntity constant){
        constantMap.put(constant.getLowerName(), constant);
    }

    public ClassEntity findUserClass(String name){
        return classMap.get(name);
    }

    public FunctionEntity findUserFunction(String name){
        return functionMap.get(name);
    }

    public ConstantEntity findUserConstant(String name){
        return constantMap.get(name.toLowerCase());
    }

    public CompileConstant findCompileConstant(String name){
        return compileConstantMap.get(name);
    }

    public CompileFunction findCompileFunction(String name){
        return compileFunctionMap.get(name.toLowerCase());
    }

    public ClassEntity loadClass(String name){
        ClassEntity entity = findUserClass(name.toLowerCase());
        if (entity == null)
            return null;

        classLoader.loadClass(entity);
        return entity;
    }
}
