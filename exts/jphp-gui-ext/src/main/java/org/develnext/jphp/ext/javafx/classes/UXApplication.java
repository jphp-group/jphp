package org.develnext.jphp.ext.javafx.classes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.develnext.jphp.ext.javafx.FXLauncher;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.CustomErrorException;
import php.runtime.ext.core.classes.stream.ResourceStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Abstract
@Name(JavaFXExtension.NS + "UXApplication")
public class UXApplication extends BaseWrapper<Application> {
    private static Invoker onStart;
    private static boolean shutdown = false;

    public UXApplication(Environment env, Application wrappedObject) {
        super(env, wrappedObject);
    }

    public UXApplication(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static String getPid() {
        // Should return something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');

        String pid = jvmName.substring(0, index);
        return pid;
    }

    @Signature
    public static boolean isShutdown() {
        return shutdown;
    }

    @Signature
    public static void setImplicitExit(boolean value) {
        Platform.setImplicitExit(value);
    }

    @Signature
    public static boolean isImplicitExit() {
        return Platform.isImplicitExit();
    }

    @Signature
    public static String getMacAddress() {
        InetAddress ip;

        try {
            ip = InetAddress.getLocalHost();

            if (ip == null) {
                return null;
            }

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            if (network == null) {
                return null;
            }

            byte[] mac = network.getHardwareAddress();

            if (mac == null) {
                return null;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }

            return sb.toString();
        } catch (UnknownHostException e) {
            return null;
        } catch (SocketException e){
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Signature
    public static boolean isUiThread() {
        return Platform.isFxApplicationThread();
    }

    @Signature
    public static void setTheme(final Memory value) {
        new JFXPanel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (isShutdown()) {
                    return;
                }

                if (value.instanceOf(Stream.class)) {
                    if (value.instanceOf(ResourceStream.class)) {
                        Application.setUserAgentStylesheet(value.toObject(ResourceStream.class).getUrl().toExternalForm());
                    } else {
                        Application.setUserAgentStylesheet(value.toObject(Stream.class).getPath());
                    }
                } else {
                    Application.setUserAgentStylesheet(value.toString());
                }
            }
        });
    }

    @Signature
    public static Memory runLaterAndWait(final Invoker callback) throws Throwable {
        if (isShutdown()) {
            return Memory.NULL;
        }

        if (Platform.isFxApplicationThread()) {
            return callback.call();
        }

        new JFXPanel();

        FutureTask<Memory> futureTask = new FutureTask<>(() -> {
            try {
                return callback.callNoThrow();
            } catch (Exception e) {
                callback.getEnvironment().catchUncaught(e);
                return Memory.NULL;
            }
        });

        Platform.runLater(futureTask);

        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CriticalException(e);
        }
    }

    @Signature
    public static void runLater(final Invoker callback) {
        if (isShutdown()) {
            return;
        }

        new JFXPanel();

        Platform.runLater(() -> {
            try {
                callback.callNoThrow();
            } catch (Exception e) {
                callback.getEnvironment().catchUncaught(e);
            }
        });
    }

    @Signature
    public static UXForm getSplash(Environment env) {
        FXLauncher fxLauncher = FXLauncher.current();

        if (fxLauncher != null) {
            Stage splashStage = fxLauncher.getSplashStage();

            if (splashStage == null) {
                return null;
            }

            return new UXForm(env, splashStage);
        } else {
            return null;
        }
    }

    @Signature
    public static void shutdown() {
        shutdown = true;
        Platform.exit();

        Window[] windows = Window.getWindows();

        for (Window window : windows) {
            window.dispose();
        }
    }

    @Signature
    public static void launch(Invoker onStart) {
        Environment.addThreadSupport(onStart.getEnvironment());

        UXApplication.onStart = onStart;

        Application.launch(CustomApplication.class);
    }

    public static class CustomApplication extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            Environment.addThreadSupport(onStart.getEnvironment());

            Thread thread = Thread.currentThread();
            ClassLoader old = thread.getContextClassLoader();

            //thread.setUncaughtExceptionHandler(buildUncaughtExceptionHandler(onStart.getEnvironment()));

            new Button();  // fix.
            try {
                thread.setContextClassLoader(onStart.getEnvironment().scope.getClassLoader());
                UXApplication.onStart.callAny(stage);
            } catch (Exception throwable) {
                if (throwable instanceof CriticalException) {
                    throwable.printStackTrace();
                }

                if (throwable instanceof CustomErrorException) {
                    CustomErrorException error = (CustomErrorException) throwable;
                    onStart.getEnvironment().error(error.getType(), error.getMessage());
                } else {
                    onStart.getEnvironment().catchUncaught(throwable);
                }
            } catch (Throwable throwable) {
                throw throwable;
            } finally {
                thread.setContextClassLoader(old);
            }
        }

        @Override
        public void stop() throws Exception {
            super.stop();
            shutdown = true;
        }
    }
}
