package ru.regenix.jphp.runtime.env.handler;

import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.BaseException;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

public class ExceptionHandler {
    public final Invoker invoker;
    public final Memory invokerMemory;

    public final static ExceptionHandler DEFAULT = new ExceptionHandler(null, null){
        @Override
        public boolean onException(Environment env, BaseException exception) throws Throwable {
            ClassEntity entity = exception.getReflection();
            env.getErrorReportHandler().onFatal(new FatalException(
                    "Uncaught exception '" + entity.getName() + "'",
                    exception.getTrace()
            ));
            env.echo("\nStack Trace:\n");
            env.echo(exception.getTraceAsString(env).toString());
            env.echo("\n  thrown in "
                    + exception.getTrace().getFileName()
                    + " on line " + (exception.getTrace().getStartLine() + 1) + "\n"
            );

            return false;
        }
    };

    public ExceptionHandler(Invoker invoker, Memory invokerMemory) {
        this.invoker = invoker;
        this.invokerMemory = invokerMemory;
    }

    public boolean onException(Environment env, BaseException exception)
            throws Throwable {
        Memory[] args = new Memory[]{new ObjectMemory(exception)};
        invoker.setTrace(exception.getTrace());
        return invoker.call(args).toBoolean();
    }
}
