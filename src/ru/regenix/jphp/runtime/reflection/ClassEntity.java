package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.ObjectInvokeHelper;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.lang.support.MagicSignatureClass;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;
import ru.regenix.jphp.runtime.reflection.support.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

    protected final Map<String, MethodEntity> methods;
    public MethodEntity methodConstruct;
    public MethodEntity methodDestruct;

    public MethodEntity methodMagicSet;
    public MethodEntity methodMagicGet;
    public MethodEntity methodMagicUnset;
    public MethodEntity methodMagicIsset;
    public MethodEntity methodMagicCall;
    public MethodEntity methodMagicCallStatic;
    public MethodEntity methodMagicInvoke;
    public MethodEntity methodMagicToString;
    public MethodEntity methodMagicClone;

    protected MethodEntity constructor;

    protected final Map<String, ClassEntity> interfaces;
    protected final Map<String, ClassEntity> traits;

    public final Map<String, ConstantEntity> constants;
    public final Map<String, PropertyEntity> properties;
    public final Map<String, PropertyEntity> staticProperties;
    public final Set<String> instanceOfList = new HashSet<String>();

    protected ClassEntity parent;
    protected DocumentComment docComment;

    protected boolean isAbstract = false;
    protected boolean isFinal = false;
    protected Type type = Type.CLASS;

    protected boolean isStatic;

    protected static final ClassEntity magicSignatureClass = new ClassEntity(null, MagicSignatureClass.class);

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

    public ClassEntity(CompileScope scope, Class<?> nativeClazz){
        this(null, scope, nativeClazz);
    }

    public ClassEntity(Extension extension, CompileScope scope, Class<?> nativeClazz){
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

        Reflection.Signature signature = nativeClazz.getAnnotation(Reflection.Signature.class);
        if (signature != null){
            for(Reflection.Arg arg : signature.value()){
                PropertyEntity entity = new PropertyEntity(context);
                entity.setClazz(this);
                entity.setModifier(arg.modifier());
                entity.setName(arg.value());
                entity.setStatic(false);

                if (arg.optional().exists()){
                    entity.setDefaultValue(MemoryUtils.valueOf(arg.optional().value(), arg.optional().type()));
                } else {
                    entity.setDefaultValue(null);
                }

                addProperty(entity);
            }
        }

        if (signature == null || !signature.root()){
            for (Class<?> interface_ : nativeClazz.getInterfaces()){
                if (interface_.isAnnotationPresent(Reflection.Ignore.class)) continue;

                String name = interface_.getSimpleName();
                if (interface_.isAnnotationPresent(Reflection.Name.class)){
                    name = interface_.getAnnotation(Reflection.Name.class).value();
                }
                ClassEntity entity = scope.findUserClass(name);
                if (entity == null || entity.getType() != Type.INTERFACE)
                    throw new IllegalArgumentException("Interface '"+name+"' not registered");

                MethodsResult result = addInterface(entity);
                result.check();
            }

            if (scope != null){
                Class<?> extend = nativeClazz.getSuperclass();
                if (extend != null && !extend.isAnnotationPresent(Reflection.Ignore.class)){
                    String name = extend.getSimpleName();
                    if (extend.isAnnotationPresent(Reflection.Name.class)){
                        name = extend.getAnnotation(Reflection.Name.class).value();
                    }
                    ClassEntity entity = scope.findUserClass(name);
                    if (entity == null || entity.getType() != Type.CLASS)
                        throw new IllegalArgumentException("Class '"+name+"' not registered");

                    ParentResult result = setParent(entity);
                    result.check(null);
                }
            }
        }

        for (Method method : nativeClazz.getDeclaredMethods()){
            if (method.isAnnotationPresent(Reflection.Signature.class)){
                MethodEntity entity = new MethodEntity(extension, method);
                entity.setClazz(this);
                entity.setNativeMethod(method);

                Reflection.Name name = method.getAnnotation(Reflection.Name.class);
                entity.setName(name == null ? method.getName() : name.value());

                Reflection.Signature sign = method.getAnnotation(Reflection.Signature.class);
                ParameterEntity[] params = new ParameterEntity[sign.value().length];

                int i = 0;
                for (Reflection.Arg arg : sign.value()){
                    ParameterEntity param = new ParameterEntity(context);
                    param.setMethod(entity);
                    param.setReference(arg.reference());
                    param.setType(arg.type());
                    param.setName(arg.value());

                    if (arg.optional().exists() || !arg.value().isEmpty() || arg.type() != HintType.ANY){
                        param.setDefaultValue(MemoryUtils.valueOf(arg.optional().value(), arg.optional().type()));
                    }

                    params[i++] = param;
                }

                entity.setParameters(params);
                addMethod(entity);
            }
        }

        this.setNativeClazz(nativeClazz);
        this.isInternal = true;
        doneDeclare();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
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
        if (methodConstruct != null)
            methodConstruct.setDynamicSignature(true);

        methodDestruct = methods.get("__destruct");

        methodMagicSet   = methods.get("__set");
        methodMagicGet   = methods.get("__get");
        methodMagicUnset = methods.get("__unset");
        methodMagicIsset = methods.get("__isset");
        methodMagicCall  = methods.get("__call");
        methodMagicCallStatic = methods.get("__callstatic");

        methodMagicInvoke = methods.get("__invoke");
        methodMagicToString = methods.get("__tostring");
        methodMagicClone = methods.get("__clone");
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

    public MethodsResult addMethod(MethodEntity method){
        String name = method.getLowerName();

        MethodsResult addResult = new MethodsResult();
        if (method.isAbstract && method.isFinal){
            addResult.finalAbstractMethods.add(ResultItem.error(method));
        } else if (method.isAbstractable() && !(method.isAbstract || type == Type.INTERFACE)){
            addResult.nonAbstract.add(ResultItem.error(method));
        } else if (method.isAbstract && !method.isAbstractable()){
            addResult.nonAbstractable.add(ResultItem.error(method));
        } else if (method.isAbstract && !this.isAbstract){
            addResult.nonExists.add(ResultItem.error(method));
        } else if (type == Type.INTERFACE &&
                (method.modifier != Modifier.PUBLIC || method.isFinal)){
            addResult.invalidAccessInterfaceMethods.add(ResultItem.error(method));
        }

        if (magicSignatureClass != null){
            MethodEntity systemMethod = magicSignatureClass.findMethod(name.toLowerCase());
            if (systemMethod != null && method.clazz.getId() == getId()) {
                if (method.prototype == null)
                    method.setPrototype(systemMethod);

                if (systemMethod.getModifier() == Modifier.PUBLIC && method.getModifier() != Modifier.PUBLIC){
                    addResult.mustBePublic.add(ResultItem.warning(method));
                    method.setModifier(Modifier.PUBLIC);
                }

                if (!systemMethod.equalsBySignature(method)){
                    addResult.invalidSignature.add(ResultItem.error(method));
                } else if (systemMethod.isStatic && !method.isStatic)
                    addResult.mustStatic.add(ResultItem.error(method));
                else if (!systemMethod.isStatic && method.isStatic){
                    //method.setStatic(false);
                    addResult.mustNonStatic.add(ResultItem.warning(method));
                }
            }
        }

        this.methods.put(name, method);
        if (name.equals(lowerName)){
            methods.put("__construct", method);
        }

        return addResult;
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

    public boolean isInstanceOf(ClassEntity what){
        return what != null && (id == what.id || instanceOfList.contains(what.lowerName));
    }

    public boolean isInstanceOf(String name){
        String lowerName = name.toLowerCase();
        return instanceOfList.contains(lowerName) || this.lowerName.equals(lowerName);
    }

    public MethodsResult updateParentMethods(){
        MethodsResult result = new MethodsResult();
        if (parent != null){
            for(Map.Entry<String, MethodEntity> entry : parent.getMethods().entrySet()){
                MethodEntity implMethod = findMethod(entry.getKey());
                MethodEntity method = entry.getValue();
                if (implMethod == method) continue;

                if (implMethod == null){
                    MethodsResult addResult = addMethod(method);
                    result.invalidSignature.addAll(addResult.invalidSignature);
                    result.mustStatic.addAll(addResult.mustStatic);
                    result.mustNonStatic.addAll(addResult.mustNonStatic);
                    result.nonExists.addAll(addResult.nonExists);
                    result.mustBePublic.addAll(addResult.mustBePublic);
                } else {
                    implMethod.setPrototype(method);
                    if (method.isFinal)
                        result.finalMethods.add(ResultItem.error(implMethod));

                    if (!isAbstract && method.isAbstract && implMethod.isAbstract)
                        result.nonExists.add(ResultItem.error(implMethod));

                    if (!method.dynamicSignature && !implMethod.equalsBySignature(method)){
                        result.invalidSignature.add(ResultItem.error(implMethod));
                    } else if (implMethod.isStatic() && !method.isStatic()){
                        result.mustNonStatic.add(ResultItem.error(implMethod));
                    } else if (!implMethod.isStatic() && method.isStatic()){
                        result.mustStatic.add(ResultItem.error(implMethod));
                    }
                }
            }
            doneDeclare();
        }
        return result;
    }

    public ParentResult setParent(ClassEntity parent) {
        return setParent(parent, true);
    }

    public ParentResult setParent(ClassEntity parent, boolean updateParentMethods) {
        ParentResult result = new ParentResult(parent);

        if (this.parent != null){
            throw new RuntimeException("Cannot re-assign parent for classes");
        }

        this.parent = parent;
        if (parent != null){
            this.methodCounts = parent.methodCounts;

            this.instanceOfList.add(parent.getLowerName());
            this.instanceOfList.addAll(parent.instanceOfList);

            this.properties.putAll(parent.properties);
            this.staticProperties.putAll(parent.staticProperties);
            this.constants.putAll(parent.constants);
        }

        if (updateParentMethods)
            result.methods = updateParentMethods();

        return result;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * return empty list if success else list with not valid implemented methods in class
     * @param _interface
     * @return
     */
    public MethodsResult addInterface(ClassEntity _interface) {
        MethodsResult result = new MethodsResult();

        this.constants.putAll(_interface.constants);

        this.interfaces.put(_interface.getLowerName(), _interface);
        this.instanceOfList.add(_interface.getLowerName());
        this.instanceOfList.addAll(_interface.instanceOfList);

        for(MethodEntity method : _interface.getMethods().values()){
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == method) continue;

            if (implMethod == null){
                addMethod(method);
                if (type == Type.CLASS && !isAbstract)
                    result.nonExists.add(ResultItem.error(method));
            } else {
                implMethod.setPrototype(method);

                if (!method.isDynamicSignature() && !implMethod.equalsBySignature(method)){
                    result.invalidSignature.add(ResultItem.error(implMethod));
                } else if (implMethod.isStatic() && !method.isStatic()){
                    result.mustNonStatic.add(ResultItem.error(implMethod));
                } else if (!implMethod.isStatic() && method.isStatic()){
                    result.mustStatic.add(ResultItem.error(implMethod));
                }
            }
        }
        return result;
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
        if (property.isStatic()) {
            PropertyEntity prototype = staticProperties.get(property.getLowerName());
            if (prototype != null && prototype.getModifier() != property.getModifier()){
                property.setPrototype(prototype);
            }

            staticProperties.put(property.getLowerName(), property);
        } else {
            PropertyEntity prototype = properties.get(property.getLowerName());
            if (prototype != null && prototype.getModifier() != property.getModifier()){
                property.setPrototype(prototype);
            }

            properties.put(property.getLowerName(), property);
        }
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
            case 1: env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_FATAL_ACCESS_TO_PROTECTED_PROPERTY.fetch(
                            entity.getClazz().getName(), entity.getName()
                    ));
            case 2: env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_FATAL_ACCESS_TO_PRIVATE_PROPERTY.fetch(
                            entity.getClazz().getName(), entity.getName()
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

    public IObject newObjectWithoutConstruct(Environment env) throws Throwable {
        IObject object;
        try {
            object = (IObject) nativeConstructor.newInstance(env, this);
        } catch (InvocationTargetException e){
            throw e.getCause();
        }
        return object;
    }

    public IObject newMock(Environment env) throws Throwable {
        IObject object = (IObject) nativeConstructor.newInstance(env, this);
        object.setAsMock();
        return object;
    }

    public IObject newObject(Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        if (isAbstract){
            env.error(trace, "Cannot instantiate abstract class %s", name);
        } else if (type == Type.INTERFACE)
            env.error(trace, "Cannot instantiate interface %s", name);

        IObject object;
        try {
            object = (IObject) nativeConstructor.newInstance(env, this);
        } catch (InvocationTargetException e){
            throw e.getCause();
        }
        ArrayMemory props = object.getProperties();

        for(PropertyEntity property : getProperties()) {
            if (id == property.clazz.getId() && property.defaultValue != null){
                props.putAsKeyString(
                        property.getSpecificName(),
                        property.defaultValue.toImmutable()
                );
            }
        }

        ClassEntity tmp = parent;
        while (tmp != null){
            long otherId = tmp.getId();
            for(PropertyEntity property : tmp.getProperties()) {
                if (property.getClazz().getId() == otherId && property.defaultValue != null){
                    props.getByScalarOrCreate(
                            property.getSpecificName(),
                            property.defaultValue.toImmutable()
                    );
                }
            }
            tmp = tmp.parent;
        }

        if (methodConstruct != null){
            ObjectInvokeHelper.invokeMethod(object, methodConstruct, env, trace, args);
        }
        return object;
    }

    public IObject cloneObject(IObject value, Environment env, TraceInfo trace) throws Throwable {
        IObject copy = this.newObjectWithoutConstruct(env);
        copy.getProperties().putAll(value.getProperties());
        if (methodMagicClone != null){
            ObjectInvokeHelper.invokeMethod(copy, methodMagicClone, env, trace, null);
        }

        return copy;
    }

    public Memory concatProperty(Environment env, TraceInfo trace,
                               IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return new StringMemory(o1.concat(o2));
            }
        });
    }

    public Memory plusProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, final ReferenceMemory oldValue)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                if (oldValue != null)
                    oldValue.assign(o1);

                return o1.plus(o2);
            }
        });
    }

    public Memory minusProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, final ReferenceMemory oldValue)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                if (oldValue != null)
                    oldValue.assign(o1);

                return o1.minus(o2);
            }
        });
    }

    public Memory mulProperty(Environment env, TraceInfo trace,
                                IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.mul(o2);
            }
        });
    }

    public Memory divProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.div(o2);
            }
        });
    }

    public Memory modProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.mod(o2);
            }
        });
    }

    public Memory bitAndProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitAnd(o2);
            }
        });
    }

    public Memory bitOrProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitOr(o2);
            }
        });
    }

    public Memory bitXorProperty(Environment env, TraceInfo trace,
                                IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitXor(o2);
            }
        });
    }

    public Memory bitShrProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback(){
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitShr(o2);
            }
        });
    }

    public Memory bitShlProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitShl(o2);
            }
        });
    }

    public void appendProperty(IObject object, String property, Memory value){
        object.getProperties().put(property, value);
    }

    public Memory refOfProperty(ArrayMemory props, String name){
        PropertyEntity entity = properties.get(name);
        return props.refOfIndex(entity == null ? name : entity.getSpecificName());
    }

    public Memory setProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, SetterCallback callback)
            throws Throwable {
        ReferenceMemory value;
        ClassEntity contex = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(contex) ? contex.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        value = props == null || accessFlag != 0 ? null : props.getByScalar(entity == null ? property : entity.specificName);

        if (value == null) {
            if (methodMagicSet != null) {
                StringMemory memoryProperty = new StringMemory(property);

                if (callback != null){
                    Memory o1 = Memory.NULL;
                    if (methodMagicGet != null) {
                        try {
                            Memory[] args = new Memory[]{memoryProperty};
                            env.pushCall(
                                    trace, object, args, methodMagicGet.getName(), methodMagicSet.getClazz().getName(), name
                            );
                            o1 = methodMagicGet.invokeDynamic(object, env, memoryProperty);
                        } finally {
                            env.popCall();
                        }
                    }
                    memory = callback.invoke(o1, memory);
                }

                try {
                    Memory[] args = new Memory[]{memoryProperty, memory};
                    env.pushCall(trace, object, args, methodMagicSet.getName(), methodMagicSet.getClazz().getName(), name);
                    methodMagicSet.invokeDynamic(object, env, args);
                } finally {
                    env.popCall();
                }
            } else {
                if (accessFlag != 0) {
                    invalidAccessToProperty(env, trace, entity, accessFlag);
                    return Memory.NULL;
                }

                if (callback != null)
                    memory = callback.invoke(Memory.NULL, memory);

                return props == null ? Memory.NULL : object.getProperties().refOfIndex(property).assign(memory);
            }
        } else {
            if (callback != null)
                memory = callback.invoke(value, memory);

            return value.assign(memory);
        }
        return memory;
    }

    public Memory unsetProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        ClassEntity context = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(context) ? context.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        if (props == null
                || accessFlag != 0
                || props.removeByScalar(entity == null ? property : entity.specificName) == null ){
            if (methodMagicUnset != null) {
                try {
                    Memory[] args = new Memory[]{new StringMemory(property)};
                    env.pushCall(trace, object, args, methodMagicUnset.getName(), methodMagicUnset.getClazz().getName(), name);
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

    public Memory emptyProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        ClassEntity contex = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(contex) ? contex.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        if (props != null && accessFlag == 0){
            Memory tmp = props.getByScalar(entity == null ? property : entity.specificName);
            if ( tmp != null ){
                return tmp.toBoolean() ? Memory.TRUE : Memory.NULL;
            } else
                return Memory.NULL;
        }

        if (methodMagicIsset != null){
            Memory result;
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicIsset.getName(), methodMagicIsset.getClazz().getName(), name);
                result = methodMagicIsset.invokeDynamic(object, env, new StringMemory(property))
                        .toBoolean() ? Memory.TRUE : Memory.NULL;
            } finally {
                env.popCall();
            }
            return result;
        }
        return Memory.NULL;
    }

    public Memory issetProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        ClassEntity contex = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(contex) ? contex.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        Memory tmp = props == null || accessFlag != 0
                ? null
                : props.getByScalar(entity == null ? property : entity.specificName);

        if ( tmp != null )
            return tmp.isNull() ? tmp : Memory.TRUE;

        if (methodMagicIsset != null){
            Memory result;
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicIsset.getName(), methodMagicIsset.getClazz().getName(), name);
                result = methodMagicIsset.invokeDynamic(object, env, new StringMemory(property))
                        .toBoolean() ? Memory.TRUE : Memory.NULL;
            } finally {
                env.popCall();
            }
            return result;
        }

        return Memory.NULL;
    }

    public Memory getStaticProperty(Environment env, TraceInfo trace, String property)
            throws Throwable {
        ClassEntity context = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(context)
                ? context.staticProperties.get(property) : staticProperties.get(property);

        if (entity == null){
            env.error(trace, Messages.ERR_FATAL_ACCESS_TO_UNDECLARED_STATIC_PROPERTY.fetch(name, property));
            return Memory.NULL;
        }

        int accessFlag = entity.canAccess(env);
        if (accessFlag != 0) {
            invalidAccessToProperty(env, trace, entity, accessFlag);
            return Memory.NULL;
        }

        return env.getOrCreateStatic(entity.specificName, entity.getDefaultValue().toImmutable());
    }

    public Memory getProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        ReferenceMemory value;
        ClassEntity context = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(context) ? context.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (entity != null && accessFlag != 0) {
            value = null;
        } else {
            ArrayMemory props = object.getProperties();
            value = props == null ? null : props.getByScalar(entity == null ? property : entity.specificName);
        }

        if (value != null)
            return value;

        if (methodMagicGet != null) {
            Memory result;
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicGet.getName(), methodMagicGet.getClazz().getName(), name);
                result = methodMagicGet.invokeDynamic(object, env, args);
            } finally {
                env.popCall();
            }
            return result;
        }

        if (accessFlag != 0)
            invalidAccessToProperty(env, trace, entity, accessFlag);

        env.error(trace, ErrorType.E_NOTICE, Messages.ERR_NOTICE_UNDEFINED_PROPERTY, name, property);
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


    public static class ResultItem {
        public final MethodEntity method;
        public final ErrorType errorType;

        protected ResultItem(MethodEntity method, ErrorType errorType) {
            this.method = method;
            this.errorType = errorType;
        }

        public static ResultItem warning(MethodEntity method){
            return new ResultItem(method, ErrorType.E_WARNING);
        }

        public static ResultItem error(MethodEntity method){
            return new ResultItem(method, ErrorType.E_ERROR);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ResultItem)) return false;

            ResultItem that = (ResultItem) o;

            if (!method.equals(that.method)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return method.hashCode();
        }
    }

    public class ParentResult {
        ClassEntity parent;
        MethodsResult methods;

        ParentResult(ClassEntity parent) {
            this.parent = parent;
        }

        public void check(Environment env){
            if (parent != null){
                if (parent.isFinal){
                    FatalException e = new FatalException(
                            Messages.ERR_FATAL_CLASS_MAY_NOT_INHERIT_FINAL_CLASS.fetch(ClassEntity.this.getName(), parent.getName()),
                            ClassEntity.this.trace
                    );
                    if (env != null)
                        env.error(e.getTraceInfo(), e.getType(), e.getMessage());
                    else
                        throw e;
                }
                if (ClassEntity.this.type == Type.CLASS && parent.type != Type.CLASS){
                    FatalException e = new FatalException(
                            Messages.ERR_FATAL_CANNOT_EXTENDS.fetch(ClassEntity.this.getName(), parent.getName()),
                            ClassEntity.this.trace
                    );
                    if (env != null)
                        env.error(e.getTraceInfo(), e.getType(), e.getMessage());
                    else
                        throw e;
                }
            }
            if (methods != null)
                methods.check(env);
        }
    }

    public class MethodsResult {
        final Set<ResultItem> nonExists;
        final Set<ResultItem> invalidSignature;
        final Set<ResultItem> mustStatic;
        final Set<ResultItem> mustNonStatic;
        final Set<ResultItem> mustBePublic;
        final Set<ResultItem> finalMethods;
        final Set<ResultItem> nonAbstract;
        final Set<ResultItem> nonAbstractable;
        final Set<ResultItem> invalidAccessInterfaceMethods;
        final Set<ResultItem> finalAbstractMethods;

        MethodsResult() {
            this.nonExists = new HashSet<ResultItem>();
            this.invalidSignature = new HashSet<ResultItem>();
            this.mustStatic = new HashSet<ResultItem>();
            this.mustNonStatic = new HashSet<ResultItem>();
            this.mustBePublic = new HashSet<ResultItem>();
            this.finalMethods = new HashSet<ResultItem>();
            this.nonAbstract = new HashSet<ResultItem>();
            this.nonAbstractable = new HashSet<ResultItem>();
            this.invalidAccessInterfaceMethods = new HashSet<ResultItem>();
            this.finalAbstractMethods = new HashSet<ResultItem>();
        }

        public Collection<ResultItem> getNonExists() {
            return nonExists;
        }

        public Collection<ResultItem> getInvalidSignature() {
            return invalidSignature;
        }

        public Collection<ResultItem> getMustStatic() {
            return mustStatic;
        }

        public Collection<ResultItem> getMustNonStatic() {
            return mustNonStatic;
        }

        public boolean valid(){
            return nonExists.isEmpty();
        }

        public void check(){
            check(null);
        }

        private void checkItems(Environment env, Collection<ResultItem> items, Messages.Item message){
            checkItems(env, items, message, false);
        }

        private void checkItems(Environment env, Collection<ResultItem> items, Messages.Item message, boolean prototype){
            for(ResultItem el : items){
                ErrorException e;
                if (prototype)
                    e = new FatalException(
                            message.fetch(
                                    el.method.getPrototype().getSignatureString(false),
                                    el.method.getSignatureString(false)
                            ),
                            el.method.getTrace()
                    );

                else
                     e = new FatalException(
                            message.fetch(el.method.getSignatureString(false)),
                            el.method.getTrace()
                    );
                if (env == null)
                    throw e;
                else
                    env.error(e.getTraceInfo(), el.errorType, e.getMessage());
            }
        }

        public void check(Environment env){
            if (!valid()){
                StringBuilder needs = new StringBuilder();
                Iterator<ResultItem> iterator = getNonExists().iterator();
                int size = 0;
                ErrorType errorType = ErrorType.E_NOTICE;

                while (iterator.hasNext()) {
                    ResultItem el = iterator.next();
                    if (el.errorType.value < errorType.value)
                        errorType = el.errorType;

                    needs.append(el.method.getClazz().getName())
                            .append("::")
                            .append(el.method.getName());

                    if (iterator.hasNext())
                        needs.append(", ");
                    size++;
                }

                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_IMPLEMENT_METHOD.fetch(ClassEntity.this.getName(), size, needs),
                        ClassEntity.this.getTrace()
                );

                if (env == null)
                    throw e;
                else {
                    env.error(e.getTraceInfo(), errorType, e.getMessage());
                }
            }

            for(ResultItem el : getInvalidSignature()){
                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_INVALID_METHOD_SIGNATURE.fetch(
                                el.method.getSignatureString(true), el.method.getPrototype().getSignatureString(true)
                        ),
                        el.method.getTrace()
                );
                if (env == null)
                    throw e;
                else
                    env.error(e.getTraceInfo(), el.errorType, e.getMessage());
            }

            for (ResultItem el : getMustStatic()){
                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_CANNOT_MAKE_STATIC_TO_NON_STATIC.fetch(
                                el.method.getPrototype().getSignatureString(false),
                                ClassEntity.this.getName()
                        ),
                        el.method.getTrace()
                );
                if (env == null)
                    throw e;
                else
                    env.error(e.getTraceInfo(), el.errorType, e.getMessage());
            }

            for (ResultItem el : getMustNonStatic()){
                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_CANNOT_MAKE_NON_STATIC_TO_STATIC.fetch(
                                el.method.getPrototype().getSignatureString(false),
                                ClassEntity.this.getName()
                        ),
                        el.method.getTrace()
                );
                if (env == null)
                    throw e;
                else
                    env.error(e.getTraceInfo(), el.errorType, e.getMessage());
            }

            for (ResultItem el : mustBePublic){
                if (env != null)
                    env.error(el.method.getTrace(), el.errorType,
                            "The magic method %s must have public visibility", el.method.getSignatureString(false)
                    );
            }

            checkItems(env, finalAbstractMethods, Messages.ERR_FATAL_CANNOT_USE_FINAL_ON_ABSTRACT);
            checkItems(env, finalMethods, Messages.ERR_FATAL_CANNOT_OVERRIDE_FINAL_METHOD, true);
            checkItems(env, nonAbstract, Messages.ERR_FATAL_NON_ABSTRACT_METHOD_MUST_CONTAIN_BODY);
            checkItems(env, nonAbstractable, Messages.ERR_FATAL_ABSTRACT_METHOD_CANNOT_CONTAIN_BODY);
            checkItems(env, invalidAccessInterfaceMethods, Messages.ERR_FATAL_ACCESS_TYPE_FOR_INTERFACE_METHOD);
        }
    }
}
