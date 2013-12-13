package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.DieException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.support.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModuleEntity extends Entity {

    private int id;
    private byte[] data;
    private Class<?> nativeClazz;
    protected Method nativeMethod;

    private final Map<String, ClassEntity> classes;
    private final Map<String, FunctionEntity> functions;
    private final Map<String, ConstantEntity> constants;

    protected boolean isLoaded;

    private final static Memory[] argsMock = new Memory[]{};

    public ModuleEntity(Context context) {
        super(context);
        this.classes = new HashMap<String, ClassEntity>();
        this.functions = new HashMap<String, FunctionEntity>();
        this.constants = new HashMap<String, ConstantEntity>();
        this.setName(context.getModuleNameNoThrow());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleClassName(){
        return "Module_" + getId();
    }

    public String getModuleNamespace(){
        return "php\\lang\\module";
    }

    public String getFunctionNamespace(){
        return "php\\lang\\function";
    }

    public String getFulledClassName(char sep){
        return (getModuleNamespace() + sep + getModuleClassName()).replace('\\', sep);
    }

    public String getFulledFunctionClassName(String name, char sep){
        return (getFunctionNamespace() + sep + name).replace('\\', sep);
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(Method nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    public Memory include(Environment env) throws InvocationTargetException, IllegalAccessException {
        return include(env, "", env.getGlobals());
    }

    public Memory include(Environment env, ArrayMemory locals)
            throws InvocationTargetException, IllegalAccessException {
        return include(env, "", locals);
    }

    public Memory include(Environment env, String calledClass, ArrayMemory locals)
            throws InvocationTargetException, IllegalAccessException {
        return (Memory) nativeMethod.invoke(null, env, calledClass, argsMock, locals);
    }

    public Memory include(Environment env, String calledClass) throws InvocationTargetException, IllegalAccessException {
        return (Memory) nativeMethod.invoke(null, env, calledClass, argsMock, env.getGlobals());
    }

    public Memory includeNoThrow(Environment env){
        return includeNoThrow(env, "", env.getGlobals());
    }

    public Memory includeNoThrow(Environment env, ArrayMemory locals){
        return includeNoThrow(env, "", locals);
    }

    public Memory includeNoThrow(Environment env, String calledClass, ArrayMemory locals){
        try {
            return include(env, calledClass, locals);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ErrorException)
                throw (ErrorException) e.getCause();
            if (e.getCause() instanceof DieException)
                throw (DieException) e.getCause();

            throw new RuntimeException(e.getCause());
        }
    }

    public Memory includeNoThrow(Environment env, String calledClass){
        return includeNoThrow(env, calledClass, env.getGlobals());
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public Collection<ConstantEntity> getConstants(){
        return constants.values();
    }

    public Collection<ClassEntity> getClasses(){
        return classes.values();
    }

    public Collection<FunctionEntity> getFunctions(){
        return functions.values();
    }

    public ClassEntity findClass(String name){
        return classes.get(name.toLowerCase());
    }

    public FunctionEntity findFunction(String name){
        return functions.get(name.toLowerCase());
    }

    public ConstantEntity findConstant(String name){
        return constants.get(name.toLowerCase());
    }

    public void addConstant(ConstantEntity constant){
        if (!constants.containsKey(constant.getName()))
            constants.put(constant.getName(), constant);
    }

    public void addClass(ClassEntity clazz){
        classes.put(clazz.getLowerName(), clazz);
    }

    public void addFunction(FunctionEntity function){
        functions.put(function.getLowerName(), function);
    }

    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
