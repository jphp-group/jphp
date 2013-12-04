package ru.regenix.jphp.compiler;

import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.loader.RuntimeClassLoader;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompileScope {

    protected final AtomicInteger moduleCount = new AtomicInteger(0);
    protected final AtomicLong classCount = new AtomicLong(0);
    protected final AtomicLong methodCount = new AtomicLong(0);

    public final Map<String, ModuleEntity> moduleMap;
    public final Map<String, ClassEntity> classMap;
    public final Map<String, MethodEntity> methodMap;

    public final Map<String, FunctionEntity> functionMap;
    public final Map<String, ConstantEntity> constantMap;

    protected Map<String, Extension> extensions;

    protected Map<String, CompileConstant> compileConstantMap;
    protected Map<String, CompileFunction> compileFunctionMap;

    protected RuntimeClassLoader classLoader;

    public Map<String, Memory> configuration;

    public CompileScope() {
        classLoader = new RuntimeClassLoader(Thread.currentThread().getContextClassLoader());

        moduleMap = new HashMap<String, ModuleEntity>();

        classMap = new HashMap<String, ClassEntity>();
        methodMap = new HashMap<String, MethodEntity>();
        functionMap = new HashMap<String, FunctionEntity>();
        constantMap = new HashMap<String, ConstantEntity>();

        extensions = new LinkedHashMap<String, Extension>();
        compileConstantMap = new HashMap<String, CompileConstant>();
        compileFunctionMap = new HashMap<String, CompileFunction>();
    }

    public int nextModuleIndex(){
        return moduleCount.incrementAndGet();
    }

    public long nextClassIndex(){
        return classCount.incrementAndGet();
    }

    public long nextMethodIndex(){
        return methodCount.incrementAndGet();
    }

    public void registerExtension(Extension extension){
        extension.onRegister(this);
        compileConstantMap.putAll(extension.getConstants());
        compileFunctionMap.putAll(extension.getFunctions());

        for(ClassEntity clazz : extension.getClasses().values()){
            addUserClass(clazz);
        }

        for(CompileFunction function : extension.getFunctions().values()){
            functionMap.put(function.name.toLowerCase(), new CompileFunctionEntity(function));
        }

        extensions.put(extension.getName(), extension);
    }

    public Extension getExtension(String name){
        return extensions.get(name);
    }

    public Set<String> getExtensions(){
        return extensions.keySet();
    }

    public void addUserClass(ClassEntity clazz){
        classMap.put(clazz.getLowerName(), clazz);
        for(MethodEntity method : clazz.getMethods().values()){
            methodMap.put(method.getKey(), method);
        }
    }

    public void addUserModule(ModuleEntity module){
        for(ClassEntity clazz : module.getClasses()){
            addUserClass(clazz);
        }

        for(FunctionEntity function : module.getFunctions()){
            addUserFunction(function);
        }

        for(ConstantEntity constant : module.getConstants()){
            addUserConstant(constant);
        }

        moduleMap.put(module.getName(), module);
    }

    public void addUserFunction(FunctionEntity function){
        functionMap.put(function.getLowerName(), function);
    }

    public void addUserConstant(ConstantEntity constant){
        constantMap.put(constant.getLowerName(), constant);
    }

    public ModuleEntity findUserModule(String name){
        return moduleMap.get(name);
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

    public Collection<ConstantEntity> getConstants(){
        return constantMap.values();
    }

    public CompileConstant findCompileConstant(String name){
        return compileConstantMap.get(name);
    }

    public CompileFunction findCompileFunction(String name){
        return compileFunctionMap.get(name.toLowerCase());
    }

    public ModuleEntity loadModule(String name){
        ModuleEntity entity = findUserModule(name);
        if (entity == null)
            return null;

        classLoader.loadModule(entity);
        return entity;
    }

    public void loadModule(ModuleEntity module){
        classLoader.loadModule(module);
    }
}
