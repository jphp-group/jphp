package org.develnext.jphp.ext.javafx.classes;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.DieException;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.ResourceStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.java.JavaObject;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseException;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXApplication")
public class UXApplication extends BaseWrapper<Application> {
    private static Invoker onStart;

    public UXApplication(Environment env, Application wrappedObject) {
        super(env, wrappedObject);
    }

    public UXApplication(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static void setTheme(Memory value) {
        StyleManager styleManager = StyleManager.getInstance();

        if (value.instanceOf(Stream.class)) {
            if (value.instanceOf(ResourceStream.class)) {
                styleManager.setDefaultUserAgentStylesheet(value.toObject(ResourceStream.class).getUrl().toExternalForm());
            } else {
                styleManager.setDefaultUserAgentStylesheet(value.toObject(Stream.class).getPath());
            }
        } else {
            styleManager.setDefaultUserAgentStylesheet(value.toString());
        }
    }

    @Signature
    public static void runLater(final Invoker callback) {
        new JFXPanel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.callNoThrow();
                } catch (Exception e) {
                    callback.getEnvironment().catchUncaught(e);
                }
            }
        });
    }

    @Signature
    public static void launch(Invoker onStart) {
        UXApplication.onStart = onStart;

        Application.launch(CustomApplication.class);
    }

    public static class CustomApplication extends Application {
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

        public static Thread.UncaughtExceptionHandler buildUncaughtExceptionHandler(final Environment env) {
            return new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    Invoker callback = env.getUserValue(
                            JavaFXExtension.NAMESPACE + "#exceptionHandler", Invoker.class
                    );
                    if (callback != null) {
                        Memory[] args = new Memory[1];
                        if (e instanceof BaseException)
                            args[0] = new ObjectMemory((BaseException)e);
                        else
                            args[0] = new ObjectMemory(JavaObject.of(env, e));

                        try {
                            callback.call(args);
                        } catch (DieException e1) {
                            System.exit(e1.getExitCode());
                        } catch (Throwable throwable) {
                            showExceptionMessage(env, throwable);
                        }
                    } else {
                        showExceptionMessage(env, e);
                    }
                }
            };
        }

        @Override
        public void start(Stage stage) throws Exception {
            Thread thread = Thread.currentThread();
            ClassLoader old = thread.getContextClassLoader();

            thread.setUncaughtExceptionHandler(buildUncaughtExceptionHandler(onStart.getEnvironment()));

            try {
                thread.setContextClassLoader(onStart.getEnvironment().scope.getClassLoader());
                UXApplication.onStart.callAny(stage);
            } catch (Exception e) {
                onStart.getEnvironment().catchUncaught(e);
            } finally {
                thread.setContextClassLoader(old);
            }
        }
    }
}
