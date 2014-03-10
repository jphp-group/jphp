package php.runtime.lang.spl;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.Memory;

@Reflection.Name("Countable")
public interface Countable {

    @Reflection.Signature
    public Memory count(Environment env, Memory... args);
}
