package ru.regenix.jphp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

public @interface Runtime {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD, PARAMETER})
    public @interface Reference {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD})
    public @interface Immutable {}
}
