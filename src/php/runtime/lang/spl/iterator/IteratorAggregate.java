package php.runtime.lang.spl.iterator;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.Memory;

@Reflection.Name("IteratorAggregate")
public interface IteratorAggregate {

    @Reflection.Signature
    public Memory getIterator(Environment env, Memory... args);
}
