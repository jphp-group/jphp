package php.runtime.lang.spl.iterator;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.Memory;

@Reflection.Name("RecursiveIterator")
public interface RecursiveIterator extends Iterator {

    @Reflection.Signature
    public Memory getChildren(Environment env, Memory... args);

    @Reflection.Signature
    public Memory hasChildren(Environment env, Memory... args);
}
