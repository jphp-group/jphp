package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.DieException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.helper.ClosureEntity;
import ru.regenix.jphp.runtime.reflection.support.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ModuleEntity extends Entity {

    private int id;
    private byte[] data;
    private Class<?> nativeClazz;
    protected Method nativeMethod;

    private final Map<String, ClassEntity> classes;
    private final Map<String, FunctionEntity> functions;
    private final Map<String, ConstantEntity> constants;
    private final List<ClosureEntity> closures;

    protected boolean isLoaded;

    private final static Memory[] argsMock = new Memory[]{};

    public ModuleEntity(Context context) {
        super(context);
        this.classes = new HashMap<String, ClassEntity>();
        this.functions = new HashMap<String, FunctionEntity>();
        this.constants = new HashMap<String, ConstantEntity>();
        this.closures = new ArrayList<ClosureEntity>();
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

    public Memory include(Environment env, ArrayMemory locals)
            throws InvocationTargetException, IllegalAccessException {
        return (Memory) nativeMethod.invoke(null, env, argsMock, locals);
    }

    public Memory include(Environment env) throws InvocationTargetException, IllegalAccessException {
        return (Memory) nativeMethod.invoke(null, env, argsMock, env.getGlobals());
    }

    public Memory includeNoThrow(Environment env, ArrayMemory locals){
        try {
            return include(env, locals);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable throwable = getCause(e);
            if (throwable instanceof ErrorException)
                throw (ErrorException) throwable;
            if (throwable instanceof DieException)
                throw (DieException) throwable;

            throw new RuntimeException(throwable);
        }
    }

    public Memory includeNoThrow(Environment env){
        return includeNoThrow(env, env.getGlobals());
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

    public Collection<ClosureEntity> getClosures(){
        return closures;
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

    public ClosureEntity findClosure(int index){
        if (index >= 0 && index < closures.size())
            return closures.get(index);
        else
            return null;
    }

    public void addConstant(ConstantEntity constant){
        if (!constants.containsKey(constant.getName()))
            constants.put(constant.getName(), constant);
    }

    public void addClosure(ClosureEntity closure){
        closures.add(closure);
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
