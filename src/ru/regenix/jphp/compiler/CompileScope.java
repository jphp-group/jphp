package ru.regenix.jphp.compiler;

import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.runtime.loader.RuntimeClassLoader;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompileScope {

    private final Map<String, ClassEntity> classMap;
    private final Map<String, FunctionEntity> functionMap;
    private final Map<String, ConstantEntity> constantMap;

    protected Map<String, Extension> extensions;

    protected Map<String, CompileConstant> compileConstantMap;
    protected Map<String, CompileFunction> compileFunctionMap;

    private static Map<Class, Map<String, CallableValue>> classMethods =
            new HashMap<Class, Map<String, CallableValue>>();

    protected RuntimeClassLoader classLoader;

    public CompileScope() {
        classLoader = new RuntimeClassLoader(Thread.currentThread().getContextClassLoader());

        classMap = new HashMap<String, ClassEntity>();
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
    }

    public void addUserFunction(FunctionEntity function){
        functionMap.put(function.getLowerName(), function);
    }

    public void addUserConstant(ConstantEntity constant){
        constantMap.put(constant.getLowerName(), constant);
    }

    public ClassEntity findUserClass(String name){
        return classMap.get(name.toLowerCase());
    }

    public FunctionEntity findUserFunction(String name){
        return functionMap.get(name.toLowerCase());
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

    public static void registerClass(Class clazz){
        Map<String, CallableValue> map = new HashMap<String, CallableValue>();
        for (Method method : clazz.getMethods()){
            map.put(method.getName(), new CallableValue(method));
        }
        classMethods.put(clazz, map);
    }

    public static CallableValue findMethod(Class clazz, String methodName){
        return classMethods.get(clazz).get(methodName);
    }

    public ClassEntity loadClass(String name){
        ClassEntity entity = findUserClass(name.toLowerCase());
        if (entity == null)
            return null;

        classLoader.loadClass(entity);
        return entity;
    }
}
