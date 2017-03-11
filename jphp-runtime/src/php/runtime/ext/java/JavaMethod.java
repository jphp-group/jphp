package php.runtime.ext.java;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.ClassEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\JavaMethod")
final public class JavaMethod extends JavaReflection {
    protected Method method;
    protected MemoryUtils.Converter[] converters;
    protected MemoryUtils.Converter resultConverter;
    protected Class<?>[] paramTypes;

    public JavaMethod(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setMethod(Method method) {
        this.method = method;
        method.setAccessible(true);

        this.paramTypes = method.getParameterTypes();
        converters = MemoryUtils.getConverters(paramTypes);
        resultConverter = MemoryUtils.getConverter(method.getReturnType());
    }

    @Signature({@Arg(value = "object", typeClass = "php\\lang\\JavaObject", optional = @Optional("NULL"))})
    public Memory invoke(Environment env, Memory... args){
        int len = args.length - 1;
        if (len < paramTypes.length || len > paramTypes.length)
            exception(env, new IllegalArgumentException("Invalid argument count"));

        Object[] passed = new Object[len];
        int i = 0;
        for(MemoryUtils.Converter converter : converters){
            Memory arg = args[i + 1];
            if (arg.instanceOf("php\\lang\\JavaObject")){
                passed[i] = ((JavaObject)arg.toValue(ObjectMemory.class).value).getObject();
            } else {
                if (converter != null) {
                    passed[i] = converter.run(args[i + 1]);
                } else {
                    passed[i] = null;
                }
            }
            i++;
        }

        Object obj = args[0].isNull() ? null : ((JavaObject)args[0].toValue(ObjectMemory.class).value).getObject();
        try {
            Object result = method.invoke(obj, passed);
            if (result == null)
                return Memory.NULL;

            if (resultConverter != null)
                return

                        MemoryUtils.valueOf(result);
            else {
                if (method.getReturnType() == void.class)
                    return Memory.NULL;

                return new ObjectMemory(JavaObject.of(env, result));
            }
        } catch (IllegalAccessException e) {
            exception(env, e);
        } catch (InvocationTargetException e) {
            exception(env, e.getTargetException());
        }
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "object", typeClass = "php\\lang\\JavaObject", optional = @Optional("NULL")),
            @Arg(value = "arguments", type = HintType.ARRAY, optional = @Optional)
    })
    public Memory invokeArgs(Environment env, Memory... args){
        Memory[] tmp = args[1].toValue(ArrayMemory.class).values();
        Memory[] passed = new Memory[tmp.length + 1];
        System.arraycopy(tmp, 0, passed, 1, tmp.length);
        passed[0] = args[0];

        return invoke(env, passed);
    }

    @Signature
    public Memory isStatic(Environment env, Memory... args){
        return Modifier.isStatic(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isFinal(Environment env, Memory... args){
        return Modifier.isFinal(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPrivate(Environment env, Memory... args){
        return Modifier.isPrivate(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isProtected(Environment env, Memory... args){
        return Modifier.isProtected(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPublic(Environment env, Memory... args){
        return Modifier.isPublic(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAbstract(Environment env, Memory... args){
        return Modifier.isAbstract(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isNative(Environment env, Memory... args){
        return Modifier.isNative(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isSynchronized(Environment env, Memory... args){
        return Modifier.isSynchronized(method.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getDeclaringClass(Environment env, Memory... args){
        return new ObjectMemory(JavaClass.of(env, method.getDeclaringClass()));
    }

    @Signature
    public Memory getReturnedType(Environment env, Memory... args){
        return new ObjectMemory(JavaClass.of(env, method.getReturnType()));
    }

    @Signature
    public Memory getModifiers(Environment env, Memory... args){
        return LongMemory.valueOf(method.getModifiers());
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return StringMemory.valueOf(method.getName());
    }

    @Signature
    public Memory isVarArgs(Environment env, Memory... args){
        return method.isVarArgs() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("annotationClass"))
    public Memory isAnnotationPresent(Environment env, Memory... args){
        try {
            return method.isAnnotationPresent((Class<? extends Annotation>) Class.forName(args[0].toString()))
                    ? Memory.TRUE : Memory.FALSE;
        } catch (ClassNotFoundException | ClassCastException e) {
            exception(env, e);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory getParameterTypes(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();
        for(Class<?> el : method.getParameterTypes()){
            result.add(new ObjectMemory(JavaClass.of(env, el)));
        }
        return result.toConstant();
    }

    @Signature
    public Memory getParameterCount(Environment env, Memory... args){
        return LongMemory.valueOf(method.getParameterTypes().length);
    }

    public static JavaMethod of(Environment env, Method method){
        JavaMethod javaMethod = new JavaMethod(env, env.fetchClass("php\\lang\\JavaMethod"));
        javaMethod.setMethod(method);
        return javaMethod;
    }

    public static Object[] makePassed(Environment env, MemoryUtils.Converter[] converters, Memory... args){
        Object[] passed = new Object[converters.length];
        int i = 0;

        for(MemoryUtils.Converter converter : converters){
            Memory arg = args[i];
            if (arg.instanceOf("php\\lang\\JavaObject")){
                passed[i] = ((JavaObject)arg.toValue(ObjectMemory.class).value).getObject();
            } else {
                if (converter != null) {
                    passed[i] = converter.run(args[i]);
                } else {
                    passed[i] = null;
                }
            }
            i++;
        }

        return passed;
    }
}
