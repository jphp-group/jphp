package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CompileFunctionEntity extends FunctionEntity {
    private final CompileFunction compileFunction;

    public CompileFunctionEntity(CompileFunction compileFunction) {
        super(null);
        this.compileFunction = compileFunction;
    }

    @Override
    public Memory invoke(Environment env, Memory[] arguments) throws IllegalAccessException, InvocationTargetException {
        Method method = compileFunction.find(arguments.length);

        Class<?>[] types = method.getParameterTypes();
        Object[] passed = new Object[ types.length ];

        int i = 0;
        int j = 0;
        for(Class<?> clazz : types) {
            if (clazz == Environment.class)
                passed[i] = env;
            else if (i == types.length - 1 && types[i] == Memory[].class) {
                Memory[] arg = new Memory[types.length - i];
                System.arraycopy(arguments, j, arg, 0, arg.length);
                passed[i] = arg;
                break;
            } else {
                passed[i] = MemoryUtils.toValue(arguments[j], clazz);
                j++;
            }
            i++;
        }

        if (method.getReturnType() == void.class){
            method.invoke(null, passed);
            return Memory.NULL;
        } else
            return MemoryUtils.valueOf(method.invoke(null, passed));
    }
}
