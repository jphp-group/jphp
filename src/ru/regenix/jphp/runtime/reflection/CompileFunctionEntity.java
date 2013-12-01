package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CompileFunctionEntity extends FunctionEntity {
    private final CompileFunction compileFunction;

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
    public Memory invoke(Environment env, Memory[] arguments) throws IllegalAccessException, InvocationTargetException {
        Method method = compileFunction.find(arguments.length);

        Class<?>[] types = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        Object[] passed = new Object[ types.length ];

        int i = 0;
        int j = 0;
        for(Class<?> clazz : types) {
            boolean isRef = false;
            for(Annotation annotation : annotations[i]){
                if (annotation instanceof Reflection.Reference)
                    isRef = true;
            }

            if (clazz == Environment.class)
                passed[i] = env;
            else if (i == types.length - 1 && types[i] == Memory[].class) {
                Memory[] arg = new Memory[types.length - i];
                if (!isRef){
                    for(int k = 0; k < arg.length; k++)
                        arg[i] = arguments[i].toImmutable();
                } else {
                    System.arraycopy(arguments, j, arg, 0, arg.length);
                }
                passed[i] = arg;
                break;
            } else {
                if (clazz == Memory.class) {
                    passed[i] = isRef ? arguments[j] : arguments[j].toImmutable();
                } else {
                    passed[i] = MemoryUtils.toValue(arguments[j], clazz);
                }
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
