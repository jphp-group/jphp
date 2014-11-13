package php.runtime.lang.spl;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.IObject;

@Reflection.Name("Countable")
public interface Countable extends IObject {

    @Reflection.Signature
    public Memory count(Environment env, Memory... args);
}
