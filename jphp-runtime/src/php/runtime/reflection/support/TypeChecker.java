package php.runtime.reflection.support;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import php.runtime.Memory;
import php.runtime.Memory.Type;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.Field;

abstract public class TypeChecker {
    abstract public String getSignature();
    abstract public String getHumanString();

    abstract public boolean check(Environment env, Memory value, boolean nullable, String staticClassName);
    abstract public Memory apply(Environment env, Memory value, boolean nullable, boolean strict);
    public boolean isBuiltin() {
        return false;
    }

    public boolean identical(TypeChecker typeChecker) {
        return this == typeChecker;
    }

    public static boolean identical(TypeChecker tc1, TypeChecker tc2) {
        if (tc1 != null && tc2 != null) {
            return tc1.identical(tc2);
        } else {
            return tc1 == tc2;
        }
    }

    public static TypeChecker of(HintType type) {
        return Simple.valueOf(type);
    }

    public static TypeChecker of(String className) {
        return ClassName.valueOf(className);
    }

    public static TypeChecker of(Class<?> typeNativeClass) {
        return NativeClass.valueOf(typeNativeClass);
    }

    public static TypeChecker ofEnum(Class<? extends Enum> enumClass) {
        return EnumClass.valueOf(enumClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (getClass() == obj.getClass()) {
            return identical((TypeChecker) obj);
        } else {
            return false;
        }
    }

    public static class Simple extends TypeChecker {
        protected final HintType type;

        private static final EnumMap<HintType, Simple> cache;
        static {
            cache = new EnumMap<>(HintType.class);
            for (HintType hintType : HintType.values()) {
                cache.put(hintType, new Simple(hintType));
            }
        }

        public Simple(HintType type) {
            this.type = type;
        }

        public static Simple valueOf(HintType type) {
            return cache.get(type);
        }

        public HintType getType() {
            return type;
        }

        @Override
        public String getSignature() {
            return type.toString();
        }

        @Override
        public String getHumanString() {
            return "the type " + type.toString();
        }

        @Override
        public boolean isBuiltin() {
            return type != HintType.SELF;
        }

        @Override
        public boolean identical(TypeChecker typeChecker) {
            return typeChecker instanceof Simple && type == ((Simple) typeChecker).type;
        }

        @Override
        public Memory apply(Environment env, Memory value, boolean nullable, boolean strict) {
            if (nullable && value.isNull()) {
                return value;
            }

            if (strict) {
                switch (type) {
                    case DOUBLE:
                        if (value.isNumber()) {
                            return DoubleMemory.valueOf(value.toDouble());
                        }
                    default:
                        return null;
                }
            }

            switch (type) {
                case INT:
                    switch (value.getRealType()) {
                        case BOOL:
                        case DOUBLE:
                            return LongMemory.valueOf(value.toLong());
                        default:
                            return StringMemory.toLong(value.toString(), false);
                    }
                case STRING:
                    if (value.isObject() && ((ObjectMemory) value).getReflection().methodMagicToString == null) {
                        return null;
                    } else {
                        return StringMemory.valueOf(value.toString());
                    }
                case BOOLEAN:
                    return TrueMemory.valueOf(value.toBoolean());
                case DOUBLE:
                    Memory memory = StringMemory.toNumeric(value.toString(), false, null);

                    if (memory.getRealType() == Type.INT) {
                        return DoubleMemory.valueOf(memory.toLong());
                    }

                    return memory;
            }

            return null;
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable, String staticClassName) {
            if (type == HintType.VOID) {
                // VOID must check compiler!
                return true;
            }

            if (nullable && value.isNull())
                return true;

            switch (type){
                /*case VOID:
                    return value.isUndefined();*/
                case OBJECT: return value.isObject();
                case NUMBER: return value.isNumber();
                case DOUBLE: return value.getRealType() == Memory.Type.DOUBLE;
                case INT: return value.getRealType() == Memory.Type.INT;
                case STRING: return value.isString();
                case BOOLEAN: return value.getRealType() == Memory.Type.BOOL;
                case ARRAY:
                    return value.isArray();
                case TRAVERSABLE:
                    return value.isTraversable();
                case ITERABLE:
                    return value.isTraversable();
                case CALLABLE:
                    Invoker invoker = Invoker.valueOf(env, null, value);
                    return invoker != null && invoker.canAccess(env) == 0;
                case SELF:
                    if (!value.isObject()) {
                        return false;
                    }

                    if (staticClassName == null) {
                        Memory memory = env.__getMacroClass();
                        staticClassName = memory.isNull() ? null : memory.toString();
                    }

                    if (staticClassName != null) {
                        return value.toValue(ObjectMemory.class)
                                .getReflection()
                                .isInstanceOf(staticClassName);
                    }

                    return true;
                default:
                    return true;
            }
        }
    }

    public static class ClassName extends TypeChecker {
        protected final String typeClass;
        protected final String typeClassLower;

        private final static Map<String, ClassName> cache = new HashMap<>();

        public ClassName(String typeClass) {
            this.typeClass = typeClass;
            this.typeClassLower = typeClass.toLowerCase();
        }

        public static ClassName valueOf(String typeClass) {
            return cache.computeIfAbsent(typeClass, s -> new ClassName(typeClass));
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
        public String getHumanString() {
            return "an instance of " + getTypeClass();
        }

        @Override
        public boolean identical(TypeChecker typeChecker) {
            return typeChecker instanceof ClassName && typeClassLower.equals(((ClassName) typeChecker).typeClassLower);
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable, String staticClassName) {
            if (nullable && value.isNull())
                return true;

            if (!value.isObject())
                return false;

            ObjectMemory object = value.toValue(ObjectMemory.class);
            ClassEntity oEntity = object.getReflection();

            return oEntity.isInstanceOfLower(typeClassLower);
        }

        @Override
        public Memory apply(Environment env, Memory value, boolean nullable, boolean strict) {
            return null;
        }
    }

    public static class NativeClass extends TypeChecker {
        protected final Class<?> typeNativeClass;

        private final static Map<Class<?>, NativeClass> cache = new HashMap<>();

        public NativeClass(Class<?> typeNativeClass) {
            this.typeNativeClass = typeNativeClass;
        }

        public static NativeClass valueOf(Class<?> typeNativeClass) {
            return cache.computeIfAbsent(typeNativeClass, s -> new NativeClass(typeNativeClass));
        }

        @Override
        public String getSignature() {
            return ReflectionUtils.getClassName(typeNativeClass);
        }

        @Override
        public String getHumanString() {
            return "an instance of " + ReflectionUtils.getClassName(typeNativeClass);
        }

        @Override
        public boolean identical(TypeChecker typeChecker) {
            return typeChecker instanceof NativeClass && ((NativeClass) typeChecker).typeNativeClass == typeNativeClass;
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable, String staticClassName) {
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

        @Override
        public Memory apply(Environment env, Memory value, boolean nullable, boolean strict) {
            return null;
        }
    }

    public static class EnumClass extends TypeChecker {
        protected final Class<? extends Enum> typeEnum;

        private final static Map<Class<? extends Enum>, EnumClass> cache = new HashMap<>();

        public EnumClass(Class<? extends Enum> typeEnum) {
            this.typeEnum = typeEnum;
        }

        public static EnumClass valueOf(Class<? extends Enum> typeEnum) {
            return cache.computeIfAbsent(typeEnum, s -> new EnumClass(typeEnum));
        }

        public Class<? extends Enum> getTypeEnum() {
            return typeEnum;
        }

        @Override
        public boolean identical(TypeChecker typeChecker) {
            return typeChecker instanceof EnumClass && typeEnum == ((EnumClass) typeChecker).typeEnum;
        }

        @Override
        public String getSignature() {
            return null;
        }

        @Override
        public String getHumanString() {
            Field[] fields = getTypeEnum().getFields();
            String[] names = new String[fields.length];

            for (int i = 0; i < names.length; i++) {
                names[i] = fields[i].getName();
            }

            return "one of range [" + StringUtils.join(names, ", ") + "] as string";
        }

        @Override
        public boolean check(Environment env, Memory value, boolean nullable, String staticClassName) {
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

        @Override
        public Memory apply(Environment env, Memory value, boolean nullable, boolean strict) {
            return null;
        }
    }
}
