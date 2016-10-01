package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.ext.support.compile.CompileFunctionSpec;
import php.runtime.memory.support.MemoryUtils;

import java.lang.reflect.InvocationTargetException;

public class CompileFunctionEntity extends FunctionEntity {
    private final CompileFunctionSpec compileFunctionSpec;
    private CompileFunction compileFunction;

    public CompileFunctionEntity(Extension extension, CompileFunctionSpec compileFunction) {
        super(null);
        this.compileFunctionSpec = compileFunction;
        this.setName(compileFunction.getName());
        this.setExtension(extension);
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    public CompileFunction getCompileFunction() {
        if (compileFunction != null) {
            return compileFunction;
        }

        synchronized (compileFunctionSpec) {
            if (compileFunction != null) {
                return compileFunction;
            }

            compileFunction = compileFunctionSpec.toFunction();
        }

        return compileFunction;
    }

    @Override
    public Memory invoke(Environment env, TraceInfo trace, Memory[] arguments) throws Throwable {
        CompileFunction.Method method = getCompileFunction().find(arguments.length);
        if (method == null){
            env.warning(trace, Messages.ERR_EXPECT_LEAST_PARAMS.fetch(
                    name, compileFunction.getMinArgs(), arguments.length
            ));
            return Memory.NULL;
        } else {
            if (arguments.length > method.argsCount && !method.isVarArg()) {
                env.warning(trace, Messages.ERR_EXPECT_EXACTLY_PARAMS,
                        name, method.argsCount, arguments.length
                );
                return Memory.NULL;
            }
        }

        Class<?>[] types = method.parameterTypes;
        Object[] passed = new Object[ types.length ];

        int i = 0;
        int j = 0;

        for(Class<?> clazz : types) {
            boolean isRef = method.references[i];
            boolean mutableValue = method.mutableValues[i];

            MemoryUtils.Converter<?> converter = method.converters[i];

            if (clazz == Memory.class) {
                Memory argument = arguments[j];
                passed[i] = isRef ? argument : (mutableValue ? argument.toImmutable() : argument.toValue());
                j++;
            } else if (converter != null) {
                passed[i] = converter.run(arguments[j]);
                j++;
            } else if (clazz == Environment.class) {
                passed[i] = env;
            } else if (clazz == TraceInfo.class) {
                passed[i] = trace;
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
                env.error(trace, ErrorType.E_CORE_ERROR, name + "(): Cannot call this function dynamically");
                passed[i] = Memory.NULL;
            }
            i++;
        }

        try {
            if (method.resultType == void.class){
                method.method.invoke(null, passed);
                return Memory.NULL;
            } else
                return MemoryUtils.valueOf(method.method.invoke(null, passed));
        } catch (InvocationTargetException e){
            return env.__throwException(e);
        }
    }
}
