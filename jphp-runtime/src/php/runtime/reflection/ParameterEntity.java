package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.support.Entity;
import php.runtime.reflection.support.ReflectionUtils;
import php.runtime.util.JVMStackTracer;

public class ParameterEntity extends Entity {

    public interface TypeHintingChecker {
        boolean call(Environment env, Memory value);
        String getNeeded(Environment env, Memory value);
    }

    protected ClassEntity clazz;
    protected Memory defaultValue;
    protected String defaultValueConstName;

    protected boolean isReference;
    protected HintType type = HintType.ANY;
    protected String typeClass;
    protected String typeClassLower;
    protected Class<? extends Enum> typeEnum;
    protected Class<?> typeNativeClass;
    protected TypeHintingChecker typeHintingChecker;

    protected boolean mutable = true;
    protected boolean used = true;
    protected boolean nullable = false;
    protected boolean variadic = false;

    public ParameterEntity(Context context) {
        super(context);
    }

    public String getDefaultValueConstName() {
        return defaultValueConstName;
    }

    public void setDefaultValueConstName(String defaultValueConstName) {
        this.defaultValueConstName = defaultValueConstName;
    }

    public Memory getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Memory defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    private void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public HintType getType() {
        return type == null ? HintType.ANY : type;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public String getTypeClassLower() {
        return typeClassLower;
    }

    public void setType(HintType type) {
        this.type = type == null ? HintType.ANY : type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
        this.typeClassLower = typeClass == null ? null : typeClass.toLowerCase();
    }

    public void setTypeNativeClass(Class<?> typeNativeClass) {
        this.typeNativeClass = typeNativeClass;
        Class<?> baseWrapper = MemoryOperation.getWrapper(typeNativeClass);

        if (baseWrapper == null) {
            throw new CriticalException("Support only wrapper classes");
        }

        setTypeClass(ReflectionUtils.getClassName(baseWrapper));
    }

    public void setType(String type){
        this.type = HintType.of(type);
        if (this.type == null) {
            typeClass = type;
            typeClassLower = type.toLowerCase();
        } else {
            typeClass = null;
            typeClassLower = null;
        }
    }

    public Class<? extends Enum> getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(Class<? extends Enum> typeEnum) {
        this.typeEnum = typeEnum;
    }

    public TypeHintingChecker getTypeHintingChecker() {
        return typeHintingChecker;
    }

    public void setTypeHintingChecker(TypeHintingChecker typeHintingChecker) {
        this.typeHintingChecker = typeHintingChecker;
    }

    public static void validateTypeHinting(Environment env, int index, Memory[] args, HintType type,
                                           boolean nullable) {
        Memory value = args[index - 1];

        if (!checkTypeHinting(env, value, type, nullable)) {
            String given;
            if (value == null){
                given = "none";
            } else if (value.isObject()) {
                given = "instance of " + value.toValue(ObjectMemory.class).getReflection().getName();
            } else {
                given = value.getRealType().toString();
            }

            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StackTraceElement e = stack[2];
            StackTraceElement where = stack[3];
            TraceInfo trace;
            if (where.getLineNumber() <= 0)
                trace = env.trace();
            else
                trace = new TraceInfo(where);


            JVMStackTracer.Item item = new JVMStackTracer.Item(env.scope.getClassLoader(), e);

            env.error(trace,
                    ErrorType.E_RECOVERABLE_ERROR,
                    "Argument %s passed to %s() must be of the type %s, %s given",
                    index,
                    item.getSignature(),
                    type.toString(), given
            );
        }
    }
    public static boolean checkTypeHinting(Environment env, Memory value, HintType type) {
        return checkTypeHinting(env, value, type, false);
    }

    public static boolean checkTypeHinting(Environment env, Memory value, HintType type, boolean nullable) {
        if (nullable && value.isNull())
            return true;

        switch (type){
            case SCALAR:
                switch (value.getRealType()){
                    case BOOL:
                    case INT:
                    case DOUBLE:
                    case STRING:
                        return true;
                }
                return false;
            case OBJECT: return value.isObject();
            case NUMBER: return value.isNumber();
            case DOUBLE: return value.getRealType() == Memory.Type.DOUBLE;
            case INT: return value.getRealType() == Memory.Type.INT;
            case STRING: return value.isString();
            case BOOLEAN: return value.getRealType() == Memory.Type.BOOL;
            case ARRAY:
                return value.isArray();
            case TRAVERSABLE:
                return value.isArray() || value.instanceOf("Traversable", "traversable");
            case CALLABLE:
                Invoker invoker = Invoker.valueOf(env, null, value);
                return invoker != null && invoker.canAccess(env) == 0;
            default:
                return true;
        }
    }

    public boolean checkTypeHinting(Environment env, Memory value, String typeClass, boolean nullable) {
        if (nullable && value.isNull())
            return true;

        if (!value.isObject())
            return false;

        ObjectMemory object = value.toValue(ObjectMemory.class);
        ClassEntity oEntity = object.getReflection();

        return oEntity.isInstanceOf(typeClass);
    }

    public boolean checkTypeHinting(Environment env, Memory value){
        if (typeHintingChecker != null) {
            if (nullable && value.isNull()) {
                return true;
            }

            return typeHintingChecker.call(env, value);
        }

        if (type != HintType.ANY && type != null) {
            return checkTypeHinting(env, value, type, nullable || (defaultValue != null && defaultValue.isNull()));
        } else if (typeNativeClass != null) {
            if (nullable && value.isNull()) {
                return true;
            }

            if (value.isObject()) {
                IObject object = value.toObject(IObject.class);
                if (object instanceof BaseWrapper) {
                    return typeNativeClass.isAssignableFrom(((BaseWrapper) object).getWrappedObject().getClass());
                }

                return false;
            } else {
                return false;
            }
        } else if (typeClass != null) {
            return checkTypeHinting(env, value, typeClass, nullable || (defaultValue != null && defaultValue.isNull()));
        } else if (typeEnum != null && typeEnum != Enum.class) {
            try {
                if (nullable && value.isNull()) {
                    return true;
                }

                Enum.valueOf(typeEnum, value.toString());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else
            return true;
    }

    public boolean isArray(){
        return type == HintType.ARRAY;
    }

    public boolean isCallable(){
        return type == HintType.CALLABLE;
    }

    public boolean isOptional(){
        return defaultValue != null;
    }

    public boolean isDefaultValueAvailable(){
        return defaultValue != null;
    }

    public boolean canBePassedByValue(){
        return !isReference;
    }

    public boolean isPassedByReference(){
        return isReference;
    }

    public String getSignatureString(){
        StringBuilder sb = new StringBuilder();
        if (typeClass != null)
            sb.append(typeClass).append(" ");
        else if (type != HintType.ANY){
            sb.append(type.toString()).append(" ");
        }

        if (isReference)
            sb.append("&");

        sb.append("$").append(name);
        return sb.toString();
    }

    public boolean isMutable() {
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isVariadic() {
        return variadic;
    }

    public void setVariadic(boolean variadic) {
        this.variadic = variadic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterEntity)) return false;
        if (!super.equals(o)) return false;

        ParameterEntity that = (ParameterEntity) o;

        if (isReference != that.isReference) return false;
        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        result = 31 * result + (isReference ? 1 : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
