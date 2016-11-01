package org.develnext.jphp.ext.pna.bind;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.TypeChecker;

public class RunnableMemoryOperation extends MemoryOperation<Runnable> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Runnable.class };
    }

    @Override
    public Runnable convert(final Environment env, final TraceInfo trace, final Memory arg) throws Throwable {
        return new Runnable() {
            @Override
            public void run() {
                Invoker invoker = Invoker.valueOf(env, trace, arg);
                invoker.callAny();
            }
        };
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, final Runnable arg) throws Throwable {
        return Memory.NULL;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(TypeChecker.of(HintType.CALLABLE));
    }
}
