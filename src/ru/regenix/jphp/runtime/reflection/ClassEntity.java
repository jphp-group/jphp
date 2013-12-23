package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
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

    private long id;
    protected int methodCounts = 0;
    protected boolean isInternal;

    /** byte code */
    protected byte[] data;
    protected Extension extension;
    protected Class<?> nativeClazz;
    protected Constructor nativeConstructor;
    protected ModuleEntity module;

    public final Map<String, MethodEntity> methods;
    public MethodEntity methodConstruct;
    public MethodEntity methodDestruct;

    public MethodEntity methodMagicSet;
    public MethodEntity methodMagicGet;
    public MethodEntity methodMagicUnset;
    public MethodEntity methodMagicIsset;
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
        this.isInternal = false;
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
            setName(nativeClazz.getSimpleName());
        }
        setInternalName(nativeClazz.getName().replace('.', '/'));

        for (Method method : nativeClazz.getDeclaredMethods()){
            if (method.isAnnotationPresent(Reflection.Signature.class)){
                MethodEntity entity = new MethodEntity(extension, method);
                entity.setClazz(this);
                entity.setNativeMethod(method);

                Reflection.Name name = method.getAnnotation(Reflection.Name.class);
                entity.setName(name == null ? method.getName() : name.value());

                addMethod(entity);
            }
        }
        this.setNativeClazz(nativeClazz);
        this.isInternal = true;
        doneDeclare();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void doneDeclare(){
        methodConstruct  = methods.get("__construct");
        if (methodConstruct == null)
            methodConstruct = methods.get(getLowerName());

        methodDestruct = methods.get("__destruct");

        methodMagicSet   = methods.get("__set");
        methodMagicGet   = methods.get("__get");
        methodMagicUnset = methods.get("__unset");
        methodMagicIsset = methods.get("__isset");
        methodMagicCall  = methods.get("__call");
        methodMagicCallStatic = methods.get("__callstatic");

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

    public int nextMethodIndex(){
        return methodCounts++;
    }

    public MethodEntity findMethod(String name){
        return methods.get(name);
    }

    public ConstantEntity findConstant(String name){
        return constants.get(name);
    }

    public ClassEntity getParent() {
        return parent;
    }

    public void setParent(ClassEntity parent) {
        this.parent = parent;
        this.methodCounts = parent.methodCounts;

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
        constants.put(constant.getName(), constant);
        constant.setClazz(this);
    }

    public void addProperty(PropertyEntity property){
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

    protected static void invalidAccessToProperty(Environment env, TraceInfo trace, PropertyEntity entity, int accessFlag){
        switch (accessFlag){
            case 1: env.triggerError(new FatalException(
                    Messages.ERR_FATAL_ACCESS_TO_PROTECTED_PROPERTY.fetch(
                            entity.getClazz().getName(), entity.getName()
                    ),
                    trace
            ));
            case 2: env.triggerError(new FatalException(
                    Messages.ERR_FATAL_ACCESS_TO_PRIVATE_PROPERTY.fetch(
                            entity.getClazz().getName(), entity.getName()
                    ),
                    trace
            ));
        }
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
        if (!nativeClazz.isInterface()){
            try {
                this.nativeConstructor = nativeClazz.getConstructor(Environment.class, ClassEntity.class);
                this.nativeConstructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                this.nativeConstructor = null;
                //throw new RuntimeException(e);
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
        PHPObject object = (PHPObject) nativeConstructor.newInstance(env, this);

        for(PropertyEntity property : getProperties()){
            object.__dynamicProperties__.put(property.getName(), property.getDefaultValue().toImmutable());
        }

        if (methodConstruct != null){
            try {
                env.pushCall(trace, object, args, methodConstruct.getName(), name);
                methodConstruct.invokeDynamic(object, env, args);
            } finally {
                env.popCall();
            }
        }
        return object;
    }

    public Memory concatProperty(Environment env, TraceInfo trace,
                               PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return new StringMemory(o1.concat(o2));
            }
        });
    }

    public Memory plusProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.plus(o2);
            }
        });
    }

    public Memory minusProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.minus(o2);
            }
        });
    }

    public Memory mulProperty(Environment env, TraceInfo trace,
                                PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.mul(o2);
            }
        });
    }

    public Memory divProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.div(o2);
            }
        });
    }

    public Memory modProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.mod(o2);
            }
        });
    }

    public Memory bitAndProperty(Environment env, TraceInfo trace,
                                 PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitAnd(o2);
            }
        });
    }

    public Memory bitOrProperty(Environment env, TraceInfo trace,
                                 PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitOr(o2);
            }
        });
    }

    public Memory bitXorProperty(Environment env, TraceInfo trace,
                                PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitXor(o2);
            }
        });
    }

    public Memory bitShrProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback(){
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitShr(o2);
            }
        });
    }

    public Memory bitShlProperty(Environment env, TraceInfo trace,
                                 PHPObject object, String property, Memory memory)
            throws InvocationTargetException, IllegalAccessException {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitShl(o2);
            }
        });
    }

    public Memory setProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property, Memory memory, SetterCallback callback)
            throws InvocationTargetException, IllegalAccessException {
        ReferenceMemory value;
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.__dynamicProperties__;
        value = props == null || accessFlag != 0 ? null : props.getByScalar(property);

        if (value == null) {
            if (methodMagicSet != null) {
                StringMemory memoryProperty = new StringMemory(property);

                if (callback != null){
                    Memory o1 = Memory.NULL;
                    if (methodMagicGet != null) {
                        try {
                            Memory[] args = new Memory[]{memoryProperty};
                            env.pushCall(trace, object, args, methodMagicGet.getName(), name);
                            o1 = methodMagicGet.invokeDynamic(object, env, memoryProperty);
                        } finally {
                            env.popCall();
                        }
                    }
                    memory = callback.invoke(o1, memory);
                }

                try {
                    Memory[] args = new Memory[]{memoryProperty, memory};
                    env.pushCall(trace, object, args, methodMagicSet.getName(), name);
                    methodMagicSet.invokeDynamic(object, env, args);
                } finally {
                    env.popCall();
                }
            } else {
                if (accessFlag != 0)
                    invalidAccessToProperty(env, trace, entity, accessFlag);

                if (callback != null)
                    memory = callback.invoke(Memory.NULL, memory);

                return props == null ? Memory.NULL : object.__dynamicProperties__.refOfIndex(property).assign(memory);
            }
        } else {
            if (callback != null)
                memory = callback.invoke(value, memory);

            return value.assign(memory);
        }
        return memory;
    }

    public Memory unsetProperty(Environment env, TraceInfo trace, PHPObject object, String property)
            throws InvocationTargetException, IllegalAccessException {
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (object.__dynamicProperties__ == null
                || accessFlag != 0
                || object.__dynamicProperties__.removeByScalar(property) == null ){
            if (methodMagicUnset != null) {
                try {
                    Memory[] args = new Memory[]{new StringMemory(property)};
                    env.pushCall(trace, object, args, methodMagicUnset.getName(), name);
                    methodMagicUnset.invokeDynamic(object, env, args);
                } finally {
                    env.popCall();
                }
                return Memory.NULL;
            }
        }

        if (accessFlag != 0)
            invalidAccessToProperty(env, trace, entity, accessFlag);

        return Memory.NULL;
    }

    public Memory emptyProperty(Environment env, TraceInfo trace, PHPObject object, String property)
            throws InvocationTargetException, IllegalAccessException {
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (object.__dynamicProperties__ != null && accessFlag != 0){
            Memory tmp = object.__dynamicProperties__.getByScalar(property);
            if ( tmp != null ){
                return tmp;
            }
        }

        if (methodMagicIsset != null){
            Memory result;
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicIsset.getName(), name);
                result = methodMagicIsset.invokeDynamic(object, env, new StringMemory(property))
                        .toBoolean() ? Memory.FALSE : Memory.TRUE;
            } finally {
                env.popCall();
            }
            return result;
        }
        return Memory.FALSE;
    }

    public Memory issetProperty(Environment env, TraceInfo trace, PHPObject object, String property)
            throws InvocationTargetException, IllegalAccessException {
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        Memory tmp = object.__dynamicProperties__ == null || accessFlag != 0
                ? null
                : object.__dynamicProperties__.getByScalar(property);

        if ( tmp != null )
            return tmp.isNull() ? tmp : Memory.TRUE;

        if (methodMagicIsset != null){
            Memory result;
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicIsset.getName(), name);
                result = methodMagicIsset.invokeDynamic(object, env, new StringMemory(property))
                        .toBoolean() ? Memory.TRUE : Memory.NULL;
            } finally {
                env.popCall();
            }
            return result;
        }

        return Memory.NULL;
    }

    public Memory getProperty(Environment env, TraceInfo trace,
                              PHPObject object, String property)
            throws InvocationTargetException, IllegalAccessException {
        ReferenceMemory value;
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (entity != null && accessFlag != 0) {
            value = null;
        } else
            value = object.__dynamicProperties__ == null ? null : object.__dynamicProperties__.getByScalar(property);

        if (value != null)
            return value;

        if (methodMagicGet != null) {
            Memory result;
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicGet.getName(), name);
                result = methodMagicGet.invokeDynamic(object, env, args);
            } finally {
                env.popCall();
            }
            return result;
        }

        if (accessFlag != 0)
            invalidAccessToProperty(env, trace, entity, accessFlag);

        return Memory.NULL;
    }

    private static interface SetterCallback {
        Memory invoke(Memory o1, Memory o2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassEntity)) return false;
        if (!super.equals(o)) return false;

        ClassEntity entity = (ClassEntity) o;

        if (id != entity.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
