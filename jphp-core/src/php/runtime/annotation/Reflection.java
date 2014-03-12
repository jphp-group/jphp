package php.runtime.annotation;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.memory.ObjectMemory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

final public class Reflection {

    private Reflection() { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, FIELD})
    public @interface Ignore {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    public @interface Name {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface NotRuntime {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD, TYPE})
    public @interface Signature {
        boolean root() default false;
        Arg[] value() default {};
        Arg result() default @Arg(type = HintType.ANY);
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Arg {
        String value() default "";
        Optional optional() default @Optional(exists = false);
        HintType type() default HintType.ANY;
        String typeClass() default "";
        Modifier modifier() default Modifier.PUBLIC;
        boolean reference() default false;
        boolean readOnly() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Optional {
        String value() default "";
        HintType type() default HintType.ANY;
        boolean exists() default true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    public @interface Reference {}

    public static String getClassName(Class<?> clazz) {
        Name name = clazz.getAnnotation(Name.class);
        return name == null ? clazz.getSimpleName() : name.value();
    }

    public static String getGivenName(Memory value) {
        if (value.isObject())
            return "an instance of " + value.toValue(ObjectMemory.class).getReflection().getName();
        else
            return value.getRealType().toString();
    }
}
