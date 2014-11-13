package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.env.Environment;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("Reflector")
public interface Reflector {

    @Signature
    Memory __toString(Environment env, Memory... args);
}
