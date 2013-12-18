package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;

import java.lang.reflect.InvocationTargetException;

public class CompileFunctionEntity extends FunctionEntity {
    private final CompileFunction compileFunction;
    private MemoryUtils.Converter<?> converters[][];

    public CompileFunctionEntity(CompileFunction compileFunction) {
        super(null);
        this.compileFunction = compileFunction;
        this.setName(compileFunction.name);
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public Memory invoke(Environment env, TraceInfo trace, Memory[] arguments) throws IllegalAccessException, InvocationTargetException {
        CompileFunction.Method method = compileFunction.find(arguments.length);

        Class<?>[] types = method.parameterTypes;
        Object[] passed = new Object[ types.length ];

        int i = 0;
        int j = 0;
        for(Class<?> clazz : types) {
            boolean isRef = method.references[i];
            MemoryUtils.Converter<?> converter = method.converters[i];
            if (clazz == Memory.class) {
                passed[i] = isRef ? arguments[j] : arguments[j].toImmutable();
                j++;
            } else if (converter != null) {
                passed[i] = converter.run(arguments[j]);
                j++;
            } else if (clazz == Environment.class) {
                passed[i] = env;
            } else if (clazz == TraceInfo.class) {
                passed[i] = trace;
            } else if (i == types.length - 1 && types[i] == Memory[].class){
                Memory[] arg = new Memory[types.length - i];
                if (!isRef){
                    for(int k = 0; k < arg.length; k++)
                        arg[i] = arguments[i].toImmutable();
                } else {
                    System.arraycopy(arguments, j, arg, 0, arg.length);
                }
                passed[i] = arg;
                break;
            }
            i++;
        }

        if (method.resultType == void.class){
            method.method.invoke(null, passed);
            return Memory.NULL;
        } else
            return MemoryUtils.valueOf(method.method.invoke(null, passed));
    }
}
