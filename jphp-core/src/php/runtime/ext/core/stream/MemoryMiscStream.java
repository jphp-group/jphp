package php.runtime.ext.core.stream;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;

import static php.runtime.annotation.Reflection.*;

@Name("php\\io\\MemoryStream")
public class MemoryMiscStream extends MiscStream {
    public MemoryMiscStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature({@Arg(value = "mode", optional = @Optional("r"))})
    public Memory __construct(Environment env, Memory... args) throws IOException {
        super.__construct(env, new StringMemory("memory"), args[0]);
        return Memory.NULL;
    }
}
