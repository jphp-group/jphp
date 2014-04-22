package php.runtime.ext.java;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.Field;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\JavaObject")
final public class JavaObject extends JavaReflection {
    protected Object object;

    public JavaObject(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Signature(@Arg("name"))
    public Memory __get(Environment env, Memory... args){
        String name = args[0].toString();
        try {
            Field field = object.getClass().getField(name);
            field.setAccessible(true);
            return MemoryUtils.valueOf(env, field.get(object));
        } catch (NoSuchFieldException e) {
            exception(env, e);
        } catch (IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature({@Arg("name"), @Arg("value")})
    public Memory __set(Environment env, Memory... args){
        String name = args[0].toString();
        try {
            Field field = object.getClass().getField(name);
            field.setAccessible(true);
            field.set(object, MemoryUtils.fromMemory(args[1], field.getType()));
        } catch (NoSuchFieldException e) {
            exception(env, e);
        } catch (IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Name("getClass")
    @Signature
    public Memory _getClass(Environment env, Memory... args){
        return new ObjectMemory(JavaClass.of(env, object.getClass()));
    }

    @Signature
    public Memory getClassName(Environment env, Memory... args){
        return new StringMemory(object.getClass().getName());
    }

    public static JavaObject of(Environment env, Object value){
        JavaObject javaObject = new JavaObject(env, env.fetchClass("php\\lang\\JavaObject"));
        javaObject.setObject(value);
        return javaObject;
    }
}
