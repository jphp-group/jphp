package php.runtime.lang.spl.exception;

import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name("DomainException")
public class DomainException extends LogicException {
    public DomainException(Environment env) {
        super(env);
    }

    public DomainException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
