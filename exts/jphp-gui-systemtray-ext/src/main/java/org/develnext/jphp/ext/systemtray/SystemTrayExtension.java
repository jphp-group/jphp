package org.develnext.jphp.ext.systemtray;

import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.systemtray.classes.PSystemTray;
import org.develnext.jphp.ext.systemtray.classes.WrapTrayIcon;
import org.develnext.jphp.ext.systemtray.event.TrayIconEventAdapter;
import php.runtime.env.CompileScope;

import java.awt.*;

public class SystemTrayExtension extends JavaFXExtension {
    public static final String NS = "php\\desktop";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "gui", "tray" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerEventProvider(new TrayIconEventAdapter());

        registerWrapperClass(scope, TrayIcon.class, WrapTrayIcon.class);
        registerClass(scope, PSystemTray.class);
    }
}
