package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.ObjectInvokeHelper;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;
import ru.regenix.jphp.runtime.reflection.support.Entity;

import java.lang.reflect.Constructor;
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
    public final Set<String> instanceOfList = new HashSet<String>();

    protected ClassEntity parent;
    protected DocumentComment docComment;

    protected boolean isAbstract = false;
    protected boolean isFinal = false;
    protected Type type = Type.CLASS;

    protected boolean isStatic;

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

                ClassAddResult result = addInterface(entity);
                result.check();
            }

            Class<?> extend = nativeClazz.getSuperclass();
            if (extend != null && !extend.isAnnotationPresent(Reflection.Ignore.class)){
                String name = extend.getSimpleName();
                if (extend.isAnnotationPresent(Reflection.Name.class)){
                    name = extend.getAnnotation(Reflection.Name.class).value();
                }
                ClassEntity entity = scope.findUserClass(name);
                if (entity == null || entity.getType() != Type.CLASS)
                    throw new IllegalArgumentException("Class '"+name+"' not registered");

                ClassAddResult result = addInterface(entity);
                result.check();
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

    public boolean isInstanceOf(ClassEntity what){
        return id == what.id || instanceOfList.contains(what.lowerName);
    }

    public boolean isInstanceOf(String name){
        String lowerName = name.toLowerCase();
        return instanceOfList.contains(lowerName) || this.lowerName.equals(lowerName);
    }

    public ClassAddResult updateParentMethods(){
        ClassAddResult result = new ClassAddResult();
        if (parent != null){
            for(MethodEntity method : parent.getMethods().values()){
                MethodEntity implMethod = findMethod(method.getLowerName());
                if (implMethod == null){
                    addMethod(method);
                } else {
                    implMethod.setPrototype(method);
                    if (!implMethod.equalsBySignature(method)){
                        result.invalidSignature.add(implMethod);
                    } else if (implMethod.isStatic() && !method.isStatic()){
                        result.mustNonStatic.add(implMethod);
                    } else if (!implMethod.isStatic() && method.isStatic()){
                        result.mustStatic.add(implMethod);
                    }
                }
            }
            doneDeclare();
        }
        return result;
    }

    public ClassAddResult setParent(ClassEntity parent) {
        this.parent = parent;
        this.methodCounts = parent.methodCounts;

        this.instanceOfList.add(parent.getLowerName());
        this.instanceOfList.addAll(parent.instanceOfList);

        return updateParentMethods();
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
    public ClassAddResult addInterface(ClassEntity _interface) {
        ClassAddResult result = new ClassAddResult();

        this.interfaces.put(_interface.getLowerName(), _interface);
        this.instanceOfList.add(_interface.getLowerName());
        this.instanceOfList.addAll(_interface.instanceOfList);

        for(MethodEntity method : _interface.getMethods().values()){
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == null){
                addMethod(method);
                if (type == Type.CLASS)
                    result.nonExists.add(method);
            } else {
                implMethod.setPrototype(method);

                if (!implMethod.equalsBySignature(method)){
                    result.invalidSignature.add(implMethod);
                } else if (implMethod.isStatic() && !method.isStatic()){
                    result.mustNonStatic.add(implMethod);
                } else if (!implMethod.isStatic() && method.isStatic()){
                    result.mustStatic.add(implMethod);
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

    public IObject newObject(Environment env, TraceInfo trace, Memory[] args)
            throws Throwable {
        IObject object = (IObject) nativeConstructor.newInstance(env, this);

        ArrayMemory props = object.getProperties();
        for(PropertyEntity property : getProperties()) {
            if (property.defaultValue != null)
                props.putAsKeyString(property.getName(), property.defaultValue.toImmutable());
        }

        if (methodConstruct != null){
            ObjectInvokeHelper.invokeMethod(object, methodConstruct, env, trace, args);
        }
        return object;
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
                              IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.plus(o2);
            }
        });
    }

    public Memory minusProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
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

    public Memory setProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, SetterCallback callback)
            throws Throwable {
        ReferenceMemory value;
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
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
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        if (props == null
                || accessFlag != 0
                || props.removeByScalar(property) == null ){
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

    public Memory emptyProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        if (props != null && accessFlag == 0){
            Memory tmp = props.getByScalar(property);
            if ( tmp != null ){
                return tmp.toBoolean() ? Memory.TRUE : Memory.NULL;
            } else
                return Memory.NULL;
        }

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

    public Memory issetProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        Memory tmp = props == null || accessFlag != 0
                ? null
                : props.getByScalar(property);

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
                              IObject object, String property)
            throws Throwable {
        ReferenceMemory value;
        PropertyEntity entity = properties.get(property);
        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (entity != null && accessFlag != 0) {
            value = null;
        } else {
            ArrayMemory props = object.getProperties();
            value = props == null ? null : props.getByScalar(property);
        }

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


    public class ClassAddResult {
        final List<MethodEntity> nonExists;
        final List<MethodEntity> invalidSignature;
        final List<MethodEntity> mustStatic;
        final List<MethodEntity> mustNonStatic;

        ClassAddResult() {
            this.nonExists = new ArrayList<MethodEntity>();
            this.invalidSignature = new ArrayList<MethodEntity>();
            this.mustStatic = new ArrayList<MethodEntity>();
            this.mustNonStatic = new ArrayList<MethodEntity>();
        }

        public Collection<MethodEntity> getNonExists() {
            return nonExists;
        }

        public Collection<MethodEntity> getInvalidSignature() {
            return invalidSignature;
        }

        public Collection<MethodEntity> getMustStatic() {
            return mustStatic;
        }

        public Collection<MethodEntity> getMustNonStatic() {
            return mustNonStatic;
        }

        public boolean valid(){
            return nonExists.isEmpty();
        }

        public void check(){
            check(null);
        }

        public void check(Environment env){
            if (!valid()){
                StringBuilder needs = new StringBuilder();
                Iterator<MethodEntity> iterator = getNonExists().iterator();
                int size = 0;
                while (iterator.hasNext()){
                    MethodEntity el = iterator.next();
                    needs.append(el.getClazz().getName())
                            .append("::")
                            .append(el.getName());

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
                else
                    env.triggerError(e);
            }

            for(MethodEntity el : getInvalidSignature()){
                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_INVALID_METHOD_SIGNATURE.fetch(
                                el.getSignatureString(true), el.getPrototype().getSignatureString(true)
                        ),
                        el.getTrace()
                );
                if (env == null)
                    throw e;
                else
                    env.triggerError(e);
            }

            for (MethodEntity el : getMustStatic()){
                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_CANNOT_MAKE_STATIC_TO_NON_STATIC.fetch(
                                el.getPrototype().getSignatureString(false),
                                ClassEntity.this.getName()
                        ),
                        el.getTrace()
                );
                if (env == null)
                    throw e;
                else
                    env.triggerError(e);
            }

            for (MethodEntity el : getMustNonStatic()){
                ErrorException e = new FatalException(
                        Messages.ERR_FATAL_CANNOT_MAKE_NON_STATIC_TO_STATIC.fetch(
                                el.getPrototype().getSignatureString(false),
                                ClassEntity.this.getName()
                        ),
                        el.getTrace()
                );
                if (env == null)
                    throw e;
                else
                    env.triggerError(e);
            }
        }
    }
}
