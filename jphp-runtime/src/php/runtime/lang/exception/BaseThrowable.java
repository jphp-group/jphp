package php.runtime.lang.exception;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.IObject;

@Name("Throwable")
public interface BaseThrowable extends IObject {
    @Signature
    Memory getMessage(Environment env, Memory... args);

    @Signature
    Memory getCode(Environment env, Memory... args);

    @Signature
    Memory getFile(Environment env, Memory... args);

    @Signature
    Memory getLine(Environment env, Memory... args);

    @Signature
    Memory getTrace(Environment env, Memory... args);

    @Signature
    Memory getTraceAsString(Environment env, Memory... args);

    @Signature
    Memory __toString(Environment env, Memory... args);
}
