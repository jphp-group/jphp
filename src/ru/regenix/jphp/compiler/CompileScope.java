package ru.regenix.jphp.compiler;

import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.lang.Closure;
import ru.regenix.jphp.runtime.loader.RuntimeClassLoader;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.*;
import ru.regenix.jphp.runtime.util.JVMStackTracer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompileScope {

    protected final AtomicInteger moduleCount = new AtomicInteger(0);
    protected final AtomicLong classCount = new AtomicLong(0);
    protected final AtomicLong methodCount = new AtomicLong(0);

    public final Map<String, ModuleEntity> moduleMap;
    public final Map<Integer, ModuleEntity> moduleIndexMap;

    protected final Map<String, ClassEntity> classMap;
    protected final Map<String, FunctionEntity> functionMap;
    protected final Map<String, ConstantEntity> constantMap;

    protected Map<String, Extension> extensions;

    protected Map<String, CompileConstant> compileConstantMap;
    protected Map<String, CompileFunction> compileFunctionMap;

    protected RuntimeClassLoader classLoader;

    public Map<String, Memory> configuration;

    public final Set<String> superGlobals;

    public CompileScope() {
        classLoader = new RuntimeClassLoader(Thread.currentThread().getContextClassLoader());

        moduleMap = new HashMap<String, ModuleEntity>();
        moduleIndexMap = new HashMap<Integer, ModuleEntity>();

        classMap = new HashMap<String, ClassEntity>();
        functionMap = new HashMap<String, FunctionEntity>();
        constantMap = new HashMap<String, ConstantEntity>();

        extensions = new LinkedHashMap<String, Extension>();
        compileConstantMap = new HashMap<String, CompileConstant>();
        compileFunctionMap = new HashMap<String, CompileFunction>();

        superGlobals = new HashSet<String>();

        superGlobals.add("GLOBALS");
        superGlobals.add("_ENV");
        superGlobals.add("_SERVER");
        superGlobals.add("_POST");
        superGlobals.add("_GET");
        superGlobals.add("_REQUEST");
        superGlobals.add("_FILES");
        superGlobals.add("_SESSION");
        superGlobals.add("_COOKIE");

        registerClass(new ClassEntity(null, Closure.class));
    }

    public Map<String, ClassEntity> getClassMap() {
        return classMap;
    }

    public Map<String, FunctionEntity> getFunctionMap() {
        return functionMap;
    }

    public Map<String, ConstantEntity> getConstantMap() {
        return constantMap;
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
            registerClass(clazz);
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

    public void registerClass(ClassEntity clazz){
        classMap.put(clazz.getLowerName(), clazz);
    }

    public void addUserModule(ModuleEntity module){
        /*for(ClassEntity clazz : module.getClasses()){
            registerClass(clazz);
        }*/

        /*for(FunctionEntity function : module.getFunctions()){
            registerFunction(function);
        }

        for(ConstantEntity constant : module.getConstants()){
            registerConstant(constant);
        }*/

        moduleMap.put(module.getName(), module);
        moduleIndexMap.put(module.getId(), module);
    }

    public void registerFunction(FunctionEntity function){
        functionMap.put(function.getLowerName(), function);
    }

    public void registerConstant(ConstantEntity constant){
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

    public JVMStackTracer getStackTracer(StackTraceElement[] elements){
        return new JVMStackTracer(classLoader, elements);
    }

    public JVMStackTracer getStackTracer(Throwable throwable){
        return getStackTracer(throwable.getStackTrace());
    }

    public JVMStackTracer getStackTracer(){
        return getStackTracer(Thread.currentThread().getStackTrace());
    }
}
