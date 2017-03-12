package php.runtime.env.handler;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.FatalException;
import php.runtime.ext.java.JavaException;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseException;
import php.runtime.lang.exception.BaseBaseException;
import php.runtime.lang.exception.BaseError;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.util.JVMStackTracer;

public class ExceptionHandler {
    public final Invoker invoker;
    public final Memory invokerMemory;

    public final static ExceptionHandler DEFAULT = new ExceptionHandler(null, null){
        @Override
        public boolean onException(Environment env, BaseBaseException exception) throws Throwable {
            ClassEntity entity = exception.getReflection();

            String message = "Uncaught exception '" + entity.getName() + "' with message '" + exception.getMessage(env) + "'";

            if (exception instanceof BaseError) {
                message = String.format("Uncaught %s: %s", entity.getName(), exception.getMessage(env));
            }

            env.getErrorReportHandler().onFatal(new FatalException(
                    message,
                    exception.getTrace()
            ));

            env.echo("\nStack Trace:\n");
            env.echo(exception.getTraceAsString(env).toString());

            TraceInfo trace = exception.getTrace();

            if (trace == null) {
                trace = TraceInfo.UNKNOWN;
            }

            env.echo("\n  thrown in "
                            + trace.getFileName()
                            + " on line " + (trace.getStartLine() + 1) + "\n"
            );

            if (exception instanceof JavaException && ((JavaException) exception).getThrowable() != null){
                env.echo("\nJVM Stack trace:\n");
                JVMStackTracer tracer = new JVMStackTracer(
                        env.scope.getClassLoader(), ((JavaException) exception).getThrowable().getStackTrace()
                );
                for(JVMStackTracer.Item el : tracer){
                    if (!el.isSystem())
                        env.echo("  " + el.toString() + "\n");
                }
            }
            return false;
        }
    };

    public ExceptionHandler(Invoker invoker, Memory invokerMemory) {
        this.invoker = invoker;
        this.invokerMemory = invokerMemory;
    }

    public boolean onException(Environment env, BaseBaseException exception)
            throws Throwable {
        Memory[] args = new Memory[]{new ObjectMemory(exception)};
        invoker.setTrace(exception.getTrace());
        return invoker.call(args).toBoolean();
    }
}
