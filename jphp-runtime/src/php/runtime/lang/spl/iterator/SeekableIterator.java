package php.runtime.lang.spl.iterator;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

@Reflection.Name("SeekableIterator")
public interface SeekableIterator extends Iterator {

    @Reflection.Signature({@Reflection.Arg("position")})
    public Memory seek(Environment env, Memory... args);
}
