package php.runtime.reflection.support;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

abstract public class TypeChecker {
    abstract public String getSignature();
    abstract public boolean check(Environment env, Memory value, boolean nullable);

    public static TypeChecker of(HintType type) {
        return new Simple(type);
    }

    public static TypeChecker of(String className) {
        return new ClassName(className);
    }

    public static TypeChecker of(Class<?> typeNativeClass) {
        return new NativeClass(typeNativeClass);
    }

    public static TypeChecker ofEnum(Class<? extends Enum> enumClass) {
        return new EnumClass(enumClass);
    }

    public static class Simple extends TypeChecker {
        protected HintType type;

        public Simple(HintType type) {
            this.type = type;
        }

        public HintType getType() {
            return type;
        }

        @Override
        public String getSignature() {
            return type.toString();
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable) {
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
    }

    public static class ClassName extends TypeChecker {
        protected String typeClass;
        protected String typeClassLower;

        public ClassName(String typeClass) {
            this.typeClass = typeClass;
            this.typeClassLower = typeClass.toLowerCase();
        }

        public String getTypeClass() {
            return typeClass;
        }

        public String getTypeClassLower() {
            return typeClassLower;
        }

        @Override
        public String getSignature() {
            return typeClass;
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable) {
            if (nullable && value.isNull())
                return true;

            if (!value.isObject())
                return false;

            ObjectMemory object = value.toValue(ObjectMemory.class);
            ClassEntity oEntity = object.getReflection();

            return oEntity.isInstanceOfLower(typeClassLower);
        }
    }

    public static class NativeClass extends TypeChecker {
        protected Class<?> typeNativeClass;

        public NativeClass(Class<?> typeNativeClass) {
            this.typeNativeClass = typeNativeClass;
        }

        @Override
        public String getSignature() {
            return ReflectionUtils.getClassName(typeNativeClass);
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable) {
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
        }
    }

    public static class EnumClass extends TypeChecker {
        protected Class<? extends Enum> typeEnum;

        public EnumClass(Class<? extends Enum> typeEnum) {
            this.typeEnum = typeEnum;
        }

        public Class<? extends Enum> getTypeEnum() {
            return typeEnum;
        }

        @Override
        public String getSignature() {
            return null;
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable) {
            try {
                if (nullable && value.isNull()) {
                    return true;
                }

                Enum.valueOf(typeEnum, value.toString());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }
}
