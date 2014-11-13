package php.runtime.lang.spl;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

@Reflection.Name("Serializable")
public interface Serializable extends Traversable {

    @Reflection.Signature
    public Memory serialize(Environment env, Memory... args);

    @Reflection.Signature({@Reflection.Arg("serialized")})
    public Memory unserialize(Environment env, Memory... args);
}
