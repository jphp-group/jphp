package php.runtime.ext.java;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\JavaField")
final public class JavaField extends JavaReflection {
    protected Field field;
    protected Memory cachedMemory;
    protected boolean canCaching;
    protected int mod;

    public JavaField(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setField(Field field) {
        this.field = field;
        field.setAccessible(true);
        mod = field.getModifiers();
        canCaching = Modifier.isFinal(mod) && Modifier.isStatic(mod);
    }

    @Signature({@Arg(value = "object", typeClass = "php\\lang\\JavaObject", optional = @Optional("NULL"))})
    public Memory get(Environment env, Memory... args){
        if (canCaching && cachedMemory != null)
            return cachedMemory;

        try {
            if (args[0].isNull()){
                return cachedMemory = MemoryUtils.valueOf(env, field.get(null));
            } else {
                JavaObject javaObject = ((JavaObject)args[0].toValue(ObjectMemory.class).value);
                return cachedMemory = MemoryUtils.valueOf(env, field.get(javaObject.object));
            }
        } catch (IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature({@Arg(value = "object", typeClass = "php\\lang\\JavaObject", optional = @Optional("NULL")), @Arg("value")})
    public Memory set(Environment env, Memory... args){
        try {
            if (args[0].isNull()){
                field.set(null, MemoryUtils.fromMemory(args[1], field.getType()));
            } else {
                JavaObject javaObject = ((JavaObject)args[0].toValue(ObjectMemory.class).value);
                field.set(javaObject.object, MemoryUtils.fromMemory(args[1], field.getType()));
            }
        } catch (IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature
    public Memory isFinal(Environment env, Memory... args){
        return Modifier.isFinal(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isStatic(Environment env, Memory... args){
        return Modifier.isStatic(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPublic(Environment env, Memory... args){
        return Modifier.isPublic(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isProtected(Environment env, Memory... args){
        return Modifier.isProtected(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPrivate(Environment env, Memory... args){
        return Modifier.isPrivate(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isTransient(Environment env, Memory... args){
        return Modifier.isTransient(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isVolatile(Environment env, Memory... args){
        return Modifier.isVolatile(mod) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getDeclaringClass(Environment env, Memory... args){
        return new ObjectMemory(JavaClass.of(env, field.getDeclaringClass()));
    }

    @Signature
    public Memory getModifiers(Environment env, Memory... args){
        return LongMemory.valueOf(field.getModifiers());
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(field.getName());
    }

    public static JavaField of(Environment env, Field field){
        JavaField javaField = new JavaField(env, env.fetchClass("php\\lang\\JavaField"));
        javaField.setField(field);
        return javaField;
    }
}
