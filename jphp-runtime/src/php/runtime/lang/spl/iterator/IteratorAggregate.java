package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.IObject;

@Reflection.Name("IteratorAggregate")
public interface IteratorAggregate extends IObject {

    @Reflection.Signature
    public Memory getIterator(Environment env, Memory... args);
}
