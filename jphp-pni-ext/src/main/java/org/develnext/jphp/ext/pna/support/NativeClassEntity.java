package org.develnext.jphp.ext.pna.support;

import org.develnext.jphp.ext.pna.PhpNativeInterfaceExtension;
import org.develnext.jphp.ext.pna.classes.NativeObject;
import php.runtime.Memory;
import php.runtime.common.CallbackW;
import php.runtime.common.Messages;
import php.runtime.common.Modifier;
import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.invoke.InvokeHelper;
import php.runtime.lang.IObject;
import php.runtime.memory.*;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.*;
import php.runtime.reflection.support.TypeChecker;
import php.runtime.wrap.ClassWrapper;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class NativeClassEntity extends ClassEntity {
    protected final Class<?> rawClass;
    private final CompileScope scope;

    public NativeClassEntity(CompileScope scope, Class<?> rawClass) {
        super(new ClassWrapper(scope, NativeObject.class));

        this.scope = scope;
        setId(scope.nextClassIndex());

        this.setNativeClazz(NativeObject.class);
        this.rawClass = rawClass;

        //MemoryOperation.registerWrapper(rawClass, NativeObject.class);

        setName(PhpNativeInterfaceExtension.getNativeName(rawClass));
    }

    @Override
    public <T extends IObject> T newObject(Environment env, TraceInfo trace, boolean doConstruct, Memory... args) throws Throwable {

        Object instance = null;

        for (Constructor<?> construct : rawClass.getConstructors()) {
            Class<?>[] parameterTypes = construct.getParameterTypes();

            if ((args == null && parameterTypes.length == 0) || (parameterTypes.length == args.length)) {
                Object[] passed = new Object[args == null ? 0 : args.length];

                boolean next = false;

                for (int i = 0; i < passed.length; i++) {
                    MemoryOperation op = MemoryOperation.get(parameterTypes[i], construct.getGenericParameterTypes()[i]);

                    if (op == null) {
                        next = true;
                        break;
                    }

                    passed[i] = op.convert(env, trace, args[i]);
                }

                if (next) continue;

                try {
                    instance = construct.newInstance(passed);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
        }

        NativeObject object = (NativeObject) super.newObject(env, trace, doConstruct, args);

        object.__setNativeObject(instance);

        return (T) object;
    }

    @Override
    public PropertyEntity findStaticProperty(String name) {
        try {
            PropertyEntity property = super.findStaticProperty(name);

            if (property != null) {
                return property;
            }

            synchronized (staticProperties) {
                property = super.findStaticProperty(name);

                if (property != null) {
                    return property;
                }

                Field field = rawClass.getField(name);
                property = new NativePropertyEntity(field);
                property.setName(name);

                addStaticProperty(property);

                return property;
            }
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    public MethodEntity findMethod(String name) {
        MethodEntity method = super.findMethod(name);

        if (method != null) {
            return method;
        }

        if (rawClass == null) {
            return null;
        }

        synchronized (methods) {
            method = super.findMethod(name);

            if (method != null) {
                return method;
            }

            CompileMethodEntity function = new WrapCompileMethodEntity(null);
            function.setName(name);
            function.setClazz(this);
            function.setTrace(TraceInfo.UNKNOWN);

            boolean exists = false;

            for (Method one : rawClass.getMethods()) {
                if (one.getName().equalsIgnoreCase(name)) {
                    if (function.addMethod(one, true, unknownDynamicTypeFetcher)) {
                        function.setName(one.getName());
                        exists = true;
                    }
                }
            }

            if (exists) {
                addMethod(function, function.getName());
                return function;
            } else {
                return null;
            }
        }
    }

    public Class<?> getRawClass() {
        return rawClass;
    }

    static class NativePropertyEntity extends PropertyEntity {
        private final Field field;
        protected MemoryOperation operationSet;
        protected MemoryOperation operationGet;

        public NativePropertyEntity(Field field) {
            super((Context) null);
            this.field = field;
            this.field.setAccessible(true);
            setTrace(TraceInfo.UNKNOWN);

            setModifier(Modifier.PUBLIC);

            if (java.lang.reflect.Modifier.isProtected(field.getModifiers())) {
                setModifier(php.runtime.common.Modifier.PROTECTED);
            } else if (java.lang.reflect.Modifier.isPrivate(field.getModifiers())) {
                throw new CriticalException("Unsupported bind private fields: " + field.toGenericString());
            }

            setStatic(java.lang.reflect.Modifier.isStatic(field.getModifiers()));

            this.operationSet = MemoryOperation.get(field.getType(), field.getGenericType());

            if (this.operationSet != null) {
                this.operationGet = this.operationSet;
            } else {
                this.operationGet = MemoryOperation.get(field.getType(), field.getGenericType(), true);
            }
        }

        @Override
        public Memory assignValue(Environment env, TraceInfo trace, Object object, String name, Memory value) {
            try {
                if (operationSet == null) {
                    env.error(trace, ErrorType.E_ERROR, Messages.ERR_READONLY_PROPERTY.fetch(clazz.getName(), name));
                    return Memory.NULL;
                }

                field.set(object, operationSet.convertNoThrow(env, trace, value));
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            }
            return value;
        }

        @Override
        public Memory getStaticValue(final Environment env, final TraceInfo trace) {
            try {
                Memory memory = operationGet.unconvertNoThow(env, trace, field.get(null));

                return new ReferenceMemory(memory) {
                    @Override
                    public Memory assign(Memory memory) {
                        return assignValue(env, trace, null, name, memory);
                    }

                    @Override
                    public Memory assign(long memory) {
                        return assignValue(env, trace, null, name, LongMemory.valueOf(memory));
                    }

                    @Override
                    public Memory assign(String memory) {
                        return assignValue(env, trace, null, name, StringMemory.valueOf(memory));
                    }

                    @Override
                    public Memory assign(boolean memory) {
                        return assignValue(env, trace, null, name, TrueMemory.valueOf(memory));
                    }

                    @Override
                    public Memory assign(double memory) {
                        return assignValue(env, trace, null, name, DoubleMemory.valueOf(memory));
                    }
                };
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            }
        }

        @Override
        public Memory getValue(Environment env, TraceInfo trace, Object object) {
            try {
                return operationGet.unconvertNoThow(env, trace, field.get(object));
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            }
        }
    }

    protected final UnknownDynamicTypeFetcher unknownDynamicTypeFetcher = new UnknownDynamicTypeFetcher();

    class UnknownDynamicTypeFetcher extends CallbackW<MemoryOperation, Class<?>, java.lang.reflect.Type> {
        @Override
        public MemoryOperation call(Class<?> aClass, java.lang.reflect.Type type) {
            final String typeName = PhpNativeInterfaceExtension.getNativeName(aClass);
            scope.fetchUserClass(typeName);

            return new MemoryOperation() {
                @Override
                public Class<?>[] getOperationClasses() {
                    return new Class<?>[]{rawClass};
                }

                @Override
                public Object convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
                    return arg.toObject(NativeObject.class).getWrappedObject();
                }

                @Override
                public Memory unconvert(Environment env, TraceInfo trace, Object arg) throws Throwable {
                    return ObjectMemory.valueOf(new NativeObject(env, NativeClassEntity.this, arg));
                }

                @Override
                public void applyTypeHinting(ParameterEntity parameter) {
                    parameter.setTypeChecker(TypeChecker.of(typeName));
                }
            };
        }
    }
}
