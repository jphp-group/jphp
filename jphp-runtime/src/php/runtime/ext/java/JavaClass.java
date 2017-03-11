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
import java.lang.reflect.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\JavaClass")
final public class JavaClass extends JavaReflection {
    protected Class<?> clazz;

    public JavaClass(Environment env, Class<?> clazz) {
        super(env);
        this.clazz = clazz;
    }

    public JavaClass(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature(@Arg("class"))
    public Memory __construct(Environment env, Memory... args){
        try {
            setClazz(Class.forName(args[0].toString()));
        } catch (ClassNotFoundException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Signature(@Arg("name"))
    public Memory __get(Environment env, Memory... args){
        String name = args[0].toString();
        try {
            Field field = clazz.getField(name);
            field.setAccessible(true);
            return MemoryUtils.valueOf(env, field.get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature({@Arg("name"), @Arg("value")})
    public Memory __set(Environment env, Memory... args){
        String name = args[0].toString();
        try {
            Field field = clazz.getField(name);
            field.setAccessible(true);
            field.set(null, MemoryUtils.fromMemory(args[1], field.getType()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature
    public Memory isInterface(Environment env, Memory... args){
        return clazz.isInterface() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAnnotation(Environment env, Memory... args){
        return clazz.isAnnotation() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isEnum(Environment env, Memory... args){
        return clazz.isEnum() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isArray(Environment env, Memory... args){
        return clazz.isArray() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPrimitive(Environment env, Memory... args){
        return clazz.isPrimitive() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAnonymousClass(Environment env, Memory... args){
        return clazz.isAnonymousClass() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isMemberClass(Environment env, Memory... args){
        return clazz.isMemberClass() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isFinal(Environment env, Memory... args){
        return Modifier.isFinal(clazz.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAbstract(Environment env, Memory... args){
        return Modifier.isAbstract(clazz.getModifiers()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getName(Environment env, Memory... args){
        return new StringMemory(clazz.getName());
    }

    @Signature
    public Memory getSimpleName(Environment env, Memory... args){
        return new StringMemory(clazz.getSimpleName());
    }

    @Signature
    public Memory getSuperClass(Environment env, Memory... args){
        return clazz.getSuperclass() == null ? Memory.NULL : new ObjectMemory(JavaClass.of(env, clazz.getSuperclass()));
    }

    @Signature
    public Memory getCanonicalName(Environment env, Memory... args){
        return new StringMemory(clazz.getCanonicalName());
    }

    @Signature
    public Memory getModifiers(Environment env, Memory... args){
        return LongMemory.valueOf(clazz.getModifiers());
    }

    @Signature(@Arg("annotationClass"))
    @SuppressWarnings("unchecked")
    public Memory isAnnotationPresent(Environment env, Memory... args){
        try {
            return clazz.isAnnotationPresent((Class<? extends Annotation>) Class.forName(args[0].toString()))
                    ? Memory.TRUE : Memory.FALSE;
        } catch (ClassNotFoundException | ClassCastException e) {
            exception(env, e);
        }

        return Memory.NULL;
    }

    @Signature
    public Memory getComponentType(Environment env, Memory... args){
        return new ObjectMemory(JavaClass.of(env, clazz.getComponentType()));
    }

    @Signature
    public Memory getInterfaces(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();
        for(Class<?> el : clazz.getInterfaces()){
            result.add(new ObjectMemory(JavaClass.of(env, el)));
        }
        return result.toConstant();
    }

    @Signature({@Arg("name"), @Arg(value = "types", type = HintType.ARRAY, optional = @Optional(type = HintType.ARRAY))})
    public Memory getDeclaredMethod(Environment env, Memory... args){
        try {
            return new ObjectMemory(JavaMethod.of(env,
                    clazz.getDeclaredMethod(args[0].toString(), types(env, args[1].toValue(ArrayMemory.class)))
            ));
        } catch (NoSuchMethodException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature
    public Memory getDeclaredMethods(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();
        for(Method method : clazz.getDeclaredMethods()){
            result.add(new ObjectMemory(
                JavaMethod.of(env, method)
            ));
        }
        return result.toConstant();
    }

    @Signature(@Arg("name"))
    public Memory getDeclaredField(Environment env, Memory... args){
        try {
            Field field = clazz.getDeclaredField(args[0].toString());
            return new ObjectMemory(JavaField.of(env, field));
        } catch (NoSuchFieldException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature
    public Memory getDeclaredFields(Environment env, Memory... args) {
        ArrayMemory result = new ArrayMemory();
        for(Field field : clazz.getDeclaredFields()){
            result.put(field.getName(), new ObjectMemory(JavaField.of(env, field)));
        }
        return result.toConstant();
    }

    @Signature
    public Memory newInstance(Environment env, Memory... args){
        try {
            return new ObjectMemory(JavaObject.of(env, clazz.newInstance()));
        } catch (InstantiationException | IllegalAccessException e) {
            exception(env, e);
        }
        return Memory.NULL;
    }

    @Signature({@Arg(value = "types", type = HintType.ARRAY), @Arg(value = "arguments", type = HintType.ARRAY)})
    public Memory newInstanceArgs(Environment env, Memory... args){
        try {
            Constructor constructor = clazz.getConstructor(types(env, args[0].toValue(ArrayMemory.class)));
            MemoryUtils.Converter[] converters = MemoryUtils.getConverters(constructor.getParameterTypes());
            Memory[] passed = args[1].toValue(ArrayMemory.class).values();
            if (passed.length != converters.length)
                throw new IllegalArgumentException("Invalid argument count");

            return new ObjectMemory(JavaObject.of(env,
                    constructor.newInstance(JavaMethod.makePassed(env, converters, passed))
            ));
        } catch (NoSuchMethodException | InstantiationException | IllegalArgumentException | IllegalAccessException e) {
            exception(env, e);
        } catch (InvocationTargetException e) {
            exception(env, e.getTargetException());
        }
        return Memory.NULL;
    }

    @Signature(@Arg(value = "class", nativeType = JavaClass.class))
    public Memory isAssignableFrom(Environment env, Memory... args){
        JavaClass javaClass = ((JavaClass)args[0].toValue(ObjectMemory.class).value);
        return clazz.isAssignableFrom(javaClass.clazz) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("className"))
    public Memory isSubClass(Environment env, Memory... args){
        try {
            Class<?> clazz = Class.forName(args[0].toString());
            return clazz.isAssignableFrom(clazz) ? Memory.TRUE : Memory.FALSE;
        } catch (ClassNotFoundException e) {
            exception(env, e);
        }
        return Memory.FALSE;
    }

    @Signature
    public Memory getEnumConstants(Environment env, Memory... args){
        ArrayMemory result = new ArrayMemory();
        for(Object el : clazz.getEnumConstants()){
            result.add(new ObjectMemory(JavaObject.of(env, el)));
        }
        return result;
    }

    @Signature(@Arg("name"))
    public Memory getResource(Environment env, Memory... args){
        URL url = clazz.getResource(args[0].toString());
        if (url == null || url.getFile() == null)
            return Memory.NULL;

        return new StringMemory(url.getFile());
    }

    public static JavaClass of(Environment env, Class<?> clazz){
        return new JavaClass(env, clazz);
    }

    private final static Map<String, Class<?>> primitives = new HashMap<String, Class<?>>(){{
        put("int", Integer.TYPE);
        put("byte", Byte.TYPE);
        put("short", Short.TYPE);
        put("char", Character.TYPE);
        put("long", Long.TYPE);
        put("float", Float.TYPE);
        put("double", Double.TYPE);
        put("boolean", Boolean.TYPE);
        put("string", String.class);
    }};

    @Signature(@Arg("name"))
    public static Memory primitive(Environment env, Memory... args){
        String name = args[0].toString();
        Class<?> cls = primitives.get(name);
        if (cls == null || !cls.isPrimitive())
            exception(env, new ClassNotFoundException(name));

        return new ObjectMemory(JavaClass.of(env, cls));
    }

    public static Class<?>[] types(Environment env, ArrayMemory arrays){
        Class<?>[] result = new Class[arrays.size()];

        int i = 0;
        for(Memory el : arrays.values()){
            String name = el.toString();
            Class<?> cls = primitives.get(name);
            if (cls != null)
                result[i] = cls;
            else {
                try {
                    result[i] = Class.forName(name);
                } catch (ClassNotFoundException e) {
                    exception(env, e);
                }
            }
            i++;
        }
        return result;
    }
}
