package ru.regenix.jphp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

final public class Runtime {

    private Runtime(){}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({METHOD, PARAMETER})
    public @interface Reference {}
}
