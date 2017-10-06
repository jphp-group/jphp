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
import php.runtime.reflection.support.TypeChecker;
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
    protected TypeChecker typeChecker;

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

    public TypeChecker getTypeChecker() {
        return typeChecker;
    }

    public void setTypeChecker(TypeChecker typeChecker) {
        this.typeChecker = typeChecker;
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
        this.typeChecker = type == null ? null : TypeChecker.of(type);
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isNullableOrDefaultNull() {
        return isNullable() || (getDefaultValue() != null && getDefaultValue().isNull());
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

    public void setType(String type) {
        HintType _type = HintType.of(type);
        this.typeChecker = _type == null ? null : TypeChecker.of(_type);
    }

    public Class<? extends Enum> getTypeEnum() {
        return typeChecker instanceof TypeChecker.EnumClass ? ((TypeChecker.EnumClass) typeChecker).getTypeEnum() : null;
    }

    public void setTypeEnum(Class<? extends Enum> typeEnum) {
        this.typeChecker = typeEnum == null ? null : TypeChecker.ofEnum(typeEnum);
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
        return checkTypeHinting(env, value, type, nullable, null);
    }

    public static boolean checkTypeHinting(Environment env, Memory value, HintType type, boolean nullable, String staticClassName) {
        if (nullable && value.isNull())
            return true;

        return TypeChecker.of(type).check(env, value, nullable, staticClassName);
    }

    public boolean checkTypeHinting(Environment env, Memory value, String typeClass, boolean nullable) {
        if (nullable && value.isNull())
            return true;

        if (!value.isObject()) {
            return false;
        }

        return TypeChecker.of(typeClass).check(env, value, nullable, null);
    }

    public boolean checkTypeHinting(Environment env, Memory value) {
        return checkTypeHinting(env, value, (String) null);
    }

    public boolean checkTypeHinting(Environment env, Memory value, String staticClassName) {
        if (typeChecker != null) {
            return typeChecker.check(
                    env, value, nullable || (defaultValue != null && defaultValue.isNull()), staticClassName
            );
        }

        return true;
    }

    public Memory applyTypeHinting(Environment env, Memory value, boolean strict) {
        if (typeChecker != null) {
            return typeChecker.apply(
                    env, value, nullable || (defaultValue != null && defaultValue.isNotNull()), strict
            );
        }

        return null;
    }

    public boolean isArray(){
        return getType() == HintType.ARRAY;
    }

    public boolean isCallable(){
        return getType() == HintType.CALLABLE;
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

        if (typeChecker != null) {
            String signature = typeChecker.getSignature();

            if (signature != null && !signature.isEmpty()) {
                sb.append(signature).append(" ");
            }
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
        return getType() == that.getType();

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        result = 31 * result + (isReference ? 1 : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
}
