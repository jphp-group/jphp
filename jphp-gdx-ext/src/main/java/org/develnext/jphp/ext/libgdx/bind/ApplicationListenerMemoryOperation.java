package org.develnext.jphp.ext.libgdx.bind;


import com.badlogic.gdx.ApplicationListener;
import org.develnext.jphp.ext.libgdx.classes.WrapApplicationListener;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseException;
import php.runtime.memory.LongMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public class ApplicationListenerMemoryOperation extends MemoryOperation<ApplicationListener> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { ApplicationListener.class };
    }

    protected static Thread.UncaughtExceptionHandler buildUncaughtExceptionHandler(final Environment env) {
        return new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (e instanceof InvocationTargetException) {
                    e = e.getCause();
                }

                if (e instanceof BaseException) {
                    env.catchUncaught((Exception) e);
                } else {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public ApplicationListener convert(final Environment env, TraceInfo trace, Memory arg) throws Throwable {
        final WrapApplicationListener listener = arg.toObject(WrapApplicationListener.class);

        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = buildUncaughtExceptionHandler(env);

        return new ApplicationListener() {
            @Override
            public void create() {
                Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                env.invokeMethodNoThrow(listener, "create");
            }

            @Override
            public void resize(int width, int height) {
                Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                env.invokeMethodNoThrow(listener, "resize", LongMemory.valueOf(width), LongMemory.valueOf(height));
            }

            @Override
            public void render() {
                Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                env.invokeMethodNoThrow(listener, "render");
            }

            @Override
            public void pause() {
                Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                env.invokeMethodNoThrow(listener, "pause");
            }

            @Override
            public void resume() {
                Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                env.invokeMethodNoThrow(listener, "resume");
            }

            @Override
            public void dispose() {
                Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                env.invokeMethodNoThrow(listener, "dispose");
            }
        };
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ApplicationListener arg) throws Throwable {
        throw new RuntimeException("Unsupported operation");
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeClass(ReflectionUtils.getClassName(WrapApplicationListener.class));
    }
}
