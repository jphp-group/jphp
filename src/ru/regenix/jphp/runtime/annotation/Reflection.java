package ru.regenix.jphp.runtime.annotation;

import ru.regenix.jphp.common.HintType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

public @interface Reflection {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    public @interface Name {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD})
    public @interface Signature {
        Arg[] value();
        Arg result() default @Arg(type = HintType.ANY);
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Arg {
        String value() default "";
        Optional optional() default @Optional(exists = false);
        HintType type() default HintType.ANY;
        boolean reference() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Optional {
        String value() default "";
        boolean exists() default true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({PARAMETER})
    public @interface Reference {}
}
