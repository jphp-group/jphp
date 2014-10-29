package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.Messages;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.util.WrapRegex;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.support.ReflectionUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CompileMethodEntity extends MethodEntity {
    protected CompileMethod function;

    public CompileMethodEntity(Extension extension) {
        super((Context) null);

        function = new CompileMethod();
        setExtension(extension);
    }

    public void addMethod(Method method) {
        CompileMethod.Method compileMethod = function.addMethod(method);

        int mods = method.getModifiers();

        setName(method.getName());
        setStatic(Modifier.isStatic(mods));
        setDeprecated(method.getAnnotation(Deprecated.class) != null);

        if (Modifier.isProtected(mods)) {
            setModifier(php.runtime.common.Modifier.PROTECTED);
        } else if (Modifier.isPrivate(mods)) {
            setModifier(php.runtime.common.Modifier.PRIVATE);
        } else {
            setModifier(php.runtime.common.Modifier.PUBLIC);
        }

        setReturnReference(method.getAnnotation(Reflection.Reference.class) != null);
        setFinal(Modifier.isFinal(mods));
        setAbstract(Modifier.isAbstract(mods));
        setInternalName(method.getName());

        ParameterEntity[] parameters = new ParameterEntity[method.getParameterTypes().length];
        Annotation[][] annotations   = method.getParameterAnnotations();

        int i = 0;
        for (Class<?> el : method.getParameterTypes()) {
            if (el == Environment.class || el == TraceInfo.class) {
                continue;
            }

            ParameterEntity param = new ParameterEntity(context);
            param.setName("arg" + i);

            Annotation[] argAnnotations = method.getParameterAnnotations()[i];

            if (ReflectionUtils.getAnnotation(argAnnotations, Reflection.Nullable.class) != null) {
                param.setNullable(true);
            }

            parameters[i++] = param;
        }

        compileMethod.setParameters(parameters);
    }

    @Override
    public Memory invokeDynamic(IObject _this, Environment env, Memory... arguments) throws Throwable {
        try {
            TraceInfo trace = env.trace();
            if (isAbstract){
                env.error(ErrorType.E_ERROR, "Cannot call abstract method %s", getSignatureString(false));
                return Memory.NULL;
            }

            if (_this == null && !isStatic){
                _this = clazz.newMock(env);
                if (_this == null)
                    env.error(ErrorType.E_ERROR, Messages.ERR_STATIC_METHOD_CALLED_DYNAMICALLY.fetch(
                                    getClazz().getName() + "::" + getName())
                    );
            }

            CompileMethod.Method method = function.find(arguments == null ? 0 : arguments.length);
            if (method == null){
                env.warning(trace, Messages.ERR_EXPECT_LEAST_PARAMS.fetch(
                        name, function.getMinArgs(), arguments == null ? 0 : arguments.length
                ));
                return Memory.NULL;
            } else {
                if (arguments != null && arguments.length > method.argsCount && !method.isVarArg()) {
                    env.warning(trace, Messages.ERR_EXPECT_EXACTLY_PARAMS,
                            name, method.argsCount, arguments.length
                    );
                    return Memory.NULL;
                }
            }

            ParameterEntity[] parameters = method.parameters;

            Class<?>[] types = method.parameterTypes;
            Object[] passed = new Object[ types.length ];

            int i = 0;
            int j = 0;
            for(Class<?> clazz : types) {
                boolean isRef        = method.references[i];
                boolean mutableValue = method.mutableValues[i];

                MemoryOperation<?> operation = method.argumentOperations[i];

                if (clazz == Memory.class) {
                    passed[i] = isRef ? arguments[j] : (mutableValue ? arguments[j].toValue() : arguments[j].toImmutable());
                    j++;
                } else if (operation != null) {
                    if (operation instanceof InjectMemoryOperation) {
                        passed[i] = operation.convert(env, trace, null);
                    } else {
                        passed[i] = operation.convert(env, trace, arguments[j]);
                        j++;
                    }
                } else if (i == types.length - 1 && types[i] == Memory[].class){
                    Memory[] arg = new Memory[arguments.length - i + 1];
                    if (!isRef){
                        for(int k = 0; k < arg.length; k++)
                            arg[i] = arguments[i].toImmutable();
                    } else {
                        System.arraycopy(arguments, j, arg, 0, arg.length);
                    }
                    passed[i] = arg;
                    break;
                } else {
                    env.error(trace, ErrorType.E_CORE_ERROR, name + "(): Cannot call this method dynamically");
                    passed[i] = Memory.NULL;
                }
                i++;
            }

            if (method.resultType == void.class){
                method.method.invoke(_this, passed);
                return Memory.NULL;
            } else
                return MemoryUtils.valueOf(method.method.invoke(_this, passed));

        } catch (InvocationTargetException e){
            return env.__throwException(e);
        } catch (Throwable e) {
            throw e;
        } finally {
            unsetArguments(arguments);
        }
    }

    @Override
    public ParameterEntity[] getParameters(int count) {
        CompileMethod.Method result = function.find(count);
        if (result == null) {
            return new ParameterEntity[0];
        }

        return result.parameters;
    }

    abstract public static class InjectMemoryOperation extends MemoryOperation {
        @Override
        public Class<?>[] getOperationClasses() {
            return new Class<?>[0];
        }

        @Override
        public Memory unconvert(Environment env, TraceInfo trace, Object arg) {
            throw new CriticalException("Unsupported unconvert");
        }
    }

    public static class ParameterEntity extends php.runtime.reflection.ParameterEntity {
        public ParameterEntity(Context context) {
            super(context);
        }
    }

    public static class CompileMethod extends CompileFunction {
        public CompileMethod() {
            super(null);
        }

        @Override
        public CompileMethod.Method addMethod(java.lang.reflect.Method method) {
            return (Method) super.addMethod(method);
        }

        @Override
        public CompileMethod.Method addMethod(java.lang.reflect.Method method, boolean asImmutable) {
            return (Method) super.addMethod(method, asImmutable);
        }

        @Override
        public CompileMethod.Method find(int paramCount) {
            return (Method) super.find(paramCount);
        }

        @Override
        protected CompileFunction.Method createMethod(java.lang.reflect.Method method, int count, boolean asImmutable) {
            return new Method(method, count, asImmutable);
        }

        public static class Method extends CompileFunction.Method {
            protected MemoryOperation[] argumentOperations;
            protected MemoryOperation returnOperation;

            protected ParameterEntity[] parameters;

            public Method(java.lang.reflect.Method method, int argsCount, boolean _asImmutable) {
                super(method, argsCount, _asImmutable);
            }

            public void setParameters(ParameterEntity[] parameters) {
                this.parameters = parameters;

                returnOperation    = MemoryOperation.get(resultType, method.getGenericReturnType());
                if (returnOperation == null) {
                    throw new CriticalException("Unsupported type for binding - " + resultType);
                }

                argumentOperations = new MemoryOperation[parameterTypes.length];

                MemoryOperation op;
                for (int i = 0; i < argumentOperations.length; i++) {
                    op = argumentOperations[i] = MemoryOperation.get(parameterTypes[i], method.getGenericParameterTypes()[i]);

                    if (op != null) {
                        op.applyTypeHinting(parameters[i]);
                    } else {
                        if (parameterTypes[i] == Environment.class) {
                            argumentOperations[i] = new InjectMemoryOperation() {
                                @Override
                                public Object convert(Environment env, TraceInfo trace, Memory arg) {
                                    return env;
                                }
                            };
                        } else if (parameterTypes[i] == TraceInfo.class) {
                            argumentOperations[i] = new InjectMemoryOperation() {
                                @Override
                                public Object convert(Environment env, TraceInfo trace, Memory arg) {
                                    return trace;
                                }
                            };
                        } else {
                            throw new CriticalException("Unsupported type for binding - " + parameterTypes[i]);
                        }
                    }
                }
            }
        }
    }
}
