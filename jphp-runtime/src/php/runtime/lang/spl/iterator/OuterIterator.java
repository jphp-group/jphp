package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

@Reflection.Name("OuterIterator")
public interface OuterIterator extends Iterator {

    @Reflection.Signature
    public Memory getInnerIterator(Environment env, Memory... args);
}
