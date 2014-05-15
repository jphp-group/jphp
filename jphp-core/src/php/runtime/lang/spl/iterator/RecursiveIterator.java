package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.env.Environment;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("RecursiveIterator")
public interface RecursiveIterator extends Iterator {

    @Signature
    public Memory getChildren(Environment env, Memory... args);

    @Signature
    public Memory hasChildren(Environment env, Memory... args);
}
