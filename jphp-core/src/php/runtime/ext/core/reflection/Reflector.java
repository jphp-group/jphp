package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

@Reflection.Name("Reflector")
public interface Reflector {

    @Reflection.Signature
    Memory __toString(Environment env, Memory... args);
}
