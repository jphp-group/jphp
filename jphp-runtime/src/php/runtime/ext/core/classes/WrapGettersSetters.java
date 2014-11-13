package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.MethodEntity;

import static php.runtime.annotation.Reflection.*;

@Trait
@Name("php\\lang\\GettersSetters")
public class WrapGettersSetters extends BaseObject {

    public WrapGettersSetters(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({@Arg("name")})
    public Memory __isset(Environment env, Memory... args) {
        String name = args[0].toString();
        MethodEntity methodEntity = env.getLastClassOnStack().findMethod("__get" + name.toLowerCase());
        return methodEntity == null ? Memory.FALSE : Memory.TRUE;
    }

    @Signature({@Arg("name")})
    public Memory __unset(Environment env, Memory... args) {
        env.exception(env.trace(), "Unsupported unset operation");
        return Memory.NULL;
    }

    @Signature({@Arg("name")})
    public Memory __get(Environment env, Memory... args) throws Throwable {
        String name = args[0].toString();
        MethodEntity methodEntity = env.getLastClassOnStack().findMethod("__get" + name.toLowerCase());
        if (methodEntity != null){
            return ObjectInvokeHelper.invokeMethod(env.getLateObject(), methodEntity, env, env.trace(), null);
        } else
            env.exception(env.trace(), "getting: Unknown getting property - " + args[0].toString());
        return Memory.NULL;
    }

    @Signature({@Arg("name"), @Arg("value")})
    public Memory __set(Environment env, Memory... args) throws Throwable {
        String name = args[0].toString();
        MethodEntity methodEntity = env.getLastClassOnStack().findMethod("__set" + name.toLowerCase());
        if (methodEntity != null){
            ObjectInvokeHelper.invokeMethod(env.getLateObject(), methodEntity, env, env.trace(), new Memory[]{args[1]});
        } else
            env.exception(env.trace(), "setting: Unknown property - " + args[0].toString());
        return Memory.NULL;
    }
}
