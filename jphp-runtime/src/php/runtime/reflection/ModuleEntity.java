package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.common.LangMode;
import php.runtime.env.Context;
import php.runtime.env.DieException;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.lang.BaseException;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.reflection.support.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ModuleEntity extends Entity {
    private int id;
    private Class<?> nativeClazz;
    protected Method nativeMethod;

    private boolean strictTypes = false;

    private final Map<String, ClassEntity> classes;
    private final List<FunctionEntity> functions;
    private final Map<String, ConstantEntity> constants;
    private final List<ClosureEntity> closures;
    private final List<GeneratorEntity> generators;

    protected boolean isLoaded;

    private final static Memory[] argsMock = new Memory[]{};

    public ModuleEntity(Context context) {
        super(context);
        this.classes = new LinkedHashMap<String, ClassEntity>();
        this.functions = new ArrayList<FunctionEntity>();
        this.constants = new LinkedHashMap<String, ConstantEntity>();
        this.closures = new ArrayList<ClosureEntity>();
        this.generators = new ArrayList<GeneratorEntity>();
        this.setName(context.getModuleNameNoThrow());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(Method nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    public Memory include(Environment env, ArrayMemory locals) throws Throwable {
        try {
            return (Memory) nativeMethod.invoke(null, env, argsMock, locals);
        } catch (InvocationTargetException e){
            return env.__throwException(e);
        }
    }

    public Memory include(Environment env) throws Throwable {
        try {
            return (Memory) nativeMethod.invoke(null, env, argsMock, env.getGlobals());
        } catch (InvocationTargetException e){
            return env.__throwException(e);
        }
    }

    public Memory includeNoThrow(Environment env, ArrayMemory locals){
        try {
            return include(env, locals);
        } catch (DieException | ErrorException e) {
            throw e;
        } catch (Exception e) {
            env.catchUncaught(e);
            return Memory.NULL;
        } catch (Throwable throwable) {
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

    public Collection<GeneratorEntity> getGenerators() {
        return generators;
    }

    public Collection<FunctionEntity> getFunctions(){
        return functions;
    }

    public ClassEntity findClass(String name){
        return classes.get(name.toLowerCase());
    }

    public FunctionEntity findFunction(String name) {
        name = name.toLowerCase();

        for (FunctionEntity function : functions) {
            if (function.getLowerName().equals(name)) {
                return function;
            }
        }

        return null;
    }

    public FunctionEntity findFunction(int index) {
        if (index >= 0 && index < functions.size())
            return functions.get(index);
        else
            return null;
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

    public GeneratorEntity findGenerator(int index){
        if (index >= 0 && index < generators.size())
            return generators.get(index);
        else
            return null;
    }

    public void addConstant(ConstantEntity constant){
        if (!constants.containsKey(constant.getName()))
            constants.put(constant.getName(), constant);
    }

    public void addClosure(ClosureEntity closure){
        closures.add(closure);
        if (closure.getGeneratorEntity() != null) {
            addGenerator(closure.getGeneratorEntity());
        }
    }

    public void addGenerator(GeneratorEntity generator) {
        generators.add(generator);
    }

    public void addClass(ClassEntity clazz){
        classes.put(clazz.getLowerName(), clazz);
        for (MethodEntity entity : clazz.getOwnedMethods()) {
            if (entity.getGeneratorEntity() != null) {
                addGenerator(entity.getGeneratorEntity());
            }
        }
    }

    public void addFunction(FunctionEntity function){
        functions.add(function);
        if (function.getGeneratorEntity() != null) {
            addGenerator(function.getGeneratorEntity());
        }
    }

    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
    }

    public boolean isStrictTypes() {
        return strictTypes;
    }

    public void setStrictTypes(boolean strictTypes) {
        this.strictTypes = strictTypes;
    }
}
