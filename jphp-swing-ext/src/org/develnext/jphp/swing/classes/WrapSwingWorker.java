package org.develnext.jphp.swing.classes;

import org.develnext.jphp.swing.SwingExtension;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.ConcurrentEnvironment;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "SwingWorker")
abstract public class WrapSwingWorker extends BaseObject {
    protected Worker worker;

    public WrapSwingWorker(Environment env, ClassEntity clazz) {
        super(env, clazz);
        worker = new Worker(new ConcurrentEnvironment(env));
    }

    @Signature
    abstract protected Memory doInBackground(Environment env, Memory... args);

    @Signature({
            @Arg(value = "values", type = HintType.ARRAY)
    })
    protected Memory publish(Environment env, Memory... args) {
        worker.publishValues(args[0].toValue(ArrayMemory.class).values(true));
        return Memory.NULL;
    }

    @Signature
    public Memory isCancelled(Environment env, Memory... args) {
        return TrueMemory.valueOf(worker.isCancelled());
    }

    @Signature
    public Memory isDone(Environment env, Memory... args) {
        return TrueMemory.valueOf(worker.isDone());
    }

    @Signature
    public Memory getProgress(Environment env, Memory... args) {
        return LongMemory.valueOf(worker.getProgress());
    }

    @Signature
    public Memory execute(Environment env, Memory... args) {
        worker.execute();
        return Memory.NULL;
    }

    @Signature(@Arg("mayInterruptIfRunning"))
    public Memory cancel(Environment env, Memory... args) {
        worker.cancel(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    public Memory run(Environment env, Memory... args) {
        worker.run();
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory setProgress(Environment env, Memory... args) {
        worker.setProgressEx(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory getState(Environment env, Memory... args) {
        return StringMemory.valueOf(worker.getState().name());
    }

    @Signature(@Arg(value = "timeout", optional = @Optional("-1")))
    public Memory get(Environment env, Memory... args) throws ExecutionException, InterruptedException,
            TimeoutException {
        if (args[0].toLong() == -1)
            return worker.get();
        else
            return worker.get(args[0].toLong(), TimeUnit.MILLISECONDS);
    }


    protected class Worker extends SwingWorker<Memory, Memory> {
        protected Environment env;
        protected Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        public Worker(Environment env) {
            this.env = env;
            this.uncaughtExceptionHandler = WrapSwingUtilities.buildUncaughtExceptionHandler(env);
        }

        @Override
        protected Memory doInBackground() throws Exception {
            try {
                return ObjectInvokeHelper.invokeMethod(
                        WrapSwingWorker.this,
                        WrapSwingWorker.this.getReflection().findMethod("doinbackground"),
                        env,
                        TraceInfo.UNKNOWN,
                        new Memory[]{},
                        false
                );
            } catch (Throwable throwable) {
                uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), throwable);
                return Memory.NULL;
            }
        }

        @Override
        protected void process(List<Memory> chunks) {
            try {
                ObjectInvokeHelper.invokeMethod(
                        WrapSwingWorker.this,
                        WrapSwingWorker.this.getReflection().findMethod("process"),
                        env,
                        TraceInfo.UNKNOWN,
                        new Memory[]{ new ArrayMemory(chunks) },
                        false
                );
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        public void publishValues(Memory... values) {
            super.publish(values);
        }

        public void setProgressEx(int value) {
            super.setProgress(value);
        }
    }
}
