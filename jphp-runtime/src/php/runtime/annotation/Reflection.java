package php.runtime.annotation;

import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.lang.IObject;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

public @interface Reflection {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface UseJavaLikeNames {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, FIELD})
    public @interface Ignore {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD})
    public @interface Getter {
        String value() default "";
        boolean hiddenInDebugInfo() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD})
    public @interface Setter {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface BaseType {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface WrapInterface {
        Class<?>[] value();
        boolean skipConflicts() default false;
        boolean wrapFields() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, FIELD})
    public @interface Final {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    public @interface Abstract {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    @Inherited
    public @interface Namespace {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    public @interface Name {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface NotWrapper {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface Trait {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(TYPE)
    public @interface UseTraits {
        Class<? extends IObject>[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE})
    public @interface NotRuntime {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER, METHOD})
    public @interface Nullable {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD, TYPE})
    public @interface Signature {
        boolean root() default false;
        Arg[] value() default {};
        Arg result() default @Arg(type = HintType.ANY);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({FIELD, METHOD})
    public @interface Property {
        String value() default "";
        boolean hiddenInDebugInfo() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Arg {
        String value() default "";
        Optional optional() default @Optional(exists = false);
        HintType type() default HintType.ANY;
        String typeClass() default "";

        Class<? extends IObject> nativeType() default IObject.class;
        Class<? extends Enum> typeEnum() default Enum.class;

        Modifier modifier() default Modifier.PUBLIC;
        boolean reference() default false;
        boolean readOnly() default false;
        boolean nullable() default false;
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
}
