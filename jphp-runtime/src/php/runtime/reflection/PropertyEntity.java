package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.common.Modifier;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.InvokeArgumentHelper;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.IObject;
import php.runtime.lang.exception.BaseTypeError;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.UninitializedMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.ObjectPropertyMemory;
import php.runtime.reflection.support.Entity;
import php.runtime.reflection.support.ReflectionUtils;
import php.runtime.reflection.support.TypeChecker;

import java.lang.reflect.Field;

public class PropertyEntity extends Entity {
    protected ClassEntity clazz;
    protected ClassEntity trait;
    protected Modifier modifier = Modifier.PUBLIC;
    private Memory defaultValue;
    protected DocumentComment docComment;

    protected boolean isStatic;
    protected Field field;

    protected String specificName;
    protected PropertyEntity prototype;
    protected boolean isDefault;

    protected MethodEntity getter;
    protected MethodEntity setter;

    protected TypeChecker typeChecker;
    protected boolean nullable = false;

    protected boolean hiddenInDebugInfo = false;

    public PropertyEntity(Context context) {
        super(context);
    }

    public PropertyEntity getPrototype() {
        return prototype;
    }

    public void setPrototype(PropertyEntity prototype) {
        this.prototype = prototype;
    }

    public TypeChecker getTypeChecker() {
        return typeChecker;
    }

