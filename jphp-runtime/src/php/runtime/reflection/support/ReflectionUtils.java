package php.runtime.reflection.support;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.memory.ObjectMemory;

import java.lang.annotation.Annotation;

import static php.runtime.annotation.Reflection.Name;

final public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static String getClassName(Class<?> clazz) {
        Name name = clazz.getAnnotation(Name.class);
        String result = name == null ? clazz.getSimpleName() : name.value();

        Reflection.Namespace namespace = clazz.getAnnotation(Reflection.Namespace.class);
        if (namespace != null) {
            result = namespace.value() + "\\" + result;
        }

        return result;
    }

    public static String getGivenName(Memory value) {
        if (value.isObject())
            return "an instance of " + value.toValue(ObjectMemory.class).getReflection().getName();
        else
            return value.getRealType().toString();
    }

    public static <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return (T) annotation;
            }
        }

        return null;
    }
}
