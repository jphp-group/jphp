package php.runtime.ext.core.reflection;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

@Name("ReflectionType")
public class ReflectionType extends BaseObject {
    private Memory name;
    private boolean allowsNull;
    private boolean isBuiltin;

    public ReflectionType(Environment env, String name, boolean allowsNull, boolean isBuiltin) {
        super(env);
        this.name = StringMemory.valueOf(name);
        this.allowsNull = allowsNull;
        this.isBuiltin = isBuiltin;
    }

    public ReflectionType(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg("name"),
            @Arg("allowsNull"),
            @Arg("isBuiltin")
    })
    public Memory __construct(Environment env, Memory... args) {
        allowsNull = args[1].toBoolean();
        isBuiltin = args[2].toBoolean();
        name = StringMemory.valueOf(args[0].toString());

        return Memory.UNDEFINED;
    }

    @Signature
    public Memory getName() {
        return name;
    }

    @Signature
    public Memory __toString(Environment env, Memory... args) {
        return name;
    }

    @Signature
    public Memory allowsNull(Environment env, Memory... args) {
        return allowsNull ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isBuiltin(Environment env, Memory... args) {
        return isBuiltin ? Memory.TRUE : Memory.FALSE;
    }
}
