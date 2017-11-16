package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.Messages;
import php.runtime.common.Modifier;
import php.runtime.env.ConcurrentEnvironment;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.FatalException;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.Extension;
import php.runtime.invoke.InvokeArgumentHelper;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.PropertyCallCache;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.support.MagicSignatureClass;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.ArrayMapEntryMemory;
import php.runtime.reflection.support.Entity;
import php.runtime.reflection.support.ReflectionUtils;
import php.runtime.reflection.support.TypeChecker;
import php.runtime.wrap.ClassWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ClassEntity extends Entity implements Cloneable {
    private final static int FLAG_GET = 4000;
    private final static int FLAG_SET = 4001;
    private final static int FLAG_ISSET = 4002;
    private final static int FLAG_UNSET = 4003;

    // types
    public enum Type {
        CLASS, INTERFACE, TRAIT, CLOSURE, FUNCTION, GENERATOR
    }

    private long id;

    protected int methodCounts = 0;
    protected boolean isInternal;
    protected boolean isNotRuntime;

    protected Extension extension;
    protected Class<?> nativeClazz;

    protected Constructor nativeConstructor;
    protected Method nativeInitEnvironment;

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
    public MethodEntity methodMagicSleep;
    public MethodEntity methodMagicWakeup;
    public MethodEntity methodMagicDebugInfo;

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

    protected static final ClassEntity magicSignatureClass =
            new ClassEntity(new ClassWrapper(null, MagicSignatureClass.class));

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

    public ClassEntity(ClassWrapper wrapper) {
        this((Context) null);
        wrapper.onWrap(this);
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public String getCompiledInternalName() {
        return super.getInternalName();
    }

    @Override
    public String getInternalName() {
        /*if (isTrait()) { depricated check, todo remove.
            throw new CriticalException("Disable of using internal names for traits, trait '" + getName() + "'");
        }   */

        return super.getInternalName();
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

    public boolean isTrait() {
        return type == Type.TRAIT;
    }

    public void setInternal(boolean isInternal) {
        this.isInternal = isInternal;
    }

    public boolean isNotRuntime() {
        return isNotRuntime;
    }

    public void setNotRuntime(boolean isNotRuntime) {
        this.isNotRuntime = isNotRuntime;
    }

    public void doneDeclare() {
        switch (getType()) {
            case CLOSURE:
            case GENERATOR:
            case CLASS:
                methodConstruct = methods.get("__construct");
                if (methodConstruct != null
                        && (methodConstruct.getPrototype() == null || !methodConstruct.getPrototype().isAbstractable()))
                    methodConstruct.setDynamicSignature(true);

                methodDestruct = methods.get("__destruct");

                methodMagicSet = methods.get("__set");
                methodMagicGet = methods.get("__get");
                methodMagicUnset = methods.get("__unset");
                methodMagicIsset = methods.get("__isset");
                methodMagicCall = methods.get("__call");
                methodMagicCallStatic = methods.get("__callstatic");

                methodMagicInvoke = methods.get("__invoke");
                methodMagicToString = methods.get("__tostring");
                methodMagicClone = methods.get("__clone");

                methodMagicSleep = methods.get("__sleep");
                methodMagicWakeup = methods.get("__wakeup");

                methodMagicDebugInfo = methods.get("__debuginfo");
                break;
        }
    }

    /*
ClassReader classReader;
            if (data != null)
                classReader = new ClassReader(data);
            else {
                try {
                    classReader = new ClassReader(nativeClazz.getName());
                } catch (IOException e) {
                    throw new CriticalException(e);
                }
            }
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            return cachedClassNode = classNode;
     */

    public Extension getExtension() {
        return extension;
    }

    public boolean isDeprecated() {
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

    public boolean isInterface() {
        return type == Type.INTERFACE;
    }

    public boolean isClass() {
        return type == Type.CLASS;
    }

    public boolean isHiddenInCallStack() {
        return false;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, MethodEntity> getMethods() {
        return methods;
    }

    public int getMethodCounts() {
        return methodCounts;
    }

    public void __setMethodCounts(int methodCounts) {
        this.methodCounts = methodCounts;
    }

    public List<MethodEntity> getOwnedMethods() {
        List<MethodEntity> result = new ArrayList<MethodEntity>();
        for (MethodEntity el : methods.values()) {
            if (el.isOwned(this))
                result.add(el);
        }
        return result;
    }

    public SignatureResult addMethod(MethodEntity method, String realName) {
        String name = realName == null ? method.getLowerName() : realName;

        SignatureResult addResult = new SignatureResult();
        if (method.isAbstract && method.isFinal) {
            addResult.add(InvalidMethod.error(InvalidMethod.Kind.FINAL_ABSTRACT, method));
        } else if (method.isAbstractable() && !(method.isAbstract || type == Type.INTERFACE)) {
            addResult.add(InvalidMethod.error(InvalidMethod.Kind.NON_ABSTRACT, method));
        } else if (method.isAbstract && !method.isAbstractable()) {
            addResult.add(InvalidMethod.error(InvalidMethod.Kind.NON_ABSTRACTABLE, method));
        } else if (method.isAbstract && !(this.isAbstract || isTrait())) {
            addResult.add(InvalidMethod.error(InvalidMethod.Kind.NON_EXISTS, method));
        } else if (type == Type.INTERFACE &&
                (method.modifier != Modifier.PUBLIC || method.isFinal)) {
            addResult.add(InvalidMethod.error(InvalidMethod.Kind.INVALID_ACCESS_FOR_INTERFACE, method));
        }

        if (magicSignatureClass != null) {
            MethodEntity systemMethod = magicSignatureClass.findMethod(name.toLowerCase());
            if (systemMethod != null && method.clazz.getId() == getId()) {
                if (method.prototype == null)
                    method.setPrototype(systemMethod);

                if (systemMethod.getModifier() == Modifier.PUBLIC && method.getModifier() != Modifier.PUBLIC) {
                    addResult.add(InvalidMethod.warning(InvalidMethod.Kind.MAGIC_MUST_BE_PUBLIC, method));
                    //method.setModifier(Modifier.PUBLIC);
                }

                if (!systemMethod.equalsBySignature(method, false)) {
                    addResult.add(InvalidMethod.error(InvalidMethod.Kind.INVALID_SIGNATURE, method));
                } else if (systemMethod.isStatic && !method.isStatic)
                    addResult.add(InvalidMethod.warning(InvalidMethod.Kind.MUST_STATIC, method));
                else if (!systemMethod.isStatic && method.isStatic) {
                    //method.setStatic(false);
                    addResult.add(InvalidMethod.warning(InvalidMethod.Kind.MUST_NON_STATIC, method));
                }
            }
        }

        if (parent == null || (!name.equals(parent.lowerName) || !methods.containsKey(name))) {
            this.methods.put(name, method);
        }

        if (name.equals(lowerName) && !isTrait()) {
            methods.put("__construct", method);
        }

        return addResult;
    }

    public int nextMethodIndex() {
        return methodCounts++;
    }

    public MethodEntity findMethod(String name) {
        return methods.get(name);
    }

    public ConstantEntity findConstant(String name) {
        return constants.get(name);
    }

    // use can pass specific names
    public PropertyEntity findProperty(String name) {
        int pos = name.lastIndexOf('\0');
        if (pos > -1 && pos + 1 < name.length())
            name = name.substring(pos + 1);

        return properties.get(name);
    }

    public PropertyEntity findStaticProperty(String name) {
        return staticProperties.get(name);
    }

    public ClassEntity getParent() {
        return parent;
    }

    public boolean isInstanceOf(Class<? extends IObject> clazz) {
        return isInstanceOf(ReflectionUtils.getClassName(clazz));
    }

    public boolean isInstanceOf(ClassEntity what) {
        return what != null && (id == what.id || instanceOfList.contains(what.lowerName));
    }

    public boolean isInstanceOf(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        String lowerName = name.toLowerCase();
        return instanceOfList.contains(lowerName) || this.lowerName.equals(lowerName);
    }

    public boolean isInstanceOfLower(String lowerName) {
        if (lowerName == null)
            throw new IllegalArgumentException();

        return instanceOfList.contains(lowerName) || this.lowerName.equals(lowerName);
    }

    public SignatureResult updateParentBody() {
        SignatureResult result = new SignatureResult();

        if (parent != null) {
            for (ConstantEntity constant : parent.getConstants()) {
                ConstantEntity implConstant = findConstant(constant.getName());

                if (implConstant == constant) continue;

                if (implConstant != null) {
                    if (constant.isPublic() && !implConstant.isPublic()) {
                        result.addConstant(InvalidConstant.error(InvalidConstant.Kind.MUST_BE_PUBLIC, implConstant, constant));
                    } else if (constant.isProtected() && implConstant.isPrivate()) {
                        result.addConstant(InvalidConstant.error(InvalidConstant.Kind.MUST_BE_PROTECTED, implConstant, constant));
                    }
                }
            }

            for (Map.Entry<String, MethodEntity> entry : parent.getMethods().entrySet()) {
                MethodEntity implMethod = findMethod(entry.getKey());
                MethodEntity method = entry.getValue();
                if (implMethod == method) continue;

                if (implMethod == null) {
                    SignatureResult addResult = addMethod(method, entry.getKey());
                    if (methodConstruct == null && !isTrait() && method.getName().equalsIgnoreCase(parent.getName())) {
                        if (!method.isAbstractable() && !methods.containsKey("__construct")) {
                            method.setDynamicSignature(true);
                            methods.put("__construct", method);
                        }
                    }

                    result.methods.addAll(addResult.methods);
                } else {
                    implMethod.setPrototype(method);
                    if (method.isFinal)
                        result.add(InvalidMethod.error(InvalidMethod.Kind.FINAL, implMethod));

                    if (!isAbstract && method.isAbstract && implMethod.isAbstract)
                        result.add(InvalidMethod.error(InvalidMethod.Kind.NON_EXISTS, implMethod));

                    if (!implMethod.equalsBySignature(method)) {
                        if (!method.isDynamicSignature() || method.isAbstractable()) {
                            boolean isWarning = true;
                            MethodEntity pr = method;

                            while (pr != null) {
                                if (pr.isAbstractable()) {
                                    isWarning = false;
                                    break;
                                }
                                pr = method.getPrototype();
                            }

                            if (isWarning) {
                                TypeChecker implTypeChecker = implMethod.getReturnTypeChecker();
                                TypeChecker typeChecker = method.getReturnTypeChecker();

                                if (typeChecker != implTypeChecker && (typeChecker == null || implTypeChecker == null)) {
                                    isWarning = false;
                                } else if (typeChecker != null &&
                                        !typeChecker.getSignature().equals(implTypeChecker.getSignature())) {
                                    isWarning = false;
                                }
                            }

                            result.add(
                                    !isWarning
                                            ? InvalidMethod.error(InvalidMethod.Kind.INVALID_SIGNATURE, implMethod)
                                            : InvalidMethod.warning(InvalidMethod.Kind.INVALID_SIGNATURE, implMethod)
                            );
                        }
                    } else if (implMethod.isStatic() && !method.isStatic()) {
                        result.add(InvalidMethod.error(InvalidMethod.Kind.MUST_NON_STATIC, implMethod));
                    } else if (!implMethod.isStatic() && method.isStatic()) {
                        result.add(InvalidMethod.error(InvalidMethod.Kind.MUST_STATIC, implMethod));
                    }

                    if (method.isPublic() && !implMethod.isPublic())
                        result.add(InvalidMethod.error(InvalidMethod.Kind.MUST_BE_PUBLIC, implMethod));
                    else if (method.isProtected() && implMethod.isPrivate())
                        result.add(InvalidMethod.error(InvalidMethod.Kind.MUST_BE_PROTECTED, implMethod));
                }
            }

            doneDeclare();
        }
        return result;
    }

    public ExtendsResult setParent(ClassEntity parent) {
        return setParent(parent, true);
    }

    public ExtendsResult resetParent(ClassEntity parent, boolean updateParentMethods) {
        ExtendsResult result = new ExtendsResult(parent);

        if (this.parent == parent) {
            return result;
        }

        this.parent = parent;
        this.instanceOfList.clear();

        if (parent != null) {
            if (parent.useJavaLikeNames) {
                this.useJavaLikeNames = true;
            }

            this.methodCounts = parent.methodCounts + this.methods.size();

            this.instanceOfList.add(parent.getLowerName());
            this.instanceOfList.addAll(parent.instanceOfList);
            this.interfaces.putAll(parent.interfaces);

            this.properties.putAll(parent.properties);
            this.staticProperties.putAll(parent.staticProperties);

            for (Map.Entry<String, ConstantEntity> entry : parent.constants.entrySet()) {
                if (!entry.getValue().isPrivate()) {
                    this.constants.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (updateParentMethods) {
            result.methods = updateParentBody();
        }

        return result;
    }

    public ExtendsResult setParent(ClassEntity parent, boolean updateParentMethods) {
        ExtendsResult result = new ExtendsResult(parent);

        if (this.parent != null) {
            throw new RuntimeException("Cannot re-assign parent for classes");
        }

        this.parent = parent;
        if (parent != null) {
            if (parent.useJavaLikeNames) {
                this.useJavaLikeNames = true;
            }

            this.methodCounts = parent.methodCounts;

            this.instanceOfList.add(parent.getLowerName());
            this.instanceOfList.addAll(parent.instanceOfList);
            this.interfaces.putAll(parent.interfaces);

            this.properties.putAll(parent.properties);
            this.staticProperties.putAll(parent.staticProperties);

            for (Map.Entry<String, ConstantEntity> entry : parent.constants.entrySet()) {
                if (!entry.getValue().isPrivate()) {
                    this.constants.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (updateParentMethods) {
            result.methods = updateParentBody();
        }

        return result;
    }

    /**
     * return empty list if success else list with not valid implemented methods in class
     *
     * @param _interface
     * @return
     */
    public ImplementsResult addInterface(ClassEntity _interface) {
        ImplementsResult result = new ImplementsResult(_interface);

        for (ConstantEntity e : _interface.constants.values()) {
            ConstantEntity origin = constants.get(e.getName());
            if (origin != null && e.getClazz().getId() == _interface.getId() &&
                    (parent == null || parent.constants.get(e.getName()) == null))
                result.signature.addConstant(InvalidConstant.error(InvalidConstant.Kind.INVALID_OVERRIDE, origin, e));
        }

        this.constants.putAll(_interface.constants);

        this.interfaces.put(_interface.getLowerName(), _interface);
        this.instanceOfList.add(_interface.getLowerName());
        this.instanceOfList.addAll(_interface.instanceOfList);

        for (MethodEntity method : _interface.getMethods().values()) {
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == method) continue;

            if (implMethod == null) {
                addMethod(method, null);
                if (type == Type.CLASS && !isAbstract)
                    result.signature.add(InvalidMethod.error(InvalidMethod.Kind.NON_EXISTS, method));
            } else {
                implMethod.setPrototype(method);

                if (/*!method.isDynamicSignature() &&*/ !implMethod.equalsBySignature(method)) { // checking dynamic for only extends
                    result.signature.add(InvalidMethod.error(InvalidMethod.Kind.INVALID_SIGNATURE, implMethod));
                } else if (implMethod.isStatic() && !method.isStatic()) {
                    result.signature.add(InvalidMethod.error(InvalidMethod.Kind.MUST_NON_STATIC, implMethod));
                } else if (!implMethod.isStatic() && method.isStatic()) {
                    result.signature.add(InvalidMethod.error(InvalidMethod.Kind.MUST_STATIC, implMethod));
                }
            }
        }
        return result;
    }

    public Map<String, ClassEntity> getInterfaces() {
        return interfaces;
    }

    public void addTrait(ClassEntity trait) {
        if (!trait.isTrait())
            throw new IllegalArgumentException("'" + trait.getName() + "' is not a trait");

        this.traits.put(trait.getLowerName(), trait);
    }

    public boolean hasTrait(String traitLowerName) {
        return this.traits.containsKey(traitLowerName);
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

    public SignatureResult addConstant(ConstantEntity constant) {
        constants.put(constant.getName(), constant);
        constant.setClazz(this);

        SignatureResult result = new SignatureResult();

        if (isInterface() && !constant.isPublic()) {
            result.addConstant(InvalidConstant.error(InvalidConstant.Kind.MUST_BE_PUBLIC_FOR_INTERFACE, constant, null));
        }

        return result;
    }

    public void addDynamicConstant(Environment env, String name, Memory value) {
        ConstantEntity entity = constants.get(name);
        env.getOrCreateStatic(entity.getInternalName(), value);
    }

    public void addDynamicStaticProperty(Environment env, String name, Memory value) {
        PropertyEntity prop = staticProperties.get(name);
        env.getOrCreateStatic(prop.specificName, value);
    }

    public void addDynamicProperty(Environment env, String name, Memory value) {
        PropertyEntity prop = properties.get(name);
        env.getOrCreateStatic(prop.getInternalName(), value);
    }

    public PropertyResult addProperty(PropertyEntity property) {
        PropertyResult result = new PropertyResult();
        PropertyEntity prototype = null;

        if (property.isStatic()) {
            prototype = staticProperties.get(property.getLowerName());
            if (prototype != null && prototype.getModifier() != property.getModifier()) {
                property.setPrototype(prototype);
            }

            if (prototype == null)
                prototype = properties.get(property.getName());

            staticProperties.put(property.getName(), property);
        } else {
            prototype = properties.get(property.getLowerName());
            if (prototype != null && prototype.getModifier() != property.getModifier()) {
                property.setPrototype(prototype);
            }

            if (prototype == null)
                prototype = staticProperties.get(property.getName());

            properties.put(property.getName(), property);
        }

        if (prototype != null) {
            if (property.getPrototype() != null) {
                if (prototype.isProtected() && property.isPrivate()) {
                    result.addError(InvalidProperty.Kind.MUST_BE_PROTECTED, property);
                }
                if (prototype.isPublic() && !property.isPublic()) {
                    result.addError(InvalidProperty.Kind.MUST_BE_PUBLIC, property);
                }
            }

            boolean overridden = property.modifier.ordinal() > prototype.modifier.ordinal();
            if (!property.isPrivate() && property.modifier == prototype.modifier)
                overridden = true;
            if (property.isPublic() && prototype.isProtected())
                overridden = true;

            if (prototype.isStatic() && !property.isStatic()) {
                if (overridden) {
                    property.setPrototype(prototype);
                    result.addError(InvalidProperty.Kind.STATIC_AS_NON_STATIC, property);
                }
            } else if (property.isStatic() && !prototype.isStatic()) {
                if (overridden) {
                    property.setPrototype(prototype);
                    result.addError(InvalidProperty.Kind.NON_STATIC_AS_STATIC, property);
                }
            }
        }

        property.setClazz(this);
        return result;
    }

    protected void addStaticProperty(PropertyEntity property) {
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

    public Class<?> getNativeClass() {
        return nativeClazz;
    }

    @Deprecated
    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    protected static void invalidAccessToProperty(Environment env, TraceInfo trace, PropertyEntity entity, int accessFlag) {
        switch (accessFlag) {
            case 1:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_ACCESS_TO_PROTECTED_PROPERTY.fetch(
                                entity.getClazz().getName(), entity.getName()
                        ));
            case 2:
                env.error(trace, ErrorType.E_ERROR,
                        Messages.ERR_ACCESS_TO_PRIVATE_PROPERTY.fetch(
                                entity.getClazz().getName(), entity.getName()
                        ));
        }
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;

        if (nativeClazz.getAnnotation(Reflection.UseJavaLikeNames.class) != null) {
            useJavaLikeNames = true;
        }

        if (!nativeClazz.isInterface()) {
            try {
                this.nativeConstructor = nativeClazz.getConstructor(Environment.class, ClassEntity.class);
                this.nativeConstructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                this.nativeConstructor = null;
                //if (IObject.class.isAssignableFrom(getNativeClass().getClass()))
                //  throw new CriticalException(e);
            }

            if (!this.isInternal) {
                try {
                    if (isTrait()) {
                        this.nativeInitEnvironment = nativeClazz.getDeclaredMethod(
                                "__$initEnvironment", Environment.class, String.class
                        );
                    } else {
                        this.nativeInitEnvironment = nativeClazz.getDeclaredMethod(
                                "__$initEnvironment", Environment.class
                        );
                    }
                    this.nativeInitEnvironment.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    this.nativeInitEnvironment = null;
                }
            }
        }
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public void initEnvironment(Environment env) {
        if (isClass() && nativeInitEnvironment != null) {
            try {
                nativeInitEnvironment.invoke(null, env);
            } catch (InvocationTargetException e) {
                env.__throwException(e);
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            }
        }

        if (!traits.isEmpty()) {
            Set<ClassEntity> used = new HashSet<ClassEntity>();
            try {
                for (ClassEntity trait : traits.values()) {
                    trait.initTraitEnvironment(env, this, used);
                }
            } catch (Exception e) {
                env.catchUncaught(e);
            } catch (Throwable e) {
                throw new CriticalException(e);
            }
        }
    }

    protected void initTraitEnvironment(Environment env, ClassEntity originClass, Set<ClassEntity> used) throws Throwable {
        if (nativeInitEnvironment != null) {
            try {
                nativeInitEnvironment.invoke(null, env, originClass.getName());
            } catch (InvocationTargetException e) {
                env.__throwException(e);
            }
        }
        for (ClassEntity trait : traits.values()) {
            if (used.add(trait)) {
                trait.initTraitEnvironment(env, originClass, used);
            }
        }
    }

    public <T extends IObject> T newObjectWithoutConstruct(Environment env) {
        IObject object = null;
        try {
            if (nativeConstructor != null)
                object = (IObject) nativeConstructor.newInstance(env, this);
        } catch (InvocationTargetException e) {
            env.__throwException(e);
            return null;
        } catch (InstantiationException e) {
            throw new CriticalException(e);
        } catch (IllegalAccessException e) {
            throw new CriticalException(e);
        }

        return (T) object;
    }

    public <T extends IObject> T newMock(Environment env) throws Throwable {
        if (nativeConstructor == null) {
            env.error(env.trace(), ErrorType.E_CORE_ERROR, "Cannot find a java constructor %s(Environment, ClassEntity)", getName());
        }

        try {
            IObject object = (IObject) nativeConstructor.newInstance(env, this);
            object.setAsMock();
            return (T) object;
        } catch (InstantiationException e) {
            return null;
        }
    }

    public void checkCanInstance(Environment env) {
        if (isAbstract) {
            env.error(trace, "Cannot instantiate abstract class %s", name);
        } else if (type == Type.INTERFACE) {
            env.error(trace, "Cannot instantiate interface %s", name);
        } else if (type == Type.TRAIT) {
            env.error(trace, "Cannot instantiate trait %s", name);
        }
    }

    public <T extends IObject> T newObject(Environment env, TraceInfo trace, boolean doConstruct, Memory... args)
            throws Throwable {
        return newObject(env, trace, doConstruct, true, args);
    }

    public <T extends IObject> T newObject(Environment env, TraceInfo trace, boolean doConstruct, boolean checks, Memory... args)
            throws Throwable {

        if (checks) {
            checkCanInstance(env);
        }

        IObject object;
        try {
            if (nativeConstructor == null) {
                env.error(trace, ErrorType.E_CORE_ERROR, "Cannot find a java constructor %s(Environment, ClassEntity)", getName());
            }

            object = (IObject) nativeConstructor.newInstance(env, this);
        } catch (InvocationTargetException e) {
            env.__throwException(e);
            return null;
        }

        ArrayMemory props = object.getProperties();

        for (PropertyEntity property : getProperties()) {
            if (id == property.clazz.getId() && property.getGetter() == null) {
                props.putAsKeyString(
                        property.getSpecificName(),
                        property.getDefaultValue(env).toImmutable()
                );
            }
        }

        ClassEntity tmp = parent;
        while (tmp != null) {
            long otherId = tmp.getId();
            for (PropertyEntity property : tmp.getProperties()) {
                if (property.getClazz().getId() == otherId && property.getGetter() == null) {
                    if (property.modifier != Modifier.PROTECTED || props.getByScalar(property.getName()) == null)
                        props.getByScalarOrCreate(
                                property.getSpecificName(),
                                property.getDefaultValue(env).toImmutable()
                        );
                }
            }
            tmp = tmp.parent;
        }

        if (doConstruct && methodConstruct != null) {
            ObjectInvokeHelper.invokeMethod(object, methodConstruct, env, trace, args, true);
        }

        return (T) object;
    }

    public <T extends IObject> T cloneObject(T value, Environment env, TraceInfo trace) throws Throwable {
        IObject copy = this.newObjectWithoutConstruct(env);
        ForeachIterator iterator = value.getProperties().foreachIterator(false, false);
        ArrayMemory props = copy.getProperties();
        while (iterator.next()) {
            Object key = iterator.getKey();
            if (key instanceof String) {
                String name = (String) key;
                if (name.indexOf('\0') > -1)
                    name = name.substring(name.lastIndexOf('\0') + 1);

                PropertyEntity entity = properties.get(name);
                if (entity != null) {
                    if (props.getByScalar(entity.getSpecificName()) == null)
                        props.put(entity.getSpecificName(), iterator.getValue().toImmutable());
                } else
                    props.put(key, iterator.getValue().toImmutable());
            } else
                props.put(key, iterator.getValue().toImmutable());
        }

        if (methodMagicClone != null) {
            ObjectInvokeHelper.invokeMethod(copy, methodMagicClone, env, trace, null, true);
        }

        return (T) copy;
    }

    public Memory concatProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return new StringMemory(o1.concat(o2));
            }
        }, callCache, cacheIndex);
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
        }, null, 0);
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
        }, null, 0);
    }

    public Memory mulProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.mul(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory divProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.div(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory modProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.mod(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory bitAndProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitAnd(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory bitOrProperty(Environment env, TraceInfo trace,
                                IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitOr(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory bitXorProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitXor(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory bitShrProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitShr(o2);
            }
        }, callCache, cacheIndex);
    }

    public Memory bitShlProperty(Environment env, TraceInfo trace,
                                 IObject object, String property, Memory memory, PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        return setProperty(env, trace, object, property, memory, new SetterCallback() {
            @Override
            public Memory invoke(Memory o1, Memory o2) {
                return o1.bitShl(o2);
            }
        }, callCache, cacheIndex);
    }

    public void appendProperty(IObject object, String property, Memory value) {
        object.getProperties().put(property, value);
    }

    public Memory refOfProperty(ArrayMemory props, String name) {
        PropertyEntity entity = properties.get(name);
        return props.refOfIndex(entity == null ? name : entity.getSpecificName());
    }

    public Memory setProperty(Environment env, TraceInfo trace,
                              IObject object, String property, Memory memory, SetterCallback callback,
                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        ReferenceMemory value;

        PropertyEntity entity = callCache == null ? null : callCache.get(env, cacheIndex);

        if (entity == null) {
            ClassEntity context = env.getLastClassOnStack();
            entity = isInstanceOf(context) ? context.properties.get(property) : properties.get(property);

            if (callCache != null && entity != null) {
                callCache.put(env, cacheIndex, entity);
            }
        }

        if (entity == null) {
            PropertyEntity staticEntity = staticProperties.get(property);
            if (staticEntity != null) {
                invalidAccessToProperty(env, trace, staticEntity, staticEntity.canAccess(env));
                env.error(trace, ErrorType.E_STRICT,
                        Messages.ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC,
                        staticEntity.getClazz().getName(),
                        staticEntity.getName()
                );
            }
        }

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();

        if (entity != null) {
            if (entity.setter != null) {
                if (callback != null)
                    memory = callback.invoke(getProperty(env, trace, object, property, null, 0), memory);

                try {
                    ObjectInvokeHelper.invokeMethod(object, entity.setter, env, trace, new Memory[]{memory}, false);
                } catch (IllegalArgumentException e) {
                    if (!object.getReflection().isInstanceOf(entity.setter.getClazz())) {
                        return setProperty(env, trace, object, property, memory, callback, null, 0);
                    }

                    throw e;
                }
                return memory;
            } else if (entity.getter != null) {
                env.error(trace, ErrorType.E_RECOVERABLE_ERROR, Messages.ERR_READONLY_PROPERTY.fetch(entity.getClazz().getName(), property));
            }
        }

        value = props == null || accessFlag != 0 ? null : props.getByScalar(entity == null ? property : entity.specificName);

        if (value == null) {
            boolean recursive = false;

            ClassEntity context = env.getLastClassOnStack();

            if (context != null && methodMagicSet != null && context.getId() == methodMagicSet.getClazz().getId()) {
                recursive = env.peekCall(0).flags == FLAG_SET;
            }

            if (methodMagicSet != null && !recursive) {
                StringMemory memoryProperty = new StringMemory(property);

                if (callback != null) {
                    Memory o1 = Memory.NULL;
                    if (methodMagicGet != null) {
                        try {
                            Memory[] args = new Memory[]{ memoryProperty };
                            env.pushCall(
                                    trace, object, args, methodMagicGet.getName(), methodMagicSet.getClazz().getName(), name
                            );
                            env.peekCall(0).flags = FLAG_GET;

                            args = InvokeArgumentHelper.checkType(env, trace, methodMagicGet, args);
                            o1 = methodMagicGet.invokeDynamic(object, env, trace, args);
                        } finally {
                            env.popCall();
                        }
                    }
                    memory = callback.invoke(o1, memory);
                }

                try {
                    Memory[] args = new Memory[]{memoryProperty, memory};
                    env.pushCall(trace, object, args, methodMagicSet.getName(), methodMagicSet.getClazz().getName(), name);
                    env.peekCall(0).flags = FLAG_SET;

                    args = InvokeArgumentHelper.checkType(env, trace, methodMagicSet, args);
                    methodMagicSet.invokeDynamic(object, env, trace, args);
                } finally {
                    env.popCall();
                }
            } else {
                /*if (accessFlag != 0) {
                    invalidAccessToProperty(env, trace, entity, accessFlag);
                    return Memory.NULL;
                }*/

                if (callback != null)
                    memory = callback.invoke(Memory.NULL, memory);

                String name = property;
                if (entity != null) {
                    if (accessFlag != 0 && context == null) {
                        switch (accessFlag) {
                            case 2:
                                if (object.getReflection().getId() == entity.getClazz().getId()) {
                                    invalidAccessToProperty(env, trace, entity, accessFlag);
                                    return Memory.NULL;
                                }
                                break;
                            case 1:
                                invalidAccessToProperty(env, trace, entity, accessFlag);
                                return Memory.NULL;
                        }
                    }

                    if (context != null) {
                        switch (entity.modifier) {
                            case PRIVATE:
                                if (entity.getClazz().getId() == context.getId())
                                    name = entity.specificName;
                                break;
                            case PROTECTED:
                                if (context.isInstanceOf(entity.getClazz()))
                                    name = entity.specificName;
                        }
                    }
                }

                return props == null
                        ? Memory.NULL
                        : (entity == null ? props.refOfIndex(name).assign(memory) : entity.assignValue(env, trace, object, name, memory));
            }
        } else {
            if (callback != null)
                memory = callback.invoke(value, memory);

            if (entity instanceof CompilePropertyEntity) {
                return entity.assignValue(env, trace, object, property, memory);
            }

            return value.assign(memory);
        }
        return memory;
    }

    public Memory unsetProperty(Environment env, TraceInfo trace, IObject object, String property,
                                PropertyCallCache callCache, int index)
            throws Throwable {
        ClassEntity context = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(context) ? context.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (entity == null) {
            PropertyEntity staticEntity = staticProperties.get(property);
            if (staticEntity != null) {
                invalidAccessToProperty(env, trace, staticEntity, staticEntity.canAccess(env));
            }
        }

        ArrayMemory props = object.getProperties();
        if (props == null
                || accessFlag != 0
                || props.removeByScalar(entity == null ? property : entity.specificName) == null) {
            if (methodMagicUnset != null) {
                if (context != null && context.getId() == methodMagicUnset.getClazz().getId()) {
                    if (env.peekCall(0).flags == FLAG_UNSET) {
                        return Memory.NULL;
                    }
                }

                try {
                    Memory[] args = new Memory[]{new StringMemory(property)};
                    env.pushCall(trace, object, args, methodMagicUnset.getName(), methodMagicUnset.getClazz().getName(), name);
                    env.peekCall(0).flags = FLAG_UNSET;

                    args = InvokeArgumentHelper.checkType(env, trace, methodMagicUnset, args);
                    methodMagicUnset.invokeDynamic(object, env, trace, args);
                } finally {
                    env.popCall();
                }
                return Memory.NULL;
            }
        }

        if (accessFlag != 0)
            invalidAccessToProperty(env, trace, entity, accessFlag);

        entity = staticProperties.get(property);
        if (entity != null) {
            env.error(trace, ErrorType.E_STRICT,
                    Messages.ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC,
                    entity.getClazz().getName(),
                    entity.getName()
            );
        }

        return Memory.NULL;
    }

    public Memory emptyProperty(Environment env, TraceInfo trace, IObject object, String property)
            throws Throwable {
        ClassEntity contex = env.getLastClassOnStack();
        PropertyEntity entity = isInstanceOf(contex) ? contex.properties.get(property) : properties.get(property);

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        if (props != null && accessFlag == 0) {
            Memory tmp = entity == null ? props.getByScalar(property) : entity.getValue(env, trace, object);
            if (tmp != null) {
                return tmp.toBoolean() ? Memory.TRUE : Memory.NULL;
            } else
                return Memory.NULL;
        }

        if (methodMagicIsset != null) {
            Memory result;
            if (contex != null && contex.getId() == methodMagicIsset.getClazz().getId()) {
                if (env.peekCall(0).flags == FLAG_ISSET) {
                    return object.getProperties().getByScalar(property) != null ? Memory.TRUE : Memory.NULL;
                }
            }
            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(
                        trace, object, args, methodMagicIsset.getName(), methodMagicIsset.getClazz().getName(), name
                );
                env.peekCall(0).flags = FLAG_ISSET;

                InvokeArgumentHelper.checkType(env, trace, methodMagicIsset, new StringMemory(property));
                result = methodMagicIsset.invokeDynamic(object, env, trace, new StringMemory(property))
                        .toBoolean() ? Memory.TRUE : Memory.NULL;
            } finally {
                env.popCall();
            }
            return result;
        }
        return Memory.NULL;
    }

    public Memory issetProperty(Environment env, TraceInfo trace, IObject object, String property,
                                PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        PropertyEntity entity = callCache == null || env instanceof ConcurrentEnvironment ? null : callCache.get(env, cacheIndex);

        if (entity == null) {
            ClassEntity contex = env.getLastClassOnStack();
            entity = isInstanceOf(contex) ? contex.properties.get(property) : properties.get(property);

            if (entity != null && callCache != null) {
                callCache.put(env, cacheIndex, entity);
            }
        }

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        Memory tmp = null;

        if (props != null && accessFlag == 0) {
            tmp = entity == null ? props.getByScalar(property) : entity.getValue(env, trace, object);
        }

        if (tmp != null) {
            return tmp.isNull() ? tmp : Memory.TRUE;
        }

        if (methodMagicIsset != null) {
            Memory result;

            ClassEntity context = env.getLastClassOnStack();

            if (context != null && context.getId() == methodMagicIsset.getClazz().getId())
                if (env.peekCall(0).flags == FLAG_ISSET) {
                    return object.getProperties().getByScalar(property) != null ? Memory.TRUE : Memory.NULL;
                }

            try {
                Memory[] args = new Memory[]{ new StringMemory(property) };

                env.pushCall(trace, object, args, methodMagicIsset.getName(), methodMagicIsset.getClazz().getName(), name);
                env.peekCall(0).flags = FLAG_ISSET;

                args = InvokeArgumentHelper.checkType(env, trace, methodMagicIsset, args);
                result = methodMagicIsset.invokeDynamic(object, env, trace, args)
                        .toBoolean() ? Memory.TRUE : Memory.NULL;
            } finally {
                env.popCall();
            }
            return result;
        }

        return Memory.NULL;
    }

    public Memory getStaticProperty(Environment env, TraceInfo trace, String property, boolean errorIfNotExists,
                                    boolean checkAccess, ClassEntity context, PropertyCallCache callCache, int cacheIndex,
                                    boolean lateStaticCall)
            throws Throwable {
        PropertyEntity entity = callCache == null || env instanceof ConcurrentEnvironment || context != null ? null : callCache.get(env, cacheIndex);

        if (entity == null) {
            boolean saveCache = context == null && callCache != null;

            context = context == null ? env.getLastClassOnStack() : context;
            entity = isInstanceOf(context) ? context.findStaticProperty(property) : findStaticProperty(property);

            if (saveCache && entity != null) {
                callCache.put(env, cacheIndex, entity);
            }
        }

        if (entity == null) {
            if (errorIfNotExists)
                env.error(trace, Messages.ERR_ACCESS_TO_UNDECLARED_STATIC_PROPERTY.fetch(name, property));
            return Memory.NULL;
        }

        if (checkAccess) {
            int accessFlag = entity.canAccess(env, lateStaticCall ? context : null);
            if (accessFlag != 0) {
                invalidAccessToProperty(env, trace, entity, accessFlag);
                return Memory.NULL;
            }
        }

        return entity.getStaticValue(env, trace);
    }

    public Memory getRefProperty(Environment env, TraceInfo trace, IObject object, String property,
                                 PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        Memory value;

        PropertyEntity entity = callCache == null || env instanceof ConcurrentEnvironment ? null : callCache.get(env, cacheIndex);

        if (entity == null) {
            ClassEntity context = env.getLastClassOnStack();
            entity = isInstanceOf(context) ? context.properties.get(property) : properties.get(property);

            if (callCache != null) {
                callCache.put(env, cacheIndex, entity);
            }
        }

        if (entity == null) {
            PropertyEntity staticEntity = staticProperties.get(property);
            if (staticEntity != null) {
                invalidAccessToProperty(env, trace, staticEntity, staticEntity.canAccess(env));
                env.error(trace, ErrorType.E_STRICT,
                        Messages.ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC,
                        staticEntity.getClazz().getName(),
                        staticEntity.getName()
                );
            }
        }

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        ArrayMemory props = object.getProperties();
        value = props == null || accessFlag != 0 ? null : props.getByScalar(entity == null ? property : entity.specificName);

        if (accessFlag != 0) {
            invalidAccessToProperty(env, trace, entity, accessFlag);
        }

        if (value == null) {
            if (methodMagicGet != null) {
                Memory altResult = getProperty(env, trace, object, property, callCache, cacheIndex);

                if (altResult.isObject()) {
                    return altResult;
                }
            }

            value = props == null ? new ReferenceMemory() : object.getProperties().refOfIndex(property);

            if (methodMagicGet != null || methodMagicSet != null) {
                env.error(trace,
                        props == null ? ErrorType.E_ERROR : ErrorType.E_NOTICE,
                        Messages.ERR_INDIRECT_MODIFICATION_OVERLOADED_PROPERTY, name, property);
            }
        }

        return value;
    }

    public Memory getProperty(Environment env, TraceInfo trace, IObject object, String property,
                              PropertyCallCache callCache, int cacheIndex)
            throws Throwable {
        Memory value;

        PropertyEntity entity = callCache == null || env instanceof ConcurrentEnvironment ? null : callCache.get(env, cacheIndex);

        if (entity == null) {
            ClassEntity context = env.getLastClassOnStack();
            entity = isInstanceOf(context) ? context.properties.get(property) : properties.get(property);

            if (callCache != null && entity != null) {
                callCache.put(env, cacheIndex, entity);
            }
        }

        if (entity == null) {
            PropertyEntity staticEntity = staticProperties.get(property);

            if (staticEntity != null) {
                invalidAccessToProperty(env, trace, staticEntity, staticEntity.canAccess(env));
                env.error(trace, ErrorType.E_STRICT,
                        Messages.ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC,
                        staticEntity.getClazz().getName(),
                        staticEntity.getName()
                );
            }
        }

        int accessFlag = entity == null ? 0 : entity.canAccess(env);

        if (entity != null && accessFlag != 0) {
            value = null;
        } else {
            if (entity != null) {
                value = entity.getValue(env, trace, object);
            } else {
                ArrayMemory props = object.getProperties();
                value = props == null ? null : props.getByScalar(property);
            }
        }

        if (value != null)
            return value;

        if (methodMagicGet != null) {
            Memory result;

            ClassEntity context = env.getLastClassOnStack();

            if (context != null && context.getId() == methodMagicGet.getClazz().getId()) {
                if (env.peekCall(0).flags == FLAG_GET) {
                    env.error(trace, ErrorType.E_NOTICE, Messages.ERR_UNDEFINED_PROPERTY, name, property);
                    return Memory.NULL;
                }
            }

            try {
                Memory[] args = new Memory[]{new StringMemory(property)};
                env.pushCall(trace, object, args, methodMagicGet.getName(), methodMagicGet.getClazz().getName(), name);
                env.peekCall(0).flags = FLAG_GET;

                InvokeArgumentHelper.checkType(env, trace, methodMagicGet, args);
                result = methodMagicGet.invokeDynamic(object, env, trace, args);
            } finally {
                env.popCall();
            }
            return result;
        }

        if (accessFlag != 0)
            invalidAccessToProperty(env, trace, entity, accessFlag);

        env.error(trace, ErrorType.E_NOTICE, Messages.ERR_UNDEFINED_PROPERTY, name, property);
        return Memory.NULL;
    }

    public void setProperty(IObject object, String name, Memory value) {
        PropertyEntity prop = findProperty(name);
        if (prop == null)
            throw new RuntimeException("Property '" + name + "' not found");

        object.getProperties().put(prop.specificName, value == null ? Memory.NULL : value);
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


    public static class InvalidConstant {
        public enum Kind { INVALID_OVERRIDE, MUST_BE_PUBLIC, MUST_BE_PUBLIC_FOR_INTERFACE, MUST_BE_PROTECTED }

        public final Kind kind;
        public final ConstantEntity constant;
        public final ConstantEntity prototype;
        public final ErrorType errorType;

        protected InvalidConstant(Kind kind, ConstantEntity constant, ConstantEntity prototype, ErrorType errorType) {
            this.kind = kind;
            this.constant = constant;
            this.errorType = errorType;
            this.prototype = prototype;
        }

        public static InvalidConstant error(Kind kind, ConstantEntity constant, ConstantEntity prototype) {
            return new InvalidConstant(kind, constant, prototype, ErrorType.E_ERROR);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InvalidConstant)) return false;

            InvalidConstant that = (InvalidConstant) o;

            return constant.equals(that.constant);
        }

        @Override
        public int hashCode() {
            return constant.hashCode();
        }
    }

    public static class InvalidMethod {
        public enum Kind {
            NON_EXISTS, INVALID_SIGNATURE, MUST_STATIC, MUST_NON_STATIC, MAGIC_MUST_BE_PUBLIC, MUST_BE_PUBLIC, MUST_BE_PROTECTED,
            FINAL, NON_ABSTRACT, NON_ABSTRACTABLE, INVALID_ACCESS_FOR_INTERFACE, FINAL_ABSTRACT, OVERRIDE_CONSTANTS
        }

        public final Kind kind;
        public final MethodEntity method;
        public final ErrorType errorType;

        protected InvalidMethod(Kind kind, MethodEntity method, ErrorType errorType) {
            this.kind = kind;
            this.method = method;
            this.errorType = errorType;
        }

        public static InvalidMethod warning(Kind kind, MethodEntity method) {
            return new InvalidMethod(kind, method, ErrorType.E_WARNING);
        }

        public static InvalidMethod error(Kind kind, MethodEntity method) {
            return new InvalidMethod(kind, method, ErrorType.E_ERROR);
        }

        public static InvalidMethod strict(Kind kind, MethodEntity method) {
            return new InvalidMethod(kind, method, ErrorType.E_STRICT);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InvalidMethod)) return false;

            InvalidMethod that = (InvalidMethod) o;

            return method.equals(that.method) && kind.equals(that.kind);
        }

        @Override
        public int hashCode() {
            int result = kind.hashCode();
            result = 31 * result + method.hashCode();
            return result;
        }
    }

    public static class InvalidProperty {
        public enum Kind {MUST_BE_PROTECTED, MUST_BE_PUBLIC, STATIC_AS_NON_STATIC, NON_STATIC_AS_STATIC}

        public final Kind kind;
        public final PropertyEntity property;
        public final ErrorType errorType;

        protected InvalidProperty(Kind kind, PropertyEntity property, ErrorType errorType) {
            this.kind = kind;
            this.property = property;
            this.errorType = errorType;
        }

        public static InvalidProperty warning(Kind kind, PropertyEntity property) {
            return new InvalidProperty(kind, property, ErrorType.E_WARNING);
        }

        public static InvalidProperty error(Kind kind, PropertyEntity property) {
            return new InvalidProperty(kind, property, ErrorType.E_ERROR);
        }

        public static InvalidProperty strict(Kind kind, PropertyEntity property) {
            return new InvalidProperty(kind, property, ErrorType.E_STRICT);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InvalidProperty)) return false;

            InvalidProperty that = (InvalidProperty) o;

            return property.equals(that.property) && kind.equals(that.kind);
        }

        @Override
        public int hashCode() {
            int result = kind.hashCode();
            result = 31 * result + property.hashCode();
            return result;
        }
    }

    public class PropertyResult {
        private final Set<InvalidProperty> properties;

        public PropertyResult() {
            this.properties = new HashSet<InvalidProperty>();
        }

        public void add(InvalidProperty prop) {
            this.properties.add(prop);
        }

        public void addError(InvalidProperty.Kind kind, PropertyEntity prop) {
            add(InvalidProperty.error(kind, prop));
        }

        public void check(Environment env) {
            for (InvalidProperty el : properties) {
                ErrorException e = null;
                switch (el.kind) {
                    case MUST_BE_PROTECTED:
                        e = new FatalException(
                                Messages.ERR_ACCESS_LEVEL_MUST_BE_PROTECTED_OR_WEAKER.fetch(
                                        el.property.clazz.getName(),
                                        el.property.getName(),
                                        el.property.getPrototype().getClazz().getName()
                                ),
                                el.property.getTrace()
                        );
                        break;
                    case MUST_BE_PUBLIC:
                        e = new FatalException(
                                Messages.ERR_ACCESS_LEVEL_MUST_BE_PUBLIC.fetch(
                                        el.property.clazz.getName(),
                                        el.property.getName(),
                                        el.property.getPrototype().getClazz().getName()
                                ),
                                el.property.getTrace()
                        );
                        break;
                    case STATIC_AS_NON_STATIC:
                        e = new FatalException(
                                Messages.ERR_CANNOT_REDECLARE_STATIC_AS_NON_STATIC.fetch(
                                        el.property.getPrototype().getClazz().getName(),
                                        el.property.getPrototype().getName(),
                                        el.property.clazz.getName(),
                                        el.property.getName()
                                ),
                                el.property.getTrace()
                        );
                        break;
                    case NON_STATIC_AS_STATIC:
                        e = new FatalException(
                                Messages.ERR_CANNOT_REDECLARE_NON_STATIC_AS_STATIC.fetch(
                                        el.property.getPrototype().getClazz().getName(),
                                        el.property.getPrototype().getName(),
                                        el.property.clazz.getName(),
                                        el.property.getName()
                                ),
                                el.property.getTrace()
                        );
                        break;
                }

                if (e != null) {
                    if (env == null)
                        throw e;
                    else
                        env.error(e.getTraceInfo(), el.errorType, e.getMessage());
                }
            }
        }
    }

    public class ImplementsResult {
        ClassEntity parent;
        SignatureResult signature;

        ImplementsResult(ClassEntity parent) {
            this.parent = parent;
            this.signature = new SignatureResult();
        }

        public void check(Environment env) {
            if (parent != null) {
                if (parent.isNotRuntime && !isInternal()) {
                    FatalException e = new FatalException(
                            Messages.ERR_CANNOT_USE_SYSTEM_CLASS.fetch(parent.getName(), getName()),
                            ClassEntity.this.trace
                    );
                    if (env != null)
                        env.error(e.getTraceInfo(), e.getType(), e.getMessage());
                    else
                        throw e;
                }
            }
            if (signature != null)
                signature.check(env);
        }
    }

    public class ExtendsResult {
        ClassEntity parent;
        SignatureResult methods;

        ExtendsResult(ClassEntity parent) {
            this.parent = parent;
            this.methods = new SignatureResult();
        }

        public void check(Environment env) {
            if (parent != null) {
                if (parent.isNotRuntime && !isInternal()) {
                    FatalException e = new FatalException(
                            Messages.ERR_CANNOT_USE_SYSTEM_CLASS.fetch(parent.getName(), getName()),
                            ClassEntity.this.trace
                    );
                    if (env != null)
                        env.error(e.getTraceInfo(), e.getType(), e.getMessage());
                    else
                        throw e;
                }

                if (parent.isFinal) {
                    FatalException e = new FatalException(
                            Messages.ERR_CLASS_MAY_NOT_INHERIT_FINAL_CLASS.fetch(ClassEntity.this.getName(), parent.getName()),
                            ClassEntity.this.trace
                    );
                    if (env != null)
                        env.error(e.getTraceInfo(), e.getType(), e.getMessage());
                    else
                        throw e;
                }
                if (ClassEntity.this.type == Type.CLASS && parent.type != Type.CLASS) {
                    FatalException e = new FatalException(
                            Messages.ERR_CANNOT_EXTENDS.fetch(ClassEntity.this.getName(), parent.getName()),
                            ClassEntity.this.trace
                    );
                    if (env != null)
                        env.error(e.getTraceInfo(), e.getType(), e.getMessage());
                    else
                        throw e;
                }
            }

            if (methods != null) {
                methods.check(env);
            }
        }
    }

    public class SignatureResult {
        private final Set<InvalidMethod> methods;
        private Set<InvalidConstant> overrideConstants_;

        SignatureResult() {
            methods = new HashSet<>();
        }

        public void add(InvalidMethod el) {
            methods.add(el);
        }

        public void addConstant(InvalidConstant el) {
            if (overrideConstants_ == null)
                overrideConstants_ = new HashSet<>();

            overrideConstants_.add(el);
        }

        public void check() {
            check(null);
        }

        private ErrorException getException(Environment env, InvalidMethod el, Messages.Item message) {
            return getException(env, el, message, false);
        }

        private ErrorException getException(Environment env, InvalidMethod el, Messages.Item message, boolean prototype) {
            ErrorException e;
            if (prototype) {
                e = new FatalException(
                        message.fetch(
                                el.method.getPrototype().getSignatureString(false),
                                el.method.getSignatureString(false)
                        ),
                        el.method.getTrace()
                );
            } else {
                e = new FatalException(
                        message.fetch(el.method.getSignatureString(false)),
                        el.method.getTrace()
                );
            }
            return e;
        }

        public void check(Environment env) {
            if (overrideConstants_ != null)
                for (InvalidConstant el : this.overrideConstants_) {
                    ErrorException e = null;

                    switch (el.kind) {
                        case INVALID_OVERRIDE:
                            e = new FatalException(Messages.ERR_CANNOT_INHERIT_OVERRIDE_CONSTANT.fetch(
                                            el.constant.getName(), el.prototype.getClazz().getName()
                            ), getTrace());
                            break;

                        case MUST_BE_PUBLIC:
                            e = new FatalException(Messages.ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC.fetch(
                                    el.constant.getClazz().getName(),
                                    el.constant.getName(), el.prototype.getClazz().getName()
                            ), getTrace());
                            break;

                        case MUST_BE_PUBLIC_FOR_INTERFACE:
                            e = new FatalException(Messages.ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC_FOR_INTERFACE.fetch(
                                    el.constant.getClazz().getName(),
                                    el.constant.getName()
                            ), getTrace());
                            break;

                        case MUST_BE_PROTECTED:
                            e = new FatalException(Messages.ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PROTECTED.fetch(
                                    el.constant.getClazz().getName(),
                                    el.constant.getName(), el.prototype.getClazz().getName()
                            ), getTrace());
                            break;
                    }

                    if (e != null) {
                        if (env == null)
                            throw e;
                        else {
                            env.error(e.getTraceInfo(), el.errorType, e.getMessage());
                        }
                    }
                }

            Set<InvalidMethod> nonExists = null;
            for (InvalidMethod el : methods) {
                ErrorException e = null;
                switch (el.kind) {
                    case INVALID_SIGNATURE:
                        MethodEntity prototype = el.method.getPrototype();
                        if (!prototype.isAbstractable()) {
                            while (prototype.prototype != null && prototype.prototype.isAbstractable())
                                prototype = prototype.prototype;
                        }
                        e = new FatalException(
                                Messages.ERR_INVALID_METHOD_SIGNATURE.fetch(
                                        el.method.getSignatureString(true), prototype.getSignatureString(true)
                                ),
                                el.method.getTrace()
                        );
                        break;
                    case MUST_STATIC:
                        e = new FatalException(
                                Messages.ERR_CANNOT_MAKE_STATIC_TO_NON_STATIC.fetch(
                                        el.method.getPrototype().getSignatureString(false),
                                        ClassEntity.this.getName()
                                ),
                                el.method.getTrace()
                        );
                        break;
                    case MUST_NON_STATIC:
                        e = new FatalException(
                                Messages.ERR_CANNOT_MAKE_NON_STATIC_TO_STATIC.fetch(
                                        el.method.getPrototype().getSignatureString(false),
                                        ClassEntity.this.getName()
                                ),
                                el.method.getTrace()
                        );
                        break;
                    case MAGIC_MUST_BE_PUBLIC:
                        env.error(el.method.getTrace(), el.errorType,
                                "The magic method %s must have public visibility", el.method.getSignatureString(false)
                        );
                        break;
                    case MUST_BE_PROTECTED:
                        e = new FatalException(
                                Messages.ERR_ACCESS_LEVEL_METHOD_MUST_BE_PROTECTED_OR_WEAKER.fetch(
                                        el.method.clazz.getName(),
                                        el.method.getName(),
                                        el.method.getPrototype().getClazz().getName()
                                ),
                                el.method.getTrace()
                        );
                        break;
                    case MUST_BE_PUBLIC:
                        e = new FatalException(
                                Messages.ERR_ACCESS_LEVEL_METHOD_MUST_BE_PUBLIC.fetch(
                                        el.method.clazz.getName(),
                                        el.method.getName(),
                                        el.method.getPrototype().getClazz().getName()
                                ),
                                el.method.getTrace()
                        );
                        break;
                    case FINAL_ABSTRACT:
                        e = getException(env, el, Messages.ERR_CANNOT_USE_FINAL_ON_ABSTRACT);
                        break;
                    case FINAL:
                        e = getException(env, el, Messages.ERR_CANNOT_OVERRIDE_FINAL_METHOD, true);
                        break;
                    case NON_ABSTRACT:
                        e = getException(env, el, Messages.ERR_NON_ABSTRACT_METHOD_MUST_CONTAIN_BODY);
                        break;
                    case NON_ABSTRACTABLE:
                        e = getException(env, el, Messages.ERR_ABSTRACT_METHOD_CANNOT_CONTAIN_BODY);
                        break;
                    case INVALID_ACCESS_FOR_INTERFACE:
                        e = getException(env, el, Messages.ERR_ACCESS_TYPE_FOR_INTERFACE_METHOD);
                        break;
                    case NON_EXISTS:
                        if (nonExists == null) nonExists = new HashSet<>();
                        nonExists.add(el);
                        break;
                }

                if (e != null) {
                    if (env == null)
                        throw e;
                    else {
                        env.error(e.getTraceInfo(), el.errorType, e.getMessage());
                    }
                }
            }

            if (nonExists != null) {
                StringBuilder needs = new StringBuilder();
                Iterator<InvalidMethod> iterator = nonExists.iterator();
                int size = 0;
                ErrorType errorType = ErrorType.E_NOTICE;

                while (iterator.hasNext()) {
                    InvalidMethod el = iterator.next();
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
                        Messages.ERR_IMPLEMENT_METHOD.fetch(ClassEntity.this.getName(), size, needs),
                        ClassEntity.this.getTrace()
                );

                if (env == null)
                    throw e;
                else {
                    env.error(e.getTraceInfo(), errorType, e.getMessage());
                }
            }
        }
    }
}
