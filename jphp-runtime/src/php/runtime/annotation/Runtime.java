package php.runtime.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

public @interface Runtime {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD, PARAMETER})
    public @interface Reference {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD})
    public @interface Immutable {
        boolean ignoreRefs() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD})
    public @interface FastMethod {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    public @interface MutableValue {
        boolean ignoreRefs() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    public @interface GetLocals {}
}