    public void setTypeChecker(TypeChecker typeChecker) {
        this.typeChecker = typeChecker;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setTypeClass(String typeClass) {
        typeChecker = typeClass == null ? null : TypeChecker.of(typeClass);
    }

    public void setTypeNativeClass(Class<?> typeNativeClass) {
        Class<?> baseWrapper = MemoryOperation.getWrapper(typeNativeClass);

        if (baseWrapper == null) {
            throw new CriticalException("Support only wrapper classes");
        }

        typeChecker = TypeChecker.of(ReflectionUtils.getClassName(baseWrapper));
    }

    public Class<? extends Enum> getTypeEnum() {
        return typeChecker instanceof TypeChecker.EnumClass ? ((TypeChecker.EnumClass) typeChecker).getTypeEnum() : null;
    }

    public void setTypeEnum(Class<? extends Enum> typeEnum) {
        this.typeChecker = typeEnum == null ? null : TypeChecker.ofEnum(typeEnum);
    }

    public void setType(String type) {
        HintType _type = HintType.of(type);
        this.typeChecker = _type == null ? null : TypeChecker.of(_type);
    }

    public HintType getType() {
        return typeChecker instanceof TypeChecker.Simple ? ((TypeChecker.Simple) typeChecker).getType() : HintType.ANY;
    }

    public String getTypeClass() {
        return typeChecker instanceof TypeChecker.ClassName ? ((TypeChecker.ClassName) typeChecker).getTypeClass() : null;
    }

    public String getTypeClassLower() {
        return typeChecker instanceof TypeChecker.ClassName ? ((TypeChecker.ClassName) typeChecker).getTypeClassLower() : null;
    }

    public void setType(HintType type) {
        this.typeChecker = type == null || type == HintType.ANY ? null : TypeChecker.of(type);
    }

    public boolean checkTypeHinting(Environment env, Memory value) {
        return checkTypeHinting(env, value, (String) null);
    }

    public boolean checkTypeHinting(Environment env, Memory value, String staticClassName) {
        if (typeChecker != null) {
            return typeChecker.check(
                    env, value, nullable, staticClassName
            );
        }

        return true;
    }

    public Memory applyTypeHinting(Environment env, Memory value, boolean strict) {
        if (typeChecker != null) {
            return typeChecker.apply(
                    env, value, nullable, strict
            );
        }

        return null;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        field.setAccessible(true);
        this.field = field;
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public boolean isHiddenInDebugInfo() {
        return hiddenInDebugInfo;
    }

    public void setHiddenInDebugInfo(boolean hiddenInDebugInfo) {
        this.hiddenInDebugInfo = hiddenInDebugInfo;
    }

    public Memory getDefaultValue(){
        return defaultValue;
    }

    public Memory getDefaultValue(Environment env) {
        if (defaultValue == null) {
            Memory r = env.getStatic(isStatic ? specificName : internalName);
            return r == null ? Memory.NULL : r;
        } else {
            return defaultValue;
        }
    }

    public void setDefaultTypedValue(Memory defaultValue, Environment env) {
        if (defaultValue == null) {
            setDefaultValue(getUninitializedValue());
        } else {
            this.setDefaultValue(env == null ? null : typedDefaultValue(env, defaultValue));
        }
    }

    public Memory getUninitializedValue() {
        String arg = null;
        if (getType() != HintType.ANY) {
            arg = getType().toString();
        } else if (getTypeClass() != null) {
            arg = getTypeClass();
        }

        if (arg != null) {
            return UninitializedMemory.valueOf((nullable ? "?" : "") + arg);
        }

        return Memory.NULL;
    }

    public void setDefaultValue(Memory defaultValue) {
        /*if (defaultValue == null) {
            Memory uninitializedValue = getUninitializedValue();
            defaultValue = uninitializedValue == Memory.NULL ? null : uninitializedValue;
        }*/

        this.defaultValue = defaultValue;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public boolean isPrivate(){
        return modifier == Modifier.PRIVATE;
    }

    public boolean isProtected(){
        return modifier == Modifier.PROTECTED;
    }

    public boolean isPublic(){
        return modifier == Modifier.PUBLIC;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
        updateSpecificName();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    public void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
        updateSpecificName();
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public void updateSpecificName(){
        switch (modifier){
            case PRIVATE: if (clazz != null) { specificName = "\0" + clazz.getName() + "\0" + name; } break;
            case PROTECTED: specificName = "\0*\0" + name; break;
            default:
                specificName = name;
        }

        if (isStatic && clazz != null)
            specificName = "\0" + clazz.getLowerName() + "#" + specificName;

        if (clazz != null)
            internalName = "\0" + clazz.getLowerName() + "\0#" + name;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        updateSpecificName();
    }

    public String getSpecificName() {
        return specificName;
    }

    public ClassEntity getTrait() {
        return trait;
    }

    public void setTrait(ClassEntity trait) {
        this.trait = trait;
    }

    public boolean isOwned(ClassEntity entity){
        return clazz.getId() == entity.getId();
    }

    public int canAccess(Environment env) {
        return canAccess(env, null, false);
    }

    public MethodEntity getGetter() {
        return getter;
    }

    public void setGetter(MethodEntity getter) {
        this.getter = getter;
    }

    public MethodEntity getSetter() {
        return setter;
    }

    public void setSetter(MethodEntity setter) {
        this.setter = setter;
    }


    public int canAccess(Environment env, ClassEntity context) {
        return canAccess(env, context, false);
    }

    /**
     * 0 - success
     * 1 - invalid protected
     * 2 - invalid private
     * @param env
     * @return
     */
    public int canAccess(Environment env, ClassEntity context, boolean lateStaticCall) {
        switch (modifier){
            case PUBLIC: return 0;
            case PRIVATE:
                ClassEntity cl = context == null ? (lateStaticCall ? env.getLateStaticClass() : env.getLastClassOnStack()) : context;
                return cl != null && cl.getId() == this.clazz.getId() ? 0 : 2;
            case PROTECTED:
                ClassEntity clazz = context == null ? (lateStaticCall ? env.getLateStaticClass() : env.getLastClassOnStack()) : context;
                if (clazz == null)
                    return 1;

                long id = this.clazz.getId();
                do {
                    if (clazz.getId() == id)
                        return 0;
                    clazz = clazz.parent;
                } while (clazz != null);

                return 1;
        }

        return 2;
    }

    public boolean canAccessAsNonStatic(Environment env, TraceInfo trace){
        if (isStatic){
            env.error(
                    trace, ErrorType.E_STRICT, Messages.ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC,
                    getClazz().getName(), name
            );
            return false;
        }
        return true;
    }

    public boolean isReadOnly() {
        return getter == null && setter != null;
    }

    public PropertyEntity duplicate() {
        PropertyEntity propertyEntity = new PropertyEntity(context);
        propertyEntity.setStatic(isStatic);
        propertyEntity.setDocComment(docComment);
        propertyEntity.setName(name);
        propertyEntity.setDefault(isDefault);
        propertyEntity.setDefaultValue(defaultValue);
        propertyEntity.setModifier(modifier);
        propertyEntity.setPrototype(propertyEntity);
        propertyEntity.setTrace(trace);
        propertyEntity.setGetter(getter);
        propertyEntity.setSetter(setter);
        return propertyEntity;
    }

    protected Memory typedDefaultValue(Environment env, Memory defaultValue) {
        if (typeChecker != null && defaultValue != null && !defaultValue.isUninitialized()) {
            if (!checkTypeHinting(env, defaultValue)) {
                ModuleEntity module = env.getModuleManager().findModule(trace);
                Memory memory = applyTypeHinting(env, defaultValue, module != null && module.isStrictTypes());

                if (memory == null) {
                    String mustBe = typeChecker.getSignature();

                    if (!nullable && defaultValue.isNull()) {
                        env.error(trace,
                                "Default value for property of type %s may not be null. Use the nullable type ?%s to allow null default value",
                                mustBe, mustBe
                        );
                    } else {
                        env.error(trace,
                                "Cannot use %s as default value for property %s::$%s of type %s",
                                InvokeArgumentHelper.getGiven(defaultValue, true), getClazz().getName(), getName(), mustBe
                        );
                    }
                }

                return memory;
            }
        }

        return defaultValue;
    }

    public Memory typedValue(Environment env, TraceInfo trace, Memory value) {
        if (typeChecker != null) {
            if (!checkTypeHinting(env, value)) {
                ModuleEntity module = env.getModuleManager().findModule(trace);

                Memory memory = applyTypeHinting(env, value, module != null && module.isStrictTypes());

                if (memory != null) {
                    value = memory;
                } else {
                    String mustBe = typeChecker.getSignature();
                    if (nullable) {
                        mustBe = "?" + mustBe;
                    }

                    env.exception(trace,
                            BaseTypeError.class,
                            "Cannot assign %s to property %s::$%s of type %s",
                            InvokeArgumentHelper.getGiven(value, true), getClazz().getName(), getName(), mustBe
                    );
                }
            }
        }

        return value;
    }

    public Memory assignValue(Environment env, TraceInfo trace, Object object, String name, Memory value) {
        return ((IObject) object).getProperties().refOfIndex(name).assign(typedValue(env, trace, value));
    }

    public Memory getStaticValue(Environment env, TraceInfo trace) {
        return env.getOrCreateStatic(
                specificName,
                getDefaultValue(env).fast_toImmutable()
        );
    }

    public Memory getValue(Environment env, TraceInfo trace, Object object) throws Throwable {
        if (getter != null && object instanceof IObject) {
            return ObjectInvokeHelper.invokeMethod((IObject) object, getter, env, trace, null, false);
        }

        ArrayMemory props = ((IObject) object).getProperties();

        ReferenceMemory result = props.getByScalar(specificName);
        if (result == null) {
            result = props.getByScalar(name);
        }

        if (result != null && isTyped()) {
            return new ObjectPropertyMemory(env, trace, result, this);
        }

        return result;
    }

    public boolean isTyped() {
        return typeChecker != null;
    }

    public boolean isSameTyped(PropertyEntity el) {
        if (typeChecker == null && el.typeChecker == null) {
            return true;
        }

        return typeChecker != null && typeChecker.identical(el.typeChecker);
    }
}
