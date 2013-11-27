package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.reflection.support.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassEntity extends Entity {
    // types
    public enum Type { CLASS, INTERFACE, TRAIT }

    /** byte code */
    protected byte[] data;
    protected Extension extension;
    protected Class<?> nativeClazz;
    protected Constructor nativeConstructor;
    protected ModuleEntity module;

    public final Map<String, MethodEntity> methods;
    public MethodEntity methodConstruct;

    public MethodEntity methodMagicSet;
    public MethodEntity methodMagicGet;
    public MethodEntity methodMagicUnset;
    public MethodEntity methodMagicCall;
    public MethodEntity methodMagicCallStatic;
    public MethodEntity methodMagicInvoke;

    protected MethodEntity constructor;

    protected final Map<String, ClassEntity> interfaces;
    protected final Map<String, ClassEntity> traits;

    public final Map<String, ConstantEntity> constants;
    public final Map<String, PropertyEntity> properties;
    public final Map<String, PropertyEntity> staticProperties;

    protected ClassEntity parent;
    protected DocumentComment docComment;

    protected boolean isAbstract = false;
    protected boolean isFinal = false;
    protected Type type = Type.CLASS;

    public ClassEntity(Context context) {
        super(context);
        this.methods = new LinkedHashMap<String, MethodEntity>();
        this.interfaces = new LinkedHashMap<String, ClassEntity>();
        this.traits = new LinkedHashMap<String, ClassEntity>();
        this.properties = new LinkedHashMap<String, PropertyEntity>();
        this.staticProperties = new LinkedHashMap<String, PropertyEntity>();
        this.constants = new LinkedHashMap<String, ConstantEntity>();
    }

    public ClassEntity(Extension extension, Class<?> nativeClazz){
        this(null);
        this.extension = extension;
        if (nativeClazz.isInterface())
            type = Type.INTERFACE;

        if (nativeClazz.isAnnotationPresent(Reflection.Name.class)){
            Reflection.Name name = nativeClazz.getAnnotation(Reflection.Name.class);
            setName(name.value());
        } else {
            setName(nativeClazz.getName().replaceAll("\\.", "\\"));
        }

        for (Method method : nativeClazz.getDeclaredMethods()){
            if (method.isAnnotationPresent(Reflection.Signature.class)){
                MethodEntity entity = new MethodEntity(extension, method);
                entity.setClazz(this);

                Reflection.Name name = method.getAnnotation(Reflection.Name.class);
                entity.setName(name == null ? method.getName() : name.value());

                addMethod(entity);
            }
        }
        this.setNativeClazz(nativeClazz);
        doneDeclare();
    }

    public void doneDeclare(){
        methodConstruct  = methods.get("__construct");
        if (methodConstruct == null)
            methodConstruct = methods.get(getLowerName());

        methodMagicSet   = methods.get("__set");
        methodMagicGet   = methods.get("__get");
        methodMagicUnset = methods.get("__unset");
        methodMagicCall  = methods.get("__call");
        methodMagicCallStatic = methods.get("__callStatic");

        methodMagicInvoke = methods.get("__invoke");
    }

    public Extension getExtension() {
        return extension;
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, MethodEntity> getMethods() {
        return methods;
    }

    public void addMethod(MethodEntity method){
        String name = method.getLowerName();
        this.methods.put(name, method);
    }

    public MethodEntity findMethod(String name){
        return methods.get(name);
    }

    public ClassEntity getParent() {
        return parent;
    }

    public void setParent(ClassEntity parent) {
        this.parent = parent;
        for(MethodEntity method : parent.getMethods().values()){
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == null){
                addMethod(method);
            } else {
                // TODO check signature impl method
            }
        }
        doneDeclare();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void addInterface(ClassEntity _interface) {
        this.interfaces.put(_interface.getLowerName(), _interface);
        for(MethodEntity method : _interface.getMethods().values()){
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == null){
                addMethod(method);
                // TODO throw no implemented method
            } else {
                // TODO check signature
            }
        }
    }

    public Map<String, ClassEntity> getInterfaces() {
        return interfaces;
    }

    public void addTrait(ClassEntity trait) {
        this.traits.put(trait.getLowerName(), trait);
    }

    public Map<String, ClassEntity> getTraits() {
        return traits;
    }

    public Collection<ConstantEntity> getConstants() {
        return constants.values();
    }

    public Collection<PropertyEntity> getProperties() {
        return properties.values();
    }

    public Collection<PropertyEntity> getStaticProperties() {
        return staticProperties.values();
    }

    public void addConstant(ConstantEntity constant){
        constants.put(constant.getLowerName(), constant);
        constant.setClazz(this);
    }

    protected void addProperty(PropertyEntity property){
        if (property.isStatic())
            throw new IllegalArgumentException("Property must be non-static");

        properties.put(property.getLowerName(), property);
        property.setClazz(this);
    }

    protected void addStaticProperty(PropertyEntity property){
        if (!property.isStatic())
            throw new IllegalArgumentException("Property must be static");

        staticProperties.put(property.getLowerName(), property);
        property.setClazz(this);
    }

    public MethodEntity getConstructor() {
        return constructor;
    }

    public void setConstructor(MethodEntity constructor) {
        this.constructor = constructor;
        constructor.setClazz(this);
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
        if (!nativeClazz.isInterface()){
            try {
                this.nativeConstructor = nativeClazz.getConstructor(ClassEntity.class);
                this.nativeConstructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public PHPObject newObject(Environment env, TraceInfo trace, Memory[] args)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        PHPObject object = (PHPObject) nativeConstructor.newInstance(this);
        if (methodConstruct != null){
            methodConstruct.invokeDynamic(object, getLowerName(), env, args);
        }
        return object;
    }

    public Memory setProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        ReferenceMemory value = object.__dynamicProperties__.getByScalar(property);
        if (value == null) {
            if (methodMagicSet != null)
                methodMagicSet.invokeDynamic(
                        object, getLowerName(), env,
                        new StringMemory(property),
                        memory
                );
            else
                object.__dynamicProperties__.refOfIndex(property).assign(memory);
        }
        return memory;
    }

    public Memory getProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property)
            throws InvocationTargetException, IllegalAccessException {
        ReferenceMemory value = object.__dynamicProperties__.getByScalar(property);
        if (value != null)
            return value;

        if (methodMagicGet != null)
            return methodMagicGet.invokeDynamic(
                    object, getLowerName(), env, new StringMemory(property)
            );

        /*env.triggerError(new FatalException(
                Messages.ERR_FATAL_CANNOT_GET_OBJECT_PROPERTY_OF_CLASS.fetch(property, getName()),
                trace
        ));*/
        return Memory.NULL;
    }
}
