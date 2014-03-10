package php.runtime.ext.swing.classes.components.support;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.MethodEntity;

@Reflection.Name("php\\swing\\RootObject")
public class RootObject extends BaseObject {
    public RootObject(Environment env){
        this(env, null);
        __class__ = env.fetchClass(getClass());
    }

    public RootObject(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature({@Reflection.Arg("name")})
    public Memory __isset(Environment env, Memory... args) {
        String name = args[0].toString();
        MethodEntity methodEntity = __class__.findMethod("__get" + name.toLowerCase());
        return methodEntity == null ? Memory.FALSE : Memory.TRUE;
    }

    @Reflection.Signature({@Reflection.Arg("name")})
    public Memory __unset(Environment env, Memory... args) {
        env.exception(env.trace(), "Unsupported unset operation");
        return Memory.NULL;
    }

    @Reflection.Signature({@Reflection.Arg("name")})
    public Memory __get(Environment env, Memory... args) throws Throwable {
        String name = args[0].toString();
        MethodEntity methodEntity = __class__.findMethod("__get" + name.toLowerCase());
        if (methodEntity != null){
            return ObjectInvokeHelper.invokeMethod(this, methodEntity, env, env.trace(), null);
        } else
            env.exception(env.trace(), "getting: Unknown getting property - " + args[0].toString());
        return Memory.NULL;
    }

    @Reflection.Signature({@Reflection.Arg("name"), @Reflection.Arg("value")})
    public Memory __set(Environment env, Memory... args) throws Throwable {
        String name = args[0].toString();
        MethodEntity methodEntity = __class__.findMethod("__set" + name.toLowerCase());
        if (methodEntity != null){
            ObjectInvokeHelper.invokeMethod(this, methodEntity, env, env.trace(), new Memory[]{args[1]});
        } else
            env.exception(env.trace(), "setting: Unknown property - " + args[0].toString());
        return Memory.NULL;
    }
}
