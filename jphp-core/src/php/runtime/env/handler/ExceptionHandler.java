package php.runtime.env.handler;

import php.runtime.exceptions.FatalException;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseException;
import php.runtime.memory.ObjectMemory;
import php.runtime.Memory;
import php.runtime.reflection.ClassEntity;
import php.runtime.util.JVMStackTracer;

public class ExceptionHandler {
    public final Invoker invoker;
    public final Memory invokerMemory;

    public final static ExceptionHandler DEFAULT = new ExceptionHandler(null, null){
        @Override
        public boolean onException(Environment env, BaseException exception) throws Throwable {
            ClassEntity entity = exception.getReflection();
            env.getErrorReportHandler().onFatal(new FatalException(
                    "Uncaught exception '" + entity.getName() + "' with message '" + exception.getMessage(env) + "'" ,
                    exception.getTrace()
            ));
            env.echo("\nStack Trace:\n");
            env.echo(exception.getTraceAsString(env).toString());
            env.echo("\n  thrown in "
                    + exception.getTrace().getFileName()
                    + " on line " + (exception.getTrace().getStartLine() + 1) + "\n"
            );

            if (exception instanceof JavaException && ((JavaException) exception).getThrowable() != null){
                env.echo("\nJVM Stack trace:\n");
                JVMStackTracer tracer = new JVMStackTracer(
                        env.scope.getClassLoader(), ((JavaException) exception).getThrowable().getStackTrace()
                );
                for(JVMStackTracer.Item el : tracer){
                    env.echo("        " + el.toString() + "\n");
                }
            }
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
