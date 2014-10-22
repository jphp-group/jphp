package php.runtime.wrap;

import org.develnext.jphp.core.compiler.jvm.Constants;
import org.develnext.jphp.core.compiler.jvm.node.MethodNodeImpl;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import php.runtime.annotation.Reflection;
import php.runtime.common.HintType;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.support.Extension;
import php.runtime.lang.IObject;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.getMethodDescriptor;

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

    protected void onWrapProperties(ClassEntity classEntity) {
        Reflection.Signature signature = nativeClass.getAnnotation(Reflection.Signature.class);
        if (signature != null){
            for(Reflection.Arg arg : signature.value()){
                onWrapProperty(classEntity, arg);
            }
        }
    }

    protected void onWrapArgument(ParameterEntity param, Reflection.Arg arg) {
        param.setReference(arg.reference());
        param.setType(arg.type());
        param.setName(arg.value());

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
        entity.setAbstract(java.lang.reflect.Modifier.isAbstract(method.getModifiers()));
        entity.setFinal(java.lang.reflect.Modifier.isFinal(method.getModifiers()));
        entity.setStatic(java.lang.reflect.Modifier.isStatic(method.getModifiers()));

        if (classEntity.isInterface()){
            entity.setAbstractable(true);
            entity.setAbstract(false);
        }

        if (entity.isAbstract())
            entity.setAbstractable(true);

        if (method.isAnnotationPresent(Reflection.Final.class)) {
            entity.setFinal(true);
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

    protected void onWrapMethods(ClassEntity classEntity) {
        for (Method method : nativeClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(Reflection.Signature.class)){
                onWrapMethod(classEntity, method);
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

    public void onWrapUseTraits(ClassEntity classEntity) {
        Reflection.UseTraits useTraits = nativeClass.getAnnotation(Reflection.UseTraits.class);
        if (useTraits != null) {
            for (Class<? extends IObject> traitClass : useTraits.value()) {
                ClassEntity traitEntity = scope.fetchUserClass(traitClass);
                classEntity.addTrait(traitEntity);

                ClassNode classNode = classEntity.getClassNode();
                for(MethodEntity methodEntity : traitEntity.getMethods().values()) {
                    MethodEntity origin = classEntity.findMethod(methodEntity.getLowerName());

                    MethodEntity dup = methodEntity.duplicateForInject();
                    dup.setClazz(classEntity);
                    dup.setTrait(traitEntity);

                    MethodNodeImpl methodNode = MethodNodeImpl.duplicate(methodEntity.getMethodNode());

                    if (origin != null) {
                        dup.setPrototype(origin);
                    }

                    dup.setInternalName(dup.getName() + "$" + classEntity.nextMethodIndex());
                    methodNode.name = dup.getInternalName();

                    ClassEntity.SignatureResult result = classEntity.addMethod(dup, null);
                    result.check(null);

                    classNode.methods.add(methodNode);
                }

                classNode.superName = classEntity.getInternalName();
                classNode.name = classEntity.getInternalName().replace("/", "_") + "$withTraits";
                classEntity.setInternalName(classNode.name);
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            MethodNode constructor = new MethodNodeImpl();
            constructor.name = Constants.INIT_METHOD;
            constructor.access = ACC_PUBLIC;
            constructor.desc = getMethodDescriptor(
                    org.objectweb.asm.Type.getType(void.class),
                    org.objectweb.asm.Type.getType(Environment.class),
                    org.objectweb.asm.Type.getType(ClassEntity.class)
            );
            constructor.instructions = new InsnList();
            LabelNode startL = new LabelNode();
            constructor.instructions.add(startL);

            constructor.instructions.add(new VarInsnNode(ALOAD, 0));
            constructor.instructions.add(new VarInsnNode(ALOAD, 1));
            constructor.instructions.add(new VarInsnNode(ALOAD, 2));
            constructor.instructions.add(new MethodInsnNode(
                    INVOKESPECIAL,
                    classEntity.getClassNode().superName,
                    Constants.INIT_METHOD,
                    constructor.desc,
                    false
            ));
            LabelNode endL = new LabelNode();
            constructor.instructions.add(endL);
            constructor.localVariables.add(new LocalVariableNode("this", "L" + classEntity.getClassNode().name + ";", null, startL, endL, 0));
            constructor.localVariables.add(new LocalVariableNode("env", org.objectweb.asm.Type.getDescriptor(Environment.class), null, startL, endL, 1));
            constructor.localVariables.add(new LocalVariableNode("cls", org.objectweb.asm.Type.getDescriptor(ClassEntity.class), null, startL, endL, 2));

            constructor.instructions.add(new InsnNode(RETURN));

            classEntity.getClassNode().methods.set(0, constructor);
            classEntity.getClassNode().accept(classWriter);
            classEntity.setData(classWriter.toByteArray());

            try {
                scope.getClassLoader().loadClass(classEntity);
            } catch (NoSuchMethodException e) {
                throw new CriticalException(e);
            } catch (NoSuchFieldException e) {
                throw new CriticalException(e);
            }
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

        this.onWrapName(classEntity);
        if (!classEntity.isTrait()) {
            this.onWrapConstants(classEntity);
        }

        this.onWrapProperties(classEntity);

        // ---
        classEntity.setNativeClazz(nativeClass);
        this.onWrapUseTraits(classEntity);
        // ---

        this.onWrapMethods(classEntity);

        if (this.scope != null)
            this.onWrapExtend(classEntity);

        this.onWrapImplement(classEntity);

        classEntity.doneDeclare();
    }
}
