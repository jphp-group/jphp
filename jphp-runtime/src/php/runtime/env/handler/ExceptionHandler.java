package php.runtime.env.handler;

import php.runtime.Memory;
import php.runtime.common.Messages;
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

            String message = Messages.MSG_UNCAUGHT_FATAL.fetch(entity.getName(), exception.getMessage(env));

            if (exception instanceof BaseError) {
                message = Messages.MSG_UNCAUGHT_ERROR.fetch(entity.getName(), exception.getMessage(env));
            }

            env.getErrorReportHandler().onFatal(new FatalException(
                    message,
                    exception.getTrace()
            ));

            env.echo("\n"+ Messages.MSG_STACK_TRACE +":\n");
            env.echo(exception.getTraceAsString(env).toString());

            TraceInfo trace = exception.getTrace();

            if (trace == null) {
                trace = TraceInfo.UNKNOWN;
            }

            env.echo("\n  " +
                    Messages.MSG_THROWN_IN_ON_LINE.fetch(trace.getFileName(), trace.getStartLine() + 1) +
                    "\n"
            );

            if (exception instanceof JavaException && ((JavaException) exception).getThrowable() != null){
                env.echo("\nJVM " + Messages.MSG_STACK_TRACE + ":\n");
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
