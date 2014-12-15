package php.runtime.wrap;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.support.Extension;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassWrapper {
    protected final Extension extension;
    protected final CompileScope scope;
    protected final Class<?> nativeClass;

    public ClassWrapper(CompileScope scope, Class<?> clazz) {
        this(null, scope, clazz);
    }

    public ClassWrapper(Extension extension, CompileScope scope, Class<?> clazz) {
        this.extension = extension;
        this.scope = scope;
        this.nativeClass = clazz;
    }

    public Extension getExtension() {
        return extension;
    }

    public CompileScope getScope() {
        return scope;
    }

    public Class<?> getNativeClass() {
        return nativeClass;
    }

    protected void onWrapName(ClassEntity classEntity) {
        String namespace = extension == null ? "" : extension.getNamespace();

        if (nativeClass.isAnnotationPresent(Reflection.Name.class)){
            Reflection.Name name = nativeClass.getAnnotation(Reflection.Name.class);
            classEntity.setName(namespace.isEmpty() ? name.value() : namespace + "\\" + name.value());
        } else {
            classEntity.setName(namespace.isEmpty() ? nativeClass.getSimpleName() : namespace + "\\" + nativeClass.getSimpleName());
        }

        classEntity.setInternalName(nativeClass.getName().replace('.', '/'));
    }

    protected void onWrapConstants(ClassEntity classEntity) {
        for(Field field : nativeClass.getDeclaredFields()){
            int mod = field.getModifiers();
            if (field.isAnnotationPresent(Reflection.Ignore.class))
                continue;

            if (Modifier.isFinal(mod) && Modifier.isStatic(mod) && !Modifier.isPrivate(mod)){
                try {
                    field.setAccessible(true);
                    classEntity.addConstant(new ConstantEntity(
                            field.getName(), MemoryUtils.valueOf(field.get(null)), true
                    ));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    protected void onWrapProperty(ClassEntity classEntity, Reflection.Arg arg) {
        PropertyEntity entity = new PropertyEntity(classEntity.getContext());
        entity.setClazz(classEntity);
        entity.setModifier(arg.modifier());
        entity.setName(arg.value());
        entity.setStatic(false);

        if (arg.optional().exists()){
            entity.setDefaultValue(MemoryUtils.valueOf(arg.optional().value(), arg.optional().type()));
        } else {
            entity.setDefaultValue(null);
        }

        classEntity.addProperty(entity);
    }

    protected void onWrapCompileProperty(ClassEntity classEntity, Field field, Reflection.Property property) {
        CompilePropertyEntity entity = new CompilePropertyEntity(classEntity.getContext(), field);
        entity.setClazz(classEntity);

        if (property.value().isEmpty()) {
            entity.setName(field.getName());
        } else {
            entity.setName(property.value());
        }

        classEntity.addProperty(entity);
    }

    protected void onWrapProperties(ClassEntity classEntity) {
        Reflection.Signature signature = nativeClass.getAnnotation(Reflection.Signature.class);
        if (signature != null){
            for(Reflection.Arg arg : signature.value()){
                onWrapProperty(classEntity, arg);
            }
        }

        for(Field field : nativeClass.getDeclaredFields()) {
            Reflection.Property property = field.getAnnotation(Reflection.Property.class);

            if (property != null) {
                onWrapCompileProperty(classEntity, field, property);
            }
        }

        Reflection.WrapInterface interfaces = nativeClass.getAnnotation(Reflection.WrapInterface.class);

        if (interfaces != null && interfaces.wrapFields() && BaseWrapper.class.isAssignableFrom(nativeClass)) {
            Class<?> bindClass = MemoryOperation.getClassOfWrapper(
                    (Class<? extends php.runtime.lang.BaseWrapper<Object>>) nativeClass
            );

            for (Class _interface : interfaces.value()) {
                for (Field field : _interface.getDeclaredFields()) {
                    try {
                        Field _field = bindClass.getDeclaredField(field.getName());
                        int mods = _field.getModifiers();

                        if ((/*Modifier.isProtected(mods) || */Modifier.isPublic(mods)) && !Modifier.isStatic(mods)) {
                            if (interfaces.skipConflicts()
                                    && MemoryOperation.get(_field.getType(), _field.getGenericType()) == null) {
                                continue;
                            }

                            PropertyEntity entity = new WrapCompilePropertyEntity(classEntity.getContext(), _field);
                            entity.setName(_field.getName());

                            classEntity.addProperty(entity);
                        }
                    } catch (NoSuchFieldException e) {
                        throw new CriticalException(e);
                    }
                }
            }
        }
    }

    protected void onWrapArgument(ParameterEntity param, Reflection.Arg arg) {
        param.setReference(arg.reference());
        param.setType(arg.type());
        param.setName(arg.value());
        param.setNullable(arg.nullable());
        param.setTypeEnum(arg.typeEnum());

        param.setDefaultValue(null);
        if (arg.optional().exists()
                || !arg.optional().value().isEmpty()
                || (arg.type() != HintType.STRING && !arg.optional().value().isEmpty())){
            param.setDefaultValue(MemoryUtils.valueOf(arg.optional().value(), arg.optional().type()));
        }
    }

    protected void onWrapMethod(ClassEntity classEntity, Method method) {
        MethodEntity entity = new MethodEntity(extension, method);
        entity.setClazz(classEntity);
        entity.setNativeMethod(method);
        entity.setAbstract(Modifier.isAbstract(method.getModifiers()));
        entity.setFinal(Modifier.isFinal(method.getModifiers()));
        entity.setStatic(Modifier.isStatic(method.getModifiers()));

        if (classEntity.isInterface()){
            entity.setAbstractable(true);
            entity.setAbstract(false);
        }

        if (entity.isAbstract())
            entity.setAbstractable(true);

        if (method.isAnnotationPresent(Reflection.Final.class)) {
            entity.setFinal(true);
        }

        if (method.isAnnotationPresent(Reflection.Abstract.class)) {
            entity.setAbstract(true);
            entity.setAbstractable(true);
        }

        entity.setInternalName(method.getName());

        Reflection.Name name = method.getAnnotation(Reflection.Name.class);
        entity.setName(name == null ? method.getName() : name.value());

        Reflection.Signature sign = method.getAnnotation(Reflection.Signature.class);
        ParameterEntity[] params = new ParameterEntity[sign.value().length];

        int i = 0;
        for (Reflection.Arg arg : sign.value()){
            ParameterEntity param = new ParameterEntity(classEntity.getContext());
            onWrapArgument(param, arg);
            params[i++] = param;
        }

        entity.setParameters(params);
        classEntity.addMethod(entity, null);
    }

    protected MethodEntity onWrapWrapCompileMethod(ClassEntity classEntity, Method method, boolean skipConflicts) {
        MethodEntity _entity = classEntity.findMethod(method.getName().toLowerCase());

        WrapCompileMethodEntity entity;
        if (_entity instanceof WrapCompileMethodEntity) {
            entity = (WrapCompileMethodEntity) _entity;
        } else {
            entity = new WrapCompileMethodEntity(classEntity.getExtension());
        }

        entity.addMethod(method, skipConflicts);
        if (_entity == null) {
            entity.setClazz(classEntity);
            classEntity.addMethod(entity, null);
        }

        return entity;
    }


    protected void onWrapCompileMethod(ClassEntity classEntity, Method method) {
        MethodEntity _entity = classEntity.findMethod(method.getName().toLowerCase());

        CompileMethodEntity entity;
        if (_entity instanceof CompileMethodEntity) {
            entity = (CompileMethodEntity) _entity;
        } else {
            entity = new CompileMethodEntity(classEntity.getExtension());
        }

        if (classEntity.isInterface()){
            entity.setAbstractable(true);
            entity.setAbstract(false);
        }

        entity.addMethod(method, false);
        if (_entity == null) {
            entity.setClazz(classEntity);
            classEntity.addMethod(entity, null);
        }
    }

    protected void onWrapMethods(ClassEntity classEntity) {
        Reflection.WrapInterface interfaces = nativeClass.getAnnotation(Reflection.WrapInterface.class);

        if (interfaces != null && BaseWrapper.class.isAssignableFrom(nativeClass)) {
            Class<?> bindClass = MemoryOperation.getClassOfWrapper(
                    (Class<? extends php.runtime.lang.BaseWrapper<Object>>) nativeClass
            );

            for (Class _interface : interfaces.value()) {
                for (Method method : _interface.getDeclaredMethods()) {

                    try {
                        MethodEntity entity = onWrapWrapCompileMethod(
                                classEntity, bindClass.getDeclaredMethod(method.getName(), method.getParameterTypes()),
                                interfaces.skipConflicts()
                        );

                        if (_interface.isInterface()) {
                            entity.setAbstractable(false);
                            entity.setAbstract(false);
                        }
                    } catch (NoSuchMethodException e) {
                        throw new CriticalException(e);
                    }
                }
            }
        }

        for (Method method : nativeClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Reflection.Signature.class)){
                Class<?>[] types = method.getParameterTypes();

                if (method.getReturnType() == Memory.class
                        && types.length == 2 && types[0] == Environment.class && types[1] == Memory[].class) {
                    onWrapMethod(classEntity, method);
                } else {
                    onWrapCompileMethod(classEntity, method);
                }
            }
        }
    }

    protected void onWrapExtend(ClassEntity classEntity) {
        if (nativeClass.isAnnotationPresent(Reflection.BaseType.class)) {
            return;
        }

        Class<?> extend = nativeClass.getSuperclass();
        if (extend != null && !extend.isAnnotationPresent(Reflection.Ignore.class)){
            String name = extend.getSimpleName();
            if (extend.isAnnotationPresent(Reflection.Name.class)){
                name = extend.getAnnotation(Reflection.Name.class).value();
            }
            ClassEntity entity = scope.fetchUserClass(name);
            if (extend.isAssignableFrom(IObject.class) && entity == null)
                throw new IllegalArgumentException("Class '"+name+"' not registered for '" + classEntity.getName() + "'");
            if (entity == null)
                return;

            ClassEntity.ExtendsResult result = classEntity.setParent(entity, false);
            result.check(null);
            classEntity.updateParentMethods();
        }
    }

    protected void onWrapImplement(ClassEntity classEntity) {
        for (Class<?> interface_ : nativeClass.getInterfaces()){
            if (interface_.isAnnotationPresent(Reflection.Ignore.class)) continue;
            if (interface_.getPackage().getName().startsWith("java.")) continue;

            String name = interface_.getSimpleName();
            if (interface_.isAnnotationPresent(Reflection.Name.class)){
                name = interface_.getAnnotation(Reflection.Name.class).value();
            }
            ClassEntity entity = scope.fetchUserClass(name);
            /*if (entity == null || !entity.isInterface())
                throw new IllegalArgumentException("Interface '"+name+"' not registered");*/
            if (entity == null)
                return;

            ClassEntity.ImplementsResult result = classEntity.addInterface(entity);
            result.check(null);
        }
    }

    public void onWrap(ClassEntity classEntity) {
        int mod = nativeClass.getModifiers();

        classEntity.setInternal(true);
        classEntity.setNativeClazz(nativeClass);
        classEntity.setExtension(extension);

        classEntity.setType(nativeClass.isInterface() ? ClassEntity.Type.INTERFACE : ClassEntity.Type.CLASS);
        if (nativeClass.isAnnotationPresent(Reflection.Trait.class)) {
            classEntity.setType(ClassEntity.Type.TRAIT);
        }

        classEntity.setAbstract(Modifier.isAbstract(mod));
        classEntity.setFinal(Modifier.isFinal(mod));
        classEntity.setNotRuntime(nativeClass.isAnnotationPresent(Reflection.NotRuntime.class));

        if (nativeClass.isAnnotationPresent(Reflection.Final.class)) {
            classEntity.setFinal(true);
        }

        if (nativeClass.isAnnotationPresent(Reflection.Abstract.class)) {
            classEntity.setAbstract(true);
        }

        this.onWrapName(classEntity);
        if (!classEntity.isTrait()) {
            this.onWrapConstants(classEntity);
        }

        this.onWrapProperties(classEntity);

        // ---
        classEntity.setNativeClazz(nativeClass);
        // ---

        this.onWrapMethods(classEntity);

        if (this.scope != null)
            this.onWrapExtend(classEntity);

        this.onWrapImplement(classEntity);

        classEntity.doneDeclare();
    }
}
