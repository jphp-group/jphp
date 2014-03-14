package org.develnext.jphp.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.DieException;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.java.JavaObject;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseException;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "SwingUtilities")
final public class WrapSwingUtilities extends BaseObject {

    public WrapSwingUtilities(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Signature(@Arg(value = "callback", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public static Memory setExceptionHandler(Environment env, Memory... args){
        if (args[0].isNull()){
            env.setUserValue(SwingExtension.NAMESPACE + "#exceptionHandler", null);
        } else {
            final Invoker invoker = Invoker.valueOf(env, null, args[0]);
            env.setUserValue(SwingExtension.NAMESPACE + "#exceptionHandler", invoker);
        }
        return Memory.TRUE;
    }

    protected static void showExceptionMessage(Environment env, Throwable e){
        String message = e.getMessage();
        if (e instanceof Exception){
            env.pushOutputBuffer(null, 0, true);
            try {
                env.catchUncaught((Exception)e);
                message = env.peekOutputBuffer().getContents().toString();
            } catch (RuntimeException e2){
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e2.printStackTrace(printWriter);
                message = stringWriter.toString();
            } catch (UnsupportedEncodingException e1) {
                throw new RuntimeException(e1);
            } finally {
                try {
                    env.peekOutputBuffer().clean();
                    env.popOutputBuffer();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        }

        if (message.length() > 2048)
            message = message.substring(0, 2048) + " ...";

        JOptionPane.showMessageDialog(
                null, message,
                "Exception", JOptionPane.ERROR_MESSAGE
        );
    }

    @Signature(@Arg(value = "callback", type = HintType.CALLABLE))
    public static Memory invokeLater(final Environment env, Memory... args){
        final Invoker invoker = Invoker.valueOf(env, null, args[0]);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                ClassLoader old = thread.getContextClassLoader();

                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        Invoker callback = env.getUserValue(
                                SwingExtension.NAMESPACE + "#exceptionHandler", Invoker.class
                        );
                        if (callback != null) {
                            Memory[] args = new Memory[1];
                            if (e instanceof BaseException)
                                args[0] = new ObjectMemory((BaseException)e);
                            else
                                args[0] = new ObjectMemory(JavaObject.of(env, e));

                            callback.pushCall(TraceInfo.UNKNOWN, args);
                            try {
                                callback.call(args);
                            } catch (DieException e1) {
                                System.exit(e1.getExitCode());
                            } catch (Throwable throwable) {
                                showExceptionMessage(env, throwable);
                            } finally {
                                callback.popCall();
                            }
                        } else {
                            showExceptionMessage(env, e);
                        }
                    }
                });

                try {
                    thread.setContextClassLoader(env.scope.getClassLoader());
                    invoker.call();
                } catch (RuntimeException e){
                    throw e;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                } finally {
                    thread.setContextClassLoader(old);
                }
            }
        });
        return Memory.NULL;
    }
}
