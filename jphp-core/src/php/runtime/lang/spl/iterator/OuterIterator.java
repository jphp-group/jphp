package php.runtime.lang.spl.iterator;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.Memory;

@Reflection.Name("OuterIterator")
public interface OuterIterator extends Iterator {

    @Reflection.Signature
    public Memory getInnerIterator(Environment env, Memory... args);
}
