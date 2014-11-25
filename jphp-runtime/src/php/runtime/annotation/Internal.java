package php.runtime.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotations for JVM bytecode of JPHP.
 */
public @interface Internal {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    public @interface Name {
        String value();
    }
}
